package oy.interact.tira.student;

import oy.interact.tira.util.Pair;
import oy.interact.tira.util.Dictionary;

// Hajautustaulu avain/arvo-parien tallentamiseen.
public class HashTableDictionary<K, V> implements Dictionary<K,V> {


	// STUDENT TODO: remember to clear the counters below in
	// - clear()
	// - just before reallocation happens
	// - maintain the values when adding to the hash table!
	// - the values are used in getStats and printed out by some tests!

    private static final double MAX_FILL_PERCENT = 0.65; // -< Maksimi täyttöaste on asetettu 65%
    private static final int DEFAULT_CAPACITY = 16; // <- Taulukon oletuskapasiteetti
    private Pair<K, V>[] table;
    private int size;

	/** How many times adding collided in the _first_ index that was tried? */
	private int addCollided = 0;
	/** How many times collisions happended _including_ when probing? */
	private int totalCollisionCount = 0;
	/** What was the max number of probing steps add had to go through before free slot was found? */
	private int maxProbingSteps = 0;

	public HashTableDictionary() {
		// STUDENT TODO: implement initialization of hashtable correctly
		// DO NOT use too large a size for the default size. Values between 8-32 are ok.
        this(DEFAULT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public HashTableDictionary(int capacity) {
        // STUDENT TODO: implement initialization of hashtable correctly
        // Esiehtojen tarkistus
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be a positive integer");
        }
        // Varmistetaan että kapasiteetti on 2:n potenssi, tällä optimoidaan mod-laskentaa.
        int actualCapacity = (int) Math.pow(2, Math.ceil(Math.log(capacity) / Math.log(2)));
        table = new Pair[actualCapacity];
        resetStatsAndSize();
    }

    // Metodi laskee taulukon indeksin hash-arvosta. Käyttää bittioperaaatiota (0x7FFFFFFF) poistamaan etumerkin
    // näin varmistetaan että indeksi on positiivinen
    private int calculateIndex(int hash, int capacity) {
        return (hash & 0x7FFFFFFF) % capacity;
    }

    // findSlot-metodi etsii avaimelle paikan käyttäen lineaarista luotausta.
    // Metodi palauttaa -1, mikäli taulukko on täynnä. Tätät ei kuitenkaan pitäsi tapahtua rajatun täyttöasteen ja
    // rehashingin takia.
    private int findSlot(K key, int initialIndex) {
        int index = initialIndex;
        int probingSteps = 0;

        // Luodataan kunnes vapaa paikka tai vastaava avain löytyy
        do {
            // Vapaa paikka löytyi, palautaa indeksin uutta lisäystä varten
            if (table[index] == null) {
                maxProbingSteps = Math.max(maxProbingSteps, probingSteps);
                return index;
            }
            // Vastaava avain löytyi, palauttaa indeksin arvo-päivitystä varten
            if (table[index].getKey().equals(key)) {
                maxProbingSteps = Math.max(maxProbingSteps, probingSteps);
                return index;
            }
            // Törmäyksien tilastointi
            if (probingSteps > 0) {
                totalCollisionCount++; // <- Törmäys heti ensimmäisesä (hash) indeksissä
            } else {
                addCollided++; // <- Jokainen luotausaskel on "törmäys"
            }
            // Päivitetään luotausaskelten arvo
            probingSteps++;
            // Lineaarinen siirtymä
            index = (index + 1) % table.length;

        } while (index != initialIndex); // Estetään ikuinen silmukka, jos taulu on täynnä
        return -1;
    }

    // reHash-metodi kasvattaa taulukkoa ja sijoittaa taulukon alkiot uudelleen.
    // Tilastot nollataan, koska uuden taulukon myötä törmäyskäyttäytyminen muuttuu.
    private void reHash(int newCapacity) throws OutOfMemoryError {
        // Tehdään kopio olemassa ulevasta taulukosta
        Pair<K, V>[] oldTable = table;
        // Luodaan uusi taulukko
        table = new Pair[newCapacity];

        // Nollataan tilastot ja koko. HUOM! Koko päivittyy siirtämisen yhteydessä olevan add-metodin kutsun kautta
        resetStatsAndSize();
        size = 0;

        // Siirretään alkiot uuteen taulukkoon
        for (Pair<K, V> pair : oldTable) {
            if (pair != null) {
                // Kutsutaan allaolevaa add-metodia, joka laskee uuden hashin ja indeksin
                add(pair.getKey(), pair.getValue());
            }
        }
    }

    // lisäysmetodi
	@Override
	public V add(K key, V value) throws OutOfMemoryError, IllegalArgumentException {
		// STUDENT TODO: implement adding to hashtable correctly
        // Esiehtojen tarkistus
		if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }
        // Lasketaan hash-arvo
        int hashCode = key.hashCode();
        // Tarkistetaan täyttö aste ja tehdään rehash mikäli yli 65%
        double currentFillPercent = (double) size / table.length;
        if (currentFillPercent >= MAX_FILL_PERCENT ) {
            reHash(table.length * 2);
        }
        // Lasektaan indeksi ja etsitään paikka
        int initialIndex = calculateIndex(hashCode, table.length);
        int slotIndex = findSlot(key, initialIndex);

        // Tarkistus onko taulu täynnä ja voidaanko rehash toteuttaa
        if (slotIndex == -1) {
            throw new OutOfMemoryError("Hash table is full and cannot rehash");
        }
        // Avaimen lisäys tai arvon päivitys
        if (table[slotIndex] != null && table[slotIndex].getKey().equals(key)) {
            // Avain on jo olemassa, päivitetään arvo
            V oldValue = table[slotIndex].getValue();
            table[slotIndex].setValue(value);
            return oldValue;
        } else {
            // Uusi avain, lisätään taulukkoon
            table[slotIndex] = new Pair<>(key, value);
            size++;
            return null;
        }
	}

    // Hakumetodi
	@Override
	public V get(K key) throws IllegalArgumentException {
		// STUDENT TODO: implement getting from a hashtable correctly
        // Esiehtojen tarkistus
		if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        // Lasketaan hash-arvo
        int hashCode = key.hashCode();
        int initialIndex = calculateIndex(hashCode, table.length);

        // Luodataan samalla logiikalla kuin add-metodisa
        int index = initialIndex;
        do {
            if (table[index] == null) {
                return null; // <- Avainta ei ole
            }
            if (table[index].getKey().equals(key)) {
                return table[index].getValue(); // <- Avain löytyi
            }

            index = (index + 1) % table.length;
        } while (index != initialIndex);
        return null;
	}

	@Override
	public int size() {
		// STUDENT TODO: implement this correctly
		return size;
	}

	@Override
	public void clear() {
		// STUDENT TODO: implement this correctly
        table = new Pair[DEFAULT_CAPACITY];
        resetStatsAndSize();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Pair<K,V> [] toArray() throws OutOfMemoryError {
		// STUDENT TODO: implement this correctly
		Pair<K, V>[] array = new Pair[size];
        int index = 0;

        for (Pair<K, V> pair : table) {
            if (pair != null) {
                array[index++] = pair;
            }
        }
        return array;
	}

	@Override
	public boolean isEmpty() {
		// STUDENT TODO: implement this correctly
		return size == 0;
	}

    // Compress-metodi tiivistää taulukon pienemmäksi.
	@Override
	public void compress() throws OutOfMemoryError {
		// STUDENT TODO: implement this correctly
        // Pienennetään taulukkoa, jos se on kovin tyhjä, pidetään täyttöaste kuitenkin järkevänä
        int targetCapacity = (int) Math.ceil((double) size / MAX_FILL_PERCENT);
        // Jos tavoitekapasiteetti on alle oletuksen asetetaan kapasiteetti oletusarvoon
        if (targetCapacity < DEFAULT_CAPACITY) {
            targetCapacity = DEFAULT_CAPACITY;
        }
        // Varmistetaan jälleen 2:n potenssi
        int actualCapacity = (int) Math.pow(2, Math.ceil(Math.log(targetCapacity) / Math.log(2)));
        // Toteutetaan rehash kompressoidulle taulukolle
        reHash(actualCapacity);
	}

	@Override
	public int capacity() {
		// STUDENT TODO: implement this correctly
		return table.length;
	}

    private void resetStatsAndSize() {
        addCollided = 0;
        totalCollisionCount = 0;
        maxProbingSteps = 0;
        size = 0;
    }

	// STUDENT: No need to touch this
	public class HashTableStats implements Dictionary.Stats {
		public int arrayLength = 0;
		public double fillFactor = 0; // in percents
		public int addCollisions = 0;
		public int totalCollisions = 0;
		public int maxProbingSteps = 0;
		public int size = 0;

		@Override
		public String getStats() {
			return String.format
			(
				"Array length: %d%nElements: %,d%nFill factor: %.2f%nAdd collisions: %d%nTotal collisions: %d%nMax Probing Steps: %d%n",
				this.arrayLength,
				this.size,
				this.fillFactor,
				this.addCollisions,
				this.totalCollisions,
				this.maxProbingSteps
			);
		}
	}

	@Override
	public Stats getStats() {
		HashTableStats stats = new HashTableStats();
		// STUDENT TODO: make sure you calculate, collect and maintain these values
		// in your implementation correctly! Set the values of the 
		// stats from the hashtable member variables and the stats values you maintain:
		stats.arrayLength = table.length;
		stats.size = size;
		stats.fillFactor = (double) size / table.length;
		stats.addCollisions = addCollided;
		stats.totalCollisions = totalCollisionCount;
		stats.maxProbingSteps = maxProbingSteps;
		return stats;
	}
	
}

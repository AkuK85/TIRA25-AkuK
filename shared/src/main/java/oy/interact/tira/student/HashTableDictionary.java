package oy.interact.tira.student;

import oy.interact.tira.util.Pair;
import oy.interact.tira.util.Dictionary;

public class HashTableDictionary<K, V> implements Dictionary<K,V> {


	// STUDENT TODO: remember to clear the counters below in
	// - clear()
	// - just before reallocation happens
	// - maintain the values when adding to the hash table!
	// - the values are used in getStats and printed out by some tests!

    private static final double MAX_FILL_PERCENT = 0.65;
    private static final int DEFAULT_CAPACITY = 16;
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
        if (capacity < 1) throw new IllegalArgumentException("Capacity must be a positive integer"); {
            int actualCapacity = (int) Math.pow(2, Math.ceil(Math.log(capacity) / Math.log(2)));
            table = new Pair[actualCapacity];
            size = 0;
            addCollided = 0;
            totalCollisionCount = 0;
            maxProbingSteps = 0;
        }
	}

    private int calculateIndex(int hash, int capacity) {
        return (hash & 0x7FFFFFFF) % capacity;
    }

    private int findSlot(K key, int initialIndex) {
        int index = initialIndex;
        int probingSteps = 0;

        do {
            if (table[index] == null) {
                maxProbingSteps = Math.max(maxProbingSteps, probingSteps);
                return index;
            }

            if (table[index].getKey().equals(key)) {
                maxProbingSteps = Math.max(maxProbingSteps, probingSteps);
                return index;
            }

            if (probingSteps > 0) {
                totalCollisionCount++;
            } else {
                addCollided++;
            }

            probingSteps++;
            index = (index + 1) % table.length;

        } while (index != initialIndex);
        return -1;
    }

    private void reHash(int newCapacity) throws OutOfMemoryError {
        Pair<K, V>[] oldTable = table;
        table = new Pair[newCapacity];

        addCollided = 0;
        totalCollisionCount = 0;
        maxProbingSteps = 0;
        size = 0;

        for (Pair<K, V> pair : oldTable) {
            if (pair != null) {
                add(pair.getKey(), pair.getValue());
            }
        }
    }
 
	@Override
	public V add(K key, V value) throws OutOfMemoryError, IllegalArgumentException {
		// STUDENT TODO: implement adding to hashtable correctly
		if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }

        int hashCode = key.hashCode();

        double currentFillPercent = (double) size / table.length;
        if (currentFillPercent >= MAX_FILL_PERCENT ) {
            reHash(table.length * 2);
        }

        int initialIndex = calculateIndex(hashCode, table.length);
        int slotIndex = findSlot(key, initialIndex);

        if (slotIndex == -1) {
            throw new OutOfMemoryError("Hash table is full and cannot rehash");
        }

        if (table[slotIndex] != null && table[slotIndex].getKey().equals(key)) {
            V oldValue = table[slotIndex].getValue();
            table[slotIndex].setValue(value);
            return oldValue;
        } else {
            table[slotIndex] = new Pair<>(key, value);
            size++;
            return null;
        }
	}

	@Override
	public V get(K key) throws IllegalArgumentException {
		// STUDENT TODO: implement getting from a hashtable correctly
		if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int hashCode = key.hashCode();

        int initialIndex = calculateIndex(hashCode, table.length);
        int index = initialIndex;

        do {
            if (table[index] == null) {
                return null;
            }
            if (table[index].getKey().equals(key)) {
                return table[index].getValue();
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
        size = 0;
        addCollided = 0;
        totalCollisionCount = 0;
        maxProbingSteps = 0;
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

	@Override
	public void compress() throws OutOfMemoryError {
		// STUDENT TODO: implement this correctly
        int targetCapacity = (int) Math.ceil((double) size / MAX_FILL_PERCENT);

        if (targetCapacity < DEFAULT_CAPACITY) {
            targetCapacity = DEFAULT_CAPACITY;
        }

        int actualCapacity = (int) Math.pow(2, Math.ceil(Math.log(targetCapacity) / Math.log(2)));

        reHash(actualCapacity);
	}

	@Override
	public int capacity() {
		// STUDENT TODO: implement this correctly
		return table.length;
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

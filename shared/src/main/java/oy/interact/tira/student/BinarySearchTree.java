package oy.interact.tira.student;

import oy.interact.tira.util.Pair;
import oy.interact.tira.util.Tree;

// Geneerinen BST, eli Binäärinen Hakupuu.

public class BinarySearchTree<K extends Comparable<K>, V> implements Tree<K, V> {

    // Puun muuttujat
    private Node<K, V> root;
	int size;

    // Syvyysmuuttujat
	int maxDepth = 0;
    int minDepth = Integer.MAX_VALUE;


    // Privaatti sisäinen solmuluokka.
    // Sisältää avaimen, arvon ja vittaukset lapsisolmuihin.
    private static class Node<K, V> {
        K key; // Solmun avain, käytetään hakemiseen ja järjestämiseen
        V value; // Arvo, eli solmun sisältämä varsinainen data
        Node<K, V> leftChild; // Vasen lapsi solmu = pienemmät avaimet
        Node<K, V> rightChild; // Oikea lapsi solmu = isommat avaimet

        // Solmun muodostin
        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.leftChild = null;
            this.rightChild = null;
        }
    }

    // Puun muodostin
	public BinarySearchTree() {
		// STUDENT TODO finish the implementation of this method
		root = null;
        size = 0;

	}

    // Lisäys-metodi, joka lisää tai päivittää avain/arvo-parin puuhun.
    // Aikakompleksisuus, Paras = O(log n), Pahin = O(n).
	@Override
	public V add(K key, V value) throws OutOfMemoryError, IllegalArgumentException {
        // STUDENT TODO finish the implementation of this method

        // Esiehtojen tarkistus
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value is null");
        }
        // Base Case: Puu on tyhjä. Luodaan puulle juurisolmu
        if (root == null) {
            root = new Node<>(key, value);
            size++; // <- Size-muuttujan päivitys
            return null;
        }
        // Kutsutaan allaolevaa rekursiivista apumetodia.
        return insert(root, key, value);
    }

    // Rekursiivinen apumetodi lisäämiselle, etsii oikean paikan lisättävälle tiedolle vertaamalla avaimia
    private V insert(Node<K, V> node, K key, V value ) {
        // Verrataan lisättävää avainta nykyiseen solmuun
        int compare = key.compareTo(node.key);

        if (compare < 0) {
            // Uusi avain on pienempi, siirrytään vasempaan lapsi solmuun
            if (node.leftChild == null) {
                node.leftChild = new Node<>(key, value);
                size++;
                return null;
            }
            return insert(node.leftChild, key, value);
        } else if (compare > 0) {
            // Uusi avain on suurempi, siirrytään oikeaan lapsi solmuun
            if (node.rightChild == null) {
                node.rightChild = new Node<>(key, value);
                size++;
                return null;
            }
            return insert(node.rightChild, key, value);
        } else {
            // Avain on sama. Päivitä arvo ja palauta vanha arvo
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        }
    }

    // Haku-metodi, haetaan ja palutetaan solmun arvo annetun avaimen perusteella.
    // Rekursiivinen haku toteutettu search-apumetodissa
	@Override
	public V get(K key) throws IllegalArgumentException {
		// STUDENT TODO finish the implementation of this method

        // Esiehtojen tarkistus
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        // Kutsutaan alla olevaa rekursiivista haku-apumetodia aloittaen juuresta
        return search(root, key);
	}

    // Rekursiivinen apumetodi hakemiselle
    private V search(Node<K, V> node, K key) {
        // Jos solmu on null avainta ei löytynyt
        if (node == null) {
            return null;
        }

        // Verrataan avainta käsiteltävään solmuun
        int compare = key.compareTo(node.key);

        if (compare < 0) {
            // Etsittävä on pienempi, siirrytään vasempaan lapsi solmuun
            return search(node.leftChild, key);
        } else if  (compare > 0) {
            // Etsittävä on suuuremoi, siiirrytään oikeaan lapsi solmuun
            return search(node.rightChild, key);
        } else  {
            // Avain löytyi, palautetaan solmun arvo
            return node.value;
        }
    }

	@Override
	public int size() {
		// STUDENT TODO finish the implementation of this method
		return  size;
	}

    // Tyhjennys-metodi
	@Override
	public void clear() {
		// STUDENT TODO finish the implementation of this method
        root = null;
        size = 0;
	}

    // Tarkistus-metodi sille onko puu tyhjä
	@Override
	public boolean isEmpty() {
		// STUDENT TODO finish the implementation of this method
		return size == 0;
	}

    // Taulukointi-metodi, joka palauttaa puun sisöllön lajiteltuna taulukkona.
	@Override
	@SuppressWarnings("unchecked")
	public Pair<K, V>[] toArray() throws OutOfMemoryError {
		// STUDENT TODO finish the implementation of this method

        // Luodaan täsmälleen puun kokoinen taulukko size-muuttujaa hyödyntämällä
        Pair<K, V>[] array = (Pair<K, V>[]) new Pair[size];

        if (size > 0) {
            // Käytetään indeksilaskurina yhden alkion taulukkoa rekursiossa. Tämä toimii viittauksena,
            // jolloin indeksi päivittyy kaikille kutsuille
            int[] indexCounter = {0};
            inOrderTraversal(root, array, indexCounter);
        }
        return array;
	}

    // Rekursiivinen "In-Order Traversal"-metodi. Läpinkäyntijärjestys on Vasen -> Nykyinen -> Oikea.
    // Tämä mekanismi käsitteelee alkiot niiden luonnollisessa järjestyksessä.
    private void inOrderTraversal(Node<K, V> node, Pair<K, V>[] array, int[] indexCounter) {
        if (node == null) return;
        // Vasen alipuu
        inOrderTraversal(node.leftChild, array, indexCounter);
        // Lisää käsiteltävän solmun arvo taulukkoon ja kasvata indeksiä
        array[indexCounter[0]++] = new Pair<>(node.key, node.value);
        // Oikea alipuu
        inOrderTraversal(node.rightChild, array, indexCounter);
    }

    // Tilastointi-metodi, joka laskee ja palauttaa tilastot puun rakenteesta.
	@Override
	public BSTStats getStats() {
		BSTStats stats = new BSTStats();
		// STUDENT TODO finish the implementation of this method

        // Lasketaan syvyydet vain jos puu ei ole tyhjä
        if (root != null) {
            stats.maxDepth = calculateMaxDepth(root);
            stats.minDepth = calculateMinDepth(root);
        } else {
            stats.minDepth = 0;
            stats.maxDepth = 0;
        }
        stats.size = size;

		return stats;
	}

    // Apumetodi maksimisyvyyden laskemiseen. Maksimisyvyys = pisin polku juuresta lehteen
    private int calculateMaxDepth(Node<K, V> node) {

        if (node == null) {
            return -1;
        }

        // Rekursiivinen maksimisyvyyden lasku
        int leftMaxDepth = calculateMaxDepth(node.leftChild);
        int rightMaxDepth = calculateMaxDepth(node.rightChild);

        // Palautetaan pidempi reitti
        return Math.max(leftMaxDepth, rightMaxDepth) + 1;
    }

    // Apumetodi minimisyvyyden laskemiseen. Minimysyvyys = lyhin polku juuresta "tyhjään" linkkiin
    private int calculateMinDepth(Node<K, V> node) {
        if (node == null) {
            return -1;
        }
        // Jos solmulla on tyhjä haara se on lyhin polku
        if (node.leftChild == null || node.rightChild == null) {
            return 0;
        }

        // Rekursiivinen minimisyvyyden laski
        int leftDepth = calculateMinDepth(node.leftChild);
        int rightDepth = calculateMinDepth(node.rightChild);

        // Palautetaan lyhempi reitti
        return Math.min(leftDepth, rightDepth) + 1;
    }

	public class BSTStats implements Tree.Stats {
		public int minDepth = Integer.MAX_VALUE;
		public int maxDepth = 0;
		public int size = 0;

		@Override
		public String getStats() {
			return String.format
			(
				"Tree size: %,d%nMin Depth: %d%nMax Depth: %d%n",
				this.size,
				this.minDepth,
				this.maxDepth
			);
		}
	}

}

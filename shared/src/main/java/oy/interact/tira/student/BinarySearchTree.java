package oy.interact.tira.student;

import oy.interact.tira.util.Pair;
import oy.interact.tira.util.Tree;

public class BinarySearchTree<K extends Comparable<K>, V> implements Tree<K, V> {

	int size;
	int maxDepth = 0;
	int depthSum = 0;

    // Puun juurisolmu
    private Node<K, V> root;

    // Privaatti sisäinen solmuluokka
    private static class Node<K, V> {
        K key; // Solmun avain, käytetään hakemiseen ja järjestämiseen
        V value; // Arvo, eli solmuln sisältämä varsinainen data
        Node<K, V> leftChild; // Vasen lapsi solmu = pienemmät avaimet
        Node<K, V> rightChild; // Oikea lapsi solmu = isommat avaimet

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.leftChild = null;
            this.rightChild = null;
        }
    }

	public BinarySearchTree() {
		// STUDENT TODO finish the implementation of this method
		root = null;
        size = 0;

	}

	@Override
	public V add(K key, V value) throws OutOfMemoryError, IllegalArgumentException {
        // STUDENT TODO finish the implementation of this method

        // Esiehtojen tarkistus
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value is null");
        }

        // Jos puu on tyhjä luodaan juurisolmu
        if (root == null) {
            root = new Node<>(key, value);
            size++; // <- Size-muuttujan päivitys
            return null;
        }
        // Kutsutaan allaolevaa rekursiivista apumetodia. Apumetodin käyttö selkeyttää koodia ja
        // pilkkoo sen logiikkaa ymmärrettävämpään muotoon
        return insert(root, key, value);
    }

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

	@Override
	public V get(K key) throws IllegalArgumentException {
		// STUDENT TODO finish the implementation of this method
		return null;
	}

	@Override
	public int size() {
		// STUDENT TODO finish the implementation of this method
		return 0;
	}

	@Override
	public void clear() {
		// STUDENT TODO finish the implementation of this method
	}

	@Override
	public boolean isEmpty() {
		// STUDENT TODO finish the implementation of this method
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Pair<K, V>[] toArray() throws OutOfMemoryError {
		// STUDENT TODO finish the implementation of this method
		return null;
	}

	@Override
	public BSTStats getStats() {
		BSTStats stats = new BSTStats();
		// STUDENT TODO finish the implementation of this method
		stats.minDepth = -1;
		stats.maxDepth = -1;
		stats.size = -1;
		return stats;
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

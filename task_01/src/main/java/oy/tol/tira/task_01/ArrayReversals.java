package oy.tol.tira.task_01;

class ArrayReversals {

	/**
	 * Returns a new array where the elements from input array are in reversed order.
	 * 
	 * When executed, the elements in the returned array are the same, but
	 * in reverse order. Original array is not modified.
	 * 
	 * The array parameter:
	 * - must not be null
	 * - must have at least two elements
	 * - the elements in the array must be non-null.
	 * 
	 * @param input Array to reverse.
	 * @throws IllegalArgumentException If the array is not according to the specs.
	 * @return Returns a new array where the elements of the input array are in reversed order.
	 */	
	static Integer [] reversed(Integer [] input) {
        // Tarkista esiehdot
        preCondition(input);
        // Luo uusi taulukko ja kopioi elementit käänteisessä järjestyksessä
		Integer [] output = new Integer[input.length];
		int index = 0;
		int addIndex = input.length - 1;
		for (/*empty*/; index < input.length; index++, addIndex--) {
            // Assert jolla varmistetaan, että indeksointi etenee oikein taulukkoa kääntäessä
            assert index + addIndex == input.length - 1 : "Invariant fails at index " + index;
            //  2 asserttia jolla tarkistetaan, että indeksit pysyvät taulukon rajoissa
            assert index >= 0 && index < input.length : "Index out of bounds: " + index;
            assert addIndex >= 0 && addIndex < input.length : "AddIndex out of bounds: " + addIndex;
            output[addIndex] = Integer.valueOf(input[index]);
            // Assert jolla tarkistetaan, että alkuperäisen ja uuden taulukon elementit vastaavat toisiaan
            assert input[index].equals(output[addIndex]) : "Input and output do not match!";
        }
        // Assert jolla varmistetaan, että silmukka on käynyt kaikki alkiot läpi
        assert index == input.length : "Silmukka ei käynyt kaikkia alkioita läpi! Index: " + index;
        // Tarkista jälkiehdot
        postCondition(input, output);
		return output;
	}

	static Integer [] copyOf(Integer [] array) {
		Integer [] newArray = new Integer[array.length];
		int addIndex = 0;
		for (Integer value : array) {
			newArray[addIndex++] = value;
		}
		return newArray;
	}

    private static void preCondition(Integer [] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array is null");
        }
        if (array.length < 2) {
            throw new IllegalArgumentException("Array has less than two elements");
        }
        for (Integer value : array) {
            if (value == null) {
                throw new IllegalArgumentException("Array has an element with null value");
            }
        }

    }

    private static void postCondition(final Integer [] input, final Integer [] output) {
        assert input.length == output.length : "Input and Output array length mismatch";
        int o = output.length - 1;
        for (int i = 0; i < input.length; i++, o--) {
            assert input[i].equals(output[o]) : "Element mismatch at index " + i;
        }
    }

}

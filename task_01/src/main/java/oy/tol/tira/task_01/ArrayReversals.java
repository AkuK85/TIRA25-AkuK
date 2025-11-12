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
        // Kutsutaan luokan lopussa olevaa preCondition-metodia tarkistusta varten
        preCondition(input);

        // Luo uusi taulukko ja kopioi elementit käänteisessä järjestyksessä
		Integer [] output = new Integer[input.length];
		int index = 0;
		int addIndex = input.length - 1;

		for (/*empty*/; index < input.length; index++, addIndex--) {
            // Silmukkainvariantit
            // Invariantti 1: Indexien summa, Kahden indeksin tulee olla (pituus -1)
            // Tällä varmistetaan, että indeksointi etenee oikein taulukkoa kääntäessä
            assert index + addIndex == input.length - 1 : "Invariant fails at index " + index;

            // Invriantti 2: Taulukon rajat.
            // Varmistaa että ei lueta tai kirjoiteta taulukon ulkopuolelta.
            assert index >= 0 && index < input.length : "Index out of bounds: " + index;
            assert addIndex >= 0 && addIndex < input.length : "AddIndex out of bounds: " + addIndex;
            output[addIndex] = Integer.valueOf(input[index]);

            // Invariantti 3: Tiedon vastaavuus
            // Varmistaa että kopioitu alkio on sama kuin lähdealkio.
            assert input[index].equals(output[addIndex]) : "Input and output do not match!";
        }
        // Invariantti 4: Silmukan lopetusehtojen tarkistus.
        // Varmistetaan, että silmukka on suoritettu loppuun asti
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

    // Esiehtojen tarkistus-metodi.
    // Heitää poikkeuksen jos algoritmille annettuja esiehtoja ei täytetä. Esiehtojen on täytyttävä jotta
    // algoritmi voidaan ylipäätänsä suorittaa.
    private static void preCondition(Integer [] array) {
        // Ehto 1: Taulukko ei voi olla null
        if (array == null) {
            throw new IllegalArgumentException("Array is null");
        }
        // Ehto 2: Taulukossa on oltava vähintään 2 alkiota
        if (array.length < 2) {
            throw new IllegalArgumentException("Array has less than two elements");
        }
        // Ehto 3: Yksikään taulukon alkioista ei saa olla null
        for (Integer value : array) {
            if (value == null) {
                throw new IllegalArgumentException("Array has an element with null value");
            }
        }

    }

    // Jälkiehtojen tarkistus-metodi.
    // Jälkiehtojen tarkistuksella varmistetaan, että algoritmin tuottama lopputulos vastaa annettuja sääntöjä.
    private static void postCondition(final Integer [] input, final Integer [] output) {
        // Ehto 1: Taulukoiden koko täsmää
        assert input.length == output.length : "Input and Output array length mismatch";
        // Ehto 2: Sisältö on käänteinen
        int o = output.length - 1;
        for (int i = 0; i < input.length; i++, o--) {
            assert input[i].equals(output[o]) : "Element mismatch at index " + i;
        }
    }

}

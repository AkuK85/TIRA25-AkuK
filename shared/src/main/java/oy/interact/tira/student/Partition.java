package oy.interact.tira.student;

import java.util.function.Predicate;

public class Partition {

	private Partition() {
		// empty
	}

	/**
	 * Partition an array using the rule specified with a Predicate.
	 *
	 * Moves all elements that conform to the given rule, to the
	 * beginning of the array. Implementation MUST be stable, that is
	 * the order of the moved elements remain the same as in the original
	 * array.
	 *
	 * Array may contain null elements. One usage of this algorithm may be,
	 * in fact, to move all the nulls to the end of the array. For example:
	 *
	 * Integer [] array = {3, 1, 4, null, 1, 5, 9, null, 2, 6, 5, null, 4};
    * System.out.println(Arrays.toString(array));
    * int index = Partition.byRule(array, number -> number != null );
    * System.out.format("Index of first null element: %d%n", index);
    * System.out.println(Arrays.toString(array));
	 *
	 * prints out:
	 *
	 * [3, 1, 4, null, 1, 5, 9, null, 2, 6, 5, null, 4]
    * Index of first null element: 10
    * [3, 1, 4, 1, 5, 9, 2, 6, 5, 4, null, null, null]
	 *
	 * As you can see, the conforming values (non-nulls) are in the same order as before,
	 * so the algorithm is stable.
	 *
	 * @param array The array to partition, must not be null.
	 * @param size The count of elements in the array; index up to (not including) the array is partitioned to.
	 * @param rule Rule to use in determining which elements should be moved to prefix of array.
	 * @return The index that contains the first element _not_ conforming to the rule,
	 * or the value of parameter size, if no conforming elements were found.
	 * @throws IllegalArgumentException if either the array or rule is null.
	 */
	public static <T> int byRule(T[] array, final int size, Predicate<T> rule) throws IllegalArgumentException {

        // STUDENTS TODO implement partitioning by rule

        // Esiehtojen tarkistus
        if (array == null || rule == null) {
            throw new IllegalArgumentException("Array and rule must not be null");
        }
        if (size < 0 || size > array.length) {
            throw new IllegalArgumentException("Size must be within array bounds");
        }

        // Vaihe 1: Etsitään ensimmäinen sääntöä rikkova alkio
        // Tämä on optimointia jonka avulla ohitetaan kaikki alussa jo valmiiksi oikealla paikkala olevat alkiot
        int index = 0;
        while (index < size && rule.test(array[index])) {
            index++;
        }

        // Jos kaikki alkiot noudattivat sääntöä, koko taulukko on ositettu
        if (index == size) {
            return index;
        }

        // Vaihe 2: Käsitellään loput taulukosta
        // "Index" osoittaa sääntöä rikkovaan alkioon
        // "nextIndex" etsii seuraavaa sääntöä noudattavaa alkiota vaihdettavaksi
        int nextIndex = index + 1;

        while (nextIndex < size) {
            // Jos "nextIndex" löytää sääntöä noudattavan alkion
            if (rule.test(array[nextIndex])) {
                // Vaihdetaan se "index":ssä olevan sääntöä rikkovan alkion kanssa
                ArrayUtils.swap(array, nextIndex, index);
                // Siirretään sääntöjärikkovien alkioiden rajaa yhdellä eteenpäin
                index++;
            }
            //Jatketaan seuraavan alkion tarkistamista
            nextIndex++;
        }
        // Palautetaan indeksi joka osoittaa ensimmäiseen alkioon joka ei noudata sääntöä
        return index;
    }



	/**
	 * Filters content from the input array to a new array, using a predicate.
	 * 
	 * The input array must not change in any way during the filtering.
	 * 
	 * @param array The array to filter contents from.
	 * @param count The number of elements in the array, filtering is done between 0..<count.
	 * @param predicate The predicate to use in determining which items to include in the filtered array.
	 * @return A new array with content from parameter arrary, with only elements conforming to the predicate.
	 * @throws IllegalArgumentException if either the array or rule is null.
	 */
	public static <T> T [] filter(final T [] array, int count, Predicate<T> predicate) throws IllegalArgumentException {
        // Esiehtojen tarkistus
        if (array == null || predicate == null) {
            throw new IllegalArgumentException("Array and predicate must not be null");
        }
        if (count < 0 || count > array.length) {
            throw new IllegalArgumentException("Count must be between 0 and the length of the array");
        }
        // Luodaan väliaikainen kopio jotta alkuperäinen säilyy muuttumattomana
        T[] tempArray = ArrayUtils.copyOf(array, count);
        // Osioidaan kopio, eli siirretään kaikki halutut alkiot taulukon alkuun
        // ja palautetaan haluttujen alkioiden määrä
        int newSize = byRule(tempArray, count, predicate);
        // Luo ja plauta uusi täsmälleen oikean kokoinen taulukko
        return ArrayUtils.copyOf(tempArray, newSize);

		// STUDENTS TODO implement filtering by rule, using Partition.byRule
	}

}

package oy.interact.tira.student;

import java.util.Comparator;

public class BinSearch {
	private BinSearch() {
		// empty
	}


   ///////////////////////////////////////////
   // Recursive binary search bw indices
   ///////////////////////////////////////////

   /**
    * Does a binary search on the array, returning the index of the found
    * value or -1 if one cannot be found.
    *
    * The array to search, must not be null, nor contain null elements in
    * between fromIndex...toIndex, inclusive. 
    *
    * Array must be sorted in natural order, based on T.compareTo.
    *
    * @param aValue A value to find, must not be null.
    * @param fromArray The array to search, must not be null.
    * @param fromIndex The index to start the search from, inclusive.
    * @param toIndex The index to search to, inclusive.
    * @return The index of the element found, or -1 if one was not found.
    */
   public static <T extends Comparable<T>> int searchRecursively(T aValue, T[] fromArray, int fromIndex, int toIndex) {
       // Esiehtojen tarkistus
       if (null == aValue || null == fromArray || fromIndex < 0 || toIndex >= fromArray.length) {
              throw new IllegalArgumentException("Invalid arguments for search.");
       }

       // Rekursion perusrakenne aka. Base Case. Jos aloitusindeksi on suurempi kuin lopetus indeksi,
       // silloin alkio ei ole taulukossa
       if (fromIndex > toIndex) {
           return -1; // <- Elementtiä ei löydy
         }

       // Puolitus: Lasketaan keskikohta turvallisesti välttäen mahdollinen kokonaisluvun ylivuoto
       int midIndex = fromIndex + (toIndex - fromIndex) / 2;

       // Verrataan etsittävää arvoa keskimmöiseen alkioon. Tallennnetaan tulos jotta compareTo-metodia
       // ei tarvitse kutsua useasti if-else-if rakenteessa
       int compareResult = aValue.compareTo(fromArray[midIndex]);

       // Haku:
       if (compareResult == 0) {
           // Alkio löytyi keksikohdasta
           return midIndex;
       } else if (compareResult < 0) {
           // Etsittävä alkio on pienempi, joten jatketaan hakua rekursiivisesti taulukon vasemmalta puolelta
           return searchRecursively(aValue, fromArray, fromIndex, midIndex - 1);
       } else {
           // Etsittävä alkio on suurempi, joten jatketaan hakua rekursiivisesti taulukon oikealta puolelta
           return searchRecursively(aValue, fromArray, midIndex + 1, toIndex);
         }

         // return Integer.MAX_VALUE;
       // STUDENTS TODO recursive implementation
   }

   ///////////////////////////////////////////
   // Binary search using a Comparator
   ///////////////////////////////////////////

   /**
    * Does a binary search on the array, returning the index of the found
    * value or -1 if one cannot be found.
    *
    * The array to search, must not be null, nor contain null elements in
    * between fromIndex...toIndex, inclusive. 
    *
    * Array must be sorted in the same order than the Comparator specifies.
    *
    * @param aValue A value to find, must not be null.
    * @param fromArray The array to search, must not be null.
    * @param fromIndex The index to start the search from, inclusive.
    * @param toIndex The index to search to, inclusive.
    * @param comparator The comparator to use in search.
    * @return The index of the element found, or -1 if one was not found.
    */
   public static <T> int searchRecursively(T aValue, T[] fromArray, int fromIndex,
         int toIndex, Comparator<T> comparator) {
       // Esiehtojen tarkistus
       if (null == aValue || null == fromArray || null == comparator || fromIndex < 0 ||
                 toIndex >= fromArray.length) {
           throw new IllegalArgumentException("Invalid arguments for search.");
       }

       // BaseCase
       if (fromIndex > toIndex) {
           return -1; // <- Ei löydy
       }

       // Jako: Ylivuoto-turvallinen keskikohdan lasku
       int midIndex = fromIndex + (toIndex - fromIndex) / 2;

       // Erona edelliseen metodiin, vertailu delegoidaan annetulle Comparator-oliolle
       int compareResult = comparator.compare(aValue, fromArray[midIndex]);

       // Haku:
       if (compareResult == 0) {
           return midIndex; // <- Löytyi
       } else if (compareResult < 0) {
           // Vasemmanpuolen rekursiivinen kutsu
           return searchRecursively(aValue, fromArray, fromIndex, midIndex - 1, comparator);
       } else {
           // Oikeanpuolen rekursiivinen kutsu
           return searchRecursively(aValue, fromArray, midIndex + 1, toIndex, comparator);
       }

              // return Integer.MAX_VALUE;
       // STUDENTS TODO recursive implementation
   }

}

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
         if (fromIndex > toIndex) {
              return -1; // Elementtiä ei löydy
         }
         // Keskikohdan indeksin laskeminen välttäen mahdollisen ylivuodon
         int midIndex = fromIndex + (toIndex - fromIndex) / 2;
         // Comparable rajapinnan compareTo metodin kutsu. Tallenetaan tulos muuttujaan jotta ei tarvitse
         // kutsua metodia useasti if-else if -rakenteessa.
         int compareResult = aValue.compareTo(fromArray[midIndex]);

         if (compareResult == 0) {
             return midIndex; // Elementti löytyi
         } else if (compareResult < 0) {
             return searchRecursively(aValue, fromArray, fromIndex, midIndex - 1); // Rekursiivinen kutsu vasemmalle puolelle
         } else {
             return searchRecursively(aValue, fromArray, midIndex + 1, toIndex); // Rekursiivinen kutsu oikealle puolelle
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

       if (null == aValue || null == fromArray || null == comparator || fromIndex < 0 ||
                 toIndex >= fromArray.length) {
                 throw new IllegalArgumentException("Invalid arguments for search.");
            }
              if (fromIndex > toIndex) {
                 return -1;
              }

              int midIndex = fromIndex + (toIndex - fromIndex) / 2;
              int compareResult = comparator.compare(aValue, fromArray[midIndex]);

              if (compareResult == 0) {
                return midIndex;
              } else if (compareResult < 0) {
                return searchRecursively(aValue, fromArray, fromIndex, midIndex - 1, comparator); // Rekursiivinen kutsu vasemmalle puolelle
              } else {
                return searchRecursively(aValue, fromArray, midIndex + 1, toIndex, comparator); // Rekursiivinen kutsu oikealle puolelle
              }

              // return Integer.MAX_VALUE;
       // STUDENTS TODO recursive implementation
   }

}

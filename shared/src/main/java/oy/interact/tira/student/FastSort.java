package oy.interact.tira.student;

import java.util.Comparator;

import oy.interact.tira.util.NotYetImplementedException;

public class FastSort {

	private FastSort() {
		// empty
	}

	////////////////////////////////////
   // FastSort sorting to natural order
   ////////////////////////////////////

   public static <E extends Comparable<E>> void sort(E[] array, int begin, int end) {
      if (begin < end) {
          int partitionIndex = partition(array, begin, end);
          sort(array, begin, partitionIndex - 1);
          sort(array, partitionIndex + 1, end);
      }
   }

   private static <E extends Comparable<E>> int partition(E[] array, int low, int high) {
        E pivot  = array[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                ArrayUtils.swap(array, i, j);
            }
        }
        ArrayUtils.swap(array, i + 1, high);
        return i + 1;
   }


   ///////////////////////////////////////////////////////
   // FastSort sorting to order determined by the Comparator
   ///////////////////////////////////////////////////////

   public static <E> void sort(E[] array, Comparator<E> comparator) {
      sort(array, 0, array.length - 1, comparator);
   }

   public static <E> void sort(E[] array, int begin, int end, Comparator<E> comparator) {
      if  (begin < end) {
          int partitionIndex = partition(array, begin, end, comparator);
          sort(array, begin, partitionIndex - 1, comparator);
          sort(array, partitionIndex + 1, end, comparator);
      }
   }

   private static <E> int partition(E[] array, int low, int high, Comparator<E> comparator) {
        E pivot =  array[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                ArrayUtils.swap(array, i, j);
            }
        }
        ArrayUtils.swap(array, i + 1, high);
        return i + 1;
   }

}

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

   // Rekursiivinen päämetodi, hyödyntäen Comparable-rajapintaa. Toteutus perustuu luentomateriaalissa esitettyyn
   // pseudokoodiin.
   public static <E extends Comparable<E>> void sort(E[] array, int begin, int end) {
        // Rekursion perusrakenne (Base Case)
        if (begin < end) {
            // Jako: Kutsumalla partition-metodia, osioidaan taulukko sekä haetaan pivot-alkion lopullinen indeksi
            int partitionIndex = partition(array, begin, end);
            // Lajittelu:
            // Rekursiivinen kutsu pivot-indeksiä edeltävälle osataulukolle
            sort(array, begin, partitionIndex - 1);
            // Rekursiivinen kutsu pivot-indeksin jälkeiselle osataulukolle
            sort(array, partitionIndex + 1, end);
      }
   }

   // Lomuto-osiointialgoritmi, eli ns. oppikirjaversio. Perustuu luentomateriaalin pseudokoodiin.
   private static <E extends Comparable<E>> int partition(E[] array, int low, int high) {
        // Pivot-muuttuja joka on Lomuton tapauksessa taulukon viimeinen alkio
        E pivot  = array[high];
        // "i" osoittaa viimeisimmän alkion joka on pivotia pienempi, näin pidetään kirjaa "< pivot"-rajasta
        int i = (low - 1);
        for (int j = low; j < high; j++) { // <- Käydään taulukko läpi (poislukien pivot-muuttuja)
            if (array[j].compareTo(pivot) <= 0) { // <- Jos alkio on pivotia pienempi tai yhtäsuuri...
                i++; // <- ...kasvatetaan "pienempien" osiota...
                ArrayUtils.swap(array, i, j); // <- ...ja vaihdetaan alkio osion sisään
            }
        }
       // Siirretän pivot-alkio oikealle paikalleen, eli kaikkien sitä pienempien alkioiden jälkeen
       // Lopuksi palautetaan pivot-alkion lopullinen indeksi
        ArrayUtils.swap(array, i + 1, high);
        return i + 1;
   }


   ///////////////////////////////////////////////////////
   // FastSort sorting to order determined by the Comparator
   ///////////////////////////////////////////////////////

   public static <E> void sort(E[] array, Comparator<E> comparator) {
        sort(array, 0, array.length - 1, comparator);
   }

   // Rekursiivinen päämetodi Comparator-vertailijalla. Logiikka identtinen Comparable-version kanssa.
   public static <E> void sort(E[] array, int begin, int end, Comparator<E> comparator) {
      if  (begin < end) {
          int partitionIndex = partition(array, begin, end, comparator);
          sort(array, begin, partitionIndex - 1, comparator);
          sort(array, partitionIndex + 1, end, comparator);
      }
   }

   // Lomuto-osiointialgoritmi, hyödynnetään Comparable-rajapinnan sijasta annettua Comparator-oliota vertailuun.
   private static <E> int partition(E[] array, int low, int high, Comparator<E> comparator) {
        E pivot =  array[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            // Ainoa ero tässä versus Comparable-version kanssa:
            // aValue.compareTo(b) kutsu on korvattu comparator.compare(aValue, b)-kutsulla
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                ArrayUtils.swap(array, i, j);
            }
        }
        ArrayUtils.swap(array, i + 1, high);
        return i + 1;
   }

}

package oy.interact.tira.task_04;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

import oy.interact.tira.student.Partition;
import oy.interact.tira.student.Person;
import oy.interact.tira.student.FastSort;
import oy.interact.tira.student.ArrayUtils;
import oy.interact.tira.util.PersonUtils;
import oy.interact.tira.util.SimpleSort;

public class SortApp {

	private static class Measurement {
		int n;
		long algorithm1Duration;
		long algorithm2Duration;
		long algorithm3Duration;

		@Override
		public String toString() {
			return String.format("%d,%d,%d,%d", n, algorithm1Duration, algorithm2Duration, algorithm3Duration);
		}
	}

	private static FileWriter writer = null;
	private static Measurement currentMeasurement = new Measurement();

	private static boolean printDetails = true;

	public static void main(String[] args) {

		try {
			// To see examples of how to use Comparator, uncomment the line below:
			// comparatorExamples();

			// For writing time performance measurements to file.
			writer = new FileWriter(new File("measurements.csv"));
			writer.write("N,InsertionSort,FastSort,Filtering\n");

			final int MIN_VALUE = 100_000;
			final int MAX_VALUE = 100_000;
			final int STEP_INCREASE = 1_000;
			for (int size_of_n = MIN_VALUE; size_of_n <= MAX_VALUE; size_of_n += STEP_INCREASE) {
				Person[] persons = PersonUtils.createPersons(size_of_n);
				currentMeasurement.n = persons.length;
				ArrayUtils.shuffle(persons);
				if (printDetails) System.out.println("\nStarting to count by using SimpleSort.insertionSort...");
				countByInsertionSorting(persons);

				ArrayUtils.shuffle(persons);
				if (printDetails) System.out.println("\nStarting to count by using FastSort.sort...");
				countByFastSorting(persons);

				ArrayUtils.shuffle(persons);
				if (printDetails) System.out.println("\nStarting to count by using Partition.filter...");
				countByFiltering(persons);

				writer.write(currentMeasurement.toString());
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.format("\n*** ERROR: something went wrong!: %s%n", e.getMessage());
		}
	}

    // Hitaampaa lisäyslajittelua hyödyntävä laskenta-metodi.
	private static void countByInsertionSorting(final Person[] persons) {
		int totalCount = 0;
		long start = System.currentTimeMillis();

        // Comparator-muodostin jolla määritellään lajittelusääntö. Tässä tapauksessa vertaillaan sukunimien pituutta
        Comparator<Person> lastNameLengthComparator = Comparator.comparingInt(p -> p.getLastName().length());

        // Lajittele: Kutsutaan lisäyslajittelun metodia.
        // Aika kompleksisuus, paras mahdollinen tapaus = O(n) tai huonoin mahdollinen tapaus O(n^2)
        SimpleSort.insertionSort(persons, lastNameLengthComparator);

        // Tulostetaan taulukon lyhimmän ja pisimmän nimen omaavien person-olioiden atribuutit sekä
        // sukunimen kirjaimien määrä. Taulukon ollessa nyt lajiteltuna pituuden mukaan lyhin alussa ja pisin lopussa.
        if (printDetails) {
            System.out.println("Shortest last name was " + persons[0] + ", " + persons[0].getLastName().length() +
                    " characters");
            System.out.println("Longest last name was " + persons[persons.length - 1] + ", " +
                    persons[persons.length - 1].getLastName().length() + " characters");
        }

        // Lasketaan jakauma: Käydään lajiteltu taulukko läpi kerran
        if (persons.length > 0) {
            int currentLength = persons[0].getLastName().length();
            int countForLength = 0;
            for (Person person : persons) {
                // Jos pituus on sama, kasvatetaan laskuria
                if (person.getLastName().length() == currentLength) {
                    countForLength++;
                } else {
                    // Pituus vaihtui, joten tulosteaan edelleisen pituuden summa
                    if (printDetails) System.out.format("Name length: %5d count: %d%n", currentLength, countForLength);
                    totalCount += countForLength;
                    // Aloitetaan uuden pituuden laskenta
                    currentLength = person.getLastName().length();
                    countForLength = 1;
                }
            }
            // Tulostetaan viimeinen ryhmä
            if (printDetails) System.out.format("Name length: %5d count: %d%n", currentLength, countForLength);
            totalCount += countForLength;
        }

        // Tulostetaan laskennan detailit
        if (printDetails) System.out.format("Handled %,d persons out of %,d%n", totalCount, persons.length);
		long duration = System.currentTimeMillis() - start;
		currentMeasurement.algorithm1Duration = duration;
		System.out.format("InsertionSort: N: %d - duration of counting all name lengths: %d ms%n", persons.length,
				duration);
	}

    // Laskenta-metodi joka hyödyntää nopeampaa pikalajittelu-metodia.
	private static void countByFastSorting(final Person[] persons) {
		int totalCount = 0;
		long start = System.currentTimeMillis();

        // Comparator-muodostin
        Comparator<Person> lastNameLengthComparator = Comparator.comparingInt(p -> p.getLastName().length());

        // Lajittele: Pikalajittelu-metodin kutsu
        // Paras tapaus = O(n log n), Huonoin tapaus = O(n^2)
        FastSort.sort(persons, lastNameLengthComparator);

        // Sama tulostus kuin edellisessä
        if  (printDetails) {
            System.out.println("Shortest last name was " + persons[0] + ", " + persons[0].getLastName().length() +
                    " characters");
            System.out.println("Longest last name was " + persons[persons.length - 1] + ", " +
                    persons[persons.length - 1].getLastName().length() + " characters");
        }

        // Laske jakauma, sama logiikka kuin edellisessä
        if (persons.length > 0) {
            int currentLength = persons[0].getLastName().length();
            int countForLength = 0;
            for (Person person : persons) {
                if (person.getLastName().length() == currentLength) {
                    countForLength++;
                } else {
                    if (printDetails) System.out.format("Name length: %5d count: %d%n", currentLength, countForLength);
                    totalCount += countForLength;
                    currentLength = person.getLastName().length();
                    countForLength = 1;
                }
            }
            if (printDetails) System.out.format("Name length: %5d count: %d%n", currentLength, countForLength);
            totalCount += countForLength;
        }

        // Sama tulostus kuin edellisessä
		if (printDetails) System.out.format("Handled %,d persons out of %,d%n", totalCount, persons.length);
		long duration = System.currentTimeMillis() - start;
		currentMeasurement.algorithm2Duration = duration;
		System.out.format("FastSort: N: %d - duration of counting all name lengths: %d ms%n", persons.length, duration);
	}

    // Laskenta-metodi hyödyntäen taulukon suodatusta.
	private static void countByFiltering(final Person[] persons) {
		int totalCount = 0;
		long start = System.currentTimeMillis();

        // Etsitään lyhin nimi kutsumalla findEdgeValue-metodia ja hyödyntämällä BiPredikaatia
        // BiPredicate palauattaa true jos p1:n pituus on lyhempi kuin p2:n (ensimmäinen läpikäynti O(n))
        Person shortest = ArrayUtils.findEdgeValue(persons, (p1, p2) -> p1.getLastName().length() <
                p2.getLastName().length());
        int shortestLastName = shortest.getLastName().length();

        // Etsitään pisin nimi samalla vertailulla (toinen läpikäynti O(n))
        Person longest = ArrayUtils.findEdgeValue(persons, (p1, p2) -> p1.getLastName().length() >
                p2.getLastName().length());
        int longestLastName = longest.getLastName().length();

        // Tulostus
        if (printDetails) {
            System.out.println("Shortest last name was " + shortest + ", " + shortestLastName + " characters");
            System.out.println("Longest last name was " + longest + ", " + longestLastName +  " characters");
        }

        // Käydään läpi findEdgeValuen avulla löydetty pituusalue
        for (int length = shortestLastName; length <= longestLastName; length++) {
            final int currentLength = length;
            // Suodatus: O(n)
            // Luodaan uusi taulukko joka sisältää vain ne henkilöt joiden sukunimenpituus = currentLength
            Person[] filtered = Partition.filter(persons, persons.length, p -> p.getLastName().length() == currentLength);
            // Tulostettava tulos saadaan suoraan suodatetun taulukon pituudesta
            if (filtered.length > 0) {
                if (printDetails) System.out.format("Name length: %5d count: %d%n", currentLength, filtered.length);
                totalCount += filtered.length;
            }
        }

        // Lopputulostus
		if (printDetails) System.out.format("Handled %,d persons out of %,d%n", totalCount, persons.length);
		long duration = System.currentTimeMillis() - start;
		currentMeasurement.algorithm3Duration = duration;
		System.out.format("filtering: N: %d - duration of counting all name lengths: %d ms%n", persons.length, duration);
	}

	// Examples of using Comparator in different ways
	// Use these as samples to follow in how you create and use Comparators.
	private static void comparatorExamples() {

		final Integer[] pi = { 3, 1, 4, 1, 5, 9, 2, 6, 5, 4 };

		// Sort to to natural order without comparators, using Integer.compareTo
		Integer[] array = ArrayUtils.copyOf(pi);
		SimpleSort.insertionSort(array);
		printArray(array, ", ");

		// Sort to to natural order using Comparator.naturalOrder()
		array = ArrayUtils.copyOf(pi);
		SimpleSort.insertionSort(array, Comparator.naturalOrder());
		printArray(array, ", ");

		// Sort to to reverse order using Comparator.reverseOrder()
		array = ArrayUtils.copyOf(pi);
		SimpleSort.insertionSort(array, Comparator.reverseOrder());
		printArray(array, ", ");

		// Sort to to natural order using our own Comparator
		array = ArrayUtils.copyOf(pi);
		class AscendingOrderComparatorForIntegers implements Comparator<Integer> {
			@Override
			public int compare(Integer first, Integer second) {
				return first - second;
			}
		}
		SimpleSort.insertionSort(array, new AscendingOrderComparatorForIntegers());
		printArray(array, ", ");

		// Sort to to reverse order using our own Comparator
		array = ArrayUtils.copyOf(pi);
		class DescendingOrderComparatorForIntegers implements Comparator<Integer> {
			@Override
			public int compare(Integer first, Integer second) {
				return second - first;
			}
		}
		SimpleSort.insertionSort(array, new DescendingOrderComparatorForIntegers());
		printArray(array, ", ");

		// Sort to natural order by creating the comparator in the parameter
		array = ArrayUtils.copyOf(pi);
		SimpleSort.insertionSort(array, (first, second) -> first - second);
		printArray(array, ", ");

		// Sort to age order by using Comparator.comparingInt
		try {
			Person [] personArray = PersonUtils.createPersons(10);
			Comparator<Person> comparator = Comparator.comparingInt(Person::getAge);
			SimpleSort.insertionSort(personArray, comparator);
			printArray(personArray, "\n");
		} catch (IOException e) {
			System.err.format("Failed to create person array: %s%n", e.getMessage());
		}
	}

	private static <T> void printArray(final T [] array, final String separator) {
		int counter = 0;
		System.out.print("[");
		for (T element : array) {
			System.out.format("%s", element);
			if (counter < array.length - 1) {
				System.out.print(separator);
			}
		}
		System.out.println("]");
	}
}

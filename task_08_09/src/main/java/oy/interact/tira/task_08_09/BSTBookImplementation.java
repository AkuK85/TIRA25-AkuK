package oy.interact.tira.task_08_09;

import oy.interact.tira.student.BinarySearchTree;
import oy.interact.tira.util.Pair;
import oy.interact.tira.factories.BSTFactory;


// Soveltavan tehtävän osa jossa implementoidaan BST:n toteutus osaksi BooksAndWordsApp:a.
public class BSTBookImplementation extends BookBase {

    // Tietorakenne sanojen tallennukseen.
    private BinarySearchTree<String, Integer> wordCounts;

    // Tyhjä muodostin
    public BSTBookImplementation() {
    }

    // Luodaan BST-tietorakenne, tätä metodia kutsutaan BookBase luokan countUniqueWords-metodin alussa.
    @Override
    protected void createDataStructure() {
        // BSTFactoryn avulla luoddan oikeanlainen tietorakenne.
        this.wordCounts = (BinarySearchTree<String, Integer>) BSTFactory.createStringIntegerBST();
    }

    // Insert-metodi lisää sanan puuhun tai päivittää sen esiintymismäärän
    @Override
    protected void insert(final String word) {
        // Tarkistetaan onko sana jo puussa
        Integer currentCount = wordCounts.get(word);

        if (currentCount == null) {
            // Uusi sana: lisätään arvolla 1
            wordCounts.add(word, 1);
        } else {
            // Vanha sana, päivitetään arvo +1
            wordCounts.add(word, currentCount + 1);
        }
    }

    // Metodi palauttaa uniikkien sanojen määrän eli toisin sanottuna puun koon.
    @Override
    protected int uniqueWordCount() {
        if (wordCounts == null) {
            return 0;
        }
        return wordCounts.size();
    }

    // Tämä metodi palauttaa kaikki sanat ja laskurit taulukkona. Metodi hyödyntää BST:n toArrray-metodia,
    // joka palauttaa alkiot luonnollisessa järjestyksessä.
    @Override
    @SuppressWarnings("unchecked")
    protected Pair<String,Integer>[] getAllWordCounts() {
        if (wordCounts == null) {
            return new Pair[0];
        }
        return wordCounts.toArray();
    }

    // "Resetointi"-metodi, eli tietorakenne tyhjennetään ja muisti vapautetaan.
    @Override
    protected void resetDataStructure() {
        if (wordCounts != null) {
            wordCounts.clear();
            wordCounts = null;
        }
    }

    // Metodi palauttaa tämän toteutuksen tietorakenteen nimen
    @Override
    public String getImplementationName() {
        return "Binary Search Tree";
    }

    // Hakee sanan esiintymismäärän
    @Override
    protected int getWordCount(String word) {
        Integer count = wordCounts.get(word);
        return (count != null) ? count : 0;
    }

}

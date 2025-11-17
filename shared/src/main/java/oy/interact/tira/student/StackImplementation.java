package oy.interact.tira.student;

import oy.interact.tira.util.StackInterface;


// Geneerinen taulukkopohjainen pino-tietorakenne.
// Käyttäää sisäistä dyynaamisesti kasvavaa taulukkoa. Toteuttaa StackInterface-rajapinnan.
public class StackImplementation<E>  implements StackInterface<E> {

    // Pinon oletuskapasiteetti
    private static final int DEFAULT_STACK_SIZE = 16;
    // Taulukko johon pinon alkiot tallennetaan
    private Object [] itemArray;
    // Indeksi joka osoittaa pinon päällimmäiseen alkioon, arvon ollessa -1 on pino tyhjä
    private int topIndex;

    // Luodaan pino oletuskapisettiilla
    public StackImplementation() {
        this.itemArray = new Object[DEFAULT_STACK_SIZE];
        this.topIndex = -1; // <- Tyhjä pino
    }

    // Luodaan pino annnetulla kapasiteetilla
    public StackImplementation(int capacity) {
        // Esiehtojen tarkistus
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be greater than zero.");
        }

        this.itemArray = new Object[capacity];
        this.topIndex = -1;
    }

    // Palautttaa pinon nykyisen kapasiteetin(ohjeiden mukaisesti = taulukon koko), aikakompleksisuus O(1)
    @Override
    public int capacity() {
        return itemArray.length;
    }

    // Push-metodi joka työntää käsiteltävän alkion pinon päälle. Jos pino on täynnä kutsutaan reallocate-metodia
    // jonka avulla pinon kapasitettiia kasvatetaan. Aikakompleksisuus O(1), paitsi jos tarvitaan reallokaatiota
    // jolloin aikakompleksisuus on O(n)
    @Override
    public void push(E element) throws OutOfMemoryError, NullPointerException {
        // Esiehtojen tarkistus
        if (element == null) {
            throw new IllegalArgumentException("element is null");
        }

        // Tehdään tarkistus onko pinossa tilaa vai tarvitaanko reallokaatiota,
        // tämä on tehtävä ennen uuden alkion lisäämistä
        if (topIndex + 1 == itemArray.length) {
            reallocate();
        }
        // Siirretään topIndex-muuttuja osoittamaan uuteen vapaaseen paikkaan ja lisätään alkio
        topIndex++;
        itemArray[topIndex] = element;
    }

    // Pop-metodi poistaa ja palauttaa alkion pinon päältä, aikakompleksisuus on O(1)
    @Override
    public E pop() throws IllegalStateException {
        // Esiehtojen tarkistus
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        // Otetaan päällimäinen alkio talteen
        E element = (E) itemArray[topIndex];
        // Poistetaan viittaus olioon taulukosa muistivuotojen estämiseksi
        itemArray[topIndex] = null;
        // Siirretään topIndex-muuttuja osoittamaan pinon päällimmäistä alkiota
        topIndex--;
        // palautetaan talletettu alkio
        return element;
    }

    // Peek-metodi palauttaa pinon päällimäisen alkion poistamatta sitä, aikakomplksisuus O(1)
    @Override
    public E peek() throws IllegalStateException {
        // Esiehtojen tarkistus
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty (peek)");
        }
        // Palautetaan päällimmäinen alkio, mutta ei muuteta topIndex-muuttujan arvoa
        E element = (E) itemArray[topIndex];
        return element;
    }

    // Size-metodi palauttaa pinossa olevien alkioiden määrän, aikakompleksisuus O(1)
    @Override
    public int size() {
        // Palauetaan suoraan laskettavissa oleva tieto, eikä lasketa silmukalla (silmukka olisi O(n))
        return topIndex + 1;
    }

    // Empty-metodilla tarkistetaan onko pino tyhjä, aikakompleksisuus O(1)
    @Override
    public boolean isEmpty() {
        // Toteutus tarkistamalla topIndex-muuttujan arvo
        return topIndex == -1;
    }

    // Clear-metodi tyhjentää pinon ja palauttaa kapasiteetiin oletusarvoon
    @Override
    public void clear() {
        // Ohjeistuksen mukainen O(1) aikakompleksisuus saavutetaan kun vanhan datan silmukoinnin sijasta luodaan
        // uusi vakiokokoinen taulukko ja korvataan tyhjennettävä taulukko sillä. Javan roskienkerääjä hoitaa vanhan
        // taulukon muistin vapauttamisen
        this.itemArray = new Object[DEFAULT_STACK_SIZE];
        this.topIndex = -1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (int i = 0; i <= topIndex; i++) {
            builder.append(itemArray[i]);
            if (i < topIndex) {
                builder.append(", ");
            }
        }

        builder.append("]");
        return builder.toString();
    }

    // Privaatti reallocate-apumetodi joka kasvattaa pino-taulukon kokoa luomalla uuden kaksi kertaa isomman taulukon
    // ja kopioimalla vanhan taulukon alkiot siihen
    private void reallocate() {
        int oldCapacity = itemArray.length;
        int newCapacity = oldCapacity * 2;

        // Muistiylivuodon tarkistus
        if (newCapacity <= oldCapacity) {
            if (oldCapacity == Integer.MAX_VALUE) {
                throw new OutOfMemoryError("Stack is at maximum capacity");
            }
            newCapacity = Integer.MAX_VALUE;
        }

        // Luodaan uusi taulukko
        Object[] newArray = new Object[newCapacity];
        // Kopioidaan vanhan taulukon alkiot uuteen taulukkoon
        for (int i = 0; i <= topIndex; i++) {
            newArray[i] = itemArray[i];
        }
        // Korvataan vanha taulukko uudella
        itemArray = newArray;
    }

}

package oy.interact.tira.student;

import oy.interact.tira.util.QueueInterface;

// Geneerinen, taulukkopohjainen jonotietorakenne.
// Käyttää dynaamisesti kasvavaa kiertävää taulukkoa, noudatttaa FIFO, eli "first-in, first-out" periaatetta.
public class ArrayQueue<E> implements QueueInterface<E> {

    private Object[] itemArray;
    // Indeksi joka osoittaa jonon ensimmäiseen paikkaan, eli "pää"
    private int head;
    // Indeksi joka oisoittaa jonon perällä olevaan seuraavaan vapaaseen paikkaan, eli "häntä"
    private int tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    // Jonon muodostin oletuskapasiteetilla.
    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    // Jonon muodostin annetulla kapasiteetilla.
    public ArrayQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        itemArray = new Object[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    // Capacity-metodi palauttaa jonon nykyisen fyysisen kapasiteetin O(1).
    @Override
    public int capacity() {
        return itemArray.length;
    }

    // Enqueue-metodi lisää alkion jonon häntäpäähän. Jos jono on täynnä kutsutaan reallocate-metodia.
    // Aikakopmpleksisuus on O(1), paitsi reallokaation yhteydessä,jolloin se on O(n).
    @Override
    public void enqueue(E element) throws OutOfMemoryError, NullPointerException {
        // Esiehtojen tarkistus
        if (element == null) {
            throw new NullPointerException("Element cannot be null.");
        }
        // Tarkistetaan onko taulukko täynnä ja reallokaatiolle tarvetta
        if (size == itemArray.length) {
            reallocate();
        }
        // Lisätään alkio tail-osoittimen kohdalle
        itemArray[tail] = element;
        // Kasvatetaan loogista kokoa
        size++;
        // Siirretään tail osoitinta eteenpäin, varmistaen taulukon kiertäminen hyödyntämällä modulo-operaattoria
        tail = (tail + 1) % itemArray.length;
    }

    // Dequeue-metodi poistaa ja palauttaa alkion jonon alkupäästä. Aikakompleksisuus on O(1).
    @Override
    public E dequeue() throws IllegalStateException {
        // Esiehtojen tarkistus
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        // Otetaan head-osoittimen kohdalla oleva alkio talteen
        E element = (E) itemArray[head];
        // Poistetaan viittaus käsiteltävään olioon taulukosta muistivuotojen estämiseksi
        itemArray[head] = null;
        // Pienennetään loogista kokoa
        size--;
        //  Taas modulo-operaattoria hyödyntämällä siirretään head-osoitinta eteeenpäin
        head = (head + 1) % itemArray.length;

        // Palautetaan käsiteltävänä oleva alkio
        return element;
    }

    // Element-metodi palauttaa jonon ensimmäisen alkion poistamatta sitä. Aikakompleksisuus O(1).
    @Override
    public E element() throws IllegalStateException {
        // Esiehtojen tarkistus
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        // Tallennetaan head-osoittimen kohdalla oleva alkio, ei muuteta head-, tai size-muuttujia
        E element = (E) itemArray[head];
        // Palautetaan alkio
        return element;
    }

    // Size-metodi palauttaa jonon loogisen koon, eli siinä olevien alkioiden lukumäärän. Aikakompleksisuus O(1)
    @Override
    public int size() {
        // Palautetaan suoraan ylläpidetty size-muuttuja, eikä lasketa silmukan avulla
        return size;
    }

    // Tarkistusmetodi onko jono tyhjä O(1)
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Clear-metodi tyhjentää jonon ja plauttaa kapasiteetin oletusarvoon O(1).
    @Override
    public void clear() {
        itemArray = new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("[");
        int currentIndex = head;
        for (int i = 0; i < size; i++) {
            builder.append(itemArray[currentIndex]);
            currentIndex = (currentIndex + 1) % itemArray.length;
            if (i < size - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    // Reallocate-apumetodi joka kasvatttaa taulukon kokoa. Luo uuden kaksi kertaa isomman taulukon, kopioi sekä
    // "suoristaa" vanhan jonon siihen. Aikakompleksisuus on O(n).
    private void reallocate () {
        int newCapacity = itemArray.length * 2;
        Object[] newItemArray = new Object[newCapacity];

        // Kopioidaan alkiot loogisessa järjestyksessä (head->tail) uuden taulukon alkuun (0->size)
        int oldIndex = head;
        for (int newIndex = 0; newIndex < size; newIndex++) {
            newItemArray[newIndex] = itemArray[oldIndex];
            // Siirretään vanhaa indeksiä kiertävän taulun logiikalla
            oldIndex = (oldIndex + 1) % itemArray.length;
        }

        // Otetaan uusi taulukku käyttöön
        itemArray = newItemArray;
        // Asetetaan jonon head- ja tail-osoittimet oikeille paikoilleen, koska jono on nyt suoristettu
        head = 0;
        tail = size;
    }

}

package oy.interact.tira.student;

import oy.interact.tira.util.LinkedListInterface;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedListImplementation<E> implements LinkedListInterface<E> {

    // Luokan jäsenmuuttujat
    private Node<E> head;
    private Node<E> tail;
    private int size;

    // Privaatti Node(Solmu) sisäluokka.
    // Ohjeiden mukaisesti kahteen suuntaan linkitetty.
    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> previous;

        // Solmun (Node) muodostin.
        // element = Käsiteltävä tieto
        // previous = Viittaus edelliseen solmuun
        // next = Viittaus seuraavaan solmuun
        Node(E element, Node<E> previous, Node<E> next) {
            this.element = element;
            this.previous = previous;
            this.next = next;
        }
    }

    // Linkitetyn listan muodostin joka luo tyhjän listan
    public LinkedListImplementation() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Lisäysoperaatio listan alkuun.
    @Override
    public void addFirst(E element) {
        // Talletetaan vanha ensimmäinen solmu
        final Node<E> oldHead = head;
        // Luodaan uusi solmu
        // Previous on null (koska tämä on uusi Head)
        // Next osoittaa vanhaan "päähän"
        final Node<E> newHead = new Node<E>(element, null, oldHead);
        // Asetetaan uusi "pää"
        this.head = newHead;

        // Reunatapausten käsittely:
        // Jos lista on tyhjä, uusi "pää"(head) on myös silloin listan viimeinen eli "häntä"(tail)
        if (oldHead == null) {
            this.tail = newHead;
        }
        // Jos lista ei ollutkaan tyhjä, vanhan "pään" previous-viittaus päivitetään osoittamaan uuteen solmuun
        else {
            oldHead.previous = newHead;
        }
        // Kasvatetaan kokoa
        size++;
    }

    // Lisäysoperaatio listan loppuun.
    // Mekanismi sama kuin edellisessä, mutta käsitellään listan "häntä"-päätä.
    @Override
    public void addLast(E element) {
        // Talletetaan vanha "häntä"
        final Node<E> oldTail = tail;
        // Luodaan uusi solmu, previous on vanha häntä ja next on null
        final Node<E> newTail = new Node<E>(element, oldTail, null);
        this.tail = newTail;

        if (oldTail == null) {
            // Lista oli tyhjä
            this.head = newTail;
        } else {
            // Linkitetään vanha häntä eteenpäin
            oldTail.next = newTail;
        }
        size++;

    }

    // Lisäysoperaatio jossa lisätään annettu alkio annettuun indeksiin.
    // Aikakompleksiusuus on O(n), paitsi kun lisätään listan alku- tai loppupäähän, Tämä on toteutettu metodin alussa.
    @Override
    public void add(int index, E element) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index is out of bounds. Index: " + index + ", Size: " + size);
        }

        // Hyödynnetään reunatapaukset ja toteutetaan 0(1) aikakompleksit metodit
        if (index == 0) { // <- Lisäys listan alkuun
            addFirst(element);
        } else if (index == size) { // <- Lisäys listan loppuun
            addLast(element);
        } else {
            // Lisäys keskelle listaa.
            // Haetaan indexissä oleva solmu, lisäsys toteutetaan tämän indeksissä olevan solmun eteen.
            Node<E> nodeAtIndex = getNode(index);
            // Haetaan sitä edeltävä solmu
            Node<E> prevNode = nodeAtIndex.previous;
            // Luodaan uusdi solmu ja linkitetään se vanhojen väliin
            Node<E> newNode = new Node<>(element, prevNode, nodeAtIndex);
            // Päivitetään vanhojen solmujen linkit osoittamaan uuteen lisättyyn solmuun
            nodeAtIndex.previous = newNode;
            prevNode.next = newNode;

            size++;
        }

    }

    // Hakuoperaatio joka palauttaa ensimmäisen alkion poistamatta sitä.
    // Aikakompleksisuus on O(1).
    @Override
    public E getFirst() throws NoSuchElementException {
        if (this.head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return this.head.element;
    }

    // Hakuoperaatio joka plauttaa viimeisen alkion poistamatta sitä.
    // Tälläkin aikakompleksisuus on O(1).
    @Override
    public E getLast() throws NoSuchElementException {
        if (this.tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return this.tail.element;
    }

    // Hakuoperaatio joka palauttaa alkion annetusta indeksistä. Aikakompleksisuus O(n), mutta tässä on
    // hyödynnetty haussa apumetodia hakemaan listan päästä jossa haettu indexi on (O(n/2)).
    // Apumetodi löytyy tämän luokan lopusta.
    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds. Index: " + index + ", Size: " + size);
        }
        // Hyödynnetään haussa optimoitua apumetodia
        return getNode(index).element;
    }

    // Hakuoperaatio joka etsii ensimmäisen alkiota vastaavan indeksin listasta.
    // Palauttaa indeksin josta alkio löytyi tai -1 jos ei vastaavaa löydy.
    // Aikakompleksisuus O(n).
    @Override
    public int indexOf(E element) {
        Node<E> current = head;

        // Käydään lista läpi alusta loppuun.
        for (int i = 0; i < size; i++) {
            // Hyödyntämällä Javan Objects-luokan equals-metodia tehdään null-turvalliset tarkistukset
            if (Objects.equals(element, current.element)) {
                return i; // <- Vastaava alkio löytyi ja palautetaan se
            }
            current = current.next;
        }
        return -1; // <- Ei löytynyt vastaavaa alkiota
    }

    // Poisto-operaatio joka poistaa ja palautttaa ensimmäisen alkion listasta. Tässä hyödynnetään erillistä
    // unlink-apumetodia, tämä apumetodi löytyy toiseksi alimpana luokan lopusta. Aikakompleksisuus O(1).
    @Override
    public E removeFirst() throws NoSuchElementException {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return unlink(head); // Apumetodin kutsu, apumetodin aikakompleksisuus on O(1)
    }

    // Poisto-operaatio jolla poistetaan ja palautetaan listan viimeinen alkio. Hyödyntää myös
    // unlink-apumetodia. Aikakompleksisuus O(1)
    @Override
    public E removeLast() throws NoSuchElementException {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return unlink(tail);
    }

    // Poisto-operaatio joka etsii ja poistaa ensimmäisen annettua alkiota vastaavan esiintymän listasta.
    // Taasen hyödynnetty Javan Objects.equals-metodia tukemaan null-arvojen turvallista käsittelyä,
    // sekä unlink-apumetodia solmun poistossa.
    // Aika-kompleksisuus on O(n).
    @Override
    public boolean remove(E element) {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }

        // Käydään lista läpi kerran (O(n))
        for (Node<E> current = head; current != null; current = current.next) {
            if (Objects.equals(element, current.element)) {
                unlink(current); // <- Poistetaan löydetty solmu (O(1))
                return true;
            }
        }
        return false; // Ei löydy vastaavaa
    }

    // Poisto-operaatio joka poistaa ja palauttta alkion annetusta indeksistä. Unlink-apumetodi tässäkin käytössä.
    // Aikakompleksisuus on O(n), mutta optimoitu haku käytössä (O(n/2)).
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds. Index: " + index + ", Size: " + size);
        }
        // Haetaan poistettava solmu getNode-apumetodin avulla siitä päästä listaa jossa indeksi sijaitsee
        Node<E> nodeToRemove = getNode(index);
        // Poistetaan solmu hyödyntäen unlink-apumetodia
        return unlink(nodeToRemove);
    }

    // Metodi palauttaa listassa olevien alkioiden lukumäärään. Aikakompleksisuus O(1).
    @Override
    public int size() {
        return size;
    }

    // Tyhjennys-metodi joka tyhjentää listan kaikista alkioista. Aikakompleksisuus O(1).
    // Erillistä roskien keruuta ei tarvita vaan Javan oma roskienkerääjä hoitaa muistin vapauttamisen.
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // Listan kääntö-metodi, jolla käännetään listan järjestys ns. paikallaan (in-place) muuttamalla solmujen
    // viittaukset. Aika kompleksisuus on O(n).
    @Override
    public void reverse() {
        Node<E> current = head;
        Node<E> temp = null;

        // Vaihdetaan listan pää(head) ja häntä(tail) päittäin
        temp = head;
        head = tail;
        tail = temp;

        // Käydään lista läpi ja vaihdetaan kunkin solmun previous- ja next-viittaukset keskenään
        while (current != null) {
            temp = current.previous;
            current.previous = current.next;
            current.next = temp;

            // Siirtyminen "seuraavaan" solmuun, joka löytyy nyt previous-viittauksella
            current = current.previous;
        }
    }

    // Listan kääntö-operaatio jossa kopiodaan annetun listan alkiot kohdelistaan käänteisessä järjestyksessä.
    // Aikakompleksisuus on O(n).
    @Override
    public void reversedTo(LinkedListInterface<E> target) {
        target.clear(); // <- Alustetaan kohdelista

        // Käydään annettu lista läpi alusta loppuun
        for (Node<E> current = head; current != null; current = current.next) {
            // Lisätään alkiot vuorotellen kohdelistan ALKUUN, jolloin annnettua listaa luettaessa alusta loppuun
            // kääntyy järjestys automaattisesti!
            target.addFirst(current.element);
        }
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        Node<E> current = head;

        while (current != null) {
        builder.append(current.element);
        if (current.next != null) {
            builder.append(", ");
        }
        current = current.next;
        }
        builder.append("]");
        return builder.toString();
    }

    // Privaatti apumetodi poistologiikassa käytettäväksi, tässä on hyödynnetty Linked List luennolla esiteltyä ja luentokalvon
    // sivulta 10 löytyvää pseudokoodia unlink-metodista. Tätä hyödyntämällä vältetään turhaa toistoa poistooperaation
    // metodeissa. Unlink-metodin avulla poistetaan haluttu solmu listasta, oli se sitten alussa, lopussa tai keskellä.
    // Poiston jäkeen metodi "ompelee" poistetun solmun naapurit yhteen.
    private E unlink(Node<E> nodeToUnlink) {
        final E element = nodeToUnlink.element;
        final Node<E> prev = nodeToUnlink.previous;
        final Node<E> next = nodeToUnlink.next;

        if (prev == null) {
            // Poistettava solmu oli "pää"
            head = next;
        } else {
            // Poistettava solmu oli keskeltä tai lopussa
            prev.next = next;
            nodeToUnlink.previous = null; // <- Katkaistaan turha linkki
        }

        if (next == null) {
            // Poistettava solmu oli "häntä"
            tail = prev;
        } else {
            // Poistettava solmu oli alusta tai keskeltä
            next.previous = prev;
            nodeToUnlink.next = null;
        }

        nodeToUnlink.element = null; // Autetaan roskien keruussa
        size--;
        return element;
    }

    // Privaatti apumetodi optimoimaan hakuoperaatiota, hyödynnetty taas kerran luentojen materiaalia.
    // Haetaan siitä päästä listaa joka on lähempänä haettavaa indexiä. Täyden listan hakuoperaatio olisi
    // aikakomplkesisuudeltaan O(n), mutta tällä optimoinnilla se muuttuu O(n/2).
    private Node<E> getNode(int index) {

        if (index < (size / 2)) { // <- Haetaan alusta loppuunpäin
            // Indeksi on listan ensimmisellä puoliskolla
            Node<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        }
        else  { // <- Haetaan lopusta alkuunpäin
            // Indeksi on listan jälkimmäisellä puoliskolla
            Node<E> current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.previous;
            }
            return current;
        }
    }

}

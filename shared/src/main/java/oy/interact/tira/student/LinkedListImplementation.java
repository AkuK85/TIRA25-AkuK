package oy.interact.tira.student;

import org.w3c.dom.Node;
import oy.interact.tira.util.LinkedListInterface;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedListImplementation<E> implements LinkedListInterface<E> {

    // Luokan jäsenmuuttujat
    private Node<E> head;
    private Node<E> tail;
    private int size;

    // Privaatti Node(Solmu) sisäluokka. Ohjeiden mukaisesti kahteensuuntaan linkitetty.
    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> previous;

        // Solmun muodostin
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

    // Lisäysoperaatio listan loppuun. Mekanismi sama kuin edellisessä, mutta käsitellään listan "häntä"-päätä.
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

    @Override
    public void add(int index, E element) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index is out of bounds. Index: " + index + ", Size: " + size);
        }

        // Hyödynnetään reunatapaukset ja toteutetaan 0(1) aikakompleksit metodit
        if (index == 0) {
            addFirst(element);
        } else if  (index == size) {
            addLast(element);
        } else {
            // Lisäys keskelle listaa!
            // Haetaan indexissä oleva solmu
            Node<E> nodeAtIndex = getNode(index);
            // Haetaan sitä edeltävä solmu
            Node<E> prevNode = nodeAtIndex.previous;
            // Luodaan uusdi solmu ja linkitetään se vanhojen väliin
            Node<E> newNode = new Node<>(element, prevNode, nodeAtIndex);
            // Päivitetään vanhojen solmujen linkit
            nodeAtIndex.previous = newNode;
            prevNode.next = newNode;

            size++;
        }

    }

    @Override
    public E getFirst() throws NoSuchElementException {
        if (this.head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return this.head.element;
    }

    @Override
    public E getLast() throws NoSuchElementException {
        if (this.tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return this.tail.element;
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds. Index: " + index + ", Size: " + size);
        }
        // Hyödynnetään optimoitua apumetodia
        return getNode(index).element;
    }

    @Override
    public int indexOf(E element) {
        Node<E> current = head;

        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, current.element)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    @Override
    public E removeFirst() throws NoSuchElementException {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return unlink(head);
    }

    @Override
    public E removeLast() throws NoSuchElementException {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return unlink(tail);
    }

    // Tarkista tätä tehdessä vielä erratasta!
    @Override
    public boolean remove(E element) {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }

        for (Node<E> current = head; current != null; current = current.next) {
            if (Objects.equals(element, current.element)) {
                unlink(current);
                return true;
            }
        }
        return false;
    }


    // Tarkista tämä myös errratasta!
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds. Index: " + index + ", Size: " + size);
        }
        // Haetaan poistettava solmu
        Node<E> nodeToRemove = getNode(index);
        // Poistetaan solmu hyödyntäen unlink-metodia
        return unlink(nodeToRemove);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void reverse() {

    }

    @Override
    public void reversedTo(LinkedListInterface<E> target) {

    }

    @Override
    public String toString() {

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
    // Haetaan siitä päästä listaa joka on lähempänä haettavaa indexiä.
    private Node<E> getNode(int index) {

        if (index < (size / 2)) { // <- Haetaan alusta loppuunpäin
            //
            Node<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        }
        else  { // <- Haetaan lopusta alkuunpäin
            Node<E> current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.previous;
            }
            return current;
        }
    }

}

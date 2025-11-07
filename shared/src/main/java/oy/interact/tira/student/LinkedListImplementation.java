package oy.interact.tira.student;

import org.w3c.dom.Node;
import oy.interact.tira.util.LinkedListInterface;
import java.util.NoSuchElementException;

public class LinkedListImplementation<E> implements LinkedListInterface<E> {

    // Luokan jäsenmuuttujat
    private Node<E> head;
    private Node<E> tail;
    private int size;

    // Privaatti Node(Solmu) sisäluokka. Ohjeiden mukaisesti kahteensuuntaan linkitetty.
    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E>  previous;

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
        } else  {
            // Linkitetään vanha häntä eteenpäin
            oldTail.next = newTail;
        }
        size++;

    }

    @Override
    public void add(int index, E element) throws IndexOutOfBoundsException {

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

    }

    @Override
    public int indexOf(E element) {

    }

    @Override
    public E removeFirst() throws NoSuchElementException {

    }

    @Override
    public E removeLast() throws NoSuchElementException {

    }

    // Tarkista tätä tehdessä vielä erratasta!
    @Override
    public boolean remove(E element) {

    }

    // Tarkista tämä myös errratasta!
    @Override
    public E remove(int index) {

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


}

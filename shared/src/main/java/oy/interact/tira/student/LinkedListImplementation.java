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
        Node(E element, Node<E> next, Node<E> previous) {
            this.element = element;
            this.next = next;
            this.previous = previous;
        }
    }


    // Linkitetyn listan muodostin joka luo tyhjän listan
    public LinkedListImplementation() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void addFirst(E element) {

    }

    @Override
    public void addLast(E element) {

    }

    @Override
    public void add(int index, E element) throws IndexOutOfBoundsException {

    }

    @Override
    public E getFirst() throws NoSuchElementException {

    }

    @Override
    public E getLast() throws NoSuchElementException {

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

    }

    @Override
    public void clear() {

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

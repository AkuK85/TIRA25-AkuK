package oy.interact.tira.student;

import oy.interact.tira.util.QueueInterface;

public class ArrayQueue<E> implements QueueInterface<E> {

    private Object[] itemArray;
    private int head;
    private int tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        itemArray = new Object[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public int capacity() {
        return itemArray.length;
    }

    @Override
    public void enqueue(E element) throws OutOfMemoryError, NullPointerException {
        if (element == null) {
            throw new NullPointerException("Element cannot be null.");
        }
        if (size == itemArray.length) {
            reallocate();
        }

        itemArray[tail] = element;
        size++;
        tail = (tail + 1) % itemArray.length;
    }

    @Override
    public E dequeue() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        E element = (E) itemArray[head];
        itemArray[head] = null;
        size--;
        head = (head + 1) % itemArray.length;

        return element;
    }

    @Override
    public E element() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        E element = (E) itemArray[head];

        return element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

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

    private void reallocate () {
        int newCapacity = itemArray.length * 2;
        Object[] newItemArray = new Object[newCapacity];
        int oldIndex = head;

        for (int newIndex = 0; newIndex < size; newIndex++) {
            newItemArray[newIndex] = itemArray[oldIndex];
            oldIndex = (oldIndex + 1) % itemArray.length;
        }

        itemArray = newItemArray;
        head = 0;
        tail = size;
    }

}

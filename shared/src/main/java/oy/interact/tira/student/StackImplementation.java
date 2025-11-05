package oy.interact.tira.student;

import oy.interact.tira.util.StackInterface;

public class StackImplementation<E>  implements StackInterface<E> {

    private static final int DEFAULT_STACK_SIZE = 16;
    private Object [] itemArray;
    private int topIndex;

    public StackImplementation() {
        this.itemArray = new Object[DEFAULT_STACK_SIZE];
        this.topIndex = -1;
    }

    public StackImplementation(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be greater than zero.");
        }
        this.itemArray = new Object[capacity];
        this.topIndex = -1;
    }

    @Override
    public int capacity() {
        return itemArray.length;
    }

    @Override
    public void push(E element) throws OutOfMemoryError, NullPointerException {
        if (element == null) {
            throw new IllegalArgumentException("element is null");
        }

        if (topIndex + 1 == itemArray.length) {
            reallocate();
        }
        topIndex++;
        itemArray[topIndex] = element;
    }

    @Override
    public E pop() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }

        E element = (E) itemArray[topIndex];
        itemArray[topIndex] = null;
        topIndex--;

        return element;
    }

    @Override
    public E peek() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty (peek)");
        }

        E element = (E) itemArray[topIndex];
        return element;
    }

    @Override
    public int size() {
        return topIndex + 1;
    }

    @Override
    public boolean isEmpty() {
        return topIndex == -1;
    }

    @Override
    public void clear() {
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

    private void reallocate() {
        int oldCapacity = itemArray.length;
        int newCapacity = oldCapacity * 2;

        if (newCapacity <= oldCapacity) {
            if (oldCapacity == Integer.MAX_VALUE) {
                throw new OutOfMemoryError("Stack is at maximum capacity");
            }
            newCapacity = Integer.MAX_VALUE;
        }

        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i <= topIndex; i++) {
            newArray[i] = itemArray[i];
        }
        itemArray = newArray;
    }

}

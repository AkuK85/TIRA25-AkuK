package oy.interact.tira.factories;

import oy.interact.tira.util.QueueInterface;
import oy.interact.tira.student.ArrayQueue;

public class ArrayQueueFactory {

	private ArrayQueueFactory() {
		// empty
	}

	public static QueueInterface<Integer> createIntegerQueue() {
		return new ArrayQueue<>();
	}

	public static QueueInterface<Integer> createIntegerQueue(int capacity) {
		return new ArrayQueue<>(capacity);
	}

	public static QueueInterface<String> createStringQueue() {
		return new ArrayQueue<>();
	}

	public static QueueInterface<String> createStringQueue(int capacity) {
		return new ArrayQueue<>(capacity);
	}

}

package com.org.basic;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerProblem {
    static int capacity = 10;
    static Object lock = new Object();
    private static Queue<Integer> queue = new LinkedList<>();

    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            int count = 0;
            while (true) {
                synchronized (lock) {
                    while (queue.size() == capacity) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Adding to queue: " + count);
                    queue.add(count++);
                    lock.notify(); // Notify the consumer

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Thread consumer = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    while (queue.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    final Integer poll = queue.poll();
                    System.out.println("Consuming from queue: " + poll);
                    lock.notify();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        consumer.start();
        producer.start();

    }
}

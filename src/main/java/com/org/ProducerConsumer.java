package com.org;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {

    private final Queue<Integer> buffer = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();

        Thread producer = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }

    public void produce() throws InterruptedException {
        int value = 1;
        int CAPACITY = 5;
        while (true) {
            lock.lock();
            try {
                while (buffer.size() == CAPACITY) {
                    System.out.println("Buffer full. Producer waiting...");
                    notFull.await(); // Corrected signal
                }

                System.out.println("Produced: " + value);
                buffer.add(value++);
                notEmpty.signal(); // Corrected signal
            } finally {
                lock.unlock();
            }
            Thread.sleep(5000); // simulate time to produce
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (buffer.isEmpty()) {
                    System.out.println("Buffer empty. Consumer waiting...");
                    notEmpty.await(); // Corrected signal
                }

                int val = buffer.poll();
                System.out.println("Consumed: " + val);
                notFull.signal(); // Corrected signal
            } finally {
                lock.unlock();
            }
            Thread.sleep(3000); // simulate time to consume
        }
    }
}

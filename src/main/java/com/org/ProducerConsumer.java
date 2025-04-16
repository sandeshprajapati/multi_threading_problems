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
        int value = 0;
        while (true) {
            lock.lock();
            try {
                int CAPACITY = 5;
                while (buffer.size() == CAPACITY) {
                    System.out.println("Buffer full. Producer waiting...");
                    notFull.await();
                }

                System.out.println("Produced: " + value);
                buffer.add(value++);
                notEmpty.signal(); // signal consumer
            } finally {
                lock.unlock();
            }
            Thread.sleep(500); // simulate time to produce
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (buffer.isEmpty()) {
                    System.out.println("Buffer empty. Consumer waiting...");
                    notEmpty.await();
                }

                int val = buffer.poll();
                System.out.println("Consumed: " + val);
                notFull.signal(); // signal producer
            } finally {
                lock.unlock();
            }
            Thread.sleep(1000); // simulate time to consume
        }
    }
}

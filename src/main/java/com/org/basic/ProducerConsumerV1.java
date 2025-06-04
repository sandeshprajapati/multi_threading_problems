package com.org.basic;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumerV1 {
    public static void main(String[] args) {

        final int QUEUE_CAPACITY = 5;
        final int NUM_PRODUCERS = 2;
        final int NUM_CONSUMERS = 3;
        final int RUNTIME_SECONDS = 10;

        SharedQueue sharedQueue = new SharedQueue(QUEUE_CAPACITY);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_PRODUCERS + NUM_CONSUMERS);

        // Start producers
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            executor.execute(new Producer(sharedQueue));
        }

        // Start consumers
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            executor.execute(new Consumer(sharedQueue));
        }

    }
}

class SharedQueue {

    private final BlockingQueue<Integer> queue;

    public SharedQueue(int capacity) {
        queue = new ArrayBlockingQueue<>(capacity);
    }

    void produce(int item) throws InterruptedException {
        queue.put(item);
        System.out.println(Thread.currentThread().getName() + " produced: " + item);
    }

    int consume() throws InterruptedException {
        int item = queue.take();  // blocks if empty
        System.out.println(Thread.currentThread().getName() + " consumed: " + item);
        return item;
    }

}

class Producer implements Runnable {

    SharedQueue queue;
    int itemCounter;

    public Producer(SharedQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int item;
                synchronized (Producer.class) {
                    item = itemCounter++;
                }
                System.out.println("Produce Item :" + item);
                queue.produce(item);
                Thread.sleep(2000);

            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " stopping (interrupted).");
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {

    SharedQueue queue;

    public Consumer(SharedQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                final int i = queue.consume();
                System.out.println("Consume Item:" + i);

            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " stopping (interrupted).");
            Thread.currentThread().interrupt();
        }


    }
}

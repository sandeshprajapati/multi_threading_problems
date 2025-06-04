package com.org.basic;

import java.util.concurrent.atomic.AtomicInteger;

public class Countdown {
    public static void main(String[] args) {
        //Implement a simple countdown timer using threads.
        AtomicInteger count = new AtomicInteger();

        Thread t = new Thread(() -> {
            while (true) {
                System.out.println(count.getAndIncrement());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

    }
}

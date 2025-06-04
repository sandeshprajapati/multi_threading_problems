package com.org.basic;

public class HelloPrint {
    public static void main(String[] args) {
        // Write a thread that prints “Hello” every second, five times
        Thread t = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Hello");
                try {
                    Thread.sleep(1000); // Pause for 1 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted");
                    break;
                }
            }
        });

        t.start();
    }
}
package com.org.basic;

public class ThreadLifecycleDemo {
    public static void main(String[] args) {
        final Thread t = getThread();
        System.out.println("After start thread");
        System.out.println("Thread state 3::" + t.getState());
        try {
            Thread.sleep(500);
            System.out.println("Thread state 4::" + t.getState());
            t.join();
            System.out.println("Thread state 5::" + t.getState());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted");
        }

    }

    private static Thread getThread() {
        Thread t = new Thread(() -> {
            System.out.println("Thread state 1::" + Thread.currentThread().getState());
            try {
                Thread.sleep(2000);
                System.out.println("Thread in sleep state");
                System.out.println("Thread state 2::" + Thread.currentThread().getState());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted");
            }
        });
        t.start();
        return t;
    }
}

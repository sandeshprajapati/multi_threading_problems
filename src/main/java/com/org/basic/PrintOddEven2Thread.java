package com.org.basic;

public class PrintOddEven2Thread {
    //Create two threads: one prints even numbers, another prints odd numbers from 1 to 20
    static final Object thread = new Object();
    static int current = 0;
    static int limit = 20;

    public static void main(String[] args) {


        Thread printEven = new Thread(() -> {
            while (current <= limit) {
                synchronized (thread) {
                    if (current % 2 == 0) {
                        System.out.println("print even:" + current);
                        current++;
                        thread.notify();
                    } else {
                        try {
                            thread.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        });
        Thread printOdd = new Thread(() -> {
            while (current < limit) {
                synchronized (thread) {
                    if (current % 2 != 0) {
                        System.out.println("Print Odd :" + current);
                        current++;
                        thread.notify();
                    } else {
                        try {
                            thread.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        });

        printEven.start();
        printOdd.start();
    }
}


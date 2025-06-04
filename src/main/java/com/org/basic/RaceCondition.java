package com.org.basic;

public class RaceCondition {

    int count = 0;

    public static void main(String[] args) throws InterruptedException {
        RaceCondition rc = new RaceCondition();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                rc.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                rc.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Print the count value");
        System.out.println(rc.getCount());
    }


    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

package com.org.basic;

public class One2Ten {
    public static void main(String[] args) {
        //Create a thread that prints numbers from 1 to 10
        Thread t = new Thread(new PrintNumberTask());
        t.start();
        System.out.println();
        // Create a thread that prints numbers from 1 to 10
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println(i);
            }
        });
        t1.start();
    }

}

class PrintNumberTask implements Runnable {

    @Override
    public void run() {
        int n = 0;
        while (n < 10) {
            n++;
            System.out.println(n);

        }
    }
}

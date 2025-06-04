package com.org.basic;

    public class StopAfter5Sec {
        public static void main(String[] args) {
            Thread t = new Thread(() -> {
                long startTime = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - startTime >= 5000) {
                        System.out.println("Thread stopped after 5 seconds");
                        break;
                    }
                }
            });

            t.start();
        }
    }
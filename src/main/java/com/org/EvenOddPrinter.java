package com.org;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EvenOddPrinter {

    private final int N;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private int current = 1;

    public EvenOddPrinter(int n) {
        this.N = n;
    }

    public static void main(String[] args) {
        EvenOddPrinter printer = new EvenOddPrinter(10);

        Thread oddThread = new Thread(printer::printOdd);
        Thread evenThread = new Thread(printer::printEven);

        oddThread.start();
        evenThread.start();
    }

    public void printOdd() {
        while (true) {
            lock.lock(); //  Must lock before using condition
            try {
                while (current <= N && current % 2 == 0) {
                    condition.await(); //  Must be inside lock
                }
                if (current > N) {
                    condition.signal(); // signal other thread before exit
                    break;
                }
                System.out.println("Odd: " + current);
                current++;
                condition.signal(); //  Wake up other thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock(); //  Always unlock
            }
        }
    }

    public void printEven() {
        while (true) {
            lock.lock(); //  Must lock before using condition
            try {
                while (current <= N && current % 2 != 0) {
                    condition.await(); //  Must be inside lock
                }
                if (current > N) {
                    condition.signal(); // signal other thread before exit
                    break;
                }
                System.out.println("Even: " + current);
                current++;
                condition.signal(); //  Wake up other thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock(); //  Always unlock
            }
        }
    }
}

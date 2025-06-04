package com.org.intermediate;

import java.util.concurrent.Semaphore;

public class ParkingLotSimulation {
    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        do {
            Car car1 = new Car(i);
            car1.start();
            Thread.sleep(5000);
            Car car2 = new Car(i + 10);
            car2.start();
            i++;
        } while (i != 5);


    }

}

class Car extends Thread {

    private static final int total_slot = 1;
    private static final Semaphore parkingSlots = new Semaphore(total_slot);

    private final int carId;

    public Car(int carId) {
        this.carId = carId;
    }

    @Override
    public void run() {
        try {
            if (parkingSlots.availablePermits() == 0) {
                System.out.println("Car " + carId + " is waiting for a slot...");
                Thread.sleep(50000);
            }

            System.out.println("Car " + carId + " is trying to park...");
            parkingSlots.acquire();
            System.out.println("Car " + carId + " parked");
            System.out.println("Car " + carId + " is leaving...");
            parkingSlots.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

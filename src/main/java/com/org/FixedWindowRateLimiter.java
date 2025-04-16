package com.org;

import java.util.concurrent.*;

public class FixedWindowRateLimiter {

    private final Semaphore semaphore;
    private final int maxRequests;
    private final ScheduledExecutorService scheduler;

    public FixedWindowRateLimiter(int maxRequestsPerSecond) {
        this.maxRequests = maxRequestsPerSecond;
        this.semaphore = new Semaphore(maxRequestsPerSecond);
        this.scheduler = Executors.newScheduledThreadPool(1);

        // Reset the semaphore permits every 1 second
        scheduler.scheduleAtFixedRate(() -> {
            int permitsToRelease = maxRequests - semaphore.availablePermits();
            if (permitsToRelease > 0) {
                semaphore.release(permitsToRelease);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean allowRequest() {
        return semaphore.tryAcquire(); // returns true if permit acquired, else false
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    // Simulate API call
    public void processRequest(int threadId) {
        if (allowRequest()) {
            System.out.println("Request processed by thread " + threadId + " at " + System.currentTimeMillis());
        } else {
            System.out.println("Rate limit exceeded for thread " + threadId + " at " + System.currentTimeMillis());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(5);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        Runnable task = () -> {
            for (int i = 0; i < 10; i++) {
                limiter.processRequest((int) Thread.currentThread().getId());
                try {
                    Thread.sleep(100); // simulate rapid-fire requests
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Launch multiple threads
        for (int i = 0; i < 3; i++) {
            executor.submit(task);
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        limiter.shutdown();
    }
}

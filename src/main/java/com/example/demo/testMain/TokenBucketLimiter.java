package com.example.demo.testMain;

import java.util.concurrent.*;

/**
 * @author ChangLF 2023-03-03
 */
public class TokenBucketLimiter {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final BlockingQueue<Object> tokenBucket = new ArrayBlockingQueue<>(100);
    private final int refillIntervalMs;
    private final int capacity;

    public TokenBucketLimiter(int capacity, int refillIntervalMs) {
        this.refillIntervalMs = refillIntervalMs;
        this.capacity = capacity;
        scheduler.scheduleAtFixedRate(this::refill, refillIntervalMs, refillIntervalMs, TimeUnit.MILLISECONDS);
    }

    private void refill() {
        synchronized (tokenBucket) {
            if (tokenBucket.size() < capacity) {
                tokenBucket.add(new Object());
            }
        }
    }

    public boolean tryAcquire() {
        synchronized (tokenBucket) {
            return tokenBucket.poll() != null;
        }
    }

    public static void main(String[] args) {
        TokenBucketLimiter limiter = new TokenBucketLimiter(10, 1000);
        for (int i = 0; i < 20; i++) {
            boolean result = limiter.tryAcquire();
            System.out.println("Request " + (i + 1) + ": " + (result ? "allowed" : "rejected"));
        }
    }

}

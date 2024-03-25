package com.example.demo.testMain;

import com.example.demo.entity.User;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ChangLF 2023-02-19
 */
@Slf4j
public class TestThread {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        log.info("main");
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i = 0; i < 10; i++) {
//            executorService.execute(() ->{
//                System.out.println("执行");
//            });
//        }
//        Collections.synchronizedMap(new HashMap<>());
//        new ConcurrentHashMap<>();
//
//        Thread thread = new Thread(() -> {
////            try {
////                Thread.sleep(10000);
////            } catch (InterruptedException e) {
////                log.warn("我报错了" +e);
////            }
//            log.info("new thread");
//        });
////        thread.start();
//
//        try {
//            thread.interrupt();
//        } catch (Exception e) {
//            log.warn("主线程报错了");
//        }
//
//        ExecutorService executorService3 = Executors.newFixedThreadPool(10);
//        ExecutorService executorService1 = Executors.newCachedThreadPool();
//        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
//        new LinkedBlockingQueue<Runnable>();
//        new ArrayBlockingQueue<>(10);
//        new SynchronousQueue<Runnable>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 30L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1000), new DefaultThreadFactory("my-thread"), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                r.run();
            }
        });
        for (int i = 0; i < 20; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前任务被执行" + Thread.currentThread().getName());
                }
            });
            Future<Object> submit = threadPoolExecutor.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    System.out.println("当前任务被执行" + Thread.currentThread().getName());
                    return "aaa";
                }
            });
            Object o = submit.get();
        }

        threadPoolExecutor.shutdown();
        threadPoolExecutor.shutdownNow();

        synchronized (new Object()) {
            System.out.println("aa");
        }

        new AtomicInteger();
//        Unsafe unsafe = Unsafe.getUnsafe();

        ReentrantLock lock = new ReentrantLock();
        lock.tryLock();
        Semaphore semaphore = new Semaphore(10);
        semaphore.tryAcquire(1);
        semaphore.release(1);
        CountDownLatch downLatch = new CountDownLatch(10);
        downLatch.countDown();
        downLatch.await(10, TimeUnit.SECONDS);

        new ArrayList<>();
        new Vector<>();
        Collections.synchronizedList(new ArrayList<>());
        new CopyOnWriteArrayList<>();

        new HashMap<>();
        new Hashtable<>();
        new ConcurrentHashMap<>();
        Collections.synchronizedMap(new HashMap<>());

        Map<String, User> userMap = Stream.of(new User("1"), new User("2"), new User("3")).collect(Collectors.toMap(User::getId, u -> u, (u1, u2) -> u1));
        System.out.println(userMap);

    }

    public synchronized void get() {
        new TestThread();
    }

    public synchronized static void get1() {
        Class<TestThread> testThreadClass = TestThread.class;
    }

    private volatile int value;
}

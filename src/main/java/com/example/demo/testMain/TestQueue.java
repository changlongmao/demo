package com.example.demo.testMain;

import com.example.demo.entity.TestObject;
import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author ChangLF 2022-02-27
 */
public class TestQueue {

    public static void main(String[] args) {
        ArrayBlockingQueue<Object> arrayBlockingQueue = new ArrayBlockingQueue<>(10);
        arrayBlockingQueue.offer("aaa");
        arrayBlockingQueue.poll();
        ArrayDeque<Object> arrayDeque = new ArrayDeque<>();

        ArrayQueue<Object> arrayQueue = new ArrayQueue<>(10);
        LinkedList<Object> objects = new LinkedList<>();


        LinkedBlockingQueue<Object> linkedBlockingQueue = new LinkedBlockingQueue<>();
        linkedBlockingQueue.offer("aaa");
        linkedBlockingQueue.add("bbb");
        Object poll = linkedBlockingQueue.poll();
        Object peek = linkedBlockingQueue.peek();
        Executors.newFixedThreadPool(10);
        Executors.newCachedThreadPool();


        System.out.println(Integer.MAX_VALUE);

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        priorityQueue.offer(9);
        priorityQueue.offer(5);
        priorityQueue.offer(3);
        priorityQueue.offer(7);

        System.out.println("priorityQueue" + Arrays.toString(priorityQueue.toArray()));
        System.out.println("priorityQueue" + priorityQueue.poll());
        System.out.println("priorityQueue" + priorityQueue.poll());
        System.out.println("priorityQueue" + priorityQueue.poll());
        System.out.println("priorityQueue" + priorityQueue.poll());

        CountDownLatch downLatch = new CountDownLatch(10);
    }


}

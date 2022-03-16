package com.example.demo.testMain;

import com.example.demo.entity.TestObject;
import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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


        System.out.println(Integer.MAX_VALUE);
    }


}

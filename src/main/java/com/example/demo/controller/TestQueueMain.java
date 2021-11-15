package com.example.demo.controller;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: ChangLF
 * @date: 2021/11/12 16:36
 **/
public class TestQueueMain {

    public static void main(String[] args) {
        LinkedBlockingQueue<Object> objects = new LinkedBlockingQueue<>();
        new ArrayQueue<>(100);
        new ArrayBlockingQueue<>(100);
        new ArrayList<>(100);
    }
}

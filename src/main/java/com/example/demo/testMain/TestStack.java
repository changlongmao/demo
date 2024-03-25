package com.example.demo.testMain;


import java.util.*;
import java.util.function.Function;

/**
 * @author ChangLF 2022-01-28
 */
public class TestStack {

    public static void main(String[] args) {
        Stack<Object> objects = new Stack<>();
        objects.push(1);
        objects.push(2);
        System.out.println(objects.pop());
        System.out.println(objects.pop());

        // LinkedList通常作为栈或队列使用，但是队列的效率不如ArrayQueue高。
        LinkedList<Object> linkedList = new LinkedList<>();
        // 栈
        linkedList.push(1);
        linkedList.push(2);
        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());
        // 队列
//        linkedList.offer(1);
//        linkedList.offer(2);
//        System.out.println(linkedList.poll());
//        System.out.println(linkedList.poll());
        assert linkedList.peek() == null;
        // ArrayDeque通常作为栈或队列使用，但是栈的效率不如LinkedList高。
        ArrayDeque<Object> arrayDeque = new ArrayDeque<>();
        // 栈
//        arrayDeque.push("aaa");
//        arrayDeque.push("bbb");
//        System.out.println(arrayDeque.pop());
//        System.out.println(arrayDeque.pop());
        // 队列
        arrayDeque.offer("aaa");
        arrayDeque.offer("bbb");
        System.out.println(arrayDeque.poll());
        System.out.println(arrayDeque.poll());
        Map<Object, Object> linkedHashMap = new LinkedHashMap<>();
        Map<Object, Object> hashMap = new HashMap<>(5);
        hashMap.put("aa", "bb");
        System.out.println(15 & 1000);
        int cap = 65537;
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
//        int x = 2;
//        x |= x >>> 1;
//        System.out.println(x);
        System.out.println(n + 1);
        System.out.println(65536 * 2);
        System.out.println(1010 & 15);
        System.out.println(1010 % 16);
    }
}

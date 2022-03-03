package com.example.demo.testMain;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.entity.User;
import com.example.demo.util.DateUtil;

import java.util.*;

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

        LinkedList<Object> linkedList = new LinkedList<>();
        linkedList.push(1);
        linkedList.push(2);
        System.out.println(linkedList.pop());

        ArrayDeque<Object> arrayDeque = new ArrayDeque<>();
        arrayDeque.push("aaa");
        arrayDeque.push("bbb");
        System.out.println(arrayDeque.pop());
    }
}

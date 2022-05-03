package com.example.demo.testMain;

import com.example.demo.entity.User;
import com.example.demo.tree.BinaryNode;

import java.util.*;

/**
 * @author ChangLF 2022-02-18
 */
public class TestList {

    public static void main(String[] args) {

        List<Integer> linkedList = new LinkedList<>();
        new Stack<>();
        List<? super CharSequence> arrayList = new ArrayList<>();
        arrayList.add("a");
        Object object = arrayList.get(1);

        List<? extends CharSequence> numberArrayList = new ArrayList<>();

        CharSequence charSequence = numberArrayList.get(1);

        List<User> list = new ArrayList<>();
        list.sort(Comparator.comparing(User::getId));

    }

    public static void test(List<?> list){

        Object o = list.get(1);
    }
}

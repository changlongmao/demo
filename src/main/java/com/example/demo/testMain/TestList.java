package com.example.demo.testMain;

import com.example.demo.tree.BinaryNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ChangLF 2022-02-18
 */
public class TestList {

    public static void main(String[] args) {

        List<? super CharSequence> arrayList = new ArrayList<>();
        arrayList.add("a");
        Object object = arrayList.get(1);

        List<? extends CharSequence> numberArrayList = new ArrayList<>();

        CharSequence charSequence = numberArrayList.get(1);

        List<?> list = new ArrayList<>();

    }

    public static void test(List<?> list){

        Object o = list.get(1);
    }
}

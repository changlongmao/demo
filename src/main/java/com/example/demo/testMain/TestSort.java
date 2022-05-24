package com.example.demo.testMain;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * @author ChangLF 2022-04-18
 */
public class TestSort {


    public static void main(String[] args) {

        String a = "中";
        System.out.println(a.compareTo("国"));
        System.out.println('中' + 1 - 1);
        System.out.println('国' + 1 - 1);
        System.out.println('中' - '国');
        Integer i = 1;
        i.compareTo(2);
//        new ArrayList<>().sort().stream().sorted().forEach();
        Random random = new Random();
        for (int j = 0; j < 20; j++) {
            System.out.println(random.nextInt(5) + 5);
        }
    }
}

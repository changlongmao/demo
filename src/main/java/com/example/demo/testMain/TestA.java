package com.example.demo.testMain;

import cn.hutool.bloomfilter.bitMap.BitMap;
import com.example.demo.entity.User;
import com.example.demo.util.IpUtils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * @author ChangLF 2023-03-15
 */
public class TestA {

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 2, 3, 4, 5, 6, 7, 8, 9};

        int a = 7;
        for (int i = 0; i < arr.length; i++) {
            List<Integer> list = new ArrayList<>();
            list.add(arr[i]);
            int b = arr[i];
            for (int j = i + 1; j < arr.length; j++) {
                list.add(arr[j]);
                b += arr[j];
                if (b == a) {
                    System.out.println(list);
                }
            }
        }

        new BitSet();
        System.out.println(null instanceof User);
    }
}

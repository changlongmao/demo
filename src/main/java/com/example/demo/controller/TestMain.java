package com.example.demo.controller;

import com.google.common.collect.Multimap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author ChangLF 2021-12-07
 */
public class TestMain {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello world");
//        int row = test(1, new int[100000]);
//        System.out.println("end:" + row);

        System.out.println(Integer.toBinaryString(100));
        System.out.println(Integer.toBinaryString(100 >> 2));
        System.out.println(Integer.toBinaryString(-100));
        System.out.println(Integer.toBinaryString(-100 >> 2));
        System.out.println(Integer.toBinaryString(-100 >>> 2));
        System.out.println(100 >> 2);
        System.out.println(-100 >> 2);
        System.out.println(-100 >>> 2);
        System.out.println(Integer.MAX_VALUE);


        System.out.println(Integer.toBinaryString(-128));
        System.out.println(Integer.toBinaryString(-127));
        System.out.println(Byte.MAX_VALUE);
        System.out.println(-3 % 2 );
        System.out.println(0xffffffff);
        System.out.println(0x00000000);

        System.out.println(Long.MAX_VALUE);
        System.out.println(Long.MIN_VALUE);

        Long idwork = 2199023255552L << 22 | 1 << 17 | 16 << 12 | 5;
        System.out.println(System.currentTimeMillis());
        System.out.println(Long.toBinaryString(System.currentTimeMillis()));
        System.out.println(System.currentTimeMillis() - 1420041600000L);
        System.out.println(Long.toBinaryString(System.currentTimeMillis() - 1420041600000L));
        System.out.println(System.currentTimeMillis() << 22);
        System.out.println(Long.toBinaryString(System.currentTimeMillis() << 22));
        System.out.println(System.currentTimeMillis() - 1420041600000L << 22);
        System.out.println(Long.toBinaryString(System.currentTimeMillis() - 1420041600000L << 22));
        System.out.println((1L << 41));
        System.out.println((1L << 63) - 1);
        System.out.println(1L << 63);

        System.out.println(idwork);
        System.out.println(Long.toBinaryString(idwork));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date((911273425874456576L >> 22) + 1420041600000L)));
        System.out.println(df.format(new Date(1420041600000L)));
    }

    public static int test(int x, int[] arr) throws InterruptedException {
        System.out.println("轮询次数：" + x);
        for (int i = 0; i < 1000; i++) {
            int xx = 1;
        }
//        TimeUnit.NANOSECONDS.sleep(1000000);
        int y = x + 1;
        arr[x] = y;
        if (y < 50000) {
//            test1();
            return test(y, arr);
        }
        return y;
    }

    public static void test1() {
        int i = 1;
    }
}

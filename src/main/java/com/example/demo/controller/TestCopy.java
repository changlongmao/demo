package com.example.demo.controller;

import com.example.demo.entity.TestCopyOne;
import com.example.demo.entity.User;
import org.springframework.beans.BeanUtils;

/**
 * @author ChangLF 2022-01-11
 */
public class TestCopy {

    public static void main(String[] args) throws InterruptedException {
        test(1, new int[1100000]);
    }

    public static int test(int x, int[] arr) throws InterruptedException {
        System.out.println("轮询次数：" + x);

        for (long i = 0; i < 10000L; i++) {
            int y = x + 1;
            arr[y] = y;
            if (y > 9998) {
                return test(y, arr);
            }
            x++;
        }
        return 1;
    }

}

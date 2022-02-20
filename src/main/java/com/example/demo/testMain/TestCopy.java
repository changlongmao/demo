package com.example.demo.testMain;

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

//        int a = 1;
//        int a1 = 1;
//        int a2 = 1;
//        int a3 = 1;
//        int a4 = 1;
//        int a5 = 1;
//        int a6 = 1;
//        int a7 = 1;
//        int a8 = 1;
//        int a9 = 1;
//        int a10 = 1;
        for (long i = 0; i < 11L; i++) {
            int y = x + 1;
            arr[y] = y;
            if (y > 9) {
                return test(y, arr);
            }
            x++;
        }
        return 1;
    }

}

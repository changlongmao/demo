package com.example.demo.testMain;

import com.example.demo.entity.TestCopyOne;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author ChangLF 2022-01-11
 */
@Slf4j
public class TestStackOverError {

    public static void main(String[] args) throws InterruptedException {
        test(1, new int[1100000]);
    }

    public static int test(int x, int[] arr) throws InterruptedException {
        log.info("递归次数：{}", x++);

        for (int i = 0; i < 10; i++) {
            int z = 1;
        }
        arr[x] = x;
        return test(x, arr);
    }

}

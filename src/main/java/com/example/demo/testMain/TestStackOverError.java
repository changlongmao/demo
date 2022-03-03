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
        log.info("递归次数：" + x);

        for (long i = 0; i < 2L; i++) {
            int y = x + 1;
            arr[y] = y;
            if (y > 1) {
                return test(y, arr);
            }
            x++;
        }
        return 1;
    }

}

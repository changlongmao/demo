package com.example.demo.testMain;

import com.example.demo.entity.TestCopyOne;
import com.example.demo.entity.User;
import com.example.demo.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ChangLF 2022-01-11
 */
@Slf4j
public class TestStackOverError {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(TimeZone.getDefault());
        Date date = new Date();
        System.out.println(date.toString());
        int[] a = new int[]{1};
        System.out.println(date.toInstant());
        System.out.println(date.toGMTString());
        System.out.println(date.toLocaleString());
//        test(1, new int[1100000]);
    }

    public static int test(int x, int[] arr) throws InterruptedException {
        log.info("递归次数：{}", x++);
        Long ia = 11111L;
        Long i1 = 11111L;
        Long i2 = 11111L;
        Long i3 = 11111L;
        Long i4 = 11111L;
        Long i5 = 11111L;
        Long i6 = 11111L;
        Long i7 = 11111L;
        Long i8 = 11111L;
        Long i9 = 11111L;
        Long i10 = 1111L;
        Long i11 = 1111L;
        Long i12 = 1111L;
        Long i13 = 1111L;
        Long i14 = 1111L;
        Long i15 = 1111L;
        Long i16 = 1111L;
        Long i17 = 1111L;
        Long i18 = 1111L;
        Long i19 = 1111L;
        Long i20 = 1111L;
        Long i21 = 1111L;
        Long i22 = 1111L;
        Long i23 = 1111L;
        Long i24 = 1111L;
        Long i25 = 1111L;
        Long i26 = 1111L;
        Long i27 = 1111L;
        Long i28 = 1111L;
        Long i29 = 1111L;
        Long i30 = 1111L;
        return test(x, arr);
    }

}


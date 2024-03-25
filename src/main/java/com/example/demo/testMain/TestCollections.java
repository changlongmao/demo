package com.example.demo.testMain;

import com.example.demo.enums.UserTypeEnum;

import java.util.*;

/**
 * @author ChangLF 2023/12/23
 */
public class TestCollections {


    public static void main(String[] args) {

        List<Object> objects = Collections.synchronizedList(new ArrayList<>());

        Arrays.asList("aa");

        EnumSet.of(UserTypeEnum.UNKNOWN_TYPE).add(UserTypeEnum.AMOUNT_PRODUCT);
        new A();
    }

      static class A{}
}

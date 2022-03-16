package com.example.demo.testMain;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.example.demo.entity.TestObject;

/**
 * @author ChangLF 2022-03-08
 */
public class TestHashCode {
    public static void main(String[] args) {

        System.out.println(Integer.valueOf(100000).hashCode());
        System.out.println(Integer.valueOf(200000).hashCode());
        System.out.println(String.valueOf(2).hashCode());
        System.out.println(String.valueOf(1).hashCode());
        System.out.println("a".hashCode());
        System.out.println(new TestObject().hashCode());
        char x = 'a';
        Character y = '中';

        System.out.println(x + 1);
        System.out.println(y + 1);
        System.out.println(Integer.MAX_VALUE  * Integer.MAX_VALUE);
        Character c = 'a';
        System.out.println(Character.MAX_VALUE);
        System.out.println(Character.MIN_CODE_POINT);
        System.out.println(Character.MAX_CODE_POINT);
        System.out.println(1 << 30);
    }
}

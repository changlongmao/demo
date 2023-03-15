package com.example.demo.testMain;

import java.util.ArrayList;

/**
 * @author ChangLF 2022-03-01
 */
public class TestString {
    public static final String regEx = "[\n\r`~!！@#￥$%^&*()+=|{}【】':：;；,\\[\\].<>/?？…（）—_-‘”“’。， 、]";

    public static void main(String[] args) {
        String regEx = "[\n\r`~～·!！@#￥$%…^&*()（）+=|、{}【】「」':：;；,\\[\\].<>《》/?？—_\\-‘”“’。， ]";
        String str = "您好，请问下\n您在智联招聘\r上的名称?？是；;，,什么\r\n？我看下您。。.;:''的”“简历情况，好给您介绍岗位信息哈。";

        String newString = str.replaceAll(regEx, "");

        System.out.println(newString);

        String a = "aa";
        String b = a;
        System.out.println(a == b);
        System.out.println('常' + 1);

        System.out.println(new ArrayList<>().stream().count());
    }

    public static void test(String a) {
        new ArrayList<>().forEach(e -> System.out.println(a.substring(1)));
    }
}

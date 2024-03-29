package com.example.demo.testMain;

import cn.afterturn.easypoi.exception.excel.enums.ExcelExportEnum;
import com.example.demo.entity.User;
import com.example.demo.enums.ExcelImportTypeEnum;
import com.example.demo.util.JsonUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ChangLF 2022-08-07
 */
public class TestClass {

    public static final String a = "1";

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("aa", "bb");
        map.put("aa", "cc");
        System.out.println(JsonUtils.toJson(map));

        System.out.println(foo(4));
        System.out.println(new User().getClass().getClassLoader());
        System.out.println(new BigDecimal("1.40").compareTo(new BigDecimal("1.4")));
        System.out.println("请求参数不合法：\n不能为null\n用户名最长10个字符\n用户编码不能为空\n");
        ExcelImportTypeEnum[] valus = ExcelImportTypeEnum.class.getEnumConstants();
        System.out.println(valus);
    }

    public static int foo(Integer x) {
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            System.out.println("exception"+x);
            return x;
        } finally {

            System.out.println("finally"+x);
        }
    }
}

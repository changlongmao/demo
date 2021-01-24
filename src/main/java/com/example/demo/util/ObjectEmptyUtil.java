package com.example.demo.util;


import java.util.Collection;
import java.util.Map;

/**
 * @ClassName: FyStringUtil
 * @Description:
 * @Author: Chang
 * @Date: 2020/11/19 10:49
 **/
public class ObjectEmptyUtil {

    /**
     * @Param: obj
     * @Author Chang
     * @Description 非空判断，支持字符串，集合
     * @Date 2020/11/19 11:06
     * @Return boolean
     **/
    public static boolean isBlank (Object obj) {
        if (obj == null) return true;

        if (obj instanceof String) return ((String) obj).trim().length() == 0 || ((String) obj).equalsIgnoreCase("null");

        if (obj instanceof Collection) return ((Collection) obj).isEmpty();

        if (obj instanceof Map) return ((Map) obj).isEmpty();

        return false;
    }

    /**
     * @Param: obj
     * @Author Chang
     * @Description 非空判断
     * @Date 2020/11/19 11:25
     * @Return boolean
     **/
    public static boolean isNotBlank (Object obj) {
        return !isBlank(obj);
    }
}

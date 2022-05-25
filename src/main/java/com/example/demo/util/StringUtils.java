package com.example.demo.util;

import com.example.demo.entity.Constants;

import java.util.Collection;
import java.util.Map;


public class StringUtils {

    public static boolean isEmpty(Object str) {
        return str == null || str.toString().length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }


    public static String addOne(String parentId, String maxId) {
        int ten = 10;
        if (Constants.STR_ZERO.equals(parentId)) {
            parentId = "";
        }
        if (isNullOrEmpty(maxId)) {
            return parentId + "01";
        }

        maxId = maxId.substring(maxId.length() - 2);

        int result = Integer.parseInt(maxId) + 1;

        if (result < ten) {
            return parentId + "0" + result;
        } else {
            return parentId + result + "";
        }
    }


    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (Object anObject : object) {
                if (!isNullOrEmpty(anObject)) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }


    public static String genKey(String prefix, String className, String methodName) {
        return prefix + "userId_" +
                ShiroUtils.getUserId() +
                "_" +
                className +
                "." +
                methodName;
    }
}

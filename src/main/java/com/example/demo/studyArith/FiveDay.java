package com.example.demo.studyArith;

import javax.validation.constraints.Min;
import java.util.ArrayDeque;
import java.util.LinkedList;

/**
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 * https://leetcode-cn.com/problems/longest-palindromic-substring/
 *
 * @author ChangLF 2022-03-20
 */
public class FiveDay {

    public static void main(String[] args) {
        System.out.println(longestPalindrome("cccccc"));
    }

    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 1) {
            return s;
        }
        int start;
        int end;
        char[] chars = s.toCharArray();
        String maxStr = "";
        for (int i = 1; i < chars.length; i++) {
            if ((i > 1 && chars[end = i] == chars[(start = i - 2)])) {
                maxStr = getMaxStr(chars, end, start, maxStr, s);
            }
            if (chars[end = i] == chars[(start = i - 1)]) {
                maxStr = getMaxStr(chars, end, start, maxStr, s);
            }
        }
        return maxStr.equals("") ? s.substring(0, 1) : maxStr;
    }

    private static String getMaxStr(char[] chars, int end, int start, String maxStr, String s) {
        while (true) {
            if (chars[end] != chars[start]) {
                end--;
                start++;
                break;
            }
            if (end == chars.length - 1 || start == 0) {
                break;
            }
            end++;
            start--;
        }
        if (maxStr.length() < end + 1 - start) {
            maxStr = s.substring(start, end + 1);
        }
        return maxStr;
    }
}

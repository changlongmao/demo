package com.example.demo.studyArith;

import java.util.HashMap;
import java.util.Map;

/**
 * 3. 无重复字符的最长子串
 * https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
 * @author ChangLF 2022-03-15
 */
public class ThreeDay {

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("abbcbadhjgbb"));
    }

    public static int lengthOfLongestSubstring(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;
        }
        int maxLength = 0;
        int begin = 0;
        int end = 0;
        Map<Character, Integer> map = new HashMap<>();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            Integer put = map.put(chars[i], i);
            if (put != null) {
                maxLength = Math.max(i - begin, maxLength);
                begin = Math.max(put + 1, begin);
                end = Math.max(put, end);
            }
        }
        if (begin == 0 && end == 0) {
            maxLength = s.length();
        } else {
            maxLength = Math.max(s.length() - 1 - end, maxLength);
        }
        return maxLength;
    }
}

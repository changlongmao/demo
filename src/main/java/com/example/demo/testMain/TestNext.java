package com.example.demo.testMain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChangLF 2023-03-25
 */
public class TestNext {

    /**
     * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
     * 示例 1：
     * 输入：n = 3
     * 输出：["((()))","(()())","(())()","()(())","()()()"]
     * 示例 2：
     * 输入：n = 1
     * 输出：["()"]
     * <p>
     * 提示：
     * <p>
     * 1 <= n <= 8
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(get(3));
    }

    public static Set<String> get(int n) {
        String a = "(";
        String b = ")";
        LinkedList<String> start = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            start.add(a);
            start.add(b);
        }
        Set<String> objects = new HashSet<>();

        for (int i = 0; i < n * n * n; i++) {
            Collections.shuffle(start);
            boolean flag = false;
            Stack<Object> stack = new Stack<>();
            for (int j = 0; j < n * 2; j++) {
                String s = start.get(j);
                if (s.equals(a)) {
                    stack.push(s);
                } else if (s.equals(b)){
                    if (stack.empty()) {
                        flag = true;
                        break;
                    }
                    stack.pop();
                }
            }
            if (stack.empty() && !flag) {
                objects.add(start.toString());
            }
        }

        return objects;
    }
}

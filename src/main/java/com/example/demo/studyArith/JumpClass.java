package com.example.demo.studyArith;

/**
 * @author ChangLF 2024/03/01
 */
public class JumpClass {

    public static void main(String[] args) {
        System.out.println(jump(new int[]{2,3,3,0,1,2,0,4}));
    }

    public static int jump(int[] nums) {
        int length = nums.length;
        int end = 0;
        int maxPosition = 0;
        int steps = 0;
        for (int i = 0; i < length - 1; i++) {
            maxPosition = Math.max(maxPosition, i + nums[i]);
            if (i == end) {
                end = maxPosition;
                steps++;
            }
        }
        return steps;
    }
}

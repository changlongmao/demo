package com.example.demo.studyArith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * 寻找两个正序数组的中位数
 * https://leetcode-cn.com/problems/median-of-two-sorted-arrays/
 * @author ChangLF 2022-03-16
 */
public class FourDay {

    public static void main(String[] args) {

        System.out.println(findMedianSortedArrays(new int[]{1,2}, new int[]{3,4}));
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int total = nums1.length + nums2.length;
        int[] nums3 = new int[total];
        int i = 0;
        int j = 0;
        int k = 0;
        while(i < nums1.length && j < nums2.length){
            if(nums1[i] < nums2[j]){
                nums3[k++] = nums1[i++];
            }else {
                nums3[k++] = nums2[j++];
            }
        }
        while(i < nums1.length){
            nums3[k++] = nums1[i++];
        }
        while(j < nums2.length){
            nums3[k++] = nums2[j++];
        }
        // 对1与运算为0为偶数，为1为奇数
        int index = total / 2;
        if ((total & 1) == 0) {
            return ((double) nums3[index - 1] + nums3[index]) / 2;
        } else {
            return nums3[index];
        }
    }

}
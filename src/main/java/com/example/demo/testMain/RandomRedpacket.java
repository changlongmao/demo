package com.example.demo.testMain;

import java.util.Arrays;
import java.util.Random;

/**
 * @author ChangLF 2023-03-05
 */
public class RandomRedpacket {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(100, 10)));
    }

    public static int[] solution(int a, int b) {
        int[] arr = new int[b];
        if (b <= 0 || a < b) {
            return arr;
        }
        // 剩余金额
        int lastMoney = a;
        // 剩余人数
        int lastPerson = b;
        Random random = new Random();
        for (int i = 0; i < b; i++) {
            if (lastPerson == 1) {
                arr[i] = lastMoney;
            }
            // 每次抢红包金额为剩余金额除以剩余人数乘以2以内的随机数，使分配金额相对均匀
            int money = random.nextInt(lastMoney / lastPerson * 2);
            if (money == 0 || money > lastMoney * 0.9) {
                i--;
                continue;
            }
            lastMoney = lastMoney - money;
            arr[i] = money;
            lastPerson--;
        }
        return arr;
    }
}

package com.example.demo.testMain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author ChangLF 2022-11-06
 */
public class TestPorp {

    public static void main(String[] args) {
        // Generate data
        int arraySize = 32768;
        int data[] = new int[arraySize];

        Random rnd = new Random(0);
        for (int c = 0; c < arraySize; ++c)
            data[c] = rnd.nextInt() % 256;
        long start = System.currentTimeMillis();

        // !!! With this, the next loop runs faster
        Arrays.sort(data);

        long start2 = System.currentTimeMillis();
        long sum = 0;

        for (int i = 0; i < 100; ++i) {
            // Primary loop
            for (int c = 0; c < arraySize; ++c) {
                if (data[c] >= 128)
                    sum += data[c];
            }
        }

        long end = System.currentTimeMillis();
        System.out.println((start2-start) + "ms," + (end - start2) + "ms");
        System.out.println("sum = " + sum);
    }
}


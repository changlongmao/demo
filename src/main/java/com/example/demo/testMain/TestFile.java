package com.example.demo.testMain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author ChangLF 2023-01-02
 */
public class TestFile {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/changlongmao/Desktop/demo.sh");
        // 4.1 创建字节数组
        byte[] b = new byte[2176];
        // 4.2 据读取到字节数组中.
        int len = fileInputStream.read(b);
        // 4.3 解析数组,打印字符串信息
        String msg = new String(b, 0, len);
        System.out.println(msg);

    }
}

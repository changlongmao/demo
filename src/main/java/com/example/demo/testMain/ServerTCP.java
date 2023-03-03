package com.example.demo.testMain;

import org.springframework.beans.BeanUtils;

import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ChangLF 2023-01-02
 */@WebServlet
public class ServerTCP {

    public static void main(String[] args) throws IOException {
        System.out.println("服务端启动 , 等待连接 .... ");
        // 1.创建 ServerSocket对象，绑定端口，开始等待连接
        ServerSocket ss = new ServerSocket(6666);
        while (true) {
            // 2.接收连接 accept 方法, 返回 socket 对象.
            Socket server = ss.accept();
            // 3.通过socket 获取输入流
            InputStream is = server.getInputStream(); // 4.一次性读取数据
            // 4.1 创建字节数组
            byte[] b = new byte[1024];
            // 4.2 据读取到字节数组中.
            int len = is.read(b);
            // 4.3 解析数组,打印字符串信息
            String msg = new String(b, 0, len);
            System.out.println(msg);
            // =================回写数据=======================
            // 5. 通过 socket 获取输出流
            OutputStream out = server.getOutputStream();
            // 6. 回写数据
            out.write(("HTTP/1.1 200 OK\n" +
                    "Date: Sat, 31 Dec 2005 23:59:59 GMT\n" +
                    "Content-Type: application/json\n\n" +
                    "{\"cc\": \"dd\"}").getBytes());
            // 7.关闭资源.
            out.close();
            is.close();
            server.close();
        }
    }
}

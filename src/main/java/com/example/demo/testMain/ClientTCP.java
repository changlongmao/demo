package com.example.demo.testMain;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author ChangLF 2023-01-02
 */
public class ClientTCP {

    public static void main(String[] args) throws Exception {
        System.out.println("客户端 发送数据");
        // 1.创建 Socket ( ip , port ) , 确定连接到哪里.
        Socket client = new Socket("localhost", 6667);
        // 2.获取流对象 . 输出流
        OutputStream os = client.getOutputStream();
        // 3.写出数据.
        os.write("hello? tcp , I am coming".getBytes());
        // ==============解析回写=========================
        // 4. 通过Scoket,获取 输入流对象
        InputStream in = client.getInputStream();
        // 5. 读取数据数据
        byte[] b = new byte[100];
        int len = in.read(b);
        System.out.println(new String(b, 0, len));
        // 6. 关闭资源 . in.close();
        os.close();
        client.close();
    }
}

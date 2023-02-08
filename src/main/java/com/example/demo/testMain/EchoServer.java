package com.example.demo.testMain;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

/**
 * @author ChangLF 2023-02-08
 */
public class EchoServer {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();    // 获取选择器

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // 打开服务器通道
        serverSocketChannel.configureBlocking(false);                         // 服务器通道配置为非阻塞模式
        serverSocketChannel.bind(new InetSocketAddress(9090));           // 绑定 TCP 端口 9090
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);       // 将服务器通道注册到选择器 selector 中，注册事件为 ACCEPT

        DatagramChannel datagramChannel = DatagramChannel.open();             // 打开套接字通道
        datagramChannel.configureBlocking(false);                             // 配置通道为非阻塞模式
        datagramChannel.bind(new InetSocketAddress(9090));               // 绑定 UDP 端口 9090
        datagramChannel.register(selector, SelectionKey.OP_READ);             // 将通道注册到选择器 selector 中，注册事件为读取数据

        ByteBuffer buf = ByteBuffer.allocate(1024);                           // 分配一个 1024 字节的堆字节缓冲区

        while (selector.select() > 0) {                                        // 轮询已经就绪的注册的通道的 I/O 事件
            Set<SelectionKey> keys = selector.selectedKeys();                 // 获取就绪的 I/O 事件，即选择器键集合
            for (SelectionKey key : keys) {                                    // 遍历选择键，处理就绪事件
                if (key.isAcceptable()) {                                       // 选择键的事件的是 I/O 连接事件
                    SocketChannel socketChannel = serverSocketChannel.accept(); // 执行 I/O 操作，获取套接字连接通道
                    socketChannel.configureBlocking(false);                   // 配置为套接字通道为非阻塞模式
                    socketChannel.register(selector, SelectionKey.OP_READ);   // 将套接字通过到注册到选择器，关注 READ 事件
                } else if (key.isReadable()) {                        // 选择键的事件是 READ
                    StringBuilder sb = new StringBuilder();
                    if (key.channel() instanceof DatagramChannel) {  // 选择的通道为数据报通道，客户端是通过 UDP 连接过来的
                        sb.append("UDP Client: ");
                        datagramChannel.receive(buf);              // 最多读取 1024 字节，数据报多出的部分自动丢弃
                        buf.flip();
                        while (buf.position() < buf.limit()) {
                            sb.append((char) buf.get());
                        }
                        buf.clear();
                    } else {                                          // 选择的通道为套接字通道，客户端时通过 TCP 连接过来的
                        sb.append("TCP Client: ");
                        ReadableByteChannel channel = (ReadableByteChannel) key.channel(); // 获取通道
                        int size;
                        while ((size = channel.read(buf)) > 0) {
                            buf.flip();
                            while (buf.position() < buf.limit()) {
                                sb.append((char) buf.get());
                            }
                            buf.clear();
                        }

                        if (size == -1) {
                            sb.append("Exit");
                            channel.close();
                        }
                    }
                    System.out.println(sb);
                }
            }
            keys.clear();  // 将选择键清空，防止下次循环时被重复处理
        }
    }
}


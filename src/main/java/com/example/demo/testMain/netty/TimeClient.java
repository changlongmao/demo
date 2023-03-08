package com.example.demo.testMain.netty;

import com.example.demo.testMain.netty.echo.ChatClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.List;

/**
 * @author ChangLF 2023-02-15
 */
public class TimeClient {

    public static Channel channel;
    public void connect(int port, String host) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(28));
//                            socketChannel.pipeline().addLast(new TimeClientHandler());

                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());

                            pipeline.addLast(new ChatClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            channel = channelFuture.channel();
//            ChannelFuture closeFuture = channel.closeFuture();
//            closeFuture.sync();
            for (int i = 0; i < 5; i++) {
                channel.writeAndFlush("写成功");
            }
            Thread.sleep(500000);
            channel.close();

            System.out.println("end listening ...");
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 6667;
        new TimeClient().connect(port, "127.0.0.1");
    }
}

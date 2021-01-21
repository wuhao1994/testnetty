package com.example.demo.service;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Constant;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class NettyClient {

  public static void main(String[] args) throws InterruptedException {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(group) //设置线程组
          .channel(NioSocketChannel.class) // 使用 NioSocketChannel 作为客户端的通道实现
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
              //加入处理器
              channel.pipeline().addLast(new NettyClientHandler());
            }
          });
      System.out.println("netty client start");
       //启动客户端去连接服务器端
       Channel channel = bootstrap.connect("127.0.0.1", 9000).channel();
      String reqStr = "我是客户端请求1$_";

      // 发送客户端的请求
      channel.writeAndFlush(Unpooled.copiedBuffer(reqStr.getBytes("UTF-8")));
      Thread.sleep(300);
      channel.writeAndFlush(Unpooled.copiedBuffer("我是客户端请求2$_---".getBytes("UTF-8")));
      Thread.sleep(300);
      channel.writeAndFlush(Unpooled.copiedBuffer("我是客户端请求3$_".getBytes("UTF-8")));


      // 等待直到连接中断
      channel.closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      group.shutdownGracefully();
    }
  }
}

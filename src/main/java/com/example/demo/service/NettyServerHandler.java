package com.example.demo.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelRegistered success");
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println("服务器读取线程 " + Thread.currentThread().getName());
    //Channel channel = ctx.channel();
    //ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接, 出站入站
    //将 msg 转成一个 ByteBuf，类似NIO 的 ByteBuffer
    ByteBuf buf = (ByteBuf) msg;
    System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ByteBuf buf = Unpooled.copiedBuffer("HelloClient", CharsetUtil.UTF_8);
    ctx.writeAndFlush(buf);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.close();
  }

}

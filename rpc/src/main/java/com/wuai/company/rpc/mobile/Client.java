package com.wuai.company.rpc.mobile;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by nessary on 16-8-18.
 */
public class Client {


    public static void main(String[] args) throws InterruptedException {


        EventLoopGroup group = new NioEventLoopGroup(1);

        Bootstrap bootstrap = new Bootstrap();


        ChannelFuture future = bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new JSONDecoder());
                pipeline.addLast(new JSONEncoder());
                pipeline.addLast(new ClientHandler());

            }


        }).connect("test.52woo.com", 8073).sync();


        Channel channel = future.channel();


        //token+":"+uid+":"+notify
//        channel.writeAndFlush("eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMjM0NTYiLCJpZCI6MSwiZXhwIjoxNDk5MzQ4ODM3LCJpYXQiOjE0OTkzMjcyMzh9.BpxS8gzrE4r9QAZShtJWy5LY77YV5MweSb8TVK_-KYA:123456:notify");
        channel.writeAndFlush("eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiJFQUUzRkNFRi0zMjI4LTQwQzgtQTBDMC03MjQ4QjA3NjJCNUUiLCJpZCI6MTA0MCwiZXhwIjoxNDk5NDE5OTYxLCJpYXQiOjE0OTkzOTgzNjF9.-Ov84UNAFhjXY6vWsItFEfnZjPpSE7EVfDK5nG347ek:EAE3FCEF-3228-40C8-A0C0-7248B0762B5E:notify");

    }

}

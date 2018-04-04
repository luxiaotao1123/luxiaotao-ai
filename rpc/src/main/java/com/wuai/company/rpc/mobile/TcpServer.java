package com.wuai.company.rpc.mobile;

import com.wuai.company.entity.Orders;
import com.wuai.company.enums.RpcAllowMsgEnum;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 移动端pc
 */
//@Component
public class TcpServer implements DisposableBean {


    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;
    @Value("${tcp.port}")
    private int port;


    /**
     * 日志文件
     */
    private Logger logger = LoggerFactory.getLogger(TcpServer.class);


    @PostConstruct
    public void startServer() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new JSONDecoder());
                    pipeline.addLast(new JSONEncoder());
                    pipeline.addLast(new ServerHandler());
                }

            });


            channel = b.bind(port).sync().channel();


            logger.info("移动端tcp初始化完成");


        } catch (InterruptedException e) {
            logger.warn("tcp服务端出错...", e);
        }
    }





    @Override
    public void destroy() throws Exception {
        try {


            if (channel != null && channel.isActive()) {
                channel.close();
            }


            if (bossGroup != null) {

                bossGroup.shutdownGracefully();
            }

            if (workerGroup != null) {

                workerGroup.shutdownGracefully();

            }


        } catch (Exception e) {
            logger.warn("tcp服务端关闭出错...", e);
        }
    }
}

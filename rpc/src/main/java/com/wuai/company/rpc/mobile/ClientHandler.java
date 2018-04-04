package com.wuai.company.rpc.mobile;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by nessary on 16-8-18.
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {




    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        try {
        	System.err.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }



}

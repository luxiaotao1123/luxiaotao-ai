
package com.wuai.company.rpc.mobile;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.StringUtils;


public class JSONEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        if (StringUtils.isEmpty(msg)) {
            return;
        }

        out.writeBytes(msg.getBytes());

    }


}

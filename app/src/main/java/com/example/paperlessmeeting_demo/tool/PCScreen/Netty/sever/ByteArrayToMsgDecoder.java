package com.example.paperlessmeeting_demo.tool.PCScreen.Netty.sever;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class ByteArrayToMsgDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> list) throws Exception {
        byte[] array = new byte[msg.readableBytes()];
        msg.getBytes(0, array);
        list.add(array);

    }
}

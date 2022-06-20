package com.example.paperlessmeeting_demo.tool.PCScreen.Netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

@ChannelHandler.Sharable
public class NettyClientReaderTimeOutHandler extends ReadTimeoutHandler {
    private NettyListener listener;

    public void  NettyClientReaderTimeOutHandler(NettyListener listener) {
        this.listener = listener;
    }

    public void changeListener(NettyListener listener){
        this.listener = listener;
    }

    public NettyClientReaderTimeOutHandler(int timeoutSeconds) {
        super(timeoutSeconds);

    }

    @Override
    protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
//        super.readTimedOut(ctx);
        listener.onMessageReaderTimeout();
    }
}

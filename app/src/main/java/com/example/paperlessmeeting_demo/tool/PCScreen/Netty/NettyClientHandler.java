package com.example.paperlessmeeting_demo.tool.PCScreen.Netty;

import android.util.Log;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveData;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.AnalyticDataUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final String TAG = "NettyClientHandler";
    private NettyListener listener;
    private AnalyticDataUtils mAnalyticUtils;
    public NettyClientHandler(NettyListener listener) {
        this.listener = listener;
        mAnalyticUtils = new AnalyticDataUtils();
    }

    public void changeListener(NettyListener listener){
        this.listener = listener;
    }


    //每次给服务器发送的东西， 让服务器知道我们在连接中哎
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    }

    /**
     * 连接成功
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.e(TAG, "channelActive");
        super.channelActive(ctx);
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_SUCCESS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Log.e(TAG, "channelInactive");
    }

    //接收消息的地方， 接口调用返回到activity了
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        result.release();

//        byte[] result = (byte[]) msg;
//        final String ss = new String(result1);


        final ReceiveData receiveData = mAnalyticUtils.analyticData(result1);

//        final String ss = ByteToString.toHexString(result1);

//        System.out.println("客户端开始读取服务端过来的信息" + ss);
        listener.onMessageResponse(receiveData);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当引发异常时关闭连接。
        Log.e(TAG, "exceptionCaught");
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_ERROR);
        cause.printStackTrace();
        ctx.close();
    }
}

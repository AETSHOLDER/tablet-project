package com.example.paperlessmeeting_demo.tool.PCScreen.Netty.sever;

import android.util.Log;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveData;
import com.example.paperlessmeeting_demo.tool.ScreenTools.server.EncodeV1;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.AnalyticDataUtils;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.SocketCmd;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventScreenMessage;
import org.greenrobot.eventbus.EventBus;
import java.util.Arrays;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    private AnalyticDataUtils mAnalyticUtils = new AnalyticDataUtils();
    private ChannelHandlerContext currentLinkCtx = null;
    //定义一个channle 组，管理所有的channel
    //GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 有客户端与服务器发生连接时执行此方法
     * 1.打印提示信息
     * 2.将客户端保存到 channelGroup 中
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有新的客户端与服务器发生连接。客户端地址：" + channel.remoteAddress());
        channelGroup.add(channel);
    }

    /**
     * 当有客户端与服务器断开连接时执行此方法，此时会自动将此客户端从 channelGroup 中移除
     * 1.打印提示信息
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有客户端与服务器断开连接。客户端地址：" + channel.remoteAddress());
    }

    /**
     * 表示channel 处于活动状态
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 处于活动状态");
    }

    /**
     * 表示channel 处于不活动状态
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(currentLinkCtx == ctx){
            currentLinkCtx = null;

            EventScreenMessage eventScreenMessage = new EventScreenMessage(MessageReceiveType.MessageScreenDisconnect, null);
            EventBus.getDefault().postSticky(eventScreenMessage);
        }
        System.out.println(ctx.channel().remoteAddress() + " 处于不活动状态");
    }

    /**
     * 读取到客户端发来的数据数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        //获取到当前channel
        Channel channel = ctx.channel();
//        System.err.println("有客户端发来的数据。地址：" + channel.remoteAddress() + " 111111内容：" + msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();

        ByteBuf byteBuf = (ByteBuf)msg;
        byteBuf.retain();
        byte[] array = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, array);
//        byte[] arr = ByteBufUtil.getBytes((ByteBuf)msg) ;
//        Log.e("服务端收到数据","Arrays.toString(arr)==="+Arrays.toString(array));

        final ReceiveData receiveData = mAnalyticUtils.analyticData(array);
        if(receiveData !=null && receiveData.getHeader()!=null && receiveData.getHeader().getMainCmd() == SocketCmd.SocketCmd_ScreentData){
//            listener.acceptBuff(receiveData.getBuff());

            //  发黏性事件
            EventScreenMessage eventScreenMessage = new EventScreenMessage(MessageReceiveType.MessageScreenData, receiveData);
            EventBus.getDefault().postSticky(eventScreenMessage);
        }
        if(receiveData !=null && receiveData.getHeader()!=null && receiveData.getHeader().getMainCmd() == SocketCmd.SocketCmd_ReqReceiveScreen){
//            listener.acceptBuff(receiveData.getBuff());
            EncodeV1 encodeV1 = null;
            if(currentLinkCtx !=null && currentLinkCtx != ctx){
                encodeV1 = new EncodeV1(SocketCmd.SocketCmd_RepReject_001,new byte[0]);
            }else {
                currentLinkCtx = ctx;
                encodeV1 = new EncodeV1(SocketCmd.SocketCmd_RepAccept,new byte[0]);
            }

            Log.e("服务端准备发送数据","Arrays.toString(arr)==="+Arrays.toString(encodeV1.buildSendContent()));
            ctx.writeAndFlush(Unpooled.buffer().writeBytes(encodeV1.buildSendContent()));

            //  发黏性事件
            EventScreenMessage eventScreenMessage = new EventScreenMessage(MessageReceiveType.MessageScreenReq, receiveData);
            EventBus.getDefault().postSticky(eventScreenMessage);
        }

//        System.err.println("有客户端发来的数据。地址：" + channel.remoteAddress() + " 222222内容：" + Arrays.toString(arr));
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.e("发生异常。异常信息：{}", cause.getMessage());
        //关闭通道
        ctx.close();
    }
}

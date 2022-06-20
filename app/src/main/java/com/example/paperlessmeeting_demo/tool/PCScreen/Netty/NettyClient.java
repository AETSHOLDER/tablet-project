package com.example.paperlessmeeting_demo.tool.PCScreen.Netty;

import android.os.SystemClock;
import android.util.Log;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    private static final String TAG = "NettyClient";

    private EventLoopGroup group;//Bootstrap参数

    private NettyListener listener;//写的接口用来接收服务端返回的值

    private Channel channel;//通过对象发送数据到服务端

    private boolean isConnect = false;//判断是否连接了

    private static int reconnectNum = 0;//定义的重连到时候用
    private boolean isNeedReconnect = false;//是否需要重连
    private boolean isConnecting = false;//是否正在连接
    private long reconnectIntervalTime = 5000;//重连的时间

    public String host;//ip
    public int tcp_port;//端口
    public NettyClientHandler handler = new NettyClientHandler(listener);;

    public NettyClientReaderTimeOutHandler readerhandler = new NettyClientReaderTimeOutHandler(3);
    /**
     * 构造 传入 ip和端口
     */
    public NettyClient(String host, int tcp_port) {
        this.host = host;
        this.tcp_port = tcp_port;
    }

    /**
     * 连接方法
     */
    public void connect() {

        if (isConnecting) {
            return;
        }
        //起个线程
        Thread clientThread = new Thread("client-Netty") {
            @Override
            public void run() {
                super.run();
                isNeedReconnect = false;
                reconnectNum = Integer.MAX_VALUE;
                connectServer();
            }
        };
        clientThread.start();
    }

    //连接时的具体参数设置
    private void connectServer() {
        synchronized (NettyClient.this) {
            readerhandler.NettyClientReaderTimeOutHandler(listener);
            ChannelFuture channelFuture = null;//连接管理对象
            if (!isConnect) {
                isConnecting = true;
                group = new NioEventLoopGroup();//设置的连接group
                Bootstrap bootstrap = new Bootstrap().group(group)//设置的一系列连接参数操作等
                        .option(ChannelOption.TCP_NODELAY, true)//屏蔽Nagle算法试图
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() { // 5
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
//                                ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));//服务端最后以"\n"作为结束标识
//                                ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));//解码
//                                ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));//解码
                                ch.pipeline().addLast(readerhandler);//读取超时 在设置时间内没有读取操作
                                ch.pipeline().addLast(handler);//需要的handlerAdapter
                                ch.pipeline().addLast();
                            }
                        });

                try {
                    //连接监听
                    channelFuture = bootstrap.connect(host, tcp_port).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                Log.e(TAG, "连接成功");
                                isConnect = true;
                                channel = channelFuture.channel();

                            } else {
                                Log.e(TAG, "连接失败");
                                isConnect = false;
                            }
                            isConnecting = false;
                        }
                    }).sync();

                    // 等待连接关闭
                    channelFuture.channel().closeFuture().sync();
                    Log.e(TAG, " 断开连接");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isConnect = false;
                    listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_CLOSED);//STATUS_CONNECT_CLOSED 这我自己定义的 接口标识
                    if (null != channelFuture) {
                        if (channelFuture.channel() != null && channelFuture.channel().isOpen()) {
                            channelFuture.channel().close();
                        }
                    }
                    group.shutdownGracefully();
                    reconnect();//重新连接
                }
            }
        }
    }

    //断开连接
    public void disconnect() {
        Log.e(TAG, "disconnect");
        isNeedReconnect = false;
        group.shutdownGracefully();
    }

    //重新连接
    public void reconnect() {
        Log.e(TAG, "reconnect");
        if (isNeedReconnect && reconnectNum > 0 && !isConnect) {
            reconnectNum--;
            SystemClock.sleep(reconnectIntervalTime);
            if (isNeedReconnect && reconnectNum > 0 && !isConnect) {
                Log.e(TAG, "重新连接");
                connectServer();
            }
        }
    }

    public boolean sendMsgToServer(byte[] data, ChannelFutureListener listener) {
        boolean flag = channel != null && isConnect;
        if (flag) {
            channel.writeAndFlush(Unpooled.copiedBuffer(data)).addListener(listener);
        }
        return flag;
    }


    //重连时间
    public void setReconnectNum(int reconnectNum) {
        this.reconnectNum = reconnectNum;
    }

    public void setReconnectIntervalTime(long reconnectIntervalTime) {
        this.reconnectIntervalTime = reconnectIntervalTime;
    }
    //现在连接的状态
    public boolean getConnectStatus() {
        return isConnect;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnectStatus(boolean status) {
        this.isConnect = status;
    }

    public void setListener(NettyListener listener) {
        this.listener = listener;
        handler.changeListener(listener);
        readerhandler.changeListener(listener);
    }

}

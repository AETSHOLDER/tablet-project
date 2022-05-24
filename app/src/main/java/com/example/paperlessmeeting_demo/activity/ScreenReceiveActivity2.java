package com.example.paperlessmeeting_demo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;


import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.ByteUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventScreenMessage;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.TyteUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by pengds on 2017/9/2.
 */

public class ScreenReceiveActivity2 extends BaseActivity implements SurfaceHolder.Callback {
    private static final String TAG = "ScreenReceiveActivity";
    int width = 1920;
    int height = 1200;
    int framerate = 20;
    private int mCount = 0;
    private MediaCodec mediaCodec;
    private SurfaceView surface_view;
    private SurfaceHolder mSurfaceHolder;
    // private DatagramSocket mDatagramSocket;
    private boolean getFrameData = true;
    private TextView screen_device_name;
    private MulticastSocket multicastSocket = null;//多点广播套接字
    static final String BROADCAST_IP = "233.0.0.101";
    //监听的端口号
    static final int BROADCAST_PORT = 6789;
    private InetAddress inetAddress = null;
    private MyBroadcastReceiver myBroadcastReceiver;//分享文件成功后，刷新文件列表
    int num = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_screen_receive;
    }

    @Override
    protected void initView() {
//        initUdp();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        // 保持屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
     //   setContentView(R.layout.activity_screen_receive);
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.FINISH_SHARE_SCREEN_BROADCAST);
        registerReceiver(myBroadcastReceiver, filter);
        surface_view = (SurfaceView) findViewById(R.id.screen_share_re_surface_view);
        mSurfaceHolder = surface_view.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.RGBA_8888);//优化花屏
        screen_device_name = (TextView) findViewById(R.id.tv_client_device_name);
        screen_device_name.setText("正在接受同屏");
        //  mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);//优化花屏
        //    android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen" // 花屏可以考虑设置这个样式的acticity，不过尝试了效果有限~
        mSurfaceHolder.addCallback(this);


    }

    private static final int FRAME_MAX_NUMBER = 40964;
    private byte[] frameBytes = new byte[FRAME_MAX_NUMBER];

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetStickyEvent(EventScreenMessage message) {
        if (message.getType().equals(MessageReceiveType.MessageClient)) {
//            try {
            Log.e("111","收到数据！！！");
            byte[] data  = ByteUtil.decodeValue(message.getBytes()) ;


            byte[] lengthByte = new byte[4];
            System.arraycopy(data, 0, lengthByte, 0, 4);
            int frameLenth = TyteUtil.byteArrayToInt(lengthByte);
            Log.e(TAG, frameLenth + " 接收的长度: ");
            if(frameLenth==0){
                return;
            }
            frameLenth = 40960 < frameLenth ? 40960 : frameLenth;
            byte[] frame = new byte[frameLenth];
            System.arraycopy(data, 4, frame, 0, frameLenth);
            Log.e(TAG, frameLenth + " 喂的数据: " + Arrays.toString(frame));
            Log.e(TAG, frameLenth + " 指定索引: " + frame[10]);
            Log.d("yemiankaishi", "fffffffffff");
            onFrame(frame);
        }
    }


    private void initUdp() {
        try {
            multicastSocket = new MulticastSocket(BROADCAST_PORT);
            inetAddress = InetAddress.getByName(BROADCAST_IP);
            Log.d("yemiankaishi", "111111");
            multicastSocket.joinGroup(inetAddress);
            final DatagramPacket dp = new DatagramPacket(frameBytes, frameBytes.length);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (getFrameData) {
                        try {
                            multicastSocket.receive(dp);
                            Log.d("yemiankaishi", "222222");
                            Log.d("Numbersendreceiving111", num + " ");
                            num++;
                        } catch (IOException e) {
                            Log.e(TAG, "IOException: " + e.toString());
                            e.printStackTrace();
                        }
                        byte[] data = dp.getData();
//                        Log.e(TAG, data.length+ " run: "+ Arrays.toString(data));
                        byte[] lengthByte = new byte[4];
                        System.arraycopy(data, 0, lengthByte, 0, 4);
                        int frameLenth = TyteUtil.byteArrayToInt(lengthByte);
                        Log.e(TAG, frameLenth + " 接收的长度: ");
                        if (frameLenth == 0) continue;
                        frameLenth = 40960 < frameLenth ? 40960 : frameLenth;
                        byte[] frame = new byte[frameLenth];
                        System.arraycopy(data, 4, frame, 0, frameLenth);
                        Log.e(TAG, frameLenth + " 喂的数据: " + Arrays.toString(frame));
                        Log.e(TAG, frameLenth + " 指定索引: " + frame[10]);
                        Log.d("yemiankaishi", "fffffffffff");
                        onFrame(frame);
                    }
                    Log.e(TAG, "....:结束");
                    multicastSocket.close();
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        }
    }

    public void onFrame(byte[] buf) {
        Log.d("yemiankaishi", "gggggggggggggggg");
        if (buf == null) {
            return;
        }
        if (!getFrameData) {
            return;
        }
        int length = buf.length;
        if (mediaCodec == null) {
            try {
                mediaCodec = MediaCodec.createDecoderByType("video/avc");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("yemiankaishi", "3333333");
        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();//拿到输入缓冲区,用于传送数据进行编码
        //返回一个填充了有效数据的input buffer的索引，如果没有可用的buffer则返回-1.当timeoutUs==0时，该方法立即返回；当timeoutUs<0时，无限期地等待一个可用的input buffer;当timeoutUs>0时，至多等待timeoutUs微妙
//        int inputBufferIndex = mediaCodec.dequeueInputBuffer(1);// =>0时,至多等待x微妙   如果发送源快速滑动比如放视频, 花屏明显.. ...
        int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);//    <-1时,无限期地等待一个可用的input buffer  会出现:一直等待导致加载异常, 甚至会吃掉网络通道, 没有任何异常出现...(调试中大部分是因为sps和pps没有写入到解码器, 保证图像信息的参数写入解码器很重要)
        Log.d("yemiankaishi", "bbbbbbbbbbbbbb");
        if (inputBufferIndex >= 0) {//当输入缓冲区有效时,就是>=0
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(buf, 0, length);//往输入缓冲区写入数据,关键点
//            int value = buf[4] & 0x0f;//nalu, 5是I帧, 7是sps 8是pps.
//            if (value == 7)//如果不能保证第一帧写入的是sps和pps， 可以用这种方式等待sps和pps发送到之后写入解码器
//                mediaCodec.queueInputBuffer(inputBufferIndex, 0, length, mCount * 30, MediaCodec.BUFFER_FLAG_CODEC_CONFIG);//更新sps和pps
//            else
            mediaCodec.queueInputBuffer(inputBufferIndex, 0, length, mCount * 30, 0);//将缓冲区入队
            mCount++;//用于queueInputBuffer presentationTimeUs 此缓冲区的显示时间戳（以微秒为单位），通常是这个缓冲区应该呈现的媒体时间
        }

        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        // mediaCodec.reset();
        Log.d("yemiankaishi", "mmmmmmmmmmmmm");
        int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);//拿到输出缓冲区的索引
        Log.d("yemiankaishi", "kkkkkkkkkkkkk");
        Log.d(TAG, "outputBufferIndex输出缓冲区的索引=" + outputBufferIndex);
        while (outputBufferIndex >= 0) {
            mediaCodec.releaseOutputBuffer(outputBufferIndex, true);//显示并释放资源
            outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);//再次获取数据，如果没有数据输出则outIndex=-1 循环结束
        }
        Log.d("yemiankaishi", "44444");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated");
        getFrameData = true;
        creatMediaCodec(holder);

        Log.d("yemiankaishi", "8888");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("yemiankaishi", "1010101");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        getFrameData = false;
        Log.e(TAG, "surface_surfaceDestroyed");
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }
        if (multicastSocket != null) {
            multicastSocket.close();
        }
    }

    void creatMediaCodec(SurfaceHolder holder) {
        try {
            //通过多媒体格式名创建一个可用的解码器
            mediaCodec = MediaCodec.createDecoderByType("video/avc");
        } catch (IOException e) {
            Log.e(TAG, "通过多媒体格式名创建一个可用的解码器" + e.toString());
            e.printStackTrace();
        }
        //初始化编码器
        final MediaFormat mediaformat = MediaFormat.createVideoFormat("video/avc", width, height);
        //这里可以尝试写死SPS和PPS，部分机型上的解码器可以正常工作。
//        byte[] header_sps = {0, 0, 0, 1, 103, 66, 0, 42, (byte) 149, (byte) 168, 30, 0, (byte) 137, (byte) 249, 102, (byte) 224, 32, 32, 32, 64};
//        byte[] header_pps = {0, 0, 0, 1, 104, (byte) 206, 60, (byte) 128, 0, 0, 0, 1, 6, (byte) 229, 1, (byte) 151, (byte) 128};
//        mediaformat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
//        mediaformat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
        mediaformat.setInteger(MediaFormat.KEY_FRAME_RATE, framerate);
        //指定解码后的帧格式
//            mediaformat.setInteger(MediaFormat.KEY_FRAME_RATE, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);//解码器将编码的帧解码为这种指定的格式,YUV420Flexible是几乎所有解码器都支持的

        //设置配置参数，参数介绍 ：
        // format   如果为解码器，此处表示输入数据的格式；如果为编码器，此处表示输出数据的格。式
        //surface   指定一个surface，可用作decode的输出渲染。
        //crypto    如果需要给媒体数据加密，此处指定一个crypto类.
        //   flags  如果正在配置的对象是用作编码器，此处加上CONFIGURE_FLAG_ENCODE 标签。
        mediaCodec.configure(mediaformat, holder.getSurface(), null, 0);
        mediaCodec.start();
        Log.e(TAG, "创建解码器");
        Log.d("yemiankaishi", "999999");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Activity_onDestroy~~~~~~");
        if (mSurfaceHolder != null) {
            mSurfaceHolder.getSurface().release();
        }
        mSurfaceHolder = null;
        //  surface_view = null;
        if (multicastSocket != null) {
            multicastSocket.close();
        }
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
        }
        super.onDestroy();

    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getFrameData = false;
            finish();
        }
    }
}
//如果h264中无pps及sps数据，则手动填入
//                    byte[] header_sps = {0, 0, 0, 1, 103, 66, 0, 42, (byte) 149, (byte) 168, 30, 0, (byte) 137, (byte) 249, 102, (byte) 224, 32, 32, 32, 64};
//                    byte[] header_pps = {0, 0, 0, 1, 104, (byte) 206, 60, (byte) 128, 0, 0, 0, 1, 6, (byte) 229, 1, (byte) 151, (byte) 128};
//                    mediaformat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
//                    mediaformat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
//          指定解码后的帧格式
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);//解码器将编码的帧解码为这种指定的格式,YUV420Flexible是几乎所有解码器都支持的
//        }
//        //设置码率，通常码率越高，视频越清晰，但是对应的视频也越大，这个值我默认设置成了2000000，也就是通常所说的2M，这已经不低了，如果你不想录制这么清晰的，你可以设置成500000，也就是500k
//        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
//        //设置帧率
//        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, framerate);
//        //COLOR_FormatSurface这里表明数据将是一个graphicbuffer元数据
//        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar);
//        //IFRAME_INTERVAL是指的帧间隔，这是个很有意思的值，它指的是，关键帧的间隔时间。通常情况下，你设置成多少问题都不大。
//        //比如你设置成10，那就是10秒一个关键帧。但是，如果你有需求要做视频的预览，那你最好设置成1
//        //因为如果你设置成10，那你会发现，10秒内的预览都是一个截图
//        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);//这个参数在很多手机上无效，特别是6.0以上的
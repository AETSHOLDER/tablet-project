package com.example.paperlessmeeting_demo.tool;


import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;


import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.ByteUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.util.ArrayUtil;
import com.example.paperlessmeeting_demo.util.BlackListHelper;
import com.example.paperlessmeeting_demo.util.TyteUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;


public class VideoEncoderUtil {
    private Encoder encoder;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private long timeStamp = 0;
    private int secondFrame = 1;//5s一帧关键帧, GOP
    private String ip;
    String MIME_TYPE = "video/avc";//编码格式,  h264
    private int DEFAULT_MAX_BPS = 1500;
    private int DEFAULT_FPS = 24;
    private int DEFAULT_IFI = 1;    //关键帧间隔时间 单位s

    public VideoEncoderUtil(MediaProjection mediaProjection, String ip) {
        this.mediaProjection = mediaProjection;
        this.ip = ip;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onSurfaceBind(Surface surface, int mWidth, int mHeight) {
        virtualDisplay = mediaProjection.createVirtualDisplay("-display",
                mWidth, mHeight, 1, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                surface, null, null);//将屏幕数据与surface进行关联
    }

    private void onSurfaceDestroyed(Surface surface) {
        virtualDisplay.release();
        surface.release();
    }

    public void start() {
        if (encoder == null) {
            encoder = new Encoder();
        }
        new Thread(encoder).start();
    }

    public void stop() {
        if (encoder != null) {
            encoder.release();
            encoder = null;
        }
    }

    private class Encoder implements Runnable {

        int VIDEO_FRAME_PER_SECOND = 20;//fps
        int VIDEO_I_FRAME_INTERVAL = 5;
        private int mWidth = 1280;//大屏上会因为分辨率显示马赛克
        private int mHeight = 800;
//        private int VIDEO_BITRATE = 6 * 1024 * 1024; //2M码率
        private int VIDEO_BITRATE = 3 * 1024*1024;
//      private int VIDEO_BITRATE = 500 * 1024; //500K码率,有兴趣的可以看看2M和500K的区别~
        /**
         * 子线程的hanlder
         */
        private Handler threadHandler;
        //     private DatagramSocket mDatagramSocket;
        private MediaCodec mCodec;
        private Surface mSurface;
        private Bundle params = new Bundle();
        private MulticastSocket multicastSocket = null;//多点广播套接字
        static final String BROADCAST_IP = "233.0.0.101";
        //监听的端口号
        static final int BROADCAST_PORT = 6789;
        private InetAddress inetAddress = null;
        public int DEFAULT_HEIGHT = 1200;
        public int DEFAULT_WIDTH = 1920;
        int num = 0;

        Encoder() {
            try {
                /**
                 * 1.实例化MulticastSocket对象，并指定端口
                 * 2.加入广播地址，MulticastSocket使用public void joinGroup(InetAddress mcastaddr)
                 * 3.开始接收广播
                 * 4.关闭广播
                 */
                multicastSocket = new MulticastSocket(BROADCAST_PORT);
                inetAddress = InetAddress.getByName(BROADCAST_IP);
                multicastSocket.setLoopbackMode(false);
                Log.e("UdpClientThread", "udp server start");
                multicastSocket.joinGroup(inetAddress);
              /*  byte buf[] = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                while (true) {
                    multicastSocket.send(dp);
                    multicastSocket.receive(dp);
                    Log.e("UdpClientThread", "receive a msg");
                    ip = new String(buf, 0, dp.getLength());
                    multicastSocket.leaveGroup(inetAddress);
                    multicastSocket.close();
                    MyApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.udpConnectSuccess(ip);
                        }
                    });
                }*/
            } catch (Exception e) {
              /*  MyApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.udpDisConnec(e.getMessage());
                    }
                });*/
            } finally {
                Log.e("UdpClientThread", "udp server close");
            }
//=====================================================================================
       /*     try {
                if (mDatagramSocket == null) {
                    mDatagramSocket = new DatagramSocket(null);
                    mDatagramSocket.setReuseAddress(true);
                    mDatagramSocket.bind(new InetSocketAddress(6666));
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }*/
            params.putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0);//做Bundle初始化  主要目的是请求编码器“即时”产生同步帧
            //params.putInt(MediaCodec.PARAMETER_KEY_VIDEO_BITRATE, bps * 1024);
            prepare();
        }

        @Override
        public void run() {
            Looper.prepare();
            threadHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    byte[] dataFrame = (byte[]) msg.obj;
                    int frameLength = dataFrame.length;
                    byte[] lengthByte = TyteUtil.intToByteArray(frameLength);
                    byte[] concat = ArrayUtil.concat(lengthByte, dataFrame);
                    Log.d("VideoEncoderutil",  "数据包长度=="+frameLength);
//
//                    try {
//                        DatagramPacket dp = new DatagramPacket(concat, concat.length, InetAddress.getByName(BROADCAST_IP), BROADCAST_PORT);
//                        multicastSocket.send(dp);
//                        Log.d("Numbersendreceiving222", num + " ");
//                        num++;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    JWebSocketClientService.sendByteArr(concat);
//                    sendData(frame.data);
//                    num++;
                }

            };
            Looper.loop();
        }

        void sendData(byte[] data) {
            Message message = new Message();
            message.obj = data;
            threadHandler.sendMessage(message);
        }

        private void release() {
            onSurfaceDestroyed(mSurface);
            if (mCodec != null) {
                mCodec.stop();
                mCodec.release();
                mCodec = null;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private boolean prepare() {
         /*   MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
            //COLOR_FormatSurface这里表明数据将是一个graphicbuffer元数据s
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            format.setInteger(MediaFormat.KEY_BIT_RATE, VIDEO_BITRATE);//编码器需要, 解码器可选
            format.setInteger(MediaFormat.KEY_FRAME_RATE, VIDEO_FRAME_PER_SECOND);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, VIDEO_I_FRAME_INTERVAL);//帧间隔  这个参数在很多手机上无效, 第二帧关键帧过了之后全是P帧。 GOP实现还有其它方法，全局搜关键字
*/
            int videoWidth = getVideoSize(DEFAULT_WIDTH);
            int videoHeight = getVideoSize(DEFAULT_HEIGHT);

            MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, videoWidth, videoHeight);
            //设置颜色格式
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            //设置比特率(设置码率，通常码率越高，视频越清晰)
            format.setInteger(MediaFormat.KEY_BIT_RATE, VIDEO_BITRATE);
//             format.setInteger(MediaFormat.KEY_BIT_RATE, DEFAULT_MAX_BPS * 1024);
            int fps = DEFAULT_FPS;
            //设置摄像头预览帧率
            if (BlackListHelper.deviceInFpsBlacklisted()) {
                fps = 15;
            }
            //设置帧率
            format.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
            //关键帧间隔时间，通常情况下，你设置成多少问题都不大。
            //比如你设置成10，那就是10秒一个关键帧。但是，如果你有需求要做视频的预览，那你最好设置成1
            //因为如果你设置成10，那你会发现，10秒内的预览都是一个截图
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, DEFAULT_IFI);
            // -----------------ADD BY XU.WANG 当画面静止时,重复最后一帧--------------------------------------------------------
            format.setLong(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / 45);
            //------------------MODIFY BY XU.WANG 为解决MIUI9.5花屏而增加...-------------------------------
            if (Build.MANUFACTURER.equalsIgnoreCase("XIAOMI")) {
                //设置编码模式
                format.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CQ);
            } else {
                format.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR);
            }
            //设置复用模式
            format.setInteger(MediaFormat.KEY_COMPLEXITY, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR);
//            MediaCodecInfo.EncoderCapabilities.
            try {
                mCodec = MediaCodec.createEncoderByType(MIME_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            mCodec.setCallback(new MediaCodec.Callback() {
                @Override
                public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
                }

                @Override
                public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
                    if (index > -1) {
                        ByteBuffer outputBuffer = codec.getOutputBuffer(index);
                        byte[] data = new byte[info.size];
                        assert outputBuffer != null;
                        outputBuffer.get(data);
                        sendData(data);
                        codec.releaseOutputBuffer(index, false);
                    }
                    if (System.currentTimeMillis() - timeStamp >= secondFrame) {//1秒后，设置请求关键帧的参数    GOP
                        timeStamp = System.currentTimeMillis();
                        codec.setParameters(params);
                    }
                }

                @Override
                public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
                    codec.reset();
                }

                @Override
                public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {

                }
            });

            mCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            //创建关联的输入surface
            mSurface = mCodec.createInputSurface();
            mCodec.start();
            onSurfaceBind(mSurface, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            return true;
        }

    }

    /*
    public MediaCodec getVideoMediaCodec() {
        int videoWidth = getVideoSize(DEFAULT_HEIGHT);
        int videoHeight = getVideoSize(DEFAULT_HEIGHT);

        MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, videoWidth, videoHeight);
        //设置颜色格式
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        //设置比特率(设置码率，通常码率越高，视频越清晰)
        format.setInteger(MediaFormat.KEY_BIT_RATE, DEFAULT_MAX_BPS * 1024);
        int fps = DEFAULT_FPS;
        //设置摄像头预览帧率
        if (BlackListHelper.deviceInFpsBlacklisted()) {
            fps = 15;
        }
        //设置帧率
        format.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
        //关键帧间隔时间，通常情况下，你设置成多少问题都不大。
        //比如你设置成10，那就是10秒一个关键帧。但是，如果你有需求要做视频的预览，那你最好设置成1
        //因为如果你设置成10，那你会发现，10秒内的预览都是一个截图
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, DEFAULT_IFI);
        // -----------------ADD BY XU.WANG 当画面静止时,重复最后一帧--------------------------------------------------------
        format.setLong(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / 45);
        //------------------MODIFY BY XU.WANG 为解决MIUI9.5花屏而增加...-------------------------------
        if (Build.MANUFACTURER.equalsIgnoreCase("XIAOMI")) {
            //设置编码模式
            format.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CQ);
        } else {
            format.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR);
        }
        //设置复用模式
        format.setInteger(MediaFormat.KEY_COMPLEXITY, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR);
        MediaCodec mediaCodec = null;

        try {
            mediaCodec = MediaCodec.createEncoderByType(DEFAULT_MIME);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
            if (mediaCodec != null) {
                mediaCodec.stop();
                mediaCodec.release();
                mediaCodec = null;
            }
        }
        return mediaCodec;
    }
     */

    private int getVideoSize(int size) {
        int multiple = (int) Math.ceil(size / 16.0);
        return multiple * 16;
    }

    //    转换一个视频（各项参数都很高），转换参数假设：帧率20fps，分辨率640*480,，去掉声音。
//    那么按照此参数，视频中一个像素点占据2个字节，
//    一帧就占用：640*480*2=614400个字节，
//            20帧就占用：614400*20=12288000个字节，
//    也就是每秒：12288000*8=98304000=98304k比特，也即：比特率为98304kbps
//    也就是说，在“分辨率与帧率”都已经确定的情况下，视频应有的、固有的比特率就会被唯一确定下来（至于采用H264或者AVC编码压缩，实质上还是跟刚才计算的“固有的”比特率成正比例缩小，假设压缩为原来的1%，其实还是是相当于固定码率983k）。

}

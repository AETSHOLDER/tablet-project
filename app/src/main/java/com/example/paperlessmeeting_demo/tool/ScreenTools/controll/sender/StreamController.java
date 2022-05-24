package com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender;

import android.media.MediaCodec;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.IVideoController;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.SopCastUtils;
import com.example.paperlessmeeting_demo.tool.VideoConfiguration;
import java.nio.ByteBuffer;

public class StreamController implements  OnVideoEncodeListener, Packer.OnPacketListener {
    private Packer mPacker;
    private Sender mSender;
    private IVideoController mVideoController;

    public StreamController(IVideoController videoProcessor) {
        mVideoController = videoProcessor;
    }

    public void setVideoConfiguration(VideoConfiguration videoConfiguration) {
        mVideoController.setVideoConfiguration(videoConfiguration);
    }


    public void setPacker(Packer packer) {
        mPacker = packer;
        //设置打包监听器
        mPacker.setPacketListener(this);
    }

    public void setSender(Sender sender) {
        mSender = sender;
    }

    public synchronized void start() {
        SopCastUtils.processNotUI(new SopCastUtils.INotUIProcessor() {
            @Override
            public void process() {
                if (mPacker == null) {
                    return;
                }
                if (mSender == null) {
                    return;
                }
                //开始打包
                mPacker.start();
                //开始发送
                mSender.start();
                mVideoController.setVideoEncoderListener(StreamController.this);
                mVideoController.start();
            }
        });
    }

    public synchronized void stop() {
        SopCastUtils.processNotUI(new SopCastUtils.INotUIProcessor() {
            @Override
            public void process() {
                if (mVideoController != null) {
                    mVideoController.setVideoEncoderListener(null);
                    mVideoController.stop();
                }
                if (mSender != null) {
                    mSender.stop();
                }
                if (mPacker != null) {
                    mPacker.stop();
                }
            }
        });
    }

    public synchronized void pause() {
        SopCastUtils.processNotUI(new SopCastUtils.INotUIProcessor() {
            @Override
            public void process() {
                mVideoController.pause();
            }
        });
    }

    public synchronized void resume() {
        SopCastUtils.processNotUI(new SopCastUtils.INotUIProcessor() {
            @Override
            public void process() {
                mVideoController.resume();
            }
        });
    }


    public boolean setVideoBps(int bps) {
        return mVideoController.setVideoBps(bps);
    }


    @Override
    public void onVideoEncode(ByteBuffer bb, MediaCodec.BufferInfo bi) {
        if (mPacker != null) {
            mPacker.onVideoData(bb, bi);
        }
    }

    @Override
    public void onPacket(byte[] data, int packetType) {
        if (mSender != null) {
            mSender.onData(data, packetType);
        }
    }

    @Override
    public void onSpsPps(byte[] mSpsPps) {
        if (mSender != null && mSender instanceof WSSender) {
            ((WSSender) mSender).setSpsPps(mSpsPps);
        }
    }
}

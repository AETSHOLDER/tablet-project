package com.example.paperlessmeeting_demo.tool.PCScreen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.WuHuActivity;
import com.example.paperlessmeeting_demo.tool.PCScreen.sender.TCPSender;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.ScreenImageApi;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.ScreenVideoController;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.StreamController;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.TcpPacker;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.VideoConfiguration;
import com.example.paperlessmeeting_demo.tool.constant;

public class ScreenImageService extends Service{
    private TCPSender tcpSender;
    private VideoConfiguration mVideoConfiguration;
    private StreamController mStreamController;

    private static final int foregroundId = 1234;
    NotificationManager notificationManager;
    Notification notification;
    String id = "screen_image_channel";
    String name = "无纸化会议";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ScreenImageBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
        }
        createNotification("....");
    }

    public class ScreenImageBinder extends Binder {
        public ScreenImageService getService() {
            return ScreenImageService.this;
        }
    }

    private void createNotification(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setChannelId(id)
                    .setContentTitle("无纸化会议 正在投屏中")
                    .setContentText(text)
                    .setContentIntent(getDefalutIntent(Notification.FLAG_FOREGROUND_SERVICE))
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("无纸化会议 正在投屏中")
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(getDefalutIntent(Notification.FLAG_FOREGROUND_SERVICE))
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(foregroundId, notification);
    }

    private PendingIntent getDefalutIntent(int flags) {
        Intent intent = new Intent(this, WuHuActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, flags);
        return pendingIntent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(foregroundId, notification);
        return START_NOT_STICKY;

    }

    /**
     * 开始录制屏幕
     */
    public void startController(MediaProjection mediaProjection) {
        Log.e("ScreenImageService","开始录制屏幕");
        ScreenVideoController screenVideoController = new ScreenVideoController(mediaProjection);
        mStreamController = new StreamController(screenVideoController);
        TcpPacker packer = new TcpPacker();
        tcpSender = new TCPSender();

        tcpSender.setMianCmd(ScreenImageApi.RECORD.MAIN_CMD);
        tcpSender.setSendBody(constant.CVI_PAPER_SCREEN_DATA);
        mVideoConfiguration = new VideoConfiguration.Builder().setSize(1920, 1080).build();
        mStreamController.setVideoConfiguration(mVideoConfiguration);
        mStreamController.setPacker(packer);
        mStreamController.setSender(tcpSender);
        mStreamController.start();
        tcpSender.openConnect();
    }

    /**
     * 通过ws 发送数据
     */
    public void startWSSender() {
        if(tcpSender!=null){
            tcpSender.sendWSScreenData();
        }
    }

    /**
     * 结束ws 发送线程
     */
    public void finishWSSender() {
        if(tcpSender!=null){
            tcpSender.finishWSSender();
        }
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopRecording();
        return true;
    }

    private void stopRecording() {
        if(MeetingAPP.getInstance().getNettyClient()!=null){
            MeetingAPP.getInstance().getNettyClient().disconnect();
        }

        Log.e("ttt", "stopRecording: zzz");
        if (mStreamController != null) {
            mStreamController.stop();
        }
    }

    @Override
    public void onDestroy() {
//        ToastUtils.showShort("投屏服务停止了");
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
    }
}

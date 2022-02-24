/*
package com.example.paperlessmeeting.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.paperlessmeeting.R;
import com.example.paperlessmeeting.base.BaseActivity;
import com.example.paperlessmeeting.socket.SocketManager;
import com.example.paperlessmeeting.tool.VideoChatHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoTrack;

import butterknife.BindView;
import butterknife.OnClick;
import io.socket.client.Ack;
import io.socket.client.Socket;

*/
/**
 * Created by fengli on 2018/3/2.
 *//*


public class VideoChatActivity extends BaseActivity {
    @Override
    protected void initView() {

    }

    public enum ChatVideoType {

        videoTypeCaller,
        videoTypeCallee
    }

    @BindView(R.id.video_chat_connect)
    ImageView connectBtn;

    @BindView(R.id.video_chat_disconnect)
    ImageView disconnectBtn;

    @OnClick(R.id.video_chat_connect)
    void connectClick(){

        isAnswer = true;

        connectBtn.setVisibility(View.GONE);
        disconnectBtn.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams disconnectParams = (RelativeLayout.LayoutParams) disconnectBtn.getLayoutParams();
        disconnectParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        disconnectParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        disconnectBtn.setLayoutParams(disconnectParams);
        disconnectParams.setMargins(0, 0, 0, 60);

        connectRoom(room);
    }

    @OnClick(R.id.video_chat_disconnect)
     void disconnectClick() {


        if (isAnswer == false && videoType == ChatVideoType.videoTypeCallee) {

            // 发送拒绝消息
            JSONObject object = new JSONObject();
            try {
                object.put("room", room);
                SocketManager.socket.emit("cancelVideoChat", object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            videoChatHelper.exitRoom();
        }
        finish();
    }


    private ChatVideoType videoType;
    private String fromUser;
    private String toUser;
    private String room;
    private VideoChatHelper videoChatHelper;
    private boolean isAnswer;
    private GLSurfaceView videoView;

    private VideoRenderer.Callbacks localRender;
    private VideoRenderer.Callbacks remoteRender;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_chat;
    }


    @Override
    protected void initData() {

        Bundle bundle = getIntent().getExtras();
        String fromUser = bundle.getString("fromUser");
        String toUser = bundle.getString("toUser");
        String room = bundle.getString("room");
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.room = room;
        int type = bundle.getInt("type");
        videoType = type == 0? ChatVideoType.videoTypeCaller: ChatVideoType.videoTypeCallee;
        videoView = new GLSurfaceView(mContext);
        videoView.setPreserveEGLContextOnPause(true);
        videoView.setKeepScreenOn(true);
        VideoRendererGui.setView(videoView, new Runnable() {
            @Override
            public void run() {

                initHelper();
            }
        });
        FrameLayout layout = findViewById(R.id.video_view_back);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(videoView, layoutParams);

        */
/*
        * 两者初始化顺序会影响最终渲染层的层次结构
        * *//*

        remoteRender = VideoRendererGui.create(0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
        localRender = VideoRendererGui.create(66, 0, 33, 33, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);

    }

    private void initHelper(){

        if (videoType == ChatVideoType.videoTypeCaller) {

            requestServerCreateRoom();

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    connectBtn.setVisibility(View.GONE);
                    disconnectBtn.setVisibility(View.VISIBLE);
                }
            });
        } else { // 被发起通话

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    isAnswer = false;

                    connectBtn.setVisibility(View.VISIBLE);
                    disconnectBtn.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams connectParams = (RelativeLayout.LayoutParams) connectBtn.getLayoutParams();
                    connectParams.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                    connectParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    connectBtn.setLayoutParams(connectParams);
                    connectParams.setMargins(60, 0, 0, 60);

                    RelativeLayout.LayoutParams disconnectParams = (RelativeLayout.LayoutParams) disconnectBtn.getLayoutParams();
                    disconnectParams.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                    disconnectParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    disconnectBtn.setLayoutParams(disconnectParams);
                    disconnectParams.setMargins(0, 0, 60, 60);
                }
            });
        }



        videoChatHelper = new VideoChatHelper(mContext, null, new VideoChatHelper.VideoChatCallBack() {

            @Override
            public void onSetLocalStream(final MediaStream localStream, String userId) {

                VideoRenderer renderer = new VideoRenderer(localRender);
                VideoTrack videoTrack = localStream.videoTracks.get(0);
                videoTrack.addRenderer(renderer);
            }

            @Override
            public void onCloseWithUserId(String userId) {

            }

            @Override
            public void onCloseRoom() {

                disconnectClick();
            }

            @Override
            public void onAddRemoteStream(MediaStream remoteStream, String userId) {


                VideoRenderer renderer = new VideoRenderer(remoteRender);
                VideoTrack videoTrack = remoteStream.videoTracks.get(0);
                videoTrack.addRenderer(renderer);


            }
        });

       // FLLog.i("类创建成功");
    }

    */
/*
    * 请求服务器创建房间
    * *//*

    private void requestServerCreateRoom(){

        Socket socket = SocketManager.socket;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("from_user", fromUser);
            jsonObject.put("to_user", toUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("videoChat", jsonObject, new Ack() {
            @Override
            public void call(Object... args) {

                String room = (String) args[0];
                connectRoom(room);
            }
        });
    }

    private void connectRoom(String room) {

        videoChatHelper.connectRoom(room);
    }



}
*/

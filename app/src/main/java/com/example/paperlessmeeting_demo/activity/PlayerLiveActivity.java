package com.example.paperlessmeeting_demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CameraInfoBean;
import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;
import com.example.paperlessmeeting_demo.bean.StreamConfigurationBean;
import com.example.paperlessmeeting_demo.bean.UUIdBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.network.NetWorkUuIdManager;
import com.example.paperlessmeeting_demo.socket.SocketManagerUuId;
import com.example.paperlessmeeting_demo.tool.MediaUtils;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.Ack;

public class PlayerLiveActivity extends BaseActivity {
    @BindView(R.id.fullscreen_back)
    ImageView back;
    @BindView(R.id.aa)
    TextView aa;
    private Unbinder unbinder;
    private PlayerView player;
    private Context mContext;
    private View rootView;
    private String title = "标题";
    private String strIp = "";
    private String baseUrl;
    private String positionCount;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String url = "rtmp://" + strIp + "/live/stream1";
            player.setPlaySource(url)
                    .startPlay();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.simple_player_view_player;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
                /*
* 统计用户行为日志
* */
        if (Hawk.contains("UserBehaviorBean")) {
            UserBehaviorBean userBehaviorBean = Hawk.get("UserBehaviorBean");
            UserBehaviorBean.DataBean dataBean = new UserBehaviorBean.DataBean();
            dataBean.setTittile(this.getClass().getName());
            dataBean.setTime(TimeUtils.getTime(System.currentTimeMillis()));
            List<UserBehaviorBean.DataBean> dataBeanList = userBehaviorBean.getData();
            dataBeanList.add(dataBean);
            Hawk.put("UserBehaviorBean", userBehaviorBean);
        }
        getMeeingInfo(UserUtil.meeting_record_id);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
    /*    FLUtil.requestfullScreen(this);*/
        rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
        setContentView(rootView);
        unbinder = ButterKnife.bind(this);
        /**常量*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        player = new PlayerView(this, rootView)
                .setTitle(title)
                .setScaleType(PlayStateParams.fitparent)
                .hideMenu(false)
                .hideBack(false)
                .hideSteam(true)
                .setForbidDoulbeUp(true)
                .hideCenterPlayer(true)
                .hideControlPanl(true)
                .showThumbnail(null);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPositonCall();
            }
        });
    }

    private void getUUidData(String meeting_room_id, String c_id) {

        //绑定生命周期
        NetWorkManager.getInstance().getNetWorkApiService().findUUid(meeting_room_id, c_id).compose(this.<BasicResponse<UUIdBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<UUIdBean>>() {
                    @Override
                    protected void onFail(BasicResponse<UUIdBean> response) {

                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {

                        if (response != null) {
                            UUIdBean uuIdBean = (UUIdBean) response.getData();
                            if (uuIdBean==null){
                                Toast.makeText(PlayerLiveActivity.this,"当前会议室未找到中控平台!",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            SocketManagerUuId.SocketCallBack callBack = new SocketManagerUuId.SocketCallBack() {

                                @Override
                                public void success() {
                                    //uuIdBean.getUuid()
                                    SocketManagerUuId.socket.emit("get zks address", uuIdBean.getUuid(), new Ack() {
                                        @Override
                                        public void call(Object... args) {

                                            JSONObject dataObject = (JSONObject) args[0];
                                            try {
                                                strIp = (String) dataObject.get("data");
                                                baseUrl = "http://" + strIp + ":6583/";
                                                UrlConstant.baseUuIdUrl = baseUrl;//中控摄像头动态设置IP
                                                getCameraInfo();//拿到动态服务IP请求摄像头信息
                                                getFlowMethods();//获取推流方式

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }

                                @Override
                                public void fail() {

                                }
                            };
                            SocketManagerUuId.connect(callBack);

                        }

                    }
                });

    }

    private void getMeeingInfo(String meeting__id) {
        //绑定生命周期
        NetWorkManager.getInstance().getNetWorkApiService().getMeetingInfo(meeting__id).compose(this.<BasicResponse<MeetingInfoBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<MeetingInfoBean>>() {
                    @Override
                    protected void onSuccess(BasicResponse<MeetingInfoBean> response) {
                        if (response != null) {
                            MeetingInfoBean meetingInfoBean = response.getData();
                            if (meetingInfoBean != null) {
                                if (meetingInfoBean.getMeeting_room_id().size() > 0) {
                                    MeetingInfoBean.MeetingRoomIdBean meetingRoomIdBean = meetingInfoBean.getMeeting_room_id().get(0);
                                    getUUidData(meetingRoomIdBean.get_id(), meetingInfoBean.getC_id());
                                }
                            }


                        }
                    }
                });
    }

    /*
    1.第一步先获取摄像头信息
  * 1.第一步先获取摄像头信息，第二步获取推流方式，第三步获取推流状态，第四步实现推流功能。
  * */
    private void getCameraInfo() {
        //绑定生命周期
        NetWorkUuIdManager.getInstance().getNetWorkApiService().getCameraInfo().compose(this.<BasicResponse<CameraInfoBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<CameraInfoBean>>() {
                    @Override
                    public void onNext(BasicResponse<CameraInfoBean> response) {
                        super.onNext(response);
                        if (response != null) {
                            if (1 == response.getCode()) {
                                CameraInfoBean cameraInfoBean = (CameraInfoBean) response.getData();
                                //  positionCount = cameraInfoBean.getPositionCount();
                            }

                        }

                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {

                    }

                });

    }

    /*
       2.第二步获取推流方式
     * 1.第一步先获取摄像头信息，第二步获取推流方式，第三步获取推流状态，第四步实现推流功能。
     * Push the flow method
     * */
    private void getFlowMethods() {
        //绑定生命周期
        NetWorkUuIdManager.getInstance().getNetWorkApiService().getStreamConfiguration().compose(this.<BasicResponse<StreamConfigurationBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<StreamConfigurationBean>>() {
                    @Override
                    public void onNext(BasicResponse<StreamConfigurationBean> response) {
                        super.onNext(response);
                        if (response != null) {
                            if (1 == response.getCode()) {
                                StreamConfigurationBean streamConfigurationBean = (StreamConfigurationBean) response.getData();
                                if ("rtmp".equals(streamConfigurationBean.getType())) {
                                    getFlowStatus();//获取推流状态
                                } else {
                                    Toast.makeText(PlayerLiveActivity.this, "暂不支持视频", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }

                    @Override
                    protected void onSuccess(BasicResponse<StreamConfigurationBean> response) {

                    }
                });
    }

    /*
         3.第三步获取推流状态
       * 1.第一步先获取摄像头信息，第二步获取推流方式，第三步获取推流状态，第四步实现推流功能。
       * Push the flow method
       * */
    private void getFlowStatus() {
        //绑定生命周期
        NetWorkUuIdManager.getInstance().getNetWorkApiService().getStreamStatus().compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    public void onNext(BasicResponse response) {
                        super.onNext(response);
                        if (response != null) {
                            if (1 == response.getCode()) {
                                String atr = (String) response.getData();
                                if ("pushing".equals(atr)) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            mHandler.sendEmptyMessage(0);
                                        }
                                    }.start();
                                }


                            }
                        }

                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {

                    }
                });
    }

    /*
            3.第三步获取推流状态
          * 1.第一步先获取摄像头信息，第二步获取推流方式，第三步获取推流状态，第四步实现推流功能。
          * Push the flow method
          * */
    private void setPositonCall() {
        if (Hawk.contains(constant.myNumber)) {
            positionCount = Hawk.get(constant.myNumber);
        }
        //绑定生命周期
        NetWorkUuIdManager.getInstance().getNetWorkApiService().setPositonCall(positionCount).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    public void onNext(BasicResponse response) {
                        super.onNext(response);
                        if (response != null) {
                            if (1 == response.getCode()) {
                                String atr = (String) response.getData();
                                if ("pushing".equals(atr)) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            mHandler.sendEmptyMessage(0);
                                        }
                                    }.start();
                                }


                            }
                        }

                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {

                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        MediaUtils.muteAudioFocus(mContext, false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
        if (unbinder != null) {
            unbinder.unbind();
        }

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
////        if (player != null) {
////            player.onConfigurationChanged(newConfig);
////        }
//    }

    @Override
    public void onBackPressed() {
//        if (player != null && player.onBackPressed()) {
//            return;
//        }
        super.onBackPressed();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}

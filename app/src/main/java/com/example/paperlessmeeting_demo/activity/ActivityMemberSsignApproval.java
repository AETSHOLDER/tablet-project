package com.example.paperlessmeeting_demo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.HandWritingBean;
import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;
import com.example.paperlessmeeting_demo.bean.MinuiteMeetBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.bean.WSBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.widgets.GestureSignatureView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 梅涛 on 2021/6/28.
 * 会员签批界面
 */

public class ActivityMemberSsignApproval extends BaseActivity {
    @BindView(R.id.initiate_endorsement)
    TextView initiateEndorsement;
    @BindView(R.id.temporary_summary)
    TextView temporarySummary;
    @BindView(R.id.topics)
    TextView topics;
    @BindView(R.id.meeting_time)
    TextView meetingTime;
    @BindView(R.id.meeting_place)
    TextView meetingPlace;
    @BindView(R.id.meeting_attendence)
    TextView meetingAttendence;
    @BindView(R.id.tittle1)
    TextView tittle1;
    @BindView(R.id.content1)
    TextView content1;
    @BindView(R.id.tittle2)
    TextView tittle2;
    @BindView(R.id.content2)
    TextView content2;
    @BindView(R.id.tittle3)
    TextView tittle3;
    @BindView(R.id.content3)
    TextView content3;
    @BindView(R.id.tittle4)
    TextView tittle4;
    @BindView(R.id.content4)
    TextView content4;
    @BindView(R.id.root_ll)
    LinearLayout rootLl;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.signature_pad)
    GestureSignatureView signaturePad;
    @BindView(R.id.signature_pad_ll)
    RelativeLayout signaturePadLl;
    @BindView(R.id.confir_sign)
    TextView confirSign;
    @BindView(R.id.renew_sign)
    TextView renewSign;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.content_meeting)
    LinearLayout contentMeeting;
    private MyReceiver myBrowserReceiver;
    private String fname;
    private List<MinuiteMeetBean.MeetingConten> list = new ArrayList<>();
    private List<AttendeBean> attendeBeanList = new ArrayList<>();
    private String secretaryId = "";//秘书ID
    private String otherParticipantsId;//其他参会人员
    private MeetingInfoBean meetingInfoBean;//会议实体类
    private MinuiteMeetBean minuiteMeetBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_members_sign_approval;
    }

    @Override
    protected void initView() {
        getAttenData();


    }

    @Override
    protected void initData() {
        signaturePadLl.setVisibility(View.GONE);
        contentMeeting.setVisibility(View.INVISIBLE);
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
        if (Hawk.contains("meetingInfoBean")) {
            meetingInfoBean = Hawk.get("meetingInfoBean");
            meetingTime.setText("时间：" + meetingInfoBean.getSche_start_time());

            List<MeetingInfoBean.MeetingRoomIdBean> meetingRoomIdBeanList = meetingInfoBean.getMeeting_room_id();
            if (meetingRoomIdBeanList != null) {

                topics.setText(meetingInfoBean.getName());
                meetingPlace.setText("地点：" + meetingRoomIdBeanList.get(0).getName());
            }

        }
        if (Hawk.contains("meeting_time")) {
            String time = Hawk.get("meeting_time");
            meetingTime.setText("时间：" + time);
        }

        //  EventBus.getDefault().register(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
                Locale.US);
        fname = Environment.getExternalStorageDirectory() + "/iiiiiii/" + sdf.format(new Date()) + ".png";

        signaturePadLl.getBackground().setAlpha(114);
/*
* 重签
* */
        renewSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });
           /*
        * 确认签字
        * */
        confirSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signaturePad.save(fname);
                    signaturePadLl.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        initiateEndorsement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        EventBus.getDefault().register(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(constant.MEMBER_SIGN_BROADCAST);
        myBrowserReceiver = new MyReceiver();
        registerReceiver(myBrowserReceiver, intentFilter);
        JWebSocketClientService.initSocketClient();
    }

    /**
     * 收到websocket 信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        Log.d("qianpi", "qianpi: " + message.toString() + "==" + message.getType());
        try {
            WSBean wsebean = new Gson().fromJson(message.getMessage(), WSBean.class);
            //  收到vote的websocket的信息

            if (wsebean != null) {
                if (constant.MINUTE_MEETING_CONTENT.equals(wsebean.getPackType())) {
                    HandWritingBean handWritingBean = new HandWritingBean();
                    TempWSBean<HandWritingBean> wb = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<HandWritingBean>>() {
                    }.getType());
                    if (wb != null) {
                        signaturePadLl.setVisibility(View.VISIBLE);
                        contentMeeting.setVisibility(View.VISIBLE);
                        list.clear();
                        handWritingBean = wb.getBody();
                        String str = handWritingBean.getBaseData();
                        minuiteMeetBean = JSONArray.parseObject(str, MinuiteMeetBean.class);
                        if (minuiteMeetBean == null) {
                            return;
                        }
                        list = minuiteMeetBean.getMeetingContens();
                        Log.d("qianpi", "路过会议内容！！！！" + list.size() + "");
                        tittle1.setText(list.get(0).getTittle());
                        content1.setText(list.get(0).getContent());

                        tittle2.setText(list.get(1).getTittle());
                        content2.setText(list.get(1).getContent());

                        tittle3.setText(list.get(2).getTittle());
                        content3.setText(list.get(2).getContent());

                        tittle4.setText(list.get(3).getTittle());
                        content4.setText(list.get(3).getContent());

                    }
                }
            }
        } catch (Exception e) {
            Log.d("qianpi", e.getMessage().toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (myBrowserReceiver != null) {
            unregisterReceiver(myBrowserReceiver);
        }
        EventBus.getDefault().unregister(this);
    }

    private void getAttenData() {
        String _id = UserUtil.meeting_record_id;
        if (attendeBeanList != null) {
            attendeBeanList.clear();
        }
        NetWorkManager.getInstance().getNetWorkApiService().getMeetingUserList(_id).compose(this.<BasicResponse<List<AttendeBean>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<AttendeBean>>>() {
                    @Override
                    protected void onSuccess(BasicResponse<List<AttendeBean>> response) {
                        attendeBeanList = (List<AttendeBean>) response.getData();
                        setAgendaUi(attendeBeanList);
                    }
                });

    }

    private void setAgendaUi(List<AttendeBean> attendeBeans) {
        for (int i = 0; i < attendeBeans.size(); i++) {
            if ("1".equals(attendeBeans.get(i).getRole())) {
                secretaryId = attendeBeans.get(i).getUser_id();
            }
        }
        StringBuffer sb2 = new StringBuffer();
        if (attendeBeans.size() > 0) {
            for (int i = 0; i < attendeBeans.size(); i++) {
                sb2.append(attendeBeans.get(i).getName() + "、");

            }
            meetingAttendence.setText("参会人员:" + sb2.subSequence(0, sb2.length() - 1));
        }
    }

    /**
     * 照片转byte二进制
     *
     * @param imagepath 需要转byte的照片路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }

    private String byte2Base64(byte[] imageByte) {
        if (null == imageByte) return null;
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            WSBean bean = new WSBean();
            HandWritingBean handWritingBean = new HandWritingBean();
            handWritingBean.setSendUserId(secretaryId);//秘书
            handWritingBean.setMeetingRecordId(UserUtil.meeting_record_id);
            try {
                handWritingBean.setBaseData(byte2Base64(readStream(fname)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            bean.setReqType(0);
            bean.setPackType("sign");
            bean.setDevice("app");
            bean.setUser_id(UserUtil.user_id);//普通人员
            bean.setBody(handWritingBean);

            String strJson = new Gson().toJson(bean);
            JWebSocketClientService.sendMsg(strJson);
            Log.d("ghff", "图片" + bean.getPackType());

        }
    }
}

package com.example.paperlessmeeting_demo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.Initialization.InitiaActivity;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanRequest;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanResponse;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.InitiaBean.InitiaMeeting;
import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;
import com.example.paperlessmeeting_demo.bean.PaperlessBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.UploadBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.WuHuNetWorkBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.fragment.ExtraordMeetingFragment;
import com.example.paperlessmeeting_demo.fragment.ExtraordMeetingFragment2;
import com.example.paperlessmeeting_demo.fragment.SweepCodeFragment;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.socket.InitSocketManager;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.DeleteFileUtil;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.PermissionManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.thdReceive;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.tool.VideoEncoderUtil;
import com.example.paperlessmeeting_demo.widgets.NoScrollViewPager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jyn.vcview.VerificationCodeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import q.rorbin.verticaltablayout.util.DisplayUtil;

/**
 * Created by ?????? on 2020/9/18.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.entry_meet)
    TextView entryMeet;
    @BindView(R.id.tt)
    TextView tt;
    @BindView(R.id.visitor)
    ImageView visitor;
    @BindView(R.id.theme_bg_ima)
    ImageView theme_bg_ima;
    @BindView(R.id.yaer)
    TextView yaerTv;
    @BindView(R.id.month)
    TextView monthTv;
    @BindView(R.id.date)
    TextView dateTv;
    //    private String[] titles = {"????????????", "????????????", "?????????"};
    private LoginToFragmentListener loginToFragmentListener;
    private Dialog confirmDialog;
    @BindView(R.id.initiate_meeting_tv)
    TextView init_meeting;
    @BindView(R.id.join_meeting_tv)
    TextView join_meeting;
    LinearLayout loginLeft;
    @BindView(R.id.left_rl)
    RelativeLayout leftRl;
    @BindView(R.id.name_attendee)
    TextView nameAttendee;
    @BindView(R.id.issues)
    TextView issues;
    @BindView(R.id.name1_value)
    TextView name1Value;
    @BindView(R.id.name2_value)
    TextView name2Value;
    @BindView(R.id.name3_value)
    TextView name3Value;
    @BindView(R.id.name4_value)
    TextView name4Value;
    @BindView(R.id.name5_value)
    TextView name5Value;
    RelativeLayout agendaRl;
    @BindView(R.id.name5_rl)
    RelativeLayout name5Rl;


    //    private String[] titles = {"????????????", "????????????", "?????????"};
    private String[] titles = {"????????????", "????????????"};
    private List<Fragment> fragments = new ArrayList<>();
    AlertDialog.Builder inputDialog;
    public String targetip = "192.168.0.106";
    private AlertDialog serviceDialog;
    //    private boolean isFirstInit = true;
    private static final int ACTIVITY_RESULT_CODE = 110;
    private MediaProjectionManager projectionManager;
    private VideoEncoderUtil videoEncoder;
    private Dialog initMeetingDialog;
    private Dialog joinMeetingDialog;
    //    private boolean isFirstInit = true;
    private VerificationCodeView create_invite_codeview;
    private VerificationCodeView join_invite_codeview;
    private TextView entry_meeting;
    private Spinner spinner;
    private List<String> ipcodeInfo = new ArrayList<>();
    private TextView tipsTxt;
    private List<AttendeBean> attendeBeanList = new ArrayList<>();
    private List<String> strChairman = new ArrayList<>();//????????????
    private List<String> strSecretary = new ArrayList<>();//????????????
    private List<String> strServant = new ArrayList<>();//????????????
    private List<String> strOther = new ArrayList<>();//??????????????????
    private List<String> strForeign = new ArrayList<>();//??????????????????
    private String upLoadFileType;
    private String endStrAll;
    private UploadBean uploadBean;
    private CreateFileBeanRequest createFileBeanRequest;
    private String strContent;
    private File appDir;//?????????????????????????????????
    private String fileName;//????????????????????????????????????
    private String fileShare = Environment.getExternalStorageDirectory() + constant.SHARE_FILE;//??????????????????????????????????????????
    private String COPY_PATH = Environment.getExternalStorageDirectory() + constant.COPY_PATH;
    private String VOTE_FILE = Environment.getExternalStorageDirectory() + constant.VOTE_FILE;
    private String netFilePath = Environment.getExternalStorageDirectory() + constant.WUHU_NET_FILE;//????????????????????????????????????
    private String FileCathePath = Environment.getExternalStorageDirectory() + File.separator + "cutlittlefile";   //?????????????????????????????????
    private String selfIp = "";
    private String isReuse = "";
    private Handler handler = new Handler() {
        @Override

        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendFileInfo(appDir + fileName, "3", "0");
                    }
                }, 3000);//3????????????Runnable??????run??????

            }
        }
    };
    private Handler requestMeetingHandler = new Handler();
    private Runnable requestRunable = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wuhu_login;
    }

    @Override
    protected void initView() {
        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(LoginActivity.this, UPloadActivity.class);
                startActivity(intent);
*/
            }
        });
        if (Hawk.contains(constant.user_name)) {
            UserUtil.user_name = Hawk.get(constant.user_name);
        }
        if (Hawk.contains("company_name")) {
            Hawk.delete("company_name");
        }
        if (Hawk.contains("tittle2")) {
            Hawk.delete("tittle2");
        }
  /*      //??????????????????????????????
        if (Hawk.contains("WuHuFragmentData")){
            Hawk.delete("WuHuFragmentData");
        }*/
      /*  //??????????????????
        WuHuEditBean wuHuEditBean=new WuHuEditBean();
        Hawk.put("WuHuFragmentData",wuHuEditBean);*/
        //??????????????????????????????????????????copy????????????????????????Hawk??????
      /*  if (Hawk.contains("wuhulocal")){

            Hawk.delete("wuhulocal");
        }*/
        agendaRl = findViewById(R.id.agenda_rl);
        loginLeft = findViewById(R.id.login_left);
        //???????????? ????????????????????????????????????
        File share = new File(fileShare);
        if (!share.exists()) {
            share.mkdir();
        }

        File copy = new File(COPY_PATH);
        if (!copy.exists()) {
            copy.mkdir();
        }
        File net = new File(netFilePath);
        if (!net.exists()) {
            net.mkdir();
        }

        //???????????????????????????????????????
        //   DeleteFileUtil.deleteDirectory(fileShare);
        /*
         * ??????????????????????????????????????????
         * */
        if (Hawk.contains("cacheData")) {
            Hawk.delete("cacheData");
        }
        if (Hawk.contains("handWritingBean")) {
            Hawk.delete("handWritingBean");
        }
        initViewPagerFragment();

        agendaRl.setVisibility(View.GONE);
        loginLeft.setVisibility(View.VISIBLE);


        name.setVisibility(View.INVISIBLE);
        Log.d("22", "mac===" + FLUtil.getMacAddress());

//        int ss1 = DisplayUtil.px2dp(LoginActivity.this, 2560);
//        int ss2 = DisplayUtil.px2dp(LoginActivity.this, 1600);
//        Log.e("111","ss1==="+ss1+"ss2==="+ss2);
//
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        int screenWidth = dm.widthPixels;
//        int screenHeight = dm.heightPixels;
//        Log.e("111","???=="+screenWidth+" ???=="+screenHeight);

//  ??????????????????????????????
//      new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //  ???????????????
//                EventMessage msg = new EventMessage(MessageReceiveType.MessageClient, constant.start_meeting);
//                EventBus.getDefault().post(msg);
//            }
//        }, 5000);
    }


    /**
     * ????????????????????????????????????????????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetStickyEvent(EventMessage message) {
        if (!PermissionManager.checkPermission(this, constant.PERMS_WRITE1)) {
            return;
        }
        if (message.getMessage().equals(constant.start_meeting)) {
            String aa = String.format("???????????????????????????id=%s,????????????=%s", UserUtil.meeting_record_id, UserUtil.user_name);

//            name.setVisibility(View.VISIBLE);
//            loginToFragmentListener.onUserInfoReceive();

            agendaRl.setVisibility(View.VISIBLE);
            loginLeft.setVisibility(View.GONE);
//            getAttenData();
            if (Hawk.contains(constant.InitiaMeeting)) {
                InitiaMeeting initiaMeeting = Hawk.get(constant.InitiaMeeting);
                if (initiaMeeting != null) {
//                    Drawable drawable = MeetingAPP.getContext().getResources().getDrawable(R.mipmap.bg_login);
//                    DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(drawable).showImageForEmptyUri(drawable).showImageOnFail(drawable).resetViewBeforeLoading(false).delayBeforeLoading(1000).
//                            cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).handler(new Handler()).build();
//                    ImageLoader.getInstance().displayImage(meetingInfoBean.getBackground_img(), theme_bg_ima, options);

                    Log.d("getStart_time", "" + initiaMeeting.getStart_time());
                    Log.d("getEnd_time", "" + initiaMeeting.getEnd_time());
//                    Hawk.put("background_img", meetingInfoBean.getBackground_img());//????????????????????????
//                    Hawk.put("getStart_time", initiaMeeting.getStart_time());//????????????
//                    Hawk.put("meetingInfoBean", meetingInfoBean);//???????????????
                    long startTime = TimeUtils.getStringToDate(initiaMeeting.getStart_time(), TimeUtils.DATA_FORMAT);//??????????????????
                    String yaer = TimeUtils.getTime(startTime, TimeUtils.DATA_FORMAT_NO_HOURS_DATA10);//??????-???
                    String month = TimeUtils.getTime(startTime, TimeUtils.DATA_FORMAT_NO_HOURS_DATA11);//??????-???
                    String starHous = TimeUtils.getTime(startTime, TimeUtils.DATA_FORMAT_NO_HOURS_DATA12);//??????-??????

                    long endTime = TimeUtils.getStringToDate(initiaMeeting.getEnd_time(), TimeUtils.DATA_FORMAT);//??????????????????
                    String endHous = TimeUtils.getTime(endTime, TimeUtils.DATA_FORMAT_NO_HOURS_DATA12);//??????-??????

                    yaerTv.setText(yaer);
                    monthTv.setText(month);
                    dateTv.setText(starHous + "-" + endHous);
                    Hawk.put("meeting_time", yaer + "/" + month + " " + starHous + "-" + endHous);
                    issues.setText(initiaMeeting.getMeeting_name());
                    if (Hawk.contains(constant.user_name)) {
                        nameAttendee.setText(Hawk.get(constant.user_name));
                    }
//                    name.setText(UserUtil.user_name);
                }
            }
        }
        if (message.getMessage().equals(constant.get_server_ip)) {
            //????????????APP???????????????
            verificationEquipment();
            // getMacIsRegister();
           //resumeToGetMeetingInfo();
        }
        if (message.getMessage().equals(constant.continusClick)) {
//            String tips = "????????????????????????8????????????????????????????????????";
//            Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
//            visitor.setVisibility(View.VISIBLE);
        }
    /*    //?????????????????????????????????????????????????????????????????????????????????ip??????
        if (message.getMessage().equals(constant.NETWORKSIDE)) {
            try {
                TempWSBean<WuHuNetWorkBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuNetWorkBean>>() {
                }.getType());
                //  ??????vote???websocket?????????
                if (wsebean != null) {
                    Log.d("gfdgfdfhh", "????????????????????????");
                    WuHuNetWorkBean wuHuNetWorkBean = wsebean.getBody();
                    Log.d("gfdgfdfhh", "????????????????????????    " + wuHuNetWorkBean.getIp() + "   " + wuHuNetWorkBean.getMac());
                    Hawk.put("wuHuServerIp", wuHuNetWorkBean.getIp());
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }


        }*/
        if (message.getType().equals(MessageReceiveType.MessageCreatTempMeeting)) {
            Activity topActivity = ActivityUtils.getTopActivity();
            if (topActivity != null && topActivity.getLocalClassName().contains("LoginActivity")) {
                String[] ip_code = message.getMessage().split(",");
                String ip = ip_code[0];
                String[] allCode = ip_code[1].split("/");//?????????????????????????????????????????????????????????
                if (allCode.length == 3) {
                    isReuse = allCode[2];
                    Hawk.put("isreuse", isReuse);//??????????????????????????????????????????
                    String strId = allCode[1];
                    Hawk.put("WuHuMeetingID", strId);//????????????id  ?????????????????????

                } else {
                    isReuse = allCode[1];
                    Hawk.put("isreuse", isReuse);//??????????????????????????????????????????
                }

                String code = allCode[0];
                String title = "";
                //?????????????????????????????????????????????????????????
                if (code.contains("formalmeeting-")) {
                    UserUtil.isNetDATA = true;
                    //????????????"formalmeeting-"+????????????+???/???+??????ID???
                    String[] allStr = code.split("-");
                    if (allStr.length > 1) {
                        String strName = allStr[1];

                        if (strName.length() > 23) {
                            title = "????????????" + "\n" + strName.substring(0, 22) + "...??????";
                        } else {
                            title = "????????????" + "\n" + strName;
                        }
                    }

                    //??????????????????????????????????????????????????????????????????
                } else {
                    UserUtil.isNetDATA = false;
                    title = "?????????????????????(" + code + ")???????????????!";
                }

                if (UserUtil.isNetDATA) {
                    CVIPaperDialogUtils.showCountDownCustomDialog(LoginActivity.this, title, "????????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                Log.e("111111", "???????????????");

                                UserUtil.isTempMeeting = true;
                                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);

                                boolean flag = false;
                                for (String msg : thdReceive.receiveIPArr) {
                                    if (msg.contains(ip)) {
                                        flag = true;
                                    }
                                }
                                if (!flag) {
                                    ToastUtils.showToast(LoginActivity.this, "??????????????????!!!");
                                    return;
                                }
                                Intent intent1 = new Intent(LoginActivity.this, WuHuActivity3.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("ip", ip);
                                bundle.putString("isreuse", isReuse);
                                intent1.putExtras(bundle);
                                startActivity(intent1);
                            }
                        }
                    });
                }
            }
        }
    }

    //????????????APP???????????????
    private void verificationEquipment() {

        Map<String, Object> map = new HashMap<>();
   /*     //????????????--?????????
      UUID id=UUID.randomUUID();
       String[] idd=id.toString().split("-");
      String  mac= idd[0]+idd[1]+idd[2];*/

        map.put("mac", FLUtil.getMacAddress());
        NetWorkManager.getInstance().getNetWorkApiService().verificationEquipment(map).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onFail(BasicResponse response) {
                        // Toast.makeText(LoginActivity.this, "??????APP????????????????????????", Toast.LENGTH_SHORT).show();
                        dialogView();

                        // System.exit(0);
                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {

                    }
                });

    }

    private void dialogView() {
        confirmDialog = new Dialog(LoginActivity.this, R.style.dialogTransparent);
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_confirm3, null);
        confirmDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //????????????????????????????????????????????????????????????(false???????????????????????????????????????????????????)
        // confirmDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        //??????????????????Dialog
        confirmDialog.setContentView(view);
        TextView confirm = view.findViewById(R.id.confir_btn);
        TextView contentTxt = view.findViewById(R.id.content_tv);
       /* contentTxt.setVisibility(View.INVISIBLE);
        confirm.setText("??????APP????????????????????????");*/

     /*   contentTxt.setBackgroundColor(Color.parseColor("#ffffff"));
        contentTxt.setTextColor(Color.parseColor("#3377FF"));*/
        //  contentTxt.setText("??????APP????????????????????????");
        confirmDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        confirmDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        confirmDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //???????????????????????????
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //??????
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //???????????????
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                if (confirmDialog != null && confirmDialog.getWindow() != null) {
                    confirmDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                }

            }
        });
        confirmDialog.show();
        Window window = confirmDialog.getWindow();
        WindowManager windowManager = window.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = confirmDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.45); //????????????
        lp.height = (int) (display.getHeight() * 0.23); //????????????
        confirmDialog.getWindow().setAttributes(lp);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (confirmDialog != null) {
                    confirmDialog.dismiss();
                }

                finish();
            }
        }, 2000);


    }

    /**
     * ????????????????????????
     * &&????????????????????????????????????
     */
    private void resumeToGetMeetingInfo() {
        if (Hawk.contains(constant._id) && !StringUtils.isEmpty(Hawk.get(constant._id))) {
            UserUtil.meeting_record_id = Hawk.get(constant._id);
            if (Hawk.contains(constant.user_name) && !StringUtils.isEmpty(Hawk.get(constant.user_name))) {
                UserUtil.user_name = Hawk.get(constant.user_name);
            }
            if (Hawk.contains(constant.user_id) && !StringUtils.isEmpty(Hawk.get(constant.user_id))) {
                UserUtil.user_id = Hawk.get(constant.user_id);
            }
            getMeeingInfo(UserUtil.meeting_record_id);
        } else {
            loginToFragmentListener.onHideUserInfo();
        }
    }

    private void getAttenData() {
        agendaRl.setVisibility(View.VISIBLE);
        loginLeft.setVisibility(View.GONE);
        String _id = UserUtil.meeting_record_id;
        nameAttendee.setText(UserUtil.user_name);
        if (attendeBeanList != null) {
            attendeBeanList.clear();
        }
        NetWorkManager.getInstance().getNetWorkApiService().getMeetingUserList(_id).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<AttendeBean>>>() {
                    @Override
                    protected void onSuccess(BasicResponse<List<AttendeBean>> response) {
                        attendeBeanList = response.getData();
                        Hawk.put("attendeBeanList", attendeBeanList);
                        setAgendaUi(attendeBeanList);
                    }
                });

    }

    private void setAgendaUi(List<AttendeBean> attendeBeans) {

        if (strChairman != null) {
            strChairman.clear();
        }
        if (strSecretary != null) {
            strSecretary.clear();
        }
        if (strServant != null) {
            strServant.clear();
        }
        if (strOther != null) {
            strOther.clear();
        }
        if (strForeign != null) {
            strForeign.clear();
        }
        for (int i = 0; i < attendeBeans.size(); i++) {
            switch (attendeBeans.get(i).getRole()) {
                case "0":
                    //????????????0
                    strChairman.add(attendeBeans.get(i).getName());
                    break;
                case "1":
                    //????????????1
                    strSecretary.add(attendeBeans.get(i).getName());
                    break;
                case "2":
                    //??????????????????2
                    strOther.add(attendeBeans.get(i).getName());
                    break;
                case "3":
                    //????????????3
                    strServant.add(attendeBeans.get(i).getName());
                    break;
                case "4":
                    //??????????????????4
                    strForeign.add(attendeBeans.get(i).getName());
                    break;
            }

        }
        //???????????????
        StringBuffer sb1 = new StringBuffer();
        if (strChairman.size() > 0) {
            for (int i = 0; i < strChairman.size(); i++) {
                sb1.append(strChairman.get(i) + "???");
            }
            name1Value.setText(sb1.subSequence(0, sb1.length() - 1));
        } else {
            name1Value.setText("?????????");

        }
//???????????????
        StringBuffer sb2 = new StringBuffer();
        if (strSecretary.size() > 0) {
            for (int i = 0; i < strSecretary.size(); i++) {
                sb2.append(strSecretary.get(i) + "???");

            }
            name2Value.setText(sb2.subSequence(0, sb2.length() - 1));
        } else {
            name2Value.setText("?????????");
        }
        //????????????
        StringBuffer sb3 = new StringBuffer();

        if (strServant.size() > 0) {
            for (int i = 0; i < strServant.size(); i++) {

                sb3.append(strServant.get(i) + "???");
            }
            name3Value.setText(sb3.subSequence(0, sb3.length() - 1));
        } else {
            name3Value.setText("?????????");
        }
        //??????????????????
        StringBuffer sb4 = new StringBuffer();

        if (strOther.size() > 0) {
            for (int i = 0; i < strOther.size(); i++) {
                sb4.append(strOther.get(i) + "???");
            }
            name4Value.setText(sb4.subSequence(0, sb4.length() - 1));
        } else {
            name4Value.setText("?????????");

        }
//????????????
        StringBuffer sb5 = new StringBuffer();
        if (strForeign.size() > 0) {
            name5Rl.setVisibility(View.VISIBLE);
            for (int i = 0; i < strForeign.size(); i++) {
                sb5.append(strForeign.get(i) + "???");
            }
            name5Value.setText(sb5.subSequence(0, sb5.length() - 1));
        } else {
            name5Rl.setVisibility(View.GONE);
        }
        /*
         * ??????????????????????????????????????????????????????????????????????????????ActivityMinuteMeeting???????????????????????????????????????????????????????????????????????????
         * */

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    onSaveBitmap(getBitmap(agendaRl));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void getMacIsRegister() {

        NetWorkManager.getInstance().getNetWorkApiService().findPaperLessInfo(FLUtil.getMacAddress()).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<String>>() {
                    @Override
                    protected void onSuccess(BasicResponse<String> response) {
                        if (response != null) {
                            if (StringUtils.isEmpty(response.getData())) {
                                // ??????????????????
                                Intent intent = new Intent(LoginActivity.this, InitiaActivity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    protected void onFail(BasicResponse<String> response) {
                        super.onFail(response);

                    }
                });
    }

    @Override
    protected void initData() {
        UDPBroadcastManager.getInstance().removeUDPBroastcast();
        //  ??????UDP??????
        EventBus.getDefault().register(this);

        entryMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        /**
         *  ?????????????????????????????????
         */
        visitor.setVisibility(View.GONE);
        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
            }
        });

    }


    private void initViewPagerFragment() {
        if (titles.length < 4) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }//tab?????????????????????????????????MODE_SCROLLABLE  ????????????MODE_FIXED
        FragmentManager fm = getSupportFragmentManager();
        FragmentAdapter pagerAdapter = new FragmentAdapter(fm);

        SweepCodeFragment sweepCodeFragment = SweepCodeFragment.newInstance("????????????");
        loginToFragmentListener = sweepCodeFragment;
        pagerAdapter.addFragment(ExtraordMeetingFragment2.newInstance("????????????"), "????????????");
        pagerAdapter.addFragment(sweepCodeFragment, "????????????");
//        pagerAdapter.addFragment(WhiteboardFragment.newInstance("????????????"), "????????????");

//        pagerAdapter.addFragment(SweepCodeFragment.newInstance("????????????"), "????????????");
        //    pagerAdapter.addFragment(WhiteboardFragment.newInstance("????????????"), "????????????");

//        pagerAdapter.addFragment(InvitedFragment.newInstance("?????????"), "?????????");

        viewPager.setAdapter(pagerAdapter);
        viewPager.setNoScroll(true);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getBackground().setAlpha(175);

        /**
         * ??????tablayout?????????????????????????????????
         */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //??????????????????
            }
        });
        tabLayout.getTabAt(0).select(); //?????????????????????tab
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
//        PermissionManager.RequestOverlayPermission(this);
        Log.d("fgdgg222", selfIp);
        // File file = new File("???????????????????????????");
        //?????????????????????
        File f = new File(COPY_PATH);
        if (!f.exists()) {
            f.mkdir();
        }
        File netFile = new File(netFilePath);
        if (!netFile.exists()) {
            netFile.mkdir();
        }
    }
   /* private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
           file.;
        }

    }*/

    //??????????????????????????????????????????
    private void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory() || dir.listFiles() == null)
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // ??????????????????
            else if (file.isDirectory())
                deleteDirWihtFile(file); // ??????????????????????????????
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //??????????????????????????????
        if (Hawk.contains("WuHuFragmentData")) {
            Hawk.put("isFirst", false);
        } else {
            Hawk.put("isFirst", true);
        }
        //?????????????????????????????? ??????Hawk  wuhulocal  WuHuLocalFileBean
       /* if (Hawk.contains("wuhulocal")){
            Hawk.delete("wuhulocal");
        }
        if (Hawk.contains("WuHuLocalFileBean")){
            Hawk.delete("WuHuLocalFileBean");
        }*/

        UserUtil.isTempMeeting = false;
        // ??????init???????????????????????????????????????????????????
        if (InitSocketManager.socket != null && !InitSocketManager.socket.connected()) {
            FLUtil.initSocketIO();
        }
        selfIp = FLUtil.getNetworkType();
        Hawk.put("SelfIpAddress", selfIp);//??????Ip
        Hawk.put("isSameScreen", "0");//??????????????????????????????:1-??????  0-?????????
        UDPBroadcastManager.getInstance().receiveUDP();
        agendaRl.setVisibility(View.GONE);
        loginLeft.setVisibility(View.VISIBLE);
        name.setVisibility(View.INVISIBLE);
        //app??????????????????????????????????????????????????????????????????
     /*   deleteDirWihtFile(new File(fileShare));
        deleteDirWihtFile(new File(COPY_PATH));*/
        //   deleteDirWihtFile(new File(VOTE_FILE));
        deleteDirWihtFile(new File(netFilePath));
        File voteFile = new File(VOTE_FILE);
        if (!voteFile.exists()) {
            voteFile.mkdir();
        }
        File netFile = new File(netFilePath);
        if (!netFile.exists()) {
            netFile.mkdir();
        }

        deleteDirWihtFile(new File(FileCathePath));
        File cutFiles = new File(FileCathePath);
        if (!cutFiles.exists()) {
            cutFiles.mkdir();
        }


      /*  if (Hawk.contains("wuhulocal")){

            Hawk.delete("wuhulocal");
        }*/
      /*  List<FileListBean> copyFileBeans = new ArrayList<>();//?????????????????????????????????
        Hawk.put("wuhulocal",copyFileBeans);*/
        // ??????????????????????????????

//        requestRunable = new Runnable() {
//            @Override
//            public void run() {
//                resumeToGetMeetingInfo();
//                requestMeetingHandler.postDelayed(this,6000);
//            }
//        };
//        requestMeetingHandler.postDelayed(requestRunable,100);

        //????????????Servive
        Intent intent = new Intent();
        intent.setAction(constant.SHOW_CLOSE_SERVICE_BROADCAST);
        sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        requestMeetingHandler.removeCallbacks(requestRunable);
        requestRunable = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoEncoder != null) {
            videoEncoder.stop();
            Log.d("UdpClientThread", "????????????????????????");
        }
    }

    /**
     * ??????????????????,??????fragment
     */
    public interface LoginToFragmentListener {
        void onUserInfoReceive();

        void onHideUserInfo();
    }

    private void getMeeingInfo(String meeting__id) {
        //??????????????????
        NetWorkManager.getInstance().getNetWorkApiService().getMeetingInfo(meeting__id).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<MeetingInfoBean>>() {
                    @Override
                    protected void onSuccess(BasicResponse<MeetingInfoBean> response) {
                        if (response != null) {
                            MeetingInfoBean meetingInfoBean = response.getData();
                            if (meetingInfoBean == null) {
                                return;
                            }
                            // ????????????????????????????????????
                            if (!meetingInfoBean.getStatus().equals("UNDERWAY")) {
                                // ???????????????????????????
                                Hawk.delete(constant._id);
                                agendaRl.setVisibility(View.GONE);
                                loginLeft.setVisibility(View.VISIBLE);
                                loginToFragmentListener.onHideUserInfo();
                                return;
                            }
                            // ???????????????????????????????????????
                            loginToFragmentListener.onUserInfoReceive();

                            agendaRl.setVisibility(View.VISIBLE);
                            loginLeft.setVisibility(View.GONE);
                            getAttenData();


                            if (meetingInfoBean != null) {
                                Drawable drawable = MeetingAPP.getContext().getResources().getDrawable(R.mipmap.bg_login);
                                DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(drawable).showImageForEmptyUri(drawable).showImageOnFail(drawable).resetViewBeforeLoading(false).delayBeforeLoading(1000).
                                        cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).handler(new Handler()).build();
                                ImageLoader.getInstance().displayImage(meetingInfoBean.getBackground_img(), theme_bg_ima, options);

                                Log.d("saddgg", "??????~~~~");
                                Hawk.put("background_img", meetingInfoBean.getBackground_img());//????????????????????????
                                Hawk.put("meeting_issues", meetingInfoBean.getName());//????????????
                                Hawk.put("meetingInfoBean", meetingInfoBean);//???????????????
                                long startTime = TimeUtils.getStringToDate(meetingInfoBean.getSche_start_time(), TimeUtils.DATA_FORMAT);//??????????????????
                                String yaer = TimeUtils.getTime(startTime, TimeUtils.DATA_FORMAT_NO_HOURS_DATA10);//??????-???
                                String month = TimeUtils.getTime(startTime, TimeUtils.DATA_FORMAT_NO_HOURS_DATA11);//??????-???
                                String starHous = TimeUtils.getTime(startTime, TimeUtils.DATA_FORMAT_NO_HOURS_DATA12);//??????-??????

                                long endTime = TimeUtils.getStringToDate(meetingInfoBean.getSche_end_time(), TimeUtils.DATA_FORMAT);//??????????????????
                                String endHous = TimeUtils.getTime(endTime, TimeUtils.DATA_FORMAT_NO_HOURS_DATA12);//??????-??????

                                yaerTv.setText(yaer);
                                monthTv.setText(month);
                                dateTv.setText(starHous + "-" + endHous);
                                Hawk.put("meeting_time", yaer + "/" + month + " " + starHous + "-" + endHous);
                                issues.setText(meetingInfoBean.getName());
                                if (meetingInfoBean.getMeeting_room_id().size() > 0) {
                                    MeetingInfoBean.MeetingRoomIdBean meetingRoomIdBean = meetingInfoBean.getMeeting_room_id().get(0);
                                }
                            }
                        }
                    }

                    @Override
                    protected void onFail(BasicResponse<MeetingInfoBean> response) {
                        super.onFail(response);
                        agendaRl.setVisibility(View.GONE);
                        loginLeft.setVisibility(View.VISIBLE);
                        loginToFragmentListener.onHideUserInfo();
                    }

                    // ????????????????????????
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        agendaRl.setVisibility(View.GONE);
                        loginLeft.setVisibility(View.VISIBLE);
                        loginToFragmentListener.onHideUserInfo();
                    }
                });
    }

    /**
     * Activity????????????
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionManager.onActivityResult(requestCode, resultCode, data, this);
        if (requestCode == ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            MediaProjection mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            videoEncoder = new VideoEncoderUtil(mediaProjection, "");
            videoEncoder.start();
        }
    }

    // ??????????????????????????? px(??????) ????????? ????????? dp
    public int px2dip(float pxValue) {
        // ????????????????????????????????????1???dp????????????px???
        float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f); // ??????????????????
    }

    public int px2sp(float pxValue) {

        float fontScale = getResources().getDisplayMetrics().scaledDensity;

        return (int) (pxValue / fontScale + 0.5f);

    }

    /**
     * @param view ?????????????????????view
     *             ?????????????????????????????????????????????????????????
     * @return ??????
     */
    private Bitmap getBitmap(View view) throws Exception {

        View screenView = getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();

        //????????????????????????
        Bitmap bitmap = screenView.getDrawingCache();

        if (bitmap != null) {

            //????????????????????????
            int outWidth = view.getWidth();
            int outHeight = view.getHeight();

            //????????????????????????????????????????????????(view?????????????????????
            int[] viewLocationArray = new int[2];
            view.getLocationOnScreen(viewLocationArray);

            //??????????????????????????????????????????
            bitmap = Bitmap.createBitmap(bitmap, viewLocationArray[0], viewLocationArray[1], outWidth, outHeight);
            view.setDrawingCacheEnabled(false);  //??????DrawingCahce?????????????????????
        }

        return bitmap;
    }

    //???????????????????????????
    private void onSaveBitmap(Bitmap mBitmap) {
        //???Bitmap??????????????????????????????/sdcard/Boohee/??????????????????????????????????????????,???????????????????????????????????????????????????????????????
        appDir = new File(Environment.getExternalStorageDirectory(), "iiiiiii");

        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Log.d("fgdddh", "??????~~~~");
        fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Message ms = new Message();
            ms.what = 1;
            handler.sendMessage(ms);//???????????????????????????
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));


    }

    public void sendFileInfo(String path, String type, String flag) {
        //??????????????????RequestBody???
        Log.d("dfgsgsf3333", path + "-----");
        File file = new File(path);
        if (file == null) {
            return;
        }
        upLoadFileType = getMimeType(file.getName());
        endStrAll = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse(upLoadFileType), file);
        Log.d("dfgsgsf3333", path + "---666666");
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        NetWorkManager.getInstance().getNetWorkApiService().upLoadFile(creatDirectory(type), "", "", part).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<UploadBean>>() {
                    @Override
                    protected void onFail(BasicResponse<UploadBean> response) {
                        super.onFail(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.d("dfgsgsf3333222", e.getMessage());
                    }

                    @Override
                    protected void onSuccess(BasicResponse<UploadBean> response) {
                        if (response != null) {
                            String c_id = "";
                            String user_id = "";
                            String _id = "";
                            if (Hawk.contains("_id")) {
                                _id = Hawk.get("_id");
                            }
                            if (Hawk.contains("c_id")) {
                                c_id = Hawk.get("c_id");
                            }
                            if (Hawk.contains("user_id")) {
                                user_id = Hawk.get("user_id");
                            }
                            uploadBean = response.getData();
                            //????????????????????????
                            createFileBeanRequest = new CreateFileBeanRequest();
                            createFileBeanRequest.setC_id(c_id);
                            createFileBeanRequest.setUser_id(user_id);
                            // createFileBeanRequest.setType(endStr);
                            createFileBeanRequest.setMeeting_record_id(_id);
                            List<CreateFileBeanRequest.FileListBean> fileListBeans = new ArrayList<>();
                            CreateFileBeanRequest.FileListBean fileListBean = new CreateFileBeanRequest.FileListBean();
                            fileListBean.setName(file.getName());
                            fileListBean.setPath(uploadBean.getUrl());
                            fileListBean.setSize("100");
                            fileListBean.setSuffix(endStrAll);
                            fileListBean.setUnclassified(flag);
                            fileListBean.setType(getFileType(endStrAll));//??????????????????????????????????????????????????????????????????
                            fileListBeans.add(fileListBean);
                            createFileBeanRequest.setFile_list(fileListBeans);
                            Log.d("dfgsgsf3333", path + "+++");
                            creatFile(createFileBeanRequest);


                        }

                    }
                });


    }

    private void creatFile(CreateFileBeanRequest createFileBeanRequest) {
        Log.d("dfgsgsf3333", "??????~~~~~");
        NetWorkManager.getInstance().getNetWorkApiService().createMeetingFile(createFileBeanRequest).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<CreateFileBeanResponse>>() {
                    @Override
                    protected void onFail(BasicResponse<CreateFileBeanResponse> response) {
                        super.onFail(response);
                        /* progressBarLl.setVisibility(View.GONE);*/
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        /*   progressBarLl.setVisibility(View.GONE);*/
                    }

                    @Override
                    protected void onSuccess(BasicResponse<CreateFileBeanResponse> response) {
                        if (response != null) {
                            Toast.makeText(LoginActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            //  progressBarLl.setVisibility(View.GONE);
//                            intent.setAction(constant.FRESH_FILE);
                            intent.setAction(constant.FRESH_FILE);
                            sendBroadcast(intent);
                         /*   Log.d("fhjfj", endStrAll + "------" + videoPath);
                            if ("mp4".equals(endStrAll)) {
                                Hawk.put("videoPath", videoPath);
                            }*/
                        }

                    }
                });
    }

    class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mFragmentTitle = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);//?????????List???
            mFragmentTitle.add(title);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        //???????????????????????????tab???name???
        @Override
        public CharSequence getPageTitle(int position) {
            //???????????????list?????????????????????
            return mFragmentTitle.get(position);
        }
    }

    /**
     * ????????????MimeType
     *
     * @param filename ?????????
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "multipart/form-data"; //* exe,????????????????????????
        }
        return contentType;
    }

    //??????????????????????????????
    private String creatDirectory(String end) {
        if ("1".equals(end)) {
            return "music";
        } else if ("2".equals(end)) {
            return "video";
        } else if ("3".equals(end)) {
            return "image";
        } else if ("4".equals(end)) {
            return "directory";
        } else if ("4".equals(end)) {
            return "directory";
        } else if ("4".equals(end)) {
            return "directory";
        } else if ("4".equals(end)) {
            return "directory";
        } else {
            return "directory";
        }

    }

    private String getFileType(String end) {
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return constant.OTHER;
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return constant.VIDEO;
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return constant.SUMMARY;
        } else if ((end.equals("ppt") || end.equals("pptx"))) {
            return constant.DOCUMENT;
        } else if (end.equals("xls")) {
            return constant.DOCUMENT;
        } else if (end.equals("doc") || end.equals("docx")) {
            return constant.DOCUMENT;
        } else if (end.equals("pdf")) {
            return constant.DOCUMENT;
        } else {
            return constant.OTHER;
        }

    }
}

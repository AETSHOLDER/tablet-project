package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.ActivitySearchListMeeting;
import com.example.paperlessmeeting_demo.activity.MainActivity;
import com.example.paperlessmeeting_demo.activity.WuHuActivity;
import com.example.paperlessmeeting_demo.activity.WuHuActivity2;
import com.example.paperlessmeeting_demo.adapter.WuHuMeetingListAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanResponse;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.WuHuMeetingListResponse;
import com.example.paperlessmeeting_demo.bean.WuHuNetWorkBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.DeleteFileUtil;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.thdReceive;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.widgets.MyDialog;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.google.gson.Gson;
import com.jyn.vcview.VerificationCodeView;
import com.orhanobut.hawk.Hawk;
import com.snow.common.tool.utils.FastClickUtils;
import com.tencent.smtt.sdk.TbsReaderView;

import io.reactivex.android.schedulers.AndroidSchedulers;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import butterknife.BindView;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ?????? on 2021/5/10.
 * ????????????
 */
@SuppressLint("ValidFragment")
public class ExtraordMeetingFragment extends BaseFragment implements VerificationCodeView.OnCodeFinishListener, WuHuMeetingListAdapter.onItemInterface {

    @BindView(R.id.initiate_meeting_tv)
    TextView init_meeting;
    @BindView(R.id.join_meeting_tv)
    TextView join_meeting;

    private Dialog initMeetingDialog;
    private Dialog joinMeetingDialog;
    private VerificationCodeView create_invite_codeview;
    private VerificationCodeView join_invite_codeview;
    private TextView tipsTxt;
    private TextView entry_meeting;
    private Spinner spinner;
    //    private List<String> ipcodeInfo = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();//???????????????????????????
    private boolean isReuse = false;//??????????????????????????????????????????????????????????????????????????????
    private int currentSelIndex = 0;
    private MyDialog historyConferenceDialog;//????????????
    private String fileShare = Environment.getExternalStorageDirectory() + constant.SHARE_FILE;//??????????????????????????????????????????
    private AlertDialog calenderDialog;
    private RecyclerView myList_view;
    private String requestTime;
    private WuHuMeetingListAdapter wuHuMeetingListAdapter;
    private List<WuHuMeetingListResponse> wuHuMeetingListResponses = new ArrayList<>();
    private TextView no_data;
    TbsReaderView tbsReaderView;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int port = (int)msg.obj;
                    Log.d("","port===="+port);
                    break;
                case 2:
                    String ss = (String)msg.obj;
                    Log.e("??????????????????",""+ss);
//                    Toast.makeText(SignActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 4:

                    break;
                case 5:
//                    Toast.makeText(SignActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
//                    Toast.makeText(SignActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    String df = msg.obj.toString();
                    Log.d("??????????????????++", df);
                    break;
               /* case  NetSpeedTimer.NET_SPEED_TIMER_DEFAULT:
                    String speed = (String) msg.obj;
                    //????????????????????????????????????????????????kb/s
                    init_meeting.setText(speed);
                    Log.d(TAG, "current net speed  = " + speed);
                    break;*/
                default:
                    break;
            }
        }
    };
    public static ExtraordMeetingFragment newInstance(String movie) {
        ExtraordMeetingFragment extraordMeetingFragment = new ExtraordMeetingFragment();
        return extraordMeetingFragment;
    }

    public ExtraordMeetingFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_extraordinary;
    }

    @Override
    protected void initData() {
        /*
         * ????????????????????????
         * */

    /*    TimePickerView  pickerView= new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //textView.setText(new SimpleDateFormat("yyyy'???'MM'???'dd'???'").format(date));
                }
            })
            .setSubmitText("??????")
            .setCancelText("??????")
//                .setSubmitColor(Color.BLACK)
//                .setCancelColor(Color.BLACK)
            .setType(new boolean[]{true, true, true, false, false, false})    //??????"??????????????????"?????????????????????????????????
            .isCenterLabel(false)          //???????????????????????????label?????????false?????????item???????????????label???
            //.isDialog(true)          //????????????????????????????????????????????????
            .isCyclic(true)            //??????????????????
//                .setTextColorOut(Color.GRAY)         //???????????????????????????(?????????GRAY)
//                .setTextColorCenter(Color.BLACK)       //????????????????????????(?????????BLACK)
            .build();
        //???????????????????????????????????????????????????????????????
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyyMMdd").parse("20201104"));
//      ????????????????????????
//      calendar.set(2020,10,4);       //??????????????????????????????0?????????????????????11?????????????????????10
        pickerView.setDate(calendar);
        pickerView.show();*/


        if (Hawk.contains("UserBehaviorBean")) {
            UserBehaviorBean userBehaviorBean = Hawk.get("UserBehaviorBean");
            UserBehaviorBean.DataBean dataBean = new UserBehaviorBean.DataBean();
            dataBean.setTittile(this.getClass().getName());
            dataBean.setTime(TimeUtils.getTime(System.currentTimeMillis()));
            List<UserBehaviorBean.DataBean> dataBeanList = userBehaviorBean.getData();
            dataBeanList.add(dataBean);
            Hawk.put("UserBehaviorBean", userBehaviorBean);
        }
        init_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*  if (UrlConstant.baseUrl.equals("http://192.168.1.1:3006")) {
                Toast.makeText(getActivity(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                 return;
              }*/

                initMeetingDialog();
            }
        });
        join_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UrlConstant.baseUrl.equals("http://192.168.1.1:3006")) {
                    Toast.makeText(getActivity(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }

                showHistoryDialog();
                //??????????????????????????????????????????????????????
                // joinMeetingDialog();
            }
        });
       /* //??????NetSpeedTimer??????
        mNetSpeedTimer = new NetSpeedTimer(mContext, new NetSpeed(), mHandler).setDelayTime(1000).setPeriodTime(2000);
        //????????????????????????????????????????????????
        mNetSpeedTimer.startSpeedTimer();*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        return rootView;
    }

    /*  *//**
     * ??????????????????
     *//*
    private void joinMeetingDialog() {
        joinMeetingDialog = new Dialog(getActivity(), R.style.update_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diaog_verify_invite_code, null);//?????????????????????
        join_invite_codeview = view.findViewById(R.id.verificationcodeview);
        join_invite_codeview.setOnCodeFinishListener(this);
        ImageView close_img = view.findViewById(R.id.ima_close);
        ImageView imageView = view.findViewById(R.id.clear_ima);
        tipsTxt = (TextView) view.findViewById(R.id.results);
        tipsTxt.setVisibility(View.INVISIBLE);
        joinMeetingDialog.setContentView(view);//?????????????????????????????????
        joinMeetingDialog.setCancelable(false);// ?????????????????????????????????
        joinMeetingDialog.show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_invite_codeview.setEmpty();
            }
        });
        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinMeetingDialog.dismiss();
            }
        });
    }*/

    /**
     * ??????????????????
     */
    private void joinMeetingDialog() {
//        ipcodeInfo.clear();
        stringList.clear();
        joinMeetingDialog = new Dialog(getActivity(), R.style.update_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diaog_verify_invite_code, null);//?????????????????????
        join_invite_codeview = view.findViewById(R.id.verificationcodeview);
        entry_meeting = view.findViewById(R.id.entry_meeting);
        spinner = view.findViewById(R.id.spinner);
        ImageView close_img = view.findViewById(R.id.ima_close);
        join_invite_codeview.setOnCodeFinishListener(this);
        TextView ok = view.findViewById(R.id.ok_tv);
        ok.setVisibility(View.INVISIBLE);
        ImageView imageView = view.findViewById(R.id.clear_ima);
        tipsTxt = view.findViewById(R.id.results);
        tipsTxt.setVisibility(View.GONE);
        joinMeetingDialog.setContentView(view);//?????????????????????????????????
        joinMeetingDialog.setCancelable(false);// ?????????????????????????????????
        joinMeetingDialog.show();

        for (String msg : thdReceive.receiveIPArr) {
            List<String> ipcodeInfo = FLUtil.dealUDPMsg(msg);
            // ???????????? ip code/1
            String[] code_idnex = ipcodeInfo.get(1).split("/");
            stringList.add(code_idnex[0]);
        }
//        ipcodeInfo = FLUtil.dealUDPMsg(UDPBroadcastManager.getInstance().MythdReceive.receiveMsg);
//        stringList.addAll(ipcodeInfo);
        if (stringList.size() == 0) {
            stringList.add("???????????????");
        } else {
            stringList.remove("???????????????");
        }

        //??????ArrayAdapter??????
        // stringList.add("???????????????");
        Log.d("trhhh", stringList.size() + "");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stringList);

        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        /**??????????????????*/


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (thdReceive.receiveIPArr.size() <= 0) {
                    tipsTxt.setText("???????????????,?????????!");
                    tipsTxt.setVisibility(View.VISIBLE);
//                    Toast.makeText(LoginActivity.this,"???????????????,?????????!",Toast.LENGTH_LONG);
                    return;
                }
                if (stringList.contains(parent.getAdapter().getItem(position).toString())) {
                    UserUtil.isTempMeeting = true;
                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);
                    entry_meeting.setVisibility(View.VISIBLE);
                    tipsTxt.setVisibility(View.GONE);

                    currentSelIndex = position;
                } else {
                    tipsTxt.setText("????????????????????????!");
                    tipsTxt.setVisibility(View.VISIBLE);
                }
//                if (ipcodeInfo.get(1).equals(parent.getAdapter().getItem(position).toString())) {
//                    UserUtil.isTempMeeting = true;
//                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);
//                    entry_meeting.setVisibility(View.VISIBLE);
//                    tipsTxt.setVisibility(View.GONE);
//                } else {
//                    tipsTxt.setText("????????????????????????!");
//                    tipsTxt.setVisibility(View.VISIBLE);
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_invite_codeview.setEmpty();
            }
        });

        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinMeetingDialog.dismiss();
            }
        });
        entry_meeting.setEnabled(true);
        entry_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtils.init().isClickFast()) {
                    return;
                }
                entry_meeting.setText("????????????...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        joinMeetingDialog.dismiss();
                    }
                }, 200);

                UserUtil.isTempMeeting = true;
                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);

                //  ??????receiveIPArr???????????????
                if (thdReceive.receiveIPArr.size() >= currentSelIndex + 1) {
                    List<String> ipcodeInfo = FLUtil.dealUDPMsg(thdReceive.receiveIPArr.get(currentSelIndex));
                    String[] strAll = ipcodeInfo.get(1).split("/");
                    String isRu = strAll[1];
                    Intent intent1 = new Intent(getActivity(), WuHuActivity2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ip", ipcodeInfo.get(0));
                    bundle.putString("isreuse", isRu);
                    intent1.putExtras(bundle);
                    Hawk.put("isreuse", isRu);//??????????????????????????????????????????
//                        intent1.putExtra("code",ipcodeInfo.get(1));
                    startActivity(intent1);


                } else {
                    ToastUtil.makeText(getActivity(), "???????????????!");

                }


            }
        });
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                joinMeetingDialog.dismiss();
//            }
//        });
    }

    /**
     * ??????????????????
     */
    private void initMeetingDialog() {
        initMeetingDialog = new Dialog(getActivity(), R.style.update_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diaog_create_invite_code, null);//?????????????????????
        create_invite_codeview = view.findViewById(R.id.verificationcodeview);
        create_invite_codeview.setOnCodeFinishListener(this);
        ImageView imageView = view.findViewById(R.id.clear_ima);
        ImageView close_img = view.findViewById(R.id.ima_close);
        initMeetingDialog.setContentView(view);//?????????????????????????????????
        initMeetingDialog.setCancelable(false);// ?????????????????????????????????
        initMeetingDialog.show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_invite_codeview.setEmpty();
            }
        });
        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMeetingDialog.dismiss();
            }
        });
    }


    /**
     * ???????????????????????????
     */
    @Override
    public void onTextChange(View view, String content) {
        if (view == join_invite_codeview && content.length() < 4) {
            tipsTxt.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onComplete(View view, String content) {
        if (view == create_invite_codeview) {
            isReuse = !Hawk.contains("WuHuFragmentData");
            if (!FLUtil.netIsConnect(getActivity())) {
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                ToastUtils.showToast(getActivity(), "?????????????????????????????????!");
                return;
            }

            // ???????????????????????????
            if (thdReceive.receiveIPArr.size() > 0 && !UDPBroadcastManager.MythdReceive.pause) {

                for (String msg : thdReceive.receiveIPArr) {
                    List<String> ipcodeInfo = FLUtil.dealUDPMsg(msg);
                    // ???????????? ip code/1
                    String[] code_idnex = ipcodeInfo.get(1).split("/");
                    stringList.add(code_idnex[0]);
                }
//

                // ??????????????????????????????????????????????????????server?????????
                if (stringList.contains(content)) {
                    int index = stringList.indexOf(content);
                    initMeetingDialog.dismiss();
                    CVIPaperDialogUtils.showCustomDialog(getActivity(), "??????????????????", null, "????????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                // ????????????????????????????????????
                                UserUtil.isTempMeeting = true;
                                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);

                                Intent intent1 = new Intent(getActivity(), WuHuActivity2.class);
                                if (index >= thdReceive.receiveIPArr.size()) {
                                    ToastUtils.showShort("??????????????????!");
                                    return;
                                }

                                List<String> ipcodeInfo = FLUtil.dealUDPMsg(thdReceive.receiveIPArr.get(index));
                                String[] strAll = ipcodeInfo.get(1).split("/");
                                String isRu = strAll[1];
                                UserUtil.isNetDATA = false;
                                Intent intent2 = new Intent(getActivity(), WuHuActivity2.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("ip", ipcodeInfo.get(0));
                                bundle.putString("isreuse", isRu);
                                intent2.putExtras(bundle);
                                intent2.putExtras(bundle);
                                Hawk.put("isreuse", isRu);//??????????????????????????????????????????
                                startActivity(intent2);
                                return;
                            }
                        }
                    });
                    return;
                } else {
                    initMeetingDialog.dismiss();
                    //  TODO ?????????????????????????????????
//                    CVIPaperDialogUtils.showConfirmDialog(getActivity(), "???????????????????????????????????????,?????????????????????!", "?????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
//                        @Override
//                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
//                            if (clickConfirm) {
//                                return;
//                            }
//                        }
//                    });

                    CVIPaperDialogUtils.showCustomDialog(getActivity(), "??????????????????", "?????????", "????????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                if (!isReuse) {
                                    initMeetingDialog.dismiss();
                                    UserUtil.isTempMeeting = true;
                                    UserUtil.isNetDATA = false;
                                    constant.temp_code = content;
                                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                                    Intent intent = new Intent(getActivity(), WuHuActivity2.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("code", content);
                                    bundle.putString("isreuse", "3");//1:??????????????????  2???????????????????????? 3?????????????????????
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    Hawk.put("isreuse", "3");
                                    //????????????????????? ?????????
                                  //  showRightDialog(content);
                                } else {
                                    initMeetingDialog.dismiss();
                                    UserUtil.isTempMeeting = true;
                                    UserUtil.isNetDATA = false;
                                    constant.temp_code = content;
                                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                                    Intent intent = new Intent(getActivity(), WuHuActivity2.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("code", content);
                                    bundle.putString("isreuse", "3");//1:??????????????????  2???????????????????????? 3?????????????????????
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    Hawk.put("isreuse", "3");
                                }

                            }
                        }
                    });

                }
            } else {
                if (!isReuse) {
                    initMeetingDialog.dismiss();
                    UserUtil.isTempMeeting = true;
                    UserUtil.isNetDATA = false;
                    constant.temp_code = content;
                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                    Intent intent = new Intent(getActivity(), WuHuActivity2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("code", content);
                    bundle.putString("isreuse", "3");//1:??????????????????  2???????????????????????? 3?????????????????????
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Hawk.put("isreuse", "3");

                    //????????????????????? ?????????
                  //  showRightDialog(content);
                } else {
                    initMeetingDialog.dismiss();
                    UserUtil.isTempMeeting = true;
                    UserUtil.isNetDATA = false;
                    constant.temp_code = content;
                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                    Intent intent = new Intent(getActivity(), WuHuActivity2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("code", content);
                    bundle.putString("isreuse", "3");//1:??????????????????  2???????????????????????? 3?????????????????????
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Hawk.put("isreuse", "3");
                }


            }
        } else if (view == join_invite_codeview) {

//            Log.d("codeEditView", "//??????????????????????????????====" + content);
//            if (content.length() <= 0) {
//                Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_LONG);
//                return;
//            } else if (content.length() == 4) {
//                if (UDPBroadcastManager.getInstance().MythdReceive.receiveMsg.length() <= 0) {
//                    tipsTxt.setText("???????????????,?????????!");
//                    tipsTxt.setVisibility(View.VISIBLE);
////                    Toast.makeText(LoginActivity.this,"???????????????,?????????!",Toast.LENGTH_LONG);
//                    return;
//                }
//                List<String> ipcodeInfo = FLUtil.dealUDPMsg(UDPBroadcastManager.getInstance().MythdReceive.receiveMsg);
//                if (ipcodeInfo.get(1).equals(content)) {
//                    joinMeetingDialog.dismiss();
//                    UserUtil.isTempMeeting = true;
//                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);
//
//                    Intent intent1 = new Intent(getActivity(), MainActivity.class);
//                    intent1.putExtra("ip", ipcodeInfo.get(0));
////                        intent1.putExtra("code",ipcodeInfo.get(1));
//                    startActivity(intent1);
//                } else {
//                    tipsTxt.setText("????????????????????????!");
//                    tipsTxt.setVisibility(View.VISIBLE);
//                }
//
//            } else {
//                tipsTxt.setVisibility(View.INVISIBLE);
//            }
        }
    }

    //??????????????????historyConferenceDialog
    public void showHistoryDialog() {


        //?????????dialog????????????
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.history_meeting_dialog, null);
        //?????????dialog????????????
        historyConferenceDialog = new MyDialog(getActivity(), R.style.dialogTransparent);
        historyConferenceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //????????????????????????????????????????????????????????????(false???????????????????????????????????????????????????)
        historyConferenceDialog.setCanceledOnTouchOutside(true);
        //??????????????????Dialog
        historyConferenceDialog.setContentView(inflate);
        historyConferenceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        historyConferenceDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        historyConferenceDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
                historyConferenceDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        historyConferenceDialog.setOnTouchOutside(new MyDialog.onTouchOutsideInterFace() {
            @Override
            public void outSide() {
                historyConferenceDialog.dismiss();
            }
        });
        RelativeLayout calender_rl = inflate.findViewById(R.id.calender_rl);
        TextView calender_tv = inflate.findViewById(R.id.calender_tv);
        no_data = inflate.findViewById(R.id.no_data);

        myList_view = inflate.findViewById(R.id.myList_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        myList_view.setLayoutManager(linearLayoutManager);
        //????????????Activity???????????????
        Window window = historyConferenceDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.46);//?????????
        wlp.height = (int) (height * 0.7);//?????????
        window.setAttributes(wlp);
        calender_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String data = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA7);//??????-???
                Map<String, Object> map = new HashMap<>();
                map.put("name", "");
                map.put("startTime", data);*/

                View viewDatePicker = LayoutInflater.from(getActivity()).inflate(R.layout.calender_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(viewDatePicker);
                builder.setCancelable(true);
                calenderDialog = builder.create();
                calenderDialog.show();
                WindowManager windowManager = getActivity().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = calenderDialog.getWindow().getAttributes();
                lp.width = (int) (display.getWidth() * 0.8); //????????????
                calenderDialog.getWindow().setAttributes(lp);
                DatePicker datePicker = viewDatePicker.findViewById(R.id.date_pickerdialog);
                // resizePicker(datePicker);
                Window window = calenderDialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.alpha = 1.0f;
                Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
                wlp.gravity = Gravity.CENTER;
                wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Point size = new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;
                wlp.width = (int) (width * 0.6);//?????????
                wlp.height = (int) (height * 0.54);//?????????
                window.setAttributes(wlp);
                datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strMonth;
                        String strday;
                        if ((monthOfYear + 1) < 10) {
                            strMonth = "0" + (monthOfYear + 1);
                        } else {
                            strMonth = (monthOfYear + 1) + "";
                        }
                        if (dayOfMonth < 10) {
                            strday = "0" + dayOfMonth;
                        } else {
                            strday = dayOfMonth + "";

                        }

                        requestTime = year + "-" + strMonth + "-" + strday;
                        Log.d("fgdgdfg", requestTime);
                        if (requestTime != null) {
                            calender_tv.setText(requestTime);
                            getMeetingList(requestTime);
                            calenderDialog.dismiss();
                        }

                    }
                });


                calenderDialog.show();
            }
        });
        historyConferenceDialog.show();

    }

    //????????????????????????????????????
    private void getMeetingList(String time) {
        NetWorkManager.getInstance().getNetWorkApiService().meetingList("", time).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<WuHuMeetingListResponse>>>() {
                    @Override
                    protected void onFail(BasicResponse<List<WuHuMeetingListResponse>> response) {
                        super.onFail(response);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }

                    @Override
                    protected void onSuccess(BasicResponse<List<WuHuMeetingListResponse>> response) {
                        if (response != null) {
                            wuHuMeetingListResponses.clear();
                            wuHuMeetingListResponses.addAll(response.getData());
                            initUi(wuHuMeetingListResponses);

                        }

                    }
                });

    }

    //???????????????????????????
    private void initUi(List<WuHuMeetingListResponse> wuHuMeetingListResponses) {
        Log.d("dfdgsdgsfg", wuHuMeetingListResponses.size() + "");
        if (wuHuMeetingListResponses.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
            myList_view.setVisibility(View.GONE);

        } else {
            no_data.setVisibility(View.GONE);
            myList_view.setVisibility(View.VISIBLE);
        }
        wuHuMeetingListAdapter = new WuHuMeetingListAdapter(getActivity(), wuHuMeetingListResponses);
        myList_view.setAdapter(wuHuMeetingListAdapter);
        wuHuMeetingListAdapter.setOnItemInterface(this);
        wuHuMeetingListAdapter.notifyDataSetChanged();

    }

    //??????????????????
    @Override
    public void onItem(int position, WuHuMeetingListResponse wuHuMeetingListResponse) {
        if (!FLUtil.netIsConnect(getActivity())) {
            ToastUtils.showToast(getActivity(), "?????????????????????????????????!");
            return;
        }
        // ???????????????????????????
        if (thdReceive.receiveIPArr.size() > 0 && !UDPBroadcastManager.MythdReceive.pause) {

            for (String msg : thdReceive.receiveIPArr) {
                List<String> ipcodeInfo = FLUtil.dealUDPMsg(msg);
                // ???????????? ip code/1
                String[] code_idnex = ipcodeInfo.get(1).split("/");
                stringList.add(code_idnex[0]);
            }
            UserUtil.isNetDATA = true;
            UserUtil.isTempMeeting = true;
            UserUtil.ISCHAIRMAN = true;
            constant.temp_code = "formalmeeting-" + wuHuMeetingListResponse.getName();
            Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
            Intent intent = new Intent(getActivity(), WuHuActivity2.class);
            Bundle bundle = new Bundle();
            bundle.putString("code", "formalmeeting-" + wuHuMeetingListResponse.getName() + "/" + wuHuMeetingListResponse.get_id());
            bundle.putString("isreuse", "4");//1:??????????????????  2???????????????????????? 3?????????????????????
            intent.putExtras(bundle);
            Hawk.put("WuHuMeetingListResponse", wuHuMeetingListResponse);
            getActivity().startActivity(intent);

        } else {
            UserUtil.isTempMeeting = true;
            UserUtil.ISCHAIRMAN = true;
            UserUtil.isNetDATA = true;
            constant.temp_code = "formalmeeting-" + wuHuMeetingListResponse.getName();
            Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
            Intent intent = new Intent(getActivity(), WuHuActivity2.class);
            Bundle bundle = new Bundle();
            bundle.putString("code", "formalmeeting-" + wuHuMeetingListResponse.getName() + "/" + wuHuMeetingListResponse.get_id());
            bundle.putString("isreuse", "4");//1:??????????????????  2???????????????????????? 3?????????????????????
            intent.putExtras(bundle);
            Hawk.put("WuHuMeetingListResponse", wuHuMeetingListResponse);
            getActivity().startActivity(intent);

        }

        if(historyConferenceDialog!=null){
            historyConferenceDialog.dismiss();
        }
        //====


    }

    /**
     * websocket???????????????????????????
     */
    private void wsUpdata(Object obj, String packType) {
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(packType);
        bean.setBody(obj);
        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);
    }

    private void showMeetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(null, null);
        builder.setView(null);
        builder.setCancelable(true);
        calenderDialog = builder.create();

        calenderDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = calenderDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8); //????????????
        calenderDialog.getWindow().setAttributes(lp);


    }
    /*??????FrameLayout?????????*/

    private void resizePicker(FrameLayout tp) {        //DatePicker???TimePicker?????????FrameLayout
        List<NumberPicker> npList = findNumberPicker(tp);  //???????????????NumberPicker
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);      //????????????NumberPicker?????????
        }
    }

    /**
     * ??????viewGroup ?????????numberpicker??????
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /**
     * ??????numberpicker??????
     */
    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(880, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 5, 10, 5);
        np.setLayoutParams(params);
    }


    //??????????????????
    public void showFileTransferDialog() {
        //?????????dialog????????????
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.wuhu_file_progress_dialog, null);
        //?????????dialog????????????
        historyConferenceDialog = new MyDialog(getActivity(), R.style.dialogTransparent);
        historyConferenceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //????????????????????????????????????????????????????????????(false???????????????????????????????????????????????????)
        historyConferenceDialog.setCanceledOnTouchOutside(false);
        //??????????????????Dialog
        historyConferenceDialog.setContentView(inflate);
        historyConferenceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        historyConferenceDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        historyConferenceDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
                historyConferenceDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });

        //????????????Activity???????????????
        Window window = historyConferenceDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.45);//?????????
        wlp.height = (int) (width * 0.3);//?????????
        window.setAttributes(wlp);
        historyConferenceDialog.setOnTouchOutside(new MyDialog.onTouchOutsideInterFace() {
            @Override
            public void outSide() {
                Log.d("sdfsdfdsff", "??????~~~~~");
                //  Toast.makeText(getActivity(),"??????",Toast.LENGTH_SHORT).show();
            }
        });
        historyConferenceDialog.show();
    }

    public void showRightDialog(String codeStr) {
        //?????????dialog????????????
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_reuse_wuhu_meeting, null);
        //?????????dialog????????????
        Dialog dialog = new Dialog(getActivity(), R.style.dialogTransparent);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //????????????????????????????????????????????????????????????(false???????????????????????????????????????????????????)
        dialog.setCanceledOnTouchOutside(false);
        //??????????????????Dialog
        dialog.setContentView(inflate);

        TextView confir_tx = inflate.findViewById(R.id.confir_tx);
        TextView cancle_tv = inflate.findViewById(R.id.cancle_tv);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
                dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });

        confir_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserUtil.isTempMeeting = true;
                UserUtil.isNetDATA = false;
                constant.temp_code = codeStr;
                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                Intent intent = new Intent(getActivity(), WuHuActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putString("code", codeStr);
                bundle.putString("isreuse", "1");//1:??????????????????  2???????????????????????? 3?????????????????????
                intent.putExtras(bundle);
                startActivity(intent);
                Hawk.put("isreuse", "1");
                dialog.dismiss();
            }
        });
        cancle_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserUtil.isTempMeeting = true;
                UserUtil.isNetDATA = false;
                constant.temp_code = codeStr;
                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                Intent intent = new Intent(getActivity(), WuHuActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putString("code", codeStr);
                bundle.putString("isreuse", "2");//1:??????????????????  2???????????????????????? 3?????????????????????
                intent.putExtras(bundle);
                File share = new File(fileShare);
                if (!share.exists()) {
                    share.mkdir();
                }
                startActivity(intent);
                Hawk.put("isreuse", "2");
                dialog.dismiss();
            }
        });
        //????????????Activity???????????????
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.45);//?????????
        wlp.height = (int) (height * 0.23);
        //?????????
        window.setAttributes(wlp);
       // dialog.show();
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}

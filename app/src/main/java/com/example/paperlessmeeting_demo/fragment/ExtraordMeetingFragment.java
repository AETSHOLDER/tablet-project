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
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.widgets.MyDialog;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.google.gson.Gson;
import com.jyn.vcview.VerificationCodeView;
import com.orhanobut.hawk.Hawk;
import com.snow.common.tool.utils.FastClickUtils;

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
 * Created by 梅涛 on 2021/5/10.
 * 临时会议
 */
@SuppressLint("ValidFragment")
public class ExtraordMeetingFragment extends BaseFragment implements VerificationCodeView.OnCodeFinishListener ,  WuHuMeetingListAdapter.onItemInterface{

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
    private List<String> stringList = new ArrayList<>();//暂存临时会议邀请码
    private boolean isReuse = false;//根据有无会议记录来判断芜湖版本应用是否是第一次安装。
    private int currentSelIndex = 0;
    private MyDialog historyConferenceDialog;//历史会议
    private String fileShare = Environment.getExternalStorageDirectory() + constant.SHARE_FILE;//其他设备分享得到的文件夹路径
    private AlertDialog calenderDialog;
    private RecyclerView myList_view;
    private String requestTime;
    private WuHuMeetingListAdapter wuHuMeetingListAdapter;
    private List<WuHuMeetingListResponse> wuHuMeetingListResponses = new ArrayList<>();
    private TextView no_data;

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
         * 统计用户行为日志
         * */

    /*    TimePickerView  pickerView= new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //textView.setText(new SimpleDateFormat("yyyy'年'MM'月'dd'日'").format(date));
                }
            })
            .setSubmitText("确定")
            .setCancelText("取消")
//                .setSubmitColor(Color.BLACK)
//                .setCancelColor(Color.BLACK)
            .setType(new boolean[]{true, true, true, false, false, false})    //显示"年月日时分秒"的哪几项，默认全部显示
            .isCenterLabel(false)          //是否只显示选中项的label文字，false则每项item全部都带有label。
            //.isDialog(true)          //是否是对话框样式（页面居中显示）
            .isCyclic(true)            //是否循环滚动
//                .setTextColorOut(Color.GRAY)         //未选中项的文本颜色(默认色GRAY)
//                .setTextColorCenter(Color.BLACK)       //选中项的文本颜色(默认色BLACK)
            .build();
        //设置默认的日期时间（默认显示当前日期时间）
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyyMMdd").parse("20201104"));
//      或者这样写更简单
//      calendar.set(2020,10,4);       //但这里注意：月份是从0开始的，要显示11月，参数应该为10
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
                initMeetingDialog();
            }
        });
        join_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                showHistoryDialog();
                //老版：通过选取临时会议邀请码进入会议
                // joinMeetingDialog();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        return rootView;
    }

    /*  *//**
     * 加入临时会议
     *//*
    private void joinMeetingDialog() {
        joinMeetingDialog = new Dialog(getActivity(), R.style.update_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diaog_verify_invite_code, null);//加载自己的布局
        join_invite_codeview = view.findViewById(R.id.verificationcodeview);
        join_invite_codeview.setOnCodeFinishListener(this);
        ImageView close_img = view.findViewById(R.id.ima_close);
        ImageView imageView = view.findViewById(R.id.clear_ima);
        tipsTxt = (TextView) view.findViewById(R.id.results);
        tipsTxt.setVisibility(View.INVISIBLE);
        joinMeetingDialog.setContentView(view);//这里还可以指定布局参数
        joinMeetingDialog.setCancelable(false);// 不可以用“返回键”取消
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
     * 加入临时会议
     */
    private void joinMeetingDialog() {
//        ipcodeInfo.clear();
        stringList.clear();
        joinMeetingDialog = new Dialog(getActivity(), R.style.update_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diaog_verify_invite_code, null);//加载自己的布局
        join_invite_codeview = view.findViewById(R.id.verificationcodeview);
        entry_meeting = view.findViewById(R.id.entry_meeting);
        spinner = view.findViewById(R.id.spinner);
        ImageView close_img = view.findViewById(R.id.ima_close);
        join_invite_codeview.setOnCodeFinishListener(this);
        TextView ok = view.findViewById(R.id.ok_tv);
        ok.setVisibility(View.INVISIBLE);
        ImageView imageView = view.findViewById(R.id.clear_ima);
        tipsTxt = (TextView) view.findViewById(R.id.results);
        tipsTxt.setVisibility(View.GONE);
        joinMeetingDialog.setContentView(view);//这里还可以指定布局参数
        joinMeetingDialog.setCancelable(false);// 不可以用“返回键”取消
        joinMeetingDialog.show();

        for (String msg : UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr) {
            List<String> ipcodeInfo = FLUtil.dealUDPMsg(msg);
            // 里面包含 ip code/1
            String[] code_idnex = ipcodeInfo.get(1).split("/");
            stringList.add(code_idnex[0]);
        }
//        ipcodeInfo = FLUtil.dealUDPMsg(UDPBroadcastManager.getInstance().MythdReceive.receiveMsg);
//        stringList.addAll(ipcodeInfo);
        if (stringList.size() == 0) {
            stringList.add("暂无邀请码");
        } else {
            if (stringList.contains("暂无邀请码")) {
                stringList.remove("暂无邀请码");
            }
        }

        //创建ArrayAdapter对象
        // stringList.add("暂无邀请码");
        Log.d("trhhh", stringList.size() + "");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stringList);

        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        /**选项选择监听*/


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.size() <= 0) {
                    tipsTxt.setText("暂无该会议,请检查!");
                    tipsTxt.setVisibility(View.VISIBLE);
//                    Toast.makeText(LoginActivity.this,"暂无该会议,请检查!",Toast.LENGTH_LONG);
                    return;
                }
                if (stringList.contains(parent.getAdapter().getItem(position).toString())) {
                    UserUtil.isTempMeeting = true;
                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);
                    entry_meeting.setVisibility(View.VISIBLE);
                    tipsTxt.setVisibility(View.GONE);

                    currentSelIndex = position;
                } else {
                    tipsTxt.setText("未查询到此邀请码!");
                    tipsTxt.setVisibility(View.VISIBLE);
                }
//                if (ipcodeInfo.get(1).equals(parent.getAdapter().getItem(position).toString())) {
//                    UserUtil.isTempMeeting = true;
//                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);
//                    entry_meeting.setVisibility(View.VISIBLE);
//                    tipsTxt.setVisibility(View.GONE);
//                } else {
//                    tipsTxt.setText("未查询到此邀请码!");
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
                entry_meeting.setText("正在进入...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        joinMeetingDialog.dismiss();
                    }
                }, 200);

                UserUtil.isTempMeeting = true;
                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);

                //  判断receiveIPArr是否被清了
                if (UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.size() >= currentSelIndex + 1) {
                    List<String> ipcodeInfo = FLUtil.dealUDPMsg(UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.get(currentSelIndex));
                    String[] strAll = ipcodeInfo.get(1).split("/");
                    String isRu = strAll[1];
                    Intent intent1 = new Intent(getActivity(), WuHuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ip", ipcodeInfo.get(0));
                    bundle.putString("isreuse", isRu);
                    intent1.putExtras(bundle);
                    Hawk.put("isreuse", isRu);//储存是否重复利用会议模板标识
//                        intent1.putExtra("code",ipcodeInfo.get(1));
                    startActivity(intent1);


                } else {
                    ToastUtil.makeText(getActivity(), "请重新进入!");

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
     * 创建临时会议
     */
    private void initMeetingDialog() {
        initMeetingDialog = new Dialog(getActivity(), R.style.update_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diaog_create_invite_code, null);//加载自己的布局
        create_invite_codeview = view.findViewById(R.id.verificationcodeview);
        create_invite_codeview.setOnCodeFinishListener(this);
        ImageView imageView = view.findViewById(R.id.clear_ima);
        ImageView close_img = view.findViewById(R.id.ima_close);
        initMeetingDialog.setContentView(view);//这里还可以指定布局参数
        initMeetingDialog.setCancelable(false);// 不可以用“返回键”取消
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
     * 邀请码弹框输入回调
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
            if (Hawk.contains("WuHuFragmentData")) {
                isReuse = false;
            } else {
                isReuse = true;
            }
            if (!FLUtil.netIsConnect(getActivity())) {
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                ToastUtils.showToast(getActivity(), "网络不可用，请检查网络!");
                return;
            }

            // 有在接收到广播消息
            if (UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.size() > 0 && !UDPBroadcastManager.getInstance().MythdReceive.pause) {

                for (String msg : UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr) {
                    List<String> ipcodeInfo = FLUtil.dealUDPMsg(msg);
                    // 里面包含 ip code/1
                    String[] code_idnex = ipcodeInfo.get(1).split("/");
                    stringList.add(code_idnex[0]);
                }
//

                // 要创建会议，但是当前有相同的邀请码的server已创建
                if (stringList.contains(content)) {
                    int index = stringList.indexOf(content);
                    initMeetingDialog.dismiss();
                    CVIPaperDialogUtils.showCustomDialog(getActivity(), "已存在该邀请码对应的临时会议，是否加入?", null, "加入会议", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                // 加入临时会议，而不是创建
                                UserUtil.isTempMeeting = true;
                                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);

                                Intent intent1 = new Intent(getActivity(), WuHuActivity.class);
                                if (index >= UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.size()) {
                                    ToastUtils.showShort("加入不了会议!");
                                    return;
                                }

                                List<String> ipcodeInfo = FLUtil.dealUDPMsg(UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.get(index));
                                String[] strAll = ipcodeInfo.get(1).split("/");
                                String isRu = strAll[1];
                                UserUtil.isNetDATA = false;
                                Intent intent2 = new Intent(getActivity(), WuHuActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("ip", ipcodeInfo.get(0));
                                bundle.putString("isreuse", isRu);
                                intent2.putExtras(bundle);
                                intent2.putExtras(bundle);
                                Hawk.put("isreuse", isRu);//储存是否重复利用会议模板标识
                                startActivity(intent2);
                                return;
                            }
                        }
                    });
                    return;
                } else {
                    initMeetingDialog.dismiss();
                    //  TODO 芜湖版不让创建两场会议
//                    CVIPaperDialogUtils.showConfirmDialog(getActivity(), "存在其他已经创建的临时会议,不允许继续创建!", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
//                        @Override
//                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
//                            if (clickConfirm) {
//                                return;
//                            }
//                        }
//                    });

                    CVIPaperDialogUtils.showCustomDialog(getActivity(), "存在其他已经创建的临时会议", "是否仍然创建?", "继续创建", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                if (!isReuse) {
                                    initMeetingDialog.dismiss();
                                    showRightDialog(content);
                                } else {
                                    initMeetingDialog.dismiss();
                                    UserUtil.isTempMeeting = true;
                                    UserUtil.isNetDATA = false;
                                    constant.temp_code = content;
                                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                                    Intent intent = new Intent(getActivity(), WuHuActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("code", content);
                                    bundle.putString("isreuse", "3");//1:代表复用模板  2：代表不复用模板 3：代表没有模板
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
                    showRightDialog(content);
                } else {
                    initMeetingDialog.dismiss();
                    UserUtil.isTempMeeting = true;
                    UserUtil.isNetDATA = false;
                    constant.temp_code = content;
                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                    Intent intent = new Intent(getActivity(), WuHuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("code", content);
                    bundle.putString("isreuse", "3");//1:代表复用模板  2：代表不复用模板 3：代表没有模板
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Hawk.put("isreuse", "3");
                }


            }
        } else if (view == join_invite_codeview) {

//            Log.d("codeEditView", "//输入内容改变后的回调====" + content);
//            if (content.length() <= 0) {
//                Toast.makeText(getActivity(), "邀请码不能为空", Toast.LENGTH_LONG);
//                return;
//            } else if (content.length() == 4) {
//                if (UDPBroadcastManager.getInstance().MythdReceive.receiveMsg.length() <= 0) {
//                    tipsTxt.setText("暂无该会议,请检查!");
//                    tipsTxt.setVisibility(View.VISIBLE);
////                    Toast.makeText(LoginActivity.this,"暂无该会议,请检查!",Toast.LENGTH_LONG);
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
//                    tipsTxt.setText("未查询到此邀请码!");
//                    tipsTxt.setVisibility(View.VISIBLE);
//                }
//
//            } else {
//                tipsTxt.setVisibility(View.INVISIBLE);
//            }
        }
    }

    //展示历史会议historyConferenceDialog
    public void showHistoryDialog() {


        //自定义dialog显示布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.history_meeting_dialog, null);
        //自定义dialog显示风格
        historyConferenceDialog = new MyDialog(getActivity(), R.style.dialogTransparent);
        historyConferenceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        historyConferenceDialog.setCanceledOnTouchOutside(true);
        //将布局设置给Dialog
        historyConferenceDialog.setContentView(inflate);
        historyConferenceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        historyConferenceDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        historyConferenceDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
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
        //获取当前Activity所在的窗体
        Window window = historyConferenceDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.46);//设置宽
        wlp.height = (int) (height * 0.7);//设置宽
        window.setAttributes(wlp);
        calender_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String data = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA7);//会议-年
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
                lp.width = (int) (display.getWidth() * 0.8); //设置宽度
                calenderDialog.getWindow().setAttributes(lp);
                DatePicker datePicker = viewDatePicker.findViewById(R.id.date_pickerdialog);
                // resizePicker(datePicker);
                Window window = calenderDialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.alpha = 1.0f;
                Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
                wlp.gravity = Gravity.CENTER;
                wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Point size = new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;
                wlp.width = (int) (width * 0.6);//设置宽
                wlp.height = (int) (height * 0.54);//设置宽
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

    //根据日期查询当天会议列表
    private void getMeetingList(String time) {
        NetWorkManager.getInstance().getNetWorkApiService().meetingList("", time).compose(this.<BasicResponse<List<WuHuMeetingListResponse>>>bindToLifecycle())
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

    //初始化会议记录界面
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

    @Override
    public void onItem(int position, WuHuMeetingListResponse wuHuMeetingListResponse) {
        if (!FLUtil.netIsConnect(getActivity())) {
            ToastUtils.showToast(getActivity(), "网络不可用，请检查网络!");
            return;
        }
        // 有在接收到广播消息
        if (UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.size() > 0 && !UDPBroadcastManager.getInstance().MythdReceive.pause) {

            for (String msg : UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr) {
                List<String> ipcodeInfo = FLUtil.dealUDPMsg(msg);
                // 里面包含 ip code/1
                String[] code_idnex = ipcodeInfo.get(1).split("/");
                stringList.add(code_idnex[0]);
            }
            UserUtil.isNetDATA = true;
            UserUtil.isTempMeeting=true;
            UserUtil.ISCHAIRMAN = true;
            constant.temp_code = "formalmeeting."+wuHuMeetingListResponse.getName();
            Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
            Intent intent = new Intent(getActivity(), WuHuActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("code", "formalmeeting."+wuHuMeetingListResponse.getName());
            bundle.putString("isreuse", "4");//1:代表复用模板  2：代表不复用模板 3：代表没有模板
            intent.putExtras(bundle);
            Hawk.put("WuHuMeetingListResponse", wuHuMeetingListResponse);
            getActivity().startActivity(intent);

        }else {
            UserUtil.isTempMeeting=true;
            UserUtil.ISCHAIRMAN = true;
            UserUtil.isNetDATA = true;
            constant.temp_code = "formalmeeting-"+wuHuMeetingListResponse.getName();
            Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
            Intent intent = new Intent(getActivity(), WuHuActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("code", "formalmeeting-"+wuHuMeetingListResponse.getName());
            bundle.putString("isreuse", "4");//1:代表复用模板  2：代表不复用模板 3：代表没有模板
            intent.putExtras(bundle);
            Hawk.put("WuHuMeetingListResponse", wuHuMeetingListResponse);
            getActivity().startActivity(intent);

        }



       //====



    }
    /**
     * websocket发送数据至其他设备
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
        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
        calenderDialog.getWindow().setAttributes(lp);


    }
    /*调整FrameLayout的大小*/

    private void resizePicker(FrameLayout tp) {        //DatePicker和TimePicker继承自FrameLayout
        List<NumberPicker> npList = findNumberPicker(tp);  //找到组成的NumberPicker
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);      //调整每个NumberPicker的宽度
        }
    }

    /**
     * 得到viewGroup 里面的numberpicker组件
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
     * 调整numberpicker大小
     */
    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(880, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 5, 10, 5);
        np.setLayoutParams(params);
    }


    //展示历史会议
    public void showFileTransferDialog() {
        //自定义dialog显示布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.wuhu_file_progress_dialog, null);
        //自定义dialog显示风格
        historyConferenceDialog = new MyDialog(getActivity(), R.style.dialogTransparent);
        historyConferenceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        historyConferenceDialog.setCanceledOnTouchOutside(false);
        //将布局设置给Dialog
        historyConferenceDialog.setContentView(inflate);
        historyConferenceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        historyConferenceDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        historyConferenceDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
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

        //获取当前Activity所在的窗体
        Window window = historyConferenceDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.45);//设置宽
        wlp.height = (int) (width * 0.3);//设置宽
        window.setAttributes(wlp);
        historyConferenceDialog.setOnTouchOutside(new MyDialog.onTouchOutsideInterFace() {
            @Override
            public void outSide() {
                Log.d("sdfsdfdsff", "路过~~~~~");
                //  Toast.makeText(getActivity(),"弹框",Toast.LENGTH_SHORT).show();
            }
        });
        historyConferenceDialog.show();
    }

    public void showRightDialog(String codeStr) {
        //自定义dialog显示布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_reuse_wuhu_meeting, null);
        //自定义dialog显示风格
        Dialog dialog = new Dialog(getActivity(), R.style.dialogTransparent);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        dialog.setCanceledOnTouchOutside(false);
        //将布局设置给Dialog
        dialog.setContentView(inflate);

        TextView confir_tx = inflate.findViewById(R.id.confir_tx);
        TextView cancle_tv = inflate.findViewById(R.id.cancle_tv);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
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
                constant.temp_code = codeStr;
                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                Intent intent = new Intent(getActivity(), WuHuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("code", codeStr);
                bundle.putString("isreuse", "1");//1:代表复用模板  2：代表不复用模板 3：代表没有模板
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
                constant.temp_code = codeStr;
                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                Intent intent = new Intent(getActivity(), WuHuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("code", codeStr);
                bundle.putString("isreuse", "2");//1:代表复用模板  2：代表不复用模板 3：代表没有模板
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
        //获取当前Activity所在的窗体
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.45);//设置宽
        wlp.height = (int) (height * 0.23);
        ;//设置宽
        window.setAttributes(wlp);
        dialog.show();
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

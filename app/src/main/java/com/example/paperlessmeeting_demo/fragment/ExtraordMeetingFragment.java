package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.MainActivity;
import com.example.paperlessmeeting_demo.activity.WuHuActivity;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.jyn.vcview.VerificationCodeView;
import com.orhanobut.hawk.Hawk;
import com.snow.common.tool.utils.FastClickUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 梅涛 on 2021/5/10.
 * 临时会议
 */
@SuppressLint("ValidFragment")
public class ExtraordMeetingFragment extends BaseFragment implements VerificationCodeView.OnCodeFinishListener {
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

    private int currentSelIndex = 0;

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
                joinMeetingDialog();
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

        for (String msg : UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr){
            List<String> ipcodeInfo = FLUtil.dealUDPMsg(msg);
            stringList.add(ipcodeInfo.get(1));
        }
//        ipcodeInfo = FLUtil.dealUDPMsg(UDPBroadcastManager.getInstance().MythdReceive.receiveMsg);
//        stringList.addAll(ipcodeInfo);
        if(stringList.size() == 0){
            stringList.add("暂无邀请码");
        }else {
            if(stringList.contains("暂无邀请码")){
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
                if(stringList.contains(parent.getAdapter().getItem(position).toString())){
                    UserUtil.isTempMeeting = true;
                    Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);
                    entry_meeting.setVisibility(View.VISIBLE);
                    tipsTxt.setVisibility(View.GONE);

                    currentSelIndex = position;
                }else {
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
                if(FastClickUtils.init().isClickFast()){
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
                if(UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.size() >= currentSelIndex+1){
                    List<String> ipcodeInfo = FLUtil.dealUDPMsg(UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.get(currentSelIndex));
                    Intent intent1 = new Intent(getActivity(), WuHuActivity.class);
                    intent1.putExtra("ip", ipcodeInfo.get(0));
//                        intent1.putExtra("code",ipcodeInfo.get(1));
                    startActivity(intent1);

                }else {
                    ToastUtil.makeText(getActivity(),"请重新进入!");

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
            if(!FLUtil.netIsConnect(getActivity())){
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                ToastUtils.showToast(getActivity(),"网络不可用，请检查网络!");
                return;
            }

            // 有在接收到广播消息
            if (UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.size() > 0 && !UDPBroadcastManager.getInstance().MythdReceive.pause) {

                for (String msg : UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr){
                    List<String> ipcodeInfo = FLUtil.dealUDPMsg(msg);

                    stringList.add(ipcodeInfo.get(1));
                }
//

                // 要创建会议，但是当前有相同的邀请码的server已创建
                if (stringList.contains(content)) {
                    int index = stringList.indexOf(content);
                    initMeetingDialog.dismiss();
                    CVIPaperDialogUtils.showCustomDialog(getActivity(), "已存在该邀请码对应的临时会议，是否加入?", null, "加入会议", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                // 加入临时会议，而不是创建
                                UserUtil.isTempMeeting = true;
                                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageClient);

                                Intent intent1 = new Intent(getActivity(), WuHuActivity.class);
                                if(index>=UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.size()){
                                    ToastUtils.showShort("加入不了会议!");
                                    return;
                                }
                                List<String> ipcodeInfo = FLUtil.dealUDPMsg(UDPBroadcastManager.getInstance().MythdReceive.receiveIPArr.get(index));
                                intent1.putExtra("ip", ipcodeInfo.get(0));
                                startActivity(intent1);

                                return;
                            }
                        }
                    });
                    return;
                } else {
                    initMeetingDialog.dismiss();
                    CVIPaperDialogUtils.showCustomDialog(getActivity(), "存在其他已经创建的临时会议", "是否仍然创建?", "继续创建", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                initMeetingDialog.dismiss();
                                UserUtil.isTempMeeting = true;
                                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                                Intent intent = new Intent(getActivity(), WuHuActivity.class);
                                intent.putExtra("code", content);
                                startActivity(intent);

                            }
                        }
                    });

                }
            } else {
                initMeetingDialog.dismiss();
                UserUtil.isTempMeeting = true;
                Hawk.put(constant.TEMPMEETING, MessageReceiveType.MessageServer);
                Intent intent = new Intent(getActivity(), WuHuActivity.class);
                intent.putExtra("code", content);
                startActivity(intent);

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

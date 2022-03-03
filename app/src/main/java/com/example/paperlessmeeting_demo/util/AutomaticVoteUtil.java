package com.example.paperlessmeeting_demo.util;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.ChoseBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.dialog.CheckBoxDialog;
import com.example.paperlessmeeting_demo.dialog.RadioDialog;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.constant;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.example.paperlessmeeting_demo.bean.VoteListBean.VoteBean.UserListBean;
import java.util.ArrayList;
import java.util.List;

/**
 *  投票弹框
 * */
public class AutomaticVoteUtil {

    public static void voteAlert(VoteListBean.VoteBean model,String  flag){
        // 先手动设置一下状态
        model.setStatus(model.getStatus());


        int status = model.getMvoteStatus();
        //  投票
        if (status == Constants.VoteStatusEnum.hasStartUnVote) {
            if (model.getType().equals("0")) {
                showRadioDialog(model,flag);
            } else {
                showCheckBoxDialog(model,flag);
            }
        }
    }

    //  单选框
    private static void showRadioDialog(VoteListBean.VoteBean model,String flag) {
        Activity topActivity = (Activity) ActivityUtils.getTopActivity();
        if(topActivity == null){
            return;
        }
        final List<ChoseBean> list = new ArrayList<>();
        for (int i = 0; i < model.getOptions().size(); i++) {
            ChoseBean object = new ChoseBean();
            object.setContent(model.getOptions().get(i));
            object.setChecked(false);
            list.add(object);
        }
        topActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RadioDialog dialog = new RadioDialog(topActivity, R.style.AlertDialogStyle, list,flag);
                topActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });

                dialog.setTitle(model.getTopic());
                dialog.setEndTime("投票截止时间:" + model.getEnd_time());
                dialog.setCreator("发起人：" + model.getFrom().getName());
                dialog.setNegBtn("弃权", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                if (model.getAnonymity().equals("1")) {
                    dialog.setNiming("实名");
                } else {
                    dialog.setNiming("匿名");
                }
                dialog.setPosBtn("投票", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> chose = new ArrayList<>();
                        ArrayList<VoteListBean.VoteBean.UserListBean> userbeanList = new ArrayList<>(model.getUser_list());

                        for (int i = 0; i < list.size(); i++) {
                            ChoseBean object = list.get(i);
                            if (object.isChecked()) {
                                chose.add(object.getContent());
                            }
                        }
                        VoteListBean.VoteBean.UserListBean userListBean = new VoteListBean.VoteBean.UserListBean();
                        userListBean.setStatus("FINISH");
                        userListBean.setUser_name(Hawk.get(constant.myNumber));
                        userListBean.setUser_id(FLUtil.getMacAddress());
                        userListBean.setMeeting_vote_id(model.get_id());
                        userListBean.setChoose(chose);
                        requestToVote(userListBean,flag);
                    }
                });
                dialog.setOnItemClickEvent(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }
        });
    }

    //  复选框
    private static void showCheckBoxDialog(VoteListBean.VoteBean model,String flag) {
        Activity topActivity = (Activity) ActivityUtils.getTopActivity();
        if(topActivity == null){
            return;
        }
        final List<ChoseBean> list = new ArrayList<>();
        for (int i = 0; i < model.getOptions().size(); i++) {
            ChoseBean object = new ChoseBean();
            object.setContent(model.getOptions().get(i));
            object.setChecked(false);
            list.add(object);
        }
        topActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CheckBoxDialog dialog = new CheckBoxDialog(topActivity, R.style.AlertDialogStyle, list,flag);
                dialog.show();
                dialog.setTitle(model.getTopic());
                dialog.setEndTime("投票截止时间:" + model.getEnd_time());
                dialog.setCreator("发起人：" + model.getFrom().getName());
                dialog.setNegBtn("弃权", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                if (model.getAnonymity().equals("1")) {
                    dialog.setNiming("实名");
                } else {
                    dialog.setNiming("匿名");
                }
                dialog.setPosBtn("投票", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> chose = new ArrayList<>();
                        ArrayList<VoteListBean.VoteBean.UserListBean> userbeanList = new ArrayList<>(model.getUser_list());

                        for (int i = 0; i < list.size(); i++) {
                            ChoseBean object = list.get(i);
                            if (object.isChecked()) {
                                chose.add(object.getContent());
                            }
                        }

                        UserListBean userListBean = new UserListBean();
                        userListBean.setUser_name(Hawk.get(constant.myNumber));
                        userListBean.setStatus("FINISH");
                        userListBean.setUser_id(FLUtil.getMacAddress());
                        userListBean.setMeeting_vote_id(model.get_id());
                        userListBean.setChoose(chose);
                        requestToVote(userListBean,flag);
                    }
                });
                dialog.setOnItemClickEvent(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }
        });

    }


    /**
     * 更新投票操作(单、复选投票)请求
     */
    private static void requestToVote(VoteListBean.VoteBean.UserListBean bean,String flag) {
        TempWSBean tempWSBean = new TempWSBean();
        tempWSBean.setReqType(0);
        tempWSBean.setFlag(flag);
        tempWSBean.setUserMac_id(FLUtil.getMacAddress());
        tempWSBean.setPackType(constant.UPDATEVOTE);
        tempWSBean.setBody(bean);
        String strJson = new Gson().toJson(tempWSBean);
        JWebSocketClientService.sendMsg(strJson);
    }
}

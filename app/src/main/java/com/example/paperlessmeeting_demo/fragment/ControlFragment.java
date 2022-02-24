package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.util.List;

/**
 * Created by 梅涛 on 2020/9/22.
 */
@SuppressLint("ValidFragment")
public class ControlFragment   extends BaseFragment {
    private String titles;
    private Context context;

    public ControlFragment() {
    }

    public ControlFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_control;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
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

    }

    @Override
    protected void initView() {

    }
}

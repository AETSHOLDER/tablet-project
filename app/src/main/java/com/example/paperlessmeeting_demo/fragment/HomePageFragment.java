package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.MainActivity;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 梅涛 on 2020/9/19.
 * 登录方式——扫码进入
 */
@SuppressLint("ValidFragment")
public class HomePageFragment extends BaseFragment {
    @BindView(R.id.sweep_code)
    ImageView sweepCode;
    Unbinder unbinder;
    private String titles;
    private Context context;

    public HomePageFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public HomePageFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sweep_code;
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
        sweepCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(MainActivity.class);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
            View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
     /*   if (unbinder!=null){
            unbinder.unbind();
        }*/
    }
}

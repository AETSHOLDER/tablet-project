package com.example.paperlessmeeting_demo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.fragment.WuHUVoteListFragment;
import com.example.paperlessmeeting_demo.fragment.WuHuFragment;
import com.example.paperlessmeeting_demo.tool.constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WuHuVoteActivity extends BaseActivity {
    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;
    private MyBroadcastReceiver myBroadcastReceiver;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.FINISH_WUHUVOTEACTIVITY_BROADCAST);
        registerReceiver(myBroadcastReceiver, filter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wuhu_vote;
    }

    @Override
    protected void initView() {

        getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.fragment_container,  WuHuFragment.newInstance(0+""))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();
       /* getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.fragment_container, new WuHUVoteListFragment("投票", WuHuVoteActivity.this))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();*/
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBroadcastReceiver!=null){
            unregisterReceiver(myBroadcastReceiver);
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent in) {
            finish();
        }
    }
}
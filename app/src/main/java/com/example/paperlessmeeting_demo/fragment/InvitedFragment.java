package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.MainActivity;
import com.example.paperlessmeeting_demo.activity.WuHuActivity;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 梅涛 on 2020/9/19.
 * <p>
 * 登录方式——邀请码
 */
@SuppressLint("ValidFragment")
public class InvitedFragment extends BaseFragment {
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.meeting_time)
    TextView meetingTime;
    @BindView(R.id.entry_meet)
    TextView entryMeet;
    private String titles;
    private Context context;

    public InvitedFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }
    public static InvitedFragment newInstance(String movie){
        InvitedFragment movieFragment = new InvitedFragment();
        return movieFragment;
    }
    public InvitedFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite;
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
        entryMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WuHuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      /*  if (unbinder!=null){
            unbinder.unbind();
        }*/
    }
}

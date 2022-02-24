package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.LoginActivity;


import com.example.paperlessmeeting_demo.activity.MainActivity;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.LoginBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 梅涛 on 2020/9/19.
 * 登录方式——扫码进入
 */
@SuppressLint("ValidFragment")
public class SweepCodeFragment extends BaseFragment implements LoginActivity.LoginToFragmentListener {
    @BindView(R.id.sweep_code)
    ImageView sweepCode;
//    @BindView(R.id.have_meeting)
//    LinearLayout have_meeting;


    private LinearLayout have_meeting;
    private LinearLayout no_meeting;

    private String titles;
    private Context context;
    final static int COUNTS = 8;//点击次数
    final static long DURATION = 3 * 1000;//规定有效时间
    long[] mHits = new long[COUNTS];

    public SweepCodeFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public static SweepCodeFragment newInstance(String movie) {
        SweepCodeFragment movieFragment = new SweepCodeFragment();
        return movieFragment;
    }

    public SweepCodeFragment() {
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
        ImageLoader.getInstance().displayImage("assets://login_sweep_code.png", sweepCode);
        sweepCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  loginByUserId();

                //  dialog();
                loginByUserId();
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续8次点击
//                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
//                if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
//                    EventMessage msg = new EventMessage(MessageReceiveType.MessageClick, constant.continusClick);
//                    EventBus.getDefault().post(msg);
//                }
            }
        });
    }

    /*
     *  通过userId登录
     * */
    private void loginByUserId() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", UserUtil.user_id);
        NetWorkManager.getInstance().getNetWorkApiService().loginByUserId(map).compose(this.<BasicResponse<LoginBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<LoginBean>>() {
                    @Override
                    protected void onSuccess(BasicResponse<LoginBean> response) {
                        Log.d("loginByUserId sucess", response.getData().toString() + "======");
                        LoginBean loginbean = response.getData();
                        if (response != null) {
                            Hawk.put(constant.token, loginbean.getToken());
                            Hawk.put(constant._id, UserUtil.meeting_record_id);
                            Hawk.put(constant.c_id, loginbean.getUser().getC_id().getId());
                            Hawk.put(constant.user_id, loginbean.getUser().get_id());
                            Hawk.put(constant.role, loginbean.getUser().getRole());
                            //  Intent intent = new Intent(getActivity(), MainActivity.class);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);

                        }
                    }
                });
    }

    /**
     * 收到用户信息通知
     */
    @Override
    public void onUserInfoReceive() {
        if(no_meeting != null && have_meeting!=null){
            no_meeting.setVisibility(View.GONE);
            have_meeting.setVisibility(View.VISIBLE);
        }else {
            no_meeting = getActivity().findViewById(R.id.no_meeting);
            have_meeting = getActivity().findViewById(R.id.have_meeting);

        }
    }

    @Override
    public void onHideUserInfo() {
        if(no_meeting != null && have_meeting!=null){
            no_meeting.setVisibility(View.VISIBLE);
            have_meeting.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        no_meeting.setVisibility(View.VISIBLE);
        have_meeting.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        have_meeting = (LinearLayout) rootView.findViewById(R.id.have_meeting);
        no_meeting = (LinearLayout) rootView.findViewById(R.id.no_meeting);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if (unbinder!=null){
            unbinder.unbind();
        }*/
    }
}

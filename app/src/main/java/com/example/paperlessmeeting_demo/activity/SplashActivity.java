package com.example.paperlessmeeting_demo.activity;

import android.os.Handler;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.orhanobut.hawk.Hawk;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private Handler handler = new Handler();
    private Runnable runnableToLogin = new Runnable() {
        @Override
        public void run() {
            toLoginActivity();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        /*
        * 创建用户行为日志类,存在就初始化
        * */
        if (Hawk.contains("UserBehaviorBean")) {
            Hawk.delete("UserBehaviorBean");
        }

        UserBehaviorBean userBehaviorBean = new UserBehaviorBean();
        userBehaviorBean.setUserId(UserUtil.user_id);
        userBehaviorBean.setMeetingId(UserUtil.meeting_record_id);
        List<UserBehaviorBean.DataBean> dataBeanList = new ArrayList<>();
        userBehaviorBean.setData(dataBeanList);
        Hawk.put("UserBehaviorBean", userBehaviorBean);

        //延迟2秒
        handler.postDelayed(runnableToLogin, 2000);
    }

    /**
     * 跳转到登录界面
     */
    private void toLoginActivity() {
        openActivity(LoginActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄漏
        handler.removeCallbacks(runnableToLogin);
    }
}

package com.example.paperlessmeeting_demo.activity;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.PermissionManager;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.orhanobut.hawk.Hawk;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

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
        checkWritePermission1();
        UserBehaviorBean userBehaviorBean = new UserBehaviorBean();
        userBehaviorBean.setUserId(UserUtil.user_id);
        userBehaviorBean.setMeetingId(UserUtil.meeting_record_id);
        List<UserBehaviorBean.DataBean> dataBeanList = new ArrayList<>();
        userBehaviorBean.setData(dataBeanList);
        Hawk.put("UserBehaviorBean", userBehaviorBean);

        if (PermissionManager.checkPermission(this, constant.PERMS_WRITE1)) {
            //延迟2秒
            handler.postDelayed(runnableToLogin, 2000);
        }

    }

    /**
     * 跳转到登录界面
     */
    private void toLoginActivity() {
        openActivity(LoginActivity.class);
        finish();
    }

    /**
     * 检查读写权限权限
     */
    @AfterPermissionGranted(constant.WRITE_PERMISSION_CODE1)
    private void checkWritePermission1() {
        if (!PermissionManager.checkPermission(this, constant.PERMS_WRITE1)) {
            PermissionManager.requestPermission(this, constant.WRITE_PERMISSION_TIP, constant.WRITE_PERMISSION_CODE1, constant.PERMS_WRITE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 请求权限成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(SplashActivity.this, "授权成功", Toast.LENGTH_LONG).show();
        toLoginActivity();
        initX5();
        //MeetingAPP.getInstance().initX5();
    }

    private void initX5() {
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("MeetingAPP", " x5内核加载" + (arg0 ? "成功!":"失败!"));
                Hawk.put("TBS",arg0);
            }

            @Override
            public void onCoreInitFinished() {
                Log.d("MeetingAPP22", " onViewInitFinished is " + "路过~~~~~");
            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("MeetingAPP33", " onViewInitFinished is " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("MeetingAPP444", " onViewInitFinished is " + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("MeetingAPP555", " onViewInitFinished is " + i);
            }
        });

        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
    /**
     * 请求权限失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(SplashActivity.this, "用户授权失败", Toast.LENGTH_LONG).show();
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄漏
        handler.removeCallbacks(runnableToLogin);
    }
}

package com.example.paperlessmeeting_demo.activity.Initialization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.MainActivity;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.InitiaBean.InitiaRoomBean;
import com.example.paperlessmeeting_demo.bean.LoginBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.socket.InitSocketManager;
import com.example.paperlessmeeting_demo.tool.FLLog;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.fingerth.supdialogutils.SYSDiaLogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.helloworld.library.MiddleDialogConfig;
import com.helloworld.library.utils.DialogEnum;
import com.orhanobut.hawk.Hawk;
import com.snow.common.tool.utils.FastClickUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.Ack;

import static com.fingerth.supdialogutils.SYSDiaLogUtils.handler;

public class InitiaActivity extends BaseActivity {
    private InetAddress address;
    private Integer port = 12345;
    @BindView(R.id.dianji)
    TextView dianji;

    private String[] infoArr = {"正在查找服务器...", "正在连接服务器...", "正在查找会议室列表...", "正在注册..."};
    private String TAG = "InitiaActivity";
    private Handler mHandler;
    private static String MulticastHost = "224.1.1.2";
    private static int PORT = 15000;
    private boolean isfirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "mac===" + FLUtil.getMacAddress());
    }


    @Override
    protected void initView() {
        dianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtils.init().isClickFast()) {
                    return;
                }
                fillMacNo();
            }
        });

    }



    @Override
    protected void initData() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_initia;
    }

    private void registerPPL(String name,String mac,boolean isChairMan) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("mac", mac);
        map.put("type", isChairMan ? "1":"0");

        NetWorkManager.getInstance().getNetWorkApiService().pplRegister(map).compose(InitiaActivity.this.<BasicResponse<String>>bindToLifecycle())

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<String>>() {
                    @Override
                    protected void onSuccess(BasicResponse<String> response) {
                        Log.d("注册成功 sucess", "");
                        if (response.getCode()==1) {
                            SYSDiaLogUtils.dismissProgress();
                            SYSDiaLogUtils.showSuccessDialog(InitiaActivity.this, "", "注册成功！", "确定", false);
                            SYSDiaLogUtils.dismissProgress();
                            finish();
                        }else {
                            SYSDiaLogUtils.dismissProgress();
                            SYSDiaLogUtils.showErrorDialog(InitiaActivity.this, "注册失败！", response.getMsg(), "确定", false);
                        }
                    }
                });
    }




    /**
     *  step5:填写本机机号
     * */
    private void fillMacNo() {
        new MiddleDialogConfig().builder(InitiaActivity.this)
                .setDialogStyle(DialogEnum.EDIT)
                .setTitle("请填写pad使用者姓名")
                .setEditHint("")
                .setLeftVis(false)
                .setRight("确定")
                .setRightCallBack(new MiddleDialogConfig.RightCallBack() {
                    @Override
                    public void rightBtn(String cont) {

                        Log.d(TAG, "cont===" + cont);

                        withMic( cont);
                        Hawk.put(constant.user_name, cont);
                        UserUtil.user_name = cont;
                    }
                }).show();
    }

    /**
     *  step6:是否是主席
     * */
    private void withMic( String name) {
        new MiddleDialogConfig().builder(InitiaActivity.this)
                .setDialogStyle(DialogEnum.BASIC)
                .setTitle("是否是主席?")
                .setContent("")
                .setLeft("是的")
                .setRight("否")
                .setLeftCallBack(new MiddleDialogConfig.LeftCallBack() {
                    @Override
                    public void leftBtn(String cont) {
                        registerPPL(name,FLUtil.getMacAddress(),true);
                    }
                })
                .setRightCallBack(new MiddleDialogConfig.RightCallBack() {
                    @Override
                    public void rightBtn(String cont) {
                        /**
                         *  step6:完成注册
                         * */
                        registerPPL(name,FLUtil.getMacAddress(),false);
                    }
                }).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}

package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.MainActivity;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.LoginBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 梅涛 on 2020/9/19.
 * 登录方式--手写
 */
@SuppressLint("ValidFragment")
public class WhiteboardFragment extends BaseFragment {
    /*   @BindView(R.id.signature_pad)
       SignaturePad signaturePad;*/
    Unbinder unbinder;
    @BindView(R.id.entry_meet)
    TextView entryMeet;
    private String titles;
    private Context context;

    public WhiteboardFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public static WhiteboardFragment newInstance(String movie) {
        WhiteboardFragment movieFragment = new WhiteboardFragment();
        return movieFragment;
    }

    public WhiteboardFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_white_board;
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
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
                loginByUserId();
            }
        });
       /* signaturePad.getBackground().setAlpha(175);
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });*/
    }

    @Override
    protected void initView() {

    }

    /*
        *  通过userId登录，临时使用
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
                            Hawk.put("token", loginbean.getToken());
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
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

    }
}

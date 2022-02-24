package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.ActivityAboutSoft;
import com.example.paperlessmeeting_demo.adapter.GridDailtsAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.HomeGridBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.CacheUtil;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 梅涛 on 2020/9/19.
 * 登录方式——扫码进入
 */
@SuppressLint("ValidFragment")
public class SetingFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.file_fragment)
    FrameLayout fileFragment;
    private String titles;
    private Context context;
    private GridDailtsAdapter gridDailtsAdapter;
    private List<HomeGridBean> homeGridBeanList = new ArrayList<>();
    private AlertDialog results;

    public SetingFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public SetingFragment() {
    }

    @Override

    protected int getLayoutId() {
        return R.layout.fragment_setting;
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
        HomeGridBean homeGridBean1 = new HomeGridBean(R.mipmap.ic_common_problem, "常见问题", 1);
        HomeGridBean homeGridBean2 = new HomeGridBean(R.mipmap.ic_about_software, "关于软件", 2);
        HomeGridBean homeGridBean3 = new HomeGridBean(R.mipmap.ic_clear, "清理缓存", 3);
     //   HomeGridBean homeGridBean4 = new HomeGridBean(R.mipmap.ic_exit, "退出软件", 4);
        homeGridBeanList.add(homeGridBean1);
        homeGridBeanList.add(homeGridBean2);
        homeGridBeanList.add(homeGridBean3);
       // homeGridBeanList.add(homeGridBean4);
        gridDailtsAdapter = new GridDailtsAdapter(getActivity(), homeGridBeanList);
        gridview.setAdapter(gridDailtsAdapter);
        gridDailtsAdapter.notifyDataSetChanged();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeGridBean homeGridBean = (HomeGridBean) adapterView.getAdapter().getItem(i);
                if ("常见问题".equals(homeGridBean.getName())) {
                    showResualtDialog("正在整理用户的反馈");

                } else if ("关于软件".equals(homeGridBean.getName())) {
                    Intent intent = new Intent(getActivity(), ActivityAboutSoft.class);
                    startActivity(intent);

                } else if ("清理缓存".equals(homeGridBean.getName())) {
                    showDialog("确定要清除缓存吗？", "0");
                    // Toast.makeText(getActivity(), "展会期间不允许退出", Toast.LENGTH_LONG).show();

                } else if ("退出软件".equals(homeGridBean.getName())) {
                    showDialog("确定要退出软件吗？", "1");
                   /* Toast.makeText(getActivity(), "展会期间不允许退出", Toast.LENGTH_LONG).show();*/
                }
            }
        });
    }

    private void showResualtDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.InfoDialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_results, null);
        TextView aa = view.findViewById(R.id.aa);
        aa.setText(str);
        builder.setView(view);
        builder.setCancelable(true);
        results = builder.create();
        results.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                results.dismiss();
            }
        }, 1500);
    }

    private void showDialog(String str, String flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.InfoDialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_clear_cache, null);
        TextView tip = view.findViewById(R.id.tip);
        tip.setText(str);
        Button confir_btn = view.findViewById(R.id.confir_btn);
        Button cancel_btn = view.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.dismiss();
            }
        });

        confir_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(flag)) {
                    CacheUtil.clearAllCache(getActivity());
                    Toast.makeText(getActivity(), "清理完毕！", Toast.LENGTH_SHORT).show();
                    try {
                        Log.d("fsdg", CacheUtil.getTotalCacheSize(getActivity()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.exit(0);
                   // killAppProcess();
                }
                results.dismiss();
            }

        });
        builder.setView(view);
        builder.setCancelable(false);
        results = builder.create();
        results.show();

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = results.getWindow().getAttributes();
        lp.width = (int) (display.getHeight() * 0.8); //设置宽度
        results.getWindow().setAttributes(lp);
    }

    public void killAppProcess() {
        //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
        ActivityManager mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
            if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @Override
    protected void initView() {

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

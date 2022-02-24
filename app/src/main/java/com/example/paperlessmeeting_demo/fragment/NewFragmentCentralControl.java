package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.GridCentralControlAdapter;
import com.example.paperlessmeeting_demo.adapter.GridCentralControlItemAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.AgreementBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.DeviceListBean;
import com.example.paperlessmeeting_demo.bean.DeviceTypeBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.widgets.MyGridView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 梅涛 on 2020/10/9.
 */
@SuppressLint("ValidFragment")
public class NewFragmentCentralControl extends BaseFragment {
    @BindView(R.id.gridview)
    MyGridView gridview;
    private String titles;
    private Context context;
    private GridCentralControlAdapter gridCentralControlAdapter;
    //设备类型
    private List<DeviceTypeBean.DataBean> deviceTypeBeanList = new ArrayList<>();
    //设备列表
    private List<DeviceListBean.DataBean> deviceList = new ArrayList<>();
    private List<AgreementBean.DataBean> agreementList = new ArrayList<>();
    private GridCentralControlItemAdapter gridCentralControlItemAdapter;
    private AlertDialog deviceDialog;
    private RelativeLayout progressBar_ll;
    //uiHandler在主线程中创建，所以自动绑定主线程
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressBar_ll.setVisibility(View.GONE);
                    gridCentralControlItemAdapter.setGridViewBeanList(deviceList);
                    gridCentralControlItemAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public NewFragmentCentralControl() {
    }

    public NewFragmentCentralControl(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_central_control_new;
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
        String _id = Hawk.contains(constant.c_id) ? Hawk.get(constant.c_id) : "";
        //绑定生命周期
        NetWorkManager.getInstance().getNetWorkApiService().getFindCceTypeList(_id).compose(this.<BasicResponse<List<DeviceTypeBean.DataBean>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<DeviceTypeBean.DataBean>>>() {
                    @Override
                    protected void onSuccess(BasicResponse<List<DeviceTypeBean.DataBean>> response) {
                        if (response != null) {
                            deviceTypeBeanList = response.getData();
                            gridCentralControlAdapter.setGridViewBeanList(deviceTypeBeanList);
                            gridCentralControlAdapter.notifyDataSetChanged();
                        }
                    }
                });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("fsgfgg", "rgsgsyssy");
                DeviceTypeBean.DataBean dataBean = (DeviceTypeBean.DataBean) parent.getAdapter().getItem(position);
                showDialog(dataBean.getName());
                progressBar_ll.setVisibility(View.VISIBLE);
                getDeviceListData(dataBean.getC_id(), dataBean.get_id(), dataBean.getIcon());
            }
        });
    }


    //获取设备列表
    private void getDeviceListData(String c_id, String deviceId, String icon) {

        NetWorkManager.getInstance().getNetWorkApiService().getFindCceList(c_id, deviceId).compose(this.<BasicResponse<List<DeviceListBean.DataBean>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<DeviceListBean.DataBean>>>() {
                    @Override
                    protected void onSuccess(BasicResponse<List<DeviceListBean.DataBean>> response) {
                        if (response != null) {
                            deviceList = response.getData();
                            /*
                            * 给每个设备加上图标标识
                            * */
                            for (int i = 0; i < deviceList.size(); i++) {
                                deviceList.get(i).setDeviceIcon(icon);
                            }
                            getCirculatData(deviceList);
                        }
                    }
                });
    }

    //通过循环获取每个设备类型的协议列表
    private void getCirculatData(List<DeviceListBean.DataBean> deviceList) {
        for (int i = 0; i < deviceList.size(); i++) {
            getDeviceListAgreementData(deviceList.get(i).getC_id(), deviceList.get(i).get_id(), deviceList.get(i));
        }

    }

    //获取设备-协议-列表
    private void getDeviceListAgreementData(String c_id, String cce_id, DeviceListBean.DataBean de) {

        NetWorkManager.getInstance().getNetWorkApiService().getFindAgreementList(c_id, cce_id).compose(this.<BasicResponse<List<AgreementBean.DataBean>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<AgreementBean.DataBean>>>() {
                    @Override
                    protected void onSuccess(BasicResponse<List<AgreementBean.DataBean>> response) {
                        if (response != null) {
                            List<AgreementBean.DataBean> agreementBeanList = new ArrayList<>();
                            agreementBeanList = response.getData();
                            de.setAgreementList(agreementBeanList);//将协议列表和设备列表关联起来
                            uiHandler.sendEmptyMessage(1);
                        }
                    }
                });
    }

    private void showDialog(String equipmentName) {
        gridCentralControlItemAdapter = new GridCentralControlItemAdapter(getActivity(), deviceList);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.InfoDialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_device, null);
        MyGridView myGridView = view.findViewById(R.id.gridview);
        TextView equipment_name = view.findViewById(R.id.equipment_name);
        progressBar_ll = view.findViewById(R.id.progressBar_ll);
        RelativeLayout close_rl = view.findViewById(R.id.close_rl);
        equipment_name.setText(equipmentName);
        myGridView.setAdapter(gridCentralControlItemAdapter);
        gridCentralControlItemAdapter.notifyDataSetChanged();
        close_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deviceDialog.dismiss();
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        deviceDialog = builder.create();
        deviceDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = deviceDialog.getWindow().getAttributes();
        lp.width = (int) (display.getHeight() * 0.8); //设置宽度
        lp.width = (int) (display.getWidth() * 0.8);
        deviceDialog.getWindow().setAttributes(lp);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        gridCentralControlAdapter = new GridCentralControlAdapter(getActivity(), deviceTypeBeanList);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void initView() {
        gridview.setAdapter(gridCentralControlAdapter);
        gridCentralControlAdapter.notifyDataSetChanged();

    }
}
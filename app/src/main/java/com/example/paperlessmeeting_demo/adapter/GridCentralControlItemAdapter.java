package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.DeviceListBean;
import com.example.paperlessmeeting_demo.widgets.MyGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/7/23.
 * <p>
 * 中控设备适配器
 */

public class GridCentralControlItemAdapter extends BaseAdapter {
    private Context context;
    private List<DeviceListBean.DataBean> gridViewBeanList;

    public List<DeviceListBean.DataBean> getGridViewBeanList() {
        return gridViewBeanList;
    }

    public void setGridViewBeanList(List<DeviceListBean.DataBean> gridViewBeanList) {
        this.gridViewBeanList = gridViewBeanList;
    }

    public GridCentralControlItemAdapter(Context context, List<DeviceListBean.DataBean> gridViewBeanList) {
        this.context = context;
        this.gridViewBeanList = gridViewBeanList;
    }

    @Override
    public int getCount() {
        return gridViewBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return gridViewBeanList.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viHolder = null;
        DeviceListBean.DataBean gridViewBean = gridViewBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_central_control_new, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }
        viHolder.equipmentName.setText(gridViewBean.getName());
        switch (gridViewBean.getDeviceIcon()) {
            case "airconditioning":
                viHolder.equipmentIma.setImageResource(R.mipmap.airconditioning);
                break;
            case "curtain":
                viHolder.equipmentIma.setImageResource(R.mipmap.curtain);
                break;
            case "light":
                viHolder.equipmentIma.setImageResource(R.mipmap.light);
                break;
            case "other-1":
                viHolder.equipmentIma.setImageResource(R.mipmap.other_1);
                break;
            case "other-2":
                viHolder.equipmentIma.setImageResource(R.mipmap.other_2);
                break;
            case "other-3":
                viHolder.equipmentIma.setImageResource(R.mipmap.other_3);
                break;
            case "other-4":
                viHolder.equipmentIma.setImageResource(R.mipmap.other_4);
                break;
            case "other-5":
                viHolder.equipmentIma.setImageResource(R.mipmap.other_5);
                break;
            case "other-6":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_other_6);
                break;
            case "projectionscreen":
                viHolder.equipmentIma.setImageResource(R.mipmap.projectionscreen);
                break;
            case "projector":
                viHolder.equipmentIma.setImageResource(R.mipmap.projector);
                break;
            case "webcam":
                viHolder.equipmentIma.setImageResource(R.mipmap.webcam);
                break;
        }

        if ( gridViewBean.getAgreementList() != null) {
            AgreementlControlItemAdapter agreementlControlItemAdapter = new AgreementlControlItemAdapter(context, gridViewBean.getAgreementList());//协议宫格
            viHolder.gridview.setAdapter(agreementlControlItemAdapter);
            agreementlControlItemAdapter.notifyDataSetChanged();
        }
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.equipment_ima)
        ImageView equipmentIma;
        @BindView(R.id.equipment_name)
        TextView equipmentName;
        @BindView(R.id.gridview)
        MyGridView gridview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

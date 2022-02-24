package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.DeviceTypeBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/7/23.
 * <p>
 * 中控类型适配器
 */

public class GridCentralControlAdapter extends BaseAdapter {
    private Context context;
    private List<DeviceTypeBean.DataBean> gridViewBeanList;

    public List<DeviceTypeBean.DataBean> getGridViewBeanList() {
        return gridViewBeanList;
    }

    public void setGridViewBeanList(List<DeviceTypeBean.DataBean> gridViewBeanList) {
        this.gridViewBeanList = gridViewBeanList;
    }

    public GridCentralControlAdapter(Context context, List<DeviceTypeBean.DataBean> gridViewBeanList) {
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
        DeviceTypeBean.DataBean gridViewBean = gridViewBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_central_control_device_type, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }
        switch (gridViewBean.getIcon()) {
            case "airconditioning":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_airconditioning);
                break;
            case "curtain":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_curtain);
                break;
            case "light":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_light);
                break;
            case "other-1":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_other_1);
                break;
            case "other-2":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_other_2);
                break;
            case "other-3":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_other_3);
                break;
            case "other-4":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_other_4);
                break;
            case "other-5":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_other_5);
                break;
            case "other-6":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_other_6);
                break;
            case "projectionscreen":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_projectionscreen);
                break;
            case "projector":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_projector);
                break;
            case "webcam":
                viHolder.equipmentIma.setImageResource(R.mipmap.ic_webcam);
                break;
        }

        Log.d("dsadd", gridViewBean.getName());
        viHolder.equipmentName.setText(gridViewBean.getName());

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.equipment_ima)
        ImageView equipmentIma;
        @BindView(R.id.equipment_name)
        TextView equipmentName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

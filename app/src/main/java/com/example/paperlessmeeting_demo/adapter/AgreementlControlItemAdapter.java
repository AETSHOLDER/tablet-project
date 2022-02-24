package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.AgreementBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/7/23.
 * <p>
 * 中控设备协议适配器
 */

public class AgreementlControlItemAdapter extends BaseAdapter {
    private Context context;
    private List<AgreementBean.DataBean> gridViewBeanList;

    public List<AgreementBean.DataBean> getGridViewBeanList() {
        return gridViewBeanList;
    }

    public void setGridViewBeanList(List<AgreementBean.DataBean> gridViewBeanList) {
        this.gridViewBeanList = gridViewBeanList;
    }

    public AgreementlControlItemAdapter(Context context, List<AgreementBean.DataBean> gridViewBeanList) {
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
        AgreementBean.DataBean gridViewBean = gridViewBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_gridle_agreement, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }
        viHolder.agreementName.setText(gridViewBean.getName());
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.agreement_name)
        TextView agreementName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.HomeGridBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/7/23.
 */

public class GridDailtsAdapter extends BaseAdapter {
    private Context context;
    private List<HomeGridBean> gridViewBeanList;

    public GridDailtsAdapter(Context context, List<HomeGridBean> gridViewBeanList) {
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
        HomeGridBean gridViewBean = gridViewBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_home_page_gridview, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }
        viHolder.gridIma.setImageResource(gridViewBean.getRes());
        viHolder.gridTv.setText(gridViewBean.getName());

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.grid_ima)
        ImageView gridIma;
        @BindView(R.id.grid_tv)
        TextView gridTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

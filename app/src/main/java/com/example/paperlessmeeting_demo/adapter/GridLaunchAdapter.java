package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.paperlessmeeting_demo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/7/23.
 * <p>
 * 签批适配器
 */

public class GridLaunchAdapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> gridViewBeanList;

    public List<Bitmap> getGridViewBeanList() {
        return gridViewBeanList;
    }

    public void setGridViewBeanList(List<Bitmap> gridViewBeanList) {
        this.gridViewBeanList = gridViewBeanList;
    }

    public GridLaunchAdapter(Context context, List<Bitmap> gridViewBeanList) {
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
        Bitmap bitmap = gridViewBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_gridle_launch, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }
        viHolder.imaUrl.setImageBitmap(bitmap);
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.ima_url)
        ImageView imaUrl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.widgets.ThreeStateCheckbox;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 梅涛 on 2020/7/23.
 * 参会人员控麦列表适配器
 */

public class AttendeesVolumeAdapter extends BaseAdapter {
    private Context context;
    private List<AttendeBean> gridViewBeanList;
    private static HashMap<Integer, Integer> isSelected;

    public AttendeesVolumeAdapter(Context context, List<AttendeBean> gridViewBeanList) {
        this.context = context;
        this.gridViewBeanList = gridViewBeanList;
        isSelected = new HashMap<Integer, Integer>();
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < gridViewBeanList.size(); i++) {
            getIsSelected().put(i, ThreeStateCheckbox.NORMAL);
        }
    }

    public static HashMap<Integer, Integer> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Integer> isSelected) {
        AttendeesVolumeAdapter.isSelected = isSelected;
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
        AttendeBean attendeBean = gridViewBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate( R.layout.item_user_volume, null);
            viHolder = new ViewHolder();
            viHolder.userPoto = (CircleImageView) view.findViewById(R.id.user_poto);
            viHolder.userName = (TextView) view.findViewById(R.id.user_name);
            viHolder.volumeState = (ThreeStateCheckbox) view.findViewById(R.id.volume_state);
            ImageLoader.getInstance().displayImage(attendeBean.getAvatar(), viHolder.userPoto);
            viHolder.userName.setText(attendeBean.getName());
            viHolder.volumeState.setState(getIsSelected().get(i));

            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(attendeBean.getAvatar(), viHolder.userPoto);
        viHolder.userName.setText(attendeBean.getName());
        Log.d("fsdgsgs", attendeBean.getName());

        if(attendeBean.getDevice() != null) {
            if(StringUtils.isEmpty(attendeBean.getDevice().getMac())){
                viHolder.volumeState.setVisibility(View.INVISIBLE);
            }else {
                viHolder.volumeState.setVisibility(View.VISIBLE);
            }
        }else {
            viHolder.volumeState.setVisibility(View.INVISIBLE);
        }


        viHolder.volumeState.setState(getIsSelected().get(i));
        viHolder.volumeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monItemClickListener.onItemClick(i);
            }
        });
        return view;
    }

    /**
     * 禁言按钮的监听接口
     */
    public interface onItemClickListener {
        void onItemClick(int i);
    }

    private onItemClickListener monItemClickListener;

    public void setOnItemClickListener(onItemClickListener mOnItemDeleteListener) {
        this.monItemClickListener = mOnItemDeleteListener;
    }

    public static class ViewHolder {
        CircleImageView userPoto;
        TextView userName;
        ThreeStateCheckbox volumeState;

    }
}

package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.AttendeBean;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 梅涛 on 2021/4/27.
 */

public class SameScreenAdapter extends BaseAdapter {
    // 填充数据的list
    private List<AttendeBean> attendeBeans;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public SameScreenAdapter(List<AttendeBean> list, Context context) {
        this.context = context;
        this.attendeBeans = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < attendeBeans.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return attendeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return attendeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        AttendeBean attendeBean = attendeBeans.get(position);
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.item_same_screen, null);
            holder.userPoto = (CircleImageView) convertView.findViewById(R.id.user_poto);
            holder.userName = (TextView) convertView.findViewById(R.id.user_name);
            holder.sameScrennState = (CheckBox) convertView.findViewById(R.id.same_screnn_state);
            holder.sameScrennState.setClickable(false);
            holder.sameScrennState.setEnabled(false);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.userName.setText(attendeBean.getName());
        // 根据isSelected来设置checkbox的选中状况
        holder.sameScrennState.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SameScreenAdapter.isSelected = isSelected;
    }

    public static class ViewHolder {
        public CircleImageView userPoto;
        public TextView userName;
        public CheckBox sameScrennState;

    }
}

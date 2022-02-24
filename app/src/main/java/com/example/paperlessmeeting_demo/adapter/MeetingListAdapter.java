package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 梅涛 on 2020/7/23.
 */

public class MeetingListAdapter extends BaseAdapter {
    private Context context;
    private List<AttendeBean> gridViewBeanList;
    public InterfaceSign interfaceSign;

    public InterfaceSign getInterfaceSign() {
        return interfaceSign;
    }

    public void setInterfaceSign(InterfaceSign interfaceSign) {
        this.interfaceSign = interfaceSign;
    }

    public List<AttendeBean> getGridViewBeanList() {
        return gridViewBeanList;
    }

    public void setGridViewBeanList(List<AttendeBean> gridViewBeanList) {
        this.gridViewBeanList = gridViewBeanList;
    }

    public MeetingListAdapter(Context context, List<AttendeBean> gridViewBeanList) {
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

        final ViewHolder viHolder;
        AttendeBean attendeBean = gridViewBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_list_meeting, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(attendeBean.getAvatar(), viHolder.avatar);
        viHolder.name.setText(attendeBean.getName());
        if(!UserUtil.isTempMeeting){
            viHolder.unit.setText(attendeBean.getC_id().getName() + "/" + attendeBean.getDept_id().getName());
        }else {
            viHolder.unit.setText("未知");
        }

        switch (attendeBean.getRole()) {
            case "0":
                viHolder.identification.setText("主席");
                break;
            case "1":
                viHolder.identification.setText("秘书");
                break;
            case "2":
                viHolder.identification.setText("参会人员");
                break;
            case "3":
                viHolder.identification.setText("服务员");
                break;
            case "4":
                viHolder.identification.setText("外来人员");
                break;
        }
        if (attendeBean.getSign_time().isEmpty()) {
            viHolder.status.setText("未签到");
        } else {
            viHolder.status.setText(attendeBean.getSign_time());
        }

        if (attendeBean.getSign_time().isEmpty()) {
            viHolder.statusImage.setImageResource(R.drawable.ima_status_un);
        } else {
            viHolder.statusImage.setImageResource(R.drawable.ima_status);
        }

        if (i % 2 == 0) {
            viHolder.rootLl.setBackgroundResource(R.drawable.bg_list_meet2);
        } else {
            viHolder.rootLl.setBackgroundResource(R.drawable.bg_list_meet);
        }

        viHolder.statusRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserUtil.isTempMeeting){
                    return;
                }
                Log.d("dfgg", attendeBean.get_id());
                interfaceSign.Sign(attendeBean.getUser_id(), attendeBean.getMeeting_record_id());
            }
        });
        return view;
    }

    public interface InterfaceSign {
        public void Sign(String user_id, String meeting_record_id);
    }

    static class ViewHolder {
        @BindView(R.id.avatar)
        CircleImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.unit)
        TextView unit;
        @BindView(R.id.identification)
        TextView identification;
        @BindView(R.id.status_image)
        ImageView statusImage;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.status_rl)
        RelativeLayout statusRl;
        @BindView(R.id.root_ll)
        LinearLayout rootLl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

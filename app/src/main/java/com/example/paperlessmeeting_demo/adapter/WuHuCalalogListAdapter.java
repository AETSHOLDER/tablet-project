package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.tool.UserUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/7/23.
 */
//芜湖会议目录列表适配器
public class WuHuCalalogListAdapter extends BaseAdapter {
    private Context context;
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList;

    public List<WuHuEditBean.EditListBean> getWuHuEditBeanList() {
        return wuHuEditBeanList;
    }

    public void setWuHuEditBeanList(List<WuHuEditBean.EditListBean> wuHuEditBeanList) {
        this.wuHuEditBeanList = wuHuEditBeanList;
    }

    public WuHuCalalogListAdapter(Context context, List<WuHuEditBean.EditListBean> wuHuEditBeanList) {
        this.context = context;
        this.wuHuEditBeanList = wuHuEditBeanList;
    }

    @Override
    public int getCount() {
        return wuHuEditBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return wuHuEditBeanList.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viHolder = null;
        WuHuEditBean.EditListBean editListBean = wuHuEditBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_wuhu_meeting_catalog, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }

        viHolder.toptic.setText(editListBean.getSubTopics());
        viHolder.name.setText("汇报单位：" + editListBean.getAttendeBean());
        viHolder.attend.setText("列席单位：" + editListBean.getAttendeBean2());
        if (i == 0) {
            viHolder.topic_num.setVisibility(View.GONE);
            viHolder.toptic.setVisibility(View.GONE);
            viHolder.name.setVisibility(View.GONE);
            viHolder.aa.setVisibility(View.GONE);
            viHolder.first_rl.setVisibility(View.GONE);
            viHolder.attend.setVisibility(View.GONE);

        } else {
            viHolder.topic_num.setVisibility(View.VISIBLE);
            viHolder.toptic.setVisibility(View.VISIBLE);
            viHolder.name.setVisibility(View.VISIBLE);
            viHolder.aa.setVisibility(View.VISIBLE);
            viHolder.first_rl.setVisibility(View.VISIBLE);
            viHolder.attend.setVisibility(View.VISIBLE);

        }
        switch (i) {
            case 0:
                viHolder.topic_num.setText("议题");
                break;
            case 1:
                viHolder.topic_num.setText("议题一");
                break;
            case 2:
                viHolder.topic_num.setText("议题二");
                break;
            case 3:
                viHolder.topic_num.setText("议题三");
                break;
            case 4:
                viHolder.topic_num.setText("议题四");
                break;
            case 5:
                viHolder.topic_num.setText("议题五");
                break;
            case 6:
                viHolder.topic_num.setText("议题六");
                break;
            case 7:
                viHolder.topic_num.setText("议题七");
                break;
            case 8:
                viHolder.topic_num.setText("议题八");
                break;
            case 9:
                viHolder.topic_num.setText("议题九");
                break;
            default:
                viHolder.topic_num.setText("议题" + (i));
                break;
        }
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.topic_num)
        TextView topic_num;
        @BindView(R.id.toptic)
        TextView toptic;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.attend)
        TextView attend;
        @BindView(R.id.aa)
        View aa;
        @BindView(R.id.first_rl)
        RelativeLayout first_rl;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

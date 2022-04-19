package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.ChoseBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.WuHuVoteResultBean;
import com.example.paperlessmeeting_demo.tool.Base642BitmapTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WuHuTextResultAdapter extends BaseAdapter {
    List<WuHuVoteResultBean>wuHuVoteResultBeanList;
    Context context;

    public WuHuTextResultAdapter(Context context,List<WuHuVoteResultBean>wuHuVoteResultBeanList ) {

        this.context = context;

        this.wuHuVoteResultBeanList = wuHuVoteResultBeanList;
    }

    @Override
    public int getCount() {
        return wuHuVoteResultBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return wuHuVoteResultBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        WuHuVoteResultBean  wuHuVoteResultBean=wuHuVoteResultBeanList.get(position);
        View view;
    final    ViewHolder viewHolder ;
        if (convertView == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_wuhu_voting_results_text, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
     Log.d("sdsasafasfsafsaf",wuHuVoteResultBean.getChoosenNumb()+"."+wuHuVoteResultBean.getContent());
        viewHolder.topic.setText(wuHuVoteResultBean.getChoosenNumb()+"."+wuHuVoteResultBean.getContent());
        viewHolder.vote_numb.setText(wuHuVoteResultBean.getVoteNumb()+"票");
        viewHolder.progressBar.setProgress(wuHuVoteResultBean.getProssBarNumb());
        return view;
    }

    public class ViewHolder {

        @BindView(R.id.topic)
        TextView topic;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.vote_numb)
        TextView vote_numb;

        public ViewHolder(View itemView){
            ButterKnife.bind(this,itemView);
        }
    }
}
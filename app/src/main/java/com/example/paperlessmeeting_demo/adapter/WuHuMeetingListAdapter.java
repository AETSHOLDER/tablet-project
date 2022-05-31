package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.WuHuMeetingListResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WuHuMeetingListAdapter extends RecyclerView.Adapter<WuHuMeetingListAdapter.MyViewHolder> {

    private Context context;
    private List<WuHuMeetingListResponse> wuHuMeetingListResponses;
   private onItemInterface  onItemInterface;

    public WuHuMeetingListAdapter.onItemInterface getOnItemInterface() {
        return onItemInterface;
    }

    public void setOnItemInterface(WuHuMeetingListAdapter.onItemInterface onItemInterface) {
        this.onItemInterface = onItemInterface;
    }

    public List<WuHuMeetingListResponse> getWuHuEditBeanList() {
        return wuHuMeetingListResponses;
    }

    public void setWuHuEditBeanList(List<WuHuMeetingListResponse> wuHuEditBeanList) {
        this.wuHuMeetingListResponses = wuHuEditBeanList;
        notifyDataSetChanged();
    }

    public WuHuMeetingListAdapter(Context context, List<WuHuMeetingListResponse> wuHuEditBeanList) {
        this.context = context;
        this.wuHuMeetingListResponses = wuHuEditBeanList;
        notifyDataSetChanged();
    }
    public WuHuMeetingListAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.history_item, parent,
                false));
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viHolder, int i) {
        WuHuMeetingListResponse wuHuMeetingListResponse = wuHuMeetingListResponses.get(i);
        viHolder.meeting_tittle.setText(wuHuMeetingListResponse.getName());
        viHolder.item_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onItemInterface.onItem(i,wuHuMeetingListResponse);

            }
        });

    }
    public interface onItemInterface{

        public void onItem(int position, WuHuMeetingListResponse wuHuMeetingListResponse);
    }
    @Override
    public int getItemCount() {
        return wuHuMeetingListResponses.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.meeting_tittle)
        TextView meeting_tittle;
        @BindView(R.id.item_rl)
        RelativeLayout item_rl;

        //因为删除有可能会删除中间条目，然后会造成角标越界，所以必须整体刷新一下！
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);

        }
    }
}

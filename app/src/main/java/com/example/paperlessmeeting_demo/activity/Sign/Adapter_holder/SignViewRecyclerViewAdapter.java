package com.example.paperlessmeeting_demo.activity.Sign.Adapter_holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.Sign.Bean.SignThumbBean;
import com.example.paperlessmeeting_demo.activity.Sign.CallBack.OnRecyclerItemClickListener;
import java.util.ArrayList;
import java.util.List;

public class SignViewRecyclerViewAdapter extends RecyclerView.Adapter<SignViewRecycleViewHolder> {

    public List<SignThumbBean> mDatas = new ArrayList<SignThumbBean>();
    private Context mContext;
    private OnRecyclerItemClickListener listener;
    private boolean isShowDelet = false;
    public SignViewRecyclerViewAdapter(Context context, List<SignThumbBean> datas){
        this.mContext = context;
        this.mDatas = datas;

    }


    public void showDeletImg(boolean isShow) {
        isShowDelet = isShow;
//        for (int i = 0;i<mDatas.size(); i++){
//           mDatas.get(i).setShowDel(isShow);
//        }
        notifyDataSetChanged();
    }

    @Override
    public SignViewRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SignViewRecycleViewHolder holder = new SignViewRecycleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.sign_rl_tem_view, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(SignViewRecycleViewHolder holder, final int position) {
        holder.getTxt_info().setText(mDatas.get(position).getCreatTime());
        holder.getImg().setImageBitmap(mDatas.get(position).getThumb());
        holder.getBleryV().setBlurredView(holder.getImg());
        holder.getImg().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onRecyclerItemClick(position);
                }
            }
        });
        holder.getImg_delete().setVisibility(isShowDelet ? View.VISIBLE : View.GONE);
        holder.getImg_delete().bringToFront();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.listener = listener;
    }
}

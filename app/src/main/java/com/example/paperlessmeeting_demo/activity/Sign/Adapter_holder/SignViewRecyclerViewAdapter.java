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
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    public List<SignThumbBean> mDatas = new ArrayList<SignThumbBean>();
    private Context mContext;
    private OnRecyclerItemClickListener listener;
    private int adapterIndex;
    private boolean isShowDelet = false;
    public SignViewRecyclerViewAdapter(Context context, List<SignThumbBean> datas,int adapterIndex){
        this.mContext = context;
        this.mDatas = datas;
        mSelectedPositions = new SparseBooleanArray();
        this.adapterIndex = adapterIndex;
        for (int i=0;i<mDatas.size();i++){
            setItemChecked(i, false);
        }
    }

    public void setDatas(List<SignThumbBean> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 取消全选
     * */
    public void cancelSelectAll() {
        for (int i = 0;i<mDatas.size(); i++){
            setItemChecked(i,false);
        }
        notifyDataSetChanged();
    }


    public void showDeletImg(boolean isShow) {
        isShowDelet = isShow;
        notifyDataSetChanged();
    }

    //根据位置判断条目是否选中
    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }
    //获得选中条目的索引
    public ArrayList<Integer> getSelectedItemIndex() {
        ArrayList<Integer> selectList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(i);
            }
        }
        return selectList;
    }
    //获得选中条目的结果
    public ArrayList<SignThumbBean> getSelectedItem() {
        ArrayList<SignThumbBean> selectList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(mDatas.get(i));
            }
        }
        return selectList;
    }
    //设置给定位置条目的选择状态
    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
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
                    listener.onRecyclerItemClick(adapterIndex,position);
                }
            }
        });
        holder.getImg_delete().setVisibility(isShowDelet ? View.VISIBLE : View.GONE);
        holder.getImg_delete().setSelected(false);
        holder.getImg_delete().bringToFront();
        holder.getImg_delete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(listener != null){
//                    listener.onRecyclerDeletClick(adapterIndex,position);
//                }
                holder.getDel_img().setSelected(holder.getImg_delete().isSelected());
                holder.getImg_delete().setSelected(!holder.getImg_delete().isSelected());
                setItemChecked(position,holder.getImg_delete().isSelected());
            }
        });
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

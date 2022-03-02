package com.example.paperlessmeeting_demo.adapter;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean.VoteBean;

import java.util.ArrayList;


public class VoteChairmanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    public interface voteClickListener{
        //  点击查看
        public void chairmanClickListener(int position,String flag);
        //  点击操作
        public void operationclickListener(int position,String flag);

    }
    private VoteChairmanAdapter.voteClickListener mvoterClickListener;
    private ArrayList<VoteListBean.VoteBean> list = new ArrayList<>();
    private Context context;
    public VoteChairmanAdapter(Context context) {
        this.context = context;
    }

    public void  addFinalVotestate(ArrayList<VoteListBean.VoteBean> terminalStateList){
        this.list.clear();
        this.list = terminalStateList;

        notifyDataSetChanged();
    }

    public void setList(ArrayList<VoteBean> list) {
        this.list = list;
    }

    public ArrayList<VoteBean> getList() {
        return list;
    }

    public void setOnVoteListener(VoteChairmanAdapter.voteClickListener voteClickListener){
        this.mvoterClickListener = voteClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.vote_chairman_item, null, false);
        RecyclerView.ViewHolder holder = null;
        holder = new VoteChairmanViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((VoteChairmanViewHolder) holder).title.setText(list.get(position).getTopic());
        int status = list.get(position).getMvoteStatus();
        switch (status){
            case Constants.VoteStatusEnum.hasStartUnVote:
                ((VoteChairmanViewHolder) holder).status.setText("进行中");
                ((VoteChairmanViewHolder) holder).status_desc.setVisibility(View.INVISIBLE);
                break;
            case Constants.VoteStatusEnum.hasFinshed:
                ((VoteChairmanViewHolder) holder).status.setText("查看结果");
                ((VoteChairmanViewHolder) holder).status.setTextColor( Color.parseColor("#3377FF"));
                ((VoteChairmanViewHolder) holder).status_desc.setText("已结束");
                ((VoteChairmanViewHolder) holder).status_desc.setVisibility(View.VISIBLE);
                ((VoteChairmanViewHolder) holder).operation.setVisibility(View.INVISIBLE);
                break;
            case Constants.VoteStatusEnum.hasVotedNoResult:
                ((VoteChairmanViewHolder) holder).status.setText("进行中");
                ((VoteChairmanViewHolder) holder).status_desc.setText("已投票");
                ((VoteChairmanViewHolder) holder).status_desc.setVisibility(View.VISIBLE);
                ((VoteChairmanViewHolder) holder).operation.setVisibility(View.VISIBLE);
                break;
            default:
                ((VoteChairmanViewHolder) holder).status_desc.setVisibility(View.INVISIBLE);
                break ;
        }
        ((VoteChairmanViewHolder) holder).status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  未投票，或者已有结果
                if(status==Constants.VoteStatusEnum.hasStartUnVote|| status==Constants.VoteStatusEnum.hasFinshed){
                    Log.d("gdgsdgsdgdgf4448888","flag==="+flag);
                    mvoterClickListener.chairmanClickListener(position,flag);
                }
            }
        });
        ((VoteChairmanViewHolder) holder).operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("gdgsdgsdgdgf4447777","flag==="+flag);
                //  操作  已经投票  未投票
                mvoterClickListener.operationclickListener(position,flag);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

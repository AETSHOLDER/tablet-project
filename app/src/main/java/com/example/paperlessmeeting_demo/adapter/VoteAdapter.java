package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.bean.VoteListBean;

import java.util.ArrayList;
import java.util.List;


public class VoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public interface voteClickListener{
        public void clickListener(int position,String flag);
    }
    private voteClickListener mvoterClickListener;
    private List<VoteListBean.VoteBean> list = new ArrayList<>();
    private Context context;
    public VoteAdapter(Context context) {
        this.context = context;
    }

    public void  addFinalVotestate(ArrayList terminalStateList){
        this.list.clear();
        this.list = terminalStateList;

        notifyDataSetChanged();
    }

    public void setList(List<VoteListBean.VoteBean> list) {
        this.list = list;
    }

    public List<VoteListBean.VoteBean> getList() {
        return list;
    }

    public void setOnVoteListener(voteClickListener voteClickListener){
        this.mvoterClickListener = voteClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.vote_item, null, false);
        RecyclerView.ViewHolder holder = null;
        holder = new VoteViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((VoteViewHolder) holder).title.setText(list.get(position).getTopic());
        int status = list.get(position).getMvoteStatus();

        switch (status){
            case Constants.VoteStatusEnum.hasStartUnVote:
                ((VoteViewHolder) holder).vote.setText("投票");
                ((VoteViewHolder) holder).vote.setVisibility(View.VISIBLE);
                ((VoteViewHolder) holder).status.setVisibility(View.INVISIBLE);
                break;
            case Constants.VoteStatusEnum.hasFinshed:
                ((VoteViewHolder) holder).status.setVisibility(View.VISIBLE);
                ((VoteViewHolder) holder).status.setText("已结束");

                ((VoteViewHolder) holder).vote.setVisibility(View.VISIBLE);
                ((VoteViewHolder) holder).vote.setText("查看结果");
                break;
            case Constants.VoteStatusEnum.hasVotedNoResult:
                ((VoteViewHolder) holder).status.setVisibility(View.VISIBLE);
                ((VoteViewHolder) holder).status.setText("已投票");

                ((VoteViewHolder) holder).vote.setVisibility(View.VISIBLE);
                ((VoteViewHolder) holder).vote.setText("结果统计中…");
                break;
            default:
                ((VoteViewHolder) holder).vote.setVisibility(View.INVISIBLE);
                ((VoteViewHolder) holder).status.setVisibility(View.INVISIBLE);
                break ;
        }

        ((VoteViewHolder) holder).vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("gdgsdgsdgdgf4440000","flag==="+flag);
                //  未投票，或者已有结果
                if(status==Constants.VoteStatusEnum.hasStartUnVote|| status==Constants.VoteStatusEnum.hasFinshed){
                    mvoterClickListener.clickListener(position,flag);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

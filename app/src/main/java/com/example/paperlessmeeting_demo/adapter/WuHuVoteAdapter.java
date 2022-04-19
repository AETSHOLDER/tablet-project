package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.ChoseBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.widgets.MyGridView;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WuHuVoteAdapter extends RecyclerView.Adapter<WuHuVoteAdapter.MyViewHolder> {

    private  String flag;
    private voteInterFace  vf;
    private endInterFace ef;
    private viewResultsInterface vr;

    public viewResultsInterface getVr() {
        return vr;
    }

    public void setVr(viewResultsInterface vr) {
        this.vr = vr;
    }

    public voteInterFace getVf() {
        return vf;
    }

    public void setVf(voteInterFace vf) {
        this.vf = vf;
    }

    public endInterFace getEf() {
        return ef;
    }

    public void setEf(endInterFace ef) {
        this.ef = ef;
    }

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
    private WuHuVoteAdapter.voteClickListener mvoterClickListener;
    private ArrayList<VoteListBean.VoteBean> list = new ArrayList<>();
    private Context context;
    public WuHuVoteAdapter(Context context) {
        this.context = context;
    }

    public void  addFinalVotestate(ArrayList<VoteListBean.VoteBean> terminalStateList){
        this.list.clear();
        this.list = terminalStateList;

        notifyDataSetChanged();
    }

    public void setList(ArrayList<VoteListBean.VoteBean> list) {
        this.list = list;
    }

    public ArrayList<VoteListBean.VoteBean> getList() {
        return list;
    }

    public void setOnVoteListener(WuHuVoteAdapter.voteClickListener voteClickListener){
        this.mvoterClickListener = voteClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_wuhu_vote_list, parent,
                false));

   /*     View v = LayoutInflater.from(context).inflate(R.layout.item_wuhu_vote_list, null, false);
        RecyclerView.ViewHolder holder = null;
        holder = new WuHuVoteChairmanViewHolder(v);*/
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTopic());
        VoteListBean.VoteBean  voteBean=list.get(position);
        VoteListBean.VoteBean.FromBean  fromBean=voteBean.getFrom();
        List<ChoseBean> dataList = new ArrayList<>();
        int status = list.get(position).getMvoteStatus();
        holder.creator.setText("发起人："+fromBean.getName());
        if (list.get(position).getType().equals("0")) {
            holder.danxaun.setText("单选");

            for (int i = 0; i < voteBean.getTemporBeanList().size(); i++) {
                ChoseBean object = new ChoseBean();
                object.setContent(voteBean.getTemporBeanList().get(i).getContent());
                object.setVotePath(voteBean.getTemporBeanList().get(i).getVotePath());
                object.setChecked(false);
                dataList.add(object);
            }
            if (voteBean.getFlag().equals("1")){
                holder.listview.setVisibility(View.VISIBLE);
                holder.gridview.setVisibility(View.GONE);
                WuHuRadioAdapter  radioAdapter = new WuHuRadioAdapter(context, dataList,voteBean.getFlag(),voteBean);
                holder.listview.setAdapter(radioAdapter);
                radioAdapter.notifyDataSetChanged();
            }else {
               holder.listview.setVisibility(View.GONE);
                holder.gridview.setVisibility(View.VISIBLE);
                WuHuSingleGridleAdapter wuHuSingleGridleAdapter=new WuHuSingleGridleAdapter(context, dataList,voteBean.getFlag(),voteBean);
                holder.gridview.setAdapter(wuHuSingleGridleAdapter);
                wuHuSingleGridleAdapter.notifyDataSetChanged();
            }

        } else {
            holder.danxaun.setText("多选");
            for (int i = 0; i < voteBean.getTemporBeanList().size(); i++) {
                ChoseBean object = new ChoseBean();
                object.setContent(voteBean.getOptions().get(i));
                object.setContent(voteBean.getTemporBeanList().get(i).getContent());
                object.setVotePath(voteBean.getTemporBeanList().get(i).getVotePath());
                object.setChecked(false);
                dataList.add(object);
            }
            if (voteBean.getFlag().equals("1")){
                holder.listview.setVisibility(View.VISIBLE);
                holder.gridview.setVisibility(View.GONE);
                WuHuCheckBoxAdapter checkBoxAdapter=new WuHuCheckBoxAdapter(context, dataList,voteBean.getFlag(),voteBean);
                holder.listview.setAdapter(checkBoxAdapter);
                checkBoxAdapter.notifyDataSetChanged();
            }else {
              holder.listview.setVisibility(View.GONE);
                holder.gridview.setVisibility(View.VISIBLE);
                WuHuMultipleGridleAdapter wuHuSingleGridleAdapter=new WuHuMultipleGridleAdapter(context, dataList,voteBean.getFlag(),voteBean);
                holder.gridview.setAdapter(wuHuSingleGridleAdapter);
                wuHuSingleGridleAdapter.notifyDataSetChanged();

            }

        }



        switch (status){
            case Constants.VoteStatusEnum.hasStartUnVote:
                holder.endtimeClock.setText("进行中");
                holder.btn_pos.setText("投票");
                holder.btn_pos.setEnabled(true);
                if (UserUtil.ISCHAIRMAN) {
                    holder.btn_end.setVisibility(View.VISIBLE);
                }else {
                    holder.btn_end.setVisibility(View.GONE);
                }

              //  ((WuHuVoteChairmanViewHolder) holder).status_desc.setVisibility(View.INVISIBLE);
                holder.btn_pos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean hasVote = false;
                        for (int i = 0; i < dataList.size(); i++) {
                            ChoseBean object = dataList.get(i);
                            if (object.isChecked()) {
                                hasVote = true;
                            }
                        }

                        if (!hasVote) {
                            Toast.makeText(context, "请先选择选项!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Hawk.put("ChoseBean",dataList);
                        Log.d("ggsgg","position="+position+"  voteBean.getFlag()"+voteBean.getFlag());
                        vf.vote(position,voteBean.getFlag());
                      //  radioAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case Constants.VoteStatusEnum.hasFinshed:
                if (UserUtil.ISCHAIRMAN) {
                    holder.btn_end.setVisibility(View.GONE);
                    holder.btn_result.setVisibility(View.VISIBLE);
                }else {
                    holder.btn_end.setVisibility(View.GONE);

                }
                holder. btn_end.setVisibility(View.GONE);
             //   ((WuHuVoteChairmanViewHolder) holder).status.setText("查看结果");
              //  ((WuHuVoteChairmanViewHolder) holder).status.setTextColor( Color.parseColor("#3377FF"));
                holder.endtimeClock.setText("已结束");
                holder.btn_pos.setText("已投票");
                holder.btn_pos.setEnabled(false);
             //   ((WuHuVoteChairmanViewHolder) holder).status_desc.setVisibility(View.VISIBLE);
               // ((WuHuVoteChairmanViewHolder) holder).operation.setVisibility(View.INVISIBLE);
                break;
            case Constants.VoteStatusEnum.hasVotedNoResult:
                holder.endtimeClock.setText("进行中");
                holder.btn_pos.setText("已投票");
                holder.btn_pos.setEnabled(false);
                Toast.makeText(context,"您已投过！",Toast.LENGTH_SHORT).show();
              //  ((WuHuVoteChairmanViewHolder) holder).status_desc.setVisibility(View.VISIBLE);
               // ((WuHuVoteChairmanViewHolder) holder).operation.setVisibility(View.VISIBLE);

                break;
            default:
               // holder.status_desc.setVisibility(View.INVISIBLE);
                break ;
        }
        if (list.get(position).getAnonymity().equals("1")) {
            holder.niming.setText("实名");
        } else {
            holder.niming.setText("匿名");

        }
            holder.btn_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  未投票，或者已有结果
                    if(status==Constants.VoteStatusEnum.hasFinshed){
                        Log.d("gdgsdgsdgdgf4448888","flag==="+flag);
                        vr.vr(position,voteBean.getFlag());
                        // mvoterClickListener.chairmanClickListener(position,flag);
                    }else {
                        Toast.makeText(context,"投票还未结束，暂时不能查看",Toast.LENGTH_SHORT).show();
                    }
                }
            });





        holder.btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ef.end(position,flag);
                    holder.btn_result.setTextColor(Color.parseColor("#3377FF"));
            }
        });
     /*   ((WuHuVoteChairmanViewHolder) holder).operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("gdgsdgsdgdgf4447777","flag==="+flag);
                //  操作  已经投票  未投票
                mvoterClickListener.operationclickListener(position,flag);
            }
        });*/

    }

    public interface endInterFace{
        public void   end(int  a,String flag);
    }
    public interface voteInterFace{

        public  void  vote(int  a,String flag);
    }

    public interface viewResultsInterface{

        public void vr(int a,String flag);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_result)
        Button btn_result;
        @BindView(R.id.btn_end)
        Button btn_end;
        @BindView(R.id.listview)
        MyListView listview;
        @BindView(R.id.endtimeClock)
        TextView endtimeClock;
        @BindView(R.id.creator)
        TextView creator;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.btn_pos)
        TextView btn_pos;
        @BindView(R.id.danxaun)
        TextView danxaun;
        @BindView(R.id.niming)
        TextView niming;
      /*  @BindView(R.id.avater_img)
        ImageView avater_img;*/
        @BindView(R.id.gridview)
        MyGridView gridview;
        //因为删除有可能会删除中间条目，然后会造成角标越界，所以必须整体刷新一下！
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);

        }
    }
}

package com.example.paperlessmeeting_demo.adapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.widgets.MyListView;


public class WuHuVoteChairmanViewHolder extends RecyclerView.ViewHolder {
    public TextView endtimeClock,creator,title,btn_pos,danxaun,niming;
    public ImageView avater_img;
    private Button btn_result,btn_end;
    private ListView lv;
    public WuHuVoteChairmanViewHolder(View itemView) {
        super(itemView);
        endtimeClock = itemView.findViewById(R.id.endtimeClock);//投票状态
        creator = itemView.findViewById(R.id.creator);//发起人
        title = itemView.findViewById(R.id.title);//投票标题
        avater_img = itemView.findViewById(R.id.avater_img);
        danxaun = itemView.findViewById(R.id.danxaun);
        niming = itemView.findViewById(R.id.niming);
        btn_result = itemView.findViewById(R.id.btn_result);
        btn_pos = itemView.findViewById(R.id.btn_pos);
        btn_end = itemView.findViewById(R.id.btn_end);
        lv= itemView.findViewById(R.id.listview);

    }
}

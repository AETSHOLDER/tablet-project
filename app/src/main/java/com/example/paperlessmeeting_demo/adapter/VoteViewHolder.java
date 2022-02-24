package com.example.paperlessmeeting_demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;


class VoteViewHolder extends  RecyclerView.ViewHolder {
    public TextView title,status,vote;
    public VoteViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        status = itemView.findViewById(R.id.status);
        vote = itemView.findViewById(R.id.vote);



        TextPaint paint1 = title.getPaint();
        paint1.setFakeBoldText(true);

        TextPaint paint2 = status.getPaint();
        paint1.setFakeBoldText(true);

        TextPaint paint3 = vote.getPaint();
        paint1.setFakeBoldText(true);

    }
}

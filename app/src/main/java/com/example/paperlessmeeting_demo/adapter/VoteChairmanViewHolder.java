package com.example.paperlessmeeting_demo.adapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;


public class VoteChairmanViewHolder extends RecyclerView.ViewHolder {
    public TextView title,status,status_desc;
    public ImageView operation;
    public VoteChairmanViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        status = itemView.findViewById(R.id.status);
        status_desc = itemView.findViewById(R.id.status_desc);
        operation = itemView.findViewById(R.id.operation);


        TextPaint paint1 = title.getPaint();
        paint1.setFakeBoldText(true);

        TextPaint paint2 = status.getPaint();
        paint1.setFakeBoldText(true);

        TextPaint paint3 = status_desc.getPaint();
        paint1.setFakeBoldText(true);

    }
}

package com.example.paperlessmeeting_demo.activity.Sign.Adapter_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.widgets.BlurringView;

public class SignViewRecycleViewHolder extends RecyclerView.ViewHolder {

    private TextView txt_info;
    private ImageView img_title;
    private BlurringView bleryV;
    public SignViewRecycleViewHolder(View view){
        super(view);
        txt_info = (TextView)view.findViewById(R.id.txt_info);
        img_title = (ImageView)view.findViewById(R.id.img_title);
        bleryV  = (BlurringView)view.findViewById(R.id.blurView);
    }

    public TextView getTxt_info(){
        return this.txt_info;
    }

    public ImageView getImg(){
        return this.img_title;
    }

    public BlurringView getBleryV() {
        return bleryV;
    }
}

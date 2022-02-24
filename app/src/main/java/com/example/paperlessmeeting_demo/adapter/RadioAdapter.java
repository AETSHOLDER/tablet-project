package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.ChoseBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RadioAdapter extends BaseAdapter {
    List<ChoseBean> listData;
    Context context;

    ChoseBean lastDataBean = null;

    public RadioAdapter(Context context, List<ChoseBean> list ) {
        listData = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        RadioAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_cheeckbox_list_item, null);
            viewHolder = new RadioAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (RadioAdapter.ViewHolder) view.getTag();
        }
        try {
            ChoseBean bean = listData.get(position);
            viewHolder.tvContent.setText(bean.getContent());
            if (bean.isChecked()) {
                viewHolder.ivCheckState.setImageResource(R.drawable.radio_selected);
            } else {
                viewHolder.ivCheckState.setImageResource(R.drawable.radio_unselected);
            }

            viewHolder.ivCheckState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChoseBean dataBean = listData.get(position);
                    try {
                        if (dataBean.isChecked()) {
                            return;
                        } else {
                            if(lastDataBean!=null){
                                lastDataBean.setChecked(false);
                            }
                            dataBean.setChecked(true);
                            lastDataBean = dataBean;
                            notifyDataSetChanged();
                        }
                    }catch (Exception e){}

                }
            });


        }catch (Exception e){

        }
        return view;
    }

    public class ViewHolder {

        @BindView(R.id.tv_content)
        TextView tvContent;

        @BindView(R.id.iv_check_state)
        ImageView ivCheckState;

        public ViewHolder(View itemView){
            ButterKnife.bind(this,itemView);
        }
    }
}
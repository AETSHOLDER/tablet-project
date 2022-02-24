package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.ItemBean;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {
    private Context context;
    private List<ItemBean> list;

    public RecycleAdapter(Context context, List<ItemBean> list, ChoseClickListener mchoseClickListener) {
        this.context = context;
        this.list = list;
        this.mchoseClickListener = mchoseClickListener;
    }


    //  获取当前的选项列表
    public List<ItemBean> getList() {
        return list;
    }

    //自定义接口
    private ChoseClickListener mchoseClickListener;
    public interface  ChoseClickListener{
        public void clickListener(int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_home, parent,
                false));

        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ItemBean itemObj = list.get(position);
        if(list.size()>0){

            if(position==list.size()-1){
                holder.add_chose.setVisibility(View.VISIBLE);
            }else{
                holder.add_chose.setVisibility(View.INVISIBLE);
            }
        }


        //This is important. Remove TextWatcher first.
        if (holder.editText.getTag() instanceof TextWatcher) {
            holder.editText.removeTextChangedListener((TextWatcher) holder.editText.getTag());
        }
        holder.chose_text.setText("选项"+(position+3)+":" );
        holder.editText.setText(itemObj.getText());

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    itemObj.setText("");
                } else {
                    itemObj.setText(s.toString());
                }
            }
        };

        holder.editText.addTextChangedListener(watcher);
        holder.editText.setTag(watcher);

        // 删除
        holder.add_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(position);
            }
        });

        //  添加
        holder.add_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size()<8){
                    addData(list.size());
                }else{
                    Toast.makeText(context,"最多只能添加10个选项!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    //  添加数据
    public void addData(int position) {
//      在list中添加数据，并通知条目加入一条
        list.add(new ItemBean());
        //添加动画
        notifyItemInserted(position);
        notifyDataSetChanged();
    }
    //  删除数据
    public void removeData(int position) {
        list.remove(position);
        if(list.size()==0){
            mchoseClickListener.clickListener(position);
        }
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_chose)
        TextView chose_text;

        @BindView(R.id.edit_text)
        EditText editText;

        @BindView(R.id.add_delet)
        ImageView add_delet;

        @BindView(R.id.add_chose)
        ImageView add_chose;
        //因为删除有可能会删除中间条目，然后会造成角标越界，所以必须整体刷新一下！
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,itemView);

        }
    }
}

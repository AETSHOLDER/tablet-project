package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.ChoseBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.tool.Base642BitmapTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WuHuMultipleGridleAdapter extends BaseAdapter {
    List<ChoseBean> listData;
    Context context;
    private  String flag;
    ChoseBean lastDataBean = null;
   // List< VoteListBean.VoteBean.UserListBean>  userListBeanList;
    VoteListBean.VoteBean  voteBean;
    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public WuHuMultipleGridleAdapter(Context context, List<ChoseBean> list, String flag, VoteListBean.VoteBean  voteBean ) {
        listData = list;
        this.context = context;
        this.flag = flag;
        this.voteBean = voteBean;
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
    final     WuHuMultipleGridleAdapter.ViewHolder viewHolder ;
        if (convertView == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_wuhu_vote_image_list, null);
            viewHolder = new WuHuMultipleGridleAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (WuHuMultipleGridleAdapter.ViewHolder) view.getTag();
        }
        switch (position){
            case  0:
                viewHolder.orderNumb.setText("A");
            break;
            case  1:
                viewHolder.orderNumb.setText("B");
                break;
            case  2:
                viewHolder.orderNumb.setText("C");
                break;
            case  3:
                viewHolder.orderNumb.setText("D");
                break;

            case  4:
                viewHolder.orderNumb.setText("E");
                break;

            case  5:
                viewHolder.orderNumb.setText("F");
                break;

            case  6:
                viewHolder.orderNumb.setText("G");
                break;

            case  7:
                viewHolder.orderNumb.setText("H");
                break;

            case  8:
                viewHolder.orderNumb.setText("I");
                break;
            case  9:
                    viewHolder.orderNumb.setText("L");

                break;

            case  10:
                viewHolder.orderNumb.setText("M");

                break;
        }

        try {
            ChoseBean bean = listData.get(position);
            Log.d("gsgsgsgsg22",bean.getContent()+"  ============== "+flag);
            if (flag.equals("1")){
               // viewHolder.tvContent.setVisibility(View.VISIBLE);
                viewHolder.ima_content.setVisibility(View.GONE);
                //viewHolder.tvContent.setText(bean.getContent());

            }else {
              //  viewHolder.tvContent.setVisibility(View.GONE);
                viewHolder.ima_content.setVisibility(View.VISIBLE);
                viewHolder.ima_content.setImageBitmap(Base642BitmapTool.base642Bitmap(bean.getContent()));
            }

            if (bean.isChecked()) {
                viewHolder.ivCheckState.setImageResource(R.drawable.radio_selected);
            } else {
                viewHolder.ivCheckState.setImageResource(R.drawable.radio_unselected);
            }



/*
            for (int i=0;i<userListBeanList.size();i++){
                Log.d("fdssgg1111","1111111");
                VoteListBean.VoteBean.UserListBean userListBean=userListBeanList.get(i);
                if (userListBean.getUser_id().equals(FLUtil.getMacAddress())){
                    Log.d("fdssgg2222","2222222"+"   "+userListBean.getChoose().size());
                for (int h=0;h<userListBean.getChoose().size();h++){
                    Log.d("fdssgg333",userListBean.getChoose().get(h));
                    if (bean.getContent().equals(userListBean.getChoose().get(h))) {
                        Log.d("fdssgg4444",userListBean.getChoose().get(h));
                        viewHolder.ivCheckState.setImageResource(R.drawable.radio_selected);
                    }
                }
                }
            }*/
            //创建投票用户
            VoteListBean.VoteBean.UserListBean  userListBean=new VoteListBean.VoteBean.UserListBean();
            //创建投票类
            VoteListBean.VoteBean.UserListBean.ChoseBean choseBean=new VoteListBean.VoteBean.UserListBean.ChoseBean();
            ArrayList<String> choseString = new ArrayList<>();
            List<VoteListBean.VoteBean.UserListBean> userListBeanList=voteBean.getUser_list();
            List<VoteListBean.VoteBean.UserListBean.ChoseBean>   choseBeanList=new ArrayList<>();

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
                            if(flag.equals("1")){
                               // dataBean.setContent(viewHolder.tvContent.getText().toString());
                            }else {
                                dataBean.setContent(bean.getContent());
                            }
                            dataBean.setOrderNumb(viewHolder.orderNumb.getText().toString());

                            lastDataBean = dataBean;
                            viewHolder.ivCheckState.setImageResource(R.drawable.radio_selected);
                          notifyDataSetChanged();

/*
                                //当前设备持有人初始化投票信息
                                    viewHolder.ivCheckState.setImageResource(R.drawable.radio_selected);
                                    //选项设置
                                    choseBean.setChecked(true);
                                    choseBean.setContent(viewHolder.tvContent.getText().toString());
                                    choseBean.setOrderNumb(viewHolder.orderNumb.getText().toString());
                                    choseBeanList.add(choseBean);
                                    userListBean.setChoseBeanList(choseBeanList);

                            //投票人赋值
                           // userListBean.setStatus("FINISH");
                            userListBean.setUser_name(Hawk.get(constant.myNumber));
                            userListBean.setUser_id(FLUtil.getMacAddress());
                            userListBean.setMeeting_vote_id(voteBean.get_id());

                            choseString.add(viewHolder.tvContent.getText().toString());

                            userListBean.setChoose(choseString);
                            userListBeanList.add(userListBean);
                            voteBean.setUser_list(userListBeanList);*/
                                    //notifyDataSetChanged();


                        }
                    }catch (Exception e){}

                }
            });


        }catch (Exception e){

        }
        return view;
    }

    public class ViewHolder {

      /*  @BindView(R.id.tv_content)
        TextView tvContent;*/
        @BindView(R.id.orderNumb)
        TextView orderNumb;
        @BindView(R.id.iv_check_state)
        ImageView ivCheckState;
        @BindView(R.id.ima_content)
        ImageView ima_content;

        public ViewHolder(View itemView){
            ButterKnife.bind(this,itemView);
        }
    }
}
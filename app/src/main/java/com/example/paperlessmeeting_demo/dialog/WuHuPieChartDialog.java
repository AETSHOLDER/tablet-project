package com.example.paperlessmeeting_demo.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.PieEntry;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.tool.Base642BitmapTool;
import com.example.paperlessmeeting_demo.widgets.PieView;

import java.util.ArrayList;
import java.util.List;


public class WuHuPieChartDialog extends AlertDialog {
    private Context context;
    private View.OnClickListener mButtonClickListener;
    private View mLayout;
    private TextView mTitle;
    private ImageView mButton;
    private TextView mBottom;
    private PieView mPieView;
    private WuHuPieChartDialog mDialog;
    private VoteListBean.VoteBean vote;
    private RelativeLayout number_rl1;
    private RelativeLayout number_rl2;
    private RelativeLayout number_rl3;
    private RelativeLayout number_rl4;
    private TextView  number_tv1;
    private TextView  number_tv2;
    private TextView  number_tv3;
    private TextView  number_tv4;
    private ImageView option_ima1;
    private ImageView option_ima2;
    private ImageView option_ima3;
    private ImageView option_ima4;
    private LinearLayout option_ll;
    private ArrayList<PieEntry> dataList = new ArrayList<>();
    public WuHuPieChartDialog(Context context, @StyleRes int themeResId, ArrayList<PieEntry> dataList, VoteListBean.VoteBean vote) {
        super(context, themeResId);
        this.context = context;
        this.dataList = dataList;
        this.vote = vote;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wuhu_dialog_piechart);

        mLayout = (LinearLayout) findViewById(R.id.layout_content);
        mButton = mLayout.findViewById(R.id.dialog_close_icon);
        mTitle = mLayout.findViewById(R.id.dialog_title);
        mPieView = mLayout.findViewById(R.id.pieView);
        mBottom = mLayout.findViewById(R.id.dialog_bottom);

        option_ima1= mLayout.findViewById(R.id.option_ima1);
        option_ima2= mLayout.findViewById(R.id.option_ima2);
        option_ima3= mLayout.findViewById(R.id.option_ima3);
        option_ima4= mLayout.findViewById(R.id.option_ima4);

        number_rl1= mLayout.findViewById(R.id.number_rl1);
        number_rl2= mLayout.findViewById(R.id.number_rl2);
        number_rl3= mLayout.findViewById(R.id.number_rl3);
        number_rl4= mLayout.findViewById(R.id.number_rl4);


        number_tv1= mLayout.findViewById(R.id.number_tv1);
        number_tv2= mLayout.findViewById(R.id.number_tv2);
        number_tv3= mLayout.findViewById(R.id.number_tv3);
        number_tv4= mLayout.findViewById(R.id.number_tv4);

        option_ll= mLayout.findViewById(R.id.option_ll);

        mButton.setOnClickListener(view -> {
            if (mButtonClickListener != null)
                mButtonClickListener.onClick(view);
            dismiss();
        });
        setCancelable(true);                //User can click back to close dialog_info
        setCanceledOnTouchOutside(false);   //User can not click outside area to close dialog_info

        initPieView();

        setTitle(vote.getTopic());

        List<VoteListBean.VoteBean.UserListBean> userListBeans = new ArrayList<>();
       List<VoteListBean.VoteBean.TemporBean>  temporBeanList=new ArrayList<>();
        temporBeanList=vote.getTemporBeanList();
     if (vote.getFlag().equals("1")){
      option_ll.setVisibility(View.GONE);
     }else {
         option_ll.setVisibility(View.VISIBLE);
         if (temporBeanList==null||temporBeanList.size()<1){
             option_ll.setVisibility(View.GONE);
             return;
         }
         switch (temporBeanList.size()){
             case 1:
                 number_rl1.setVisibility(View.VISIBLE);
                 number_rl2.setVisibility(View.GONE);
                 number_rl3.setVisibility(View.GONE);
                 number_rl4.setVisibility(View.GONE);
                 if (temporBeanList.size()==1){
                     number_tv1.setText(temporBeanList.get(0).getOrderNumb());
                     option_ima1.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(0).getContent()));
                 }

                 break;
             case 2:
                 number_rl1.setVisibility(View.VISIBLE);
                 number_rl2.setVisibility(View.VISIBLE);
                 number_rl3.setVisibility(View.GONE);
                 number_rl4.setVisibility(View.GONE);
                 if (temporBeanList.size()>1){
                     number_tv1.setText(temporBeanList.get(0).getOrderNumb());
                     option_ima1.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(0).getContent()));
                     number_tv2.setText(temporBeanList.get(1).getOrderNumb());
                     option_ima2.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(1).getContent()));
                 }
                 break;
             case 3:
                 number_rl1.setVisibility(View.VISIBLE);
                 number_rl2.setVisibility(View.VISIBLE);
                 number_rl3.setVisibility(View.VISIBLE);
                 number_rl4.setVisibility(View.GONE);
                 if (temporBeanList.size()>2){
                     number_tv1.setText(temporBeanList.get(0).getOrderNumb());
                     option_ima1.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(0).getContent()));
                     number_tv2.setText(temporBeanList.get(1).getOrderNumb());
                     option_ima2.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(1).getContent()));
                     number_tv3.setText(temporBeanList.get(2).getOrderNumb());
                     option_ima3.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(2).getContent()));
                 }
                 break;
             case 4:
                 number_rl1.setVisibility(View.VISIBLE);
                 number_rl2.setVisibility(View.VISIBLE);
                 number_rl3.setVisibility(View.VISIBLE);
                 number_rl4.setVisibility(View.VISIBLE);
                 if (temporBeanList.size()>3){
                     number_tv1.setText(temporBeanList.get(0).getOrderNumb());
                     option_ima1.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(0).getContent()));
                     number_tv2.setText(temporBeanList.get(1).getOrderNumb());
                     option_ima2.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(1).getContent()));
                     number_tv3.setText(temporBeanList.get(2).getOrderNumb());
                     option_ima3.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(2).getContent()));
                     number_tv4.setText(temporBeanList.get(3).getOrderNumb());
                     option_ima4.setImageBitmap(Base642BitmapTool.base642Bitmap(temporBeanList.get(3).getContent()));
                 }
                 break;

         }

     }
        // 求出已投票的用户数量
        String ss = new String();
        for(VoteListBean.VoteBean.UserListBean bean : vote.getUser_list() ){
            if(bean.getStatus().equals("FINISH")){
                userListBeans.add(bean);
            }
        }
        if(vote.getAnonymity().equals("0")){
            ss = String.format("匿名投票：%d",userListBeans.size());
        }else {
            ss = String.format("实名投票：%d",userListBeans.size());
        }
        mBottom.setText(ss);


    }


    public void setTitle(@NonNull String title) {
        mTitle.setText(title);
        mTitle.setVisibility(View.VISIBLE);
    }
    public void setButton(View.OnClickListener listener) {
        mButtonClickListener = listener;
    }


    //  给饼状图配置数据
    private void initPieView() {
        mPieView.setColors(createColors());
        mPieView.setData(dataList);
        mPieView.setAnonymity(vote.getAnonymity().equals("0"));
    }
    private ArrayList<PieEntry> createData() {
        ArrayList<PieEntry> pieLists = new ArrayList<>();
        pieLists.add(new PieEntry(20.00F, "服装"));
        pieLists.add(new PieEntry(20.00F, "数码产品"));
        pieLists.add(new PieEntry(20.00F, "保健品"));
        pieLists.add(new PieEntry(20.00F, "户外运动用品"));
        pieLists.add(new PieEntry(20.00F, "其他"));
        return pieLists;
    }

    private ArrayList<Integer> createColors() {
        ArrayList<Integer> colorLists = new ArrayList<>();
        colorLists.add(Color.parseColor("#EBBF03"));
        colorLists.add(Color.parseColor("#ff4d4d"));
        colorLists.add(Color.parseColor("#8d5ff5"));
        colorLists.add(Color.parseColor("#41D230"));
        colorLists.add(Color.parseColor("#4FAAFF"));
        return colorLists;
    }

}

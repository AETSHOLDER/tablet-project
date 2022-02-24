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
import android.widget.TextView;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.PieEntry;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.widgets.PieView;
import java.util.ArrayList;
import java.util.List;


public class PieChartDialog extends AlertDialog {
    private Context context;
    private View.OnClickListener mButtonClickListener;
    private View mLayout;
    private TextView mTitle;
    private ImageView mButton;
    private TextView mBottom;
    private PieView mPieView;
    private PieChartDialog mDialog;
    private VoteListBean.VoteBean vote;
    private ArrayList<PieEntry> dataList = new ArrayList<>();
    public PieChartDialog(Context context, @StyleRes int themeResId, ArrayList<PieEntry> dataList, VoteListBean.VoteBean vote) {
        super(context, themeResId);
        this.context = context;
        this.dataList = dataList;
        this.vote = vote;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_piechart);

        mLayout = (LinearLayout) findViewById(R.id.layout_content);
        mButton = mLayout.findViewById(R.id.dialog_close_icon);
        mTitle = mLayout.findViewById(R.id.dialog_title);
        mPieView = mLayout.findViewById(R.id.pieView);
        mBottom = mLayout.findViewById(R.id.dialog_bottom);

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

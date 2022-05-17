/*
package com.example.paperlessmeeting_demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.WuHuListAdapter;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.widgets.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditWuHuActivity extends BaseActivity {
    @BindView(R.id.myList_view)
    MyListView myList_view;
    @BindView(R.id.line_colors)
    RadioGroup line_colors;
    @BindView(R.id.theme_colors)
    RadioGroup theme_colors;

    @BindView(R.id.add_topic_rl)
    RelativeLayout add_topic_rl;
    @BindView(R.id.sava_all)
    RelativeLayout sava_all;
    @BindView(R.id.line)
    View line;

     private WuHuListAdapter wuHuListAdapter;
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        WuHuEditBean wuHuEditBean=new WuHuEditBean();
        wuHuEditBean.setTopics("2022年临时会议");
        wuHuEditBean.setTopic_type("会议记录");
        WuHuEditBean.EditListBean editListBean=new WuHuEditBean.EditListBean();
        editListBean.setSubTopics("总结2022年");
        editListBean.setReportingUnit("王二狗，李狐狸，张太郎，刘毛毛");
        WuHuEditBean.EditListBean editListBean1=new WuHuEditBean.EditListBean();
        editListBean1.setSubTopics("总结2022年");
        editListBean1.setReportingUnit("王二狗，李狐狸，张太郎，刘毛毛");
        wuHuEditBeanList.add(editListBean1);
        wuHuEditBeanList.add(editListBean);
        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);

        wuHuListAdapter=new WuHuListAdapter(EditWuHuActivity.this,wuHuEditBeanList);
        myList_view.setAdapter(wuHuListAdapter);
        wuHuListAdapter.notifyDataSetChanged();


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_wuhu;
    }

    @Override
    protected void initView() {
        line_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.color_rb1:
                        Log.d("wwwwwww","dsffafaf111");
                        line.setBackgroundColor(Color.parseColor("#EA4318"));
                        break;
                    case R.id.color_rb2:
                        Log.d("wwwwwww","dsffafaf2222");
                        line.setBackgroundColor(Color.parseColor("#1D1D1D"));
                        break;

                }
            }
        });


        theme_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkId) {
                switch (checkId){
                    case R.id.color_rb3:
                        Log.d("wwwwwww","dsffafaf3333");
                        break;
                    case R.id.color_rb4:
                        Log.d("wwwwwww","dsffafaf444");
                        break;

                        case R.id.color_rb5:
                            Log.d("wwwwwww","dsffafaf555");
                        break;
                    case R.id.color_rb6:
                        Log.d("wwwwwww","dsffafaf6666");
                        break;
                    case R.id.color_rb7:
                        Log.d("wwwwwww","dsffafaf777");
                        break;

                }
            }
        });


    }

    @Override
    protected void initData() {

    }
   */
/* //重写此方法用来设置当点击activity外部时候，关闭此弹出框
    @Override9
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
*//*


    //此方法在布局文件中定义，用来保证点击弹出框内部的时候不会被关闭，如果不设置此方法则单击弹出框内部时候会导致弹出框关闭
    public void tip(View view){
        Toast.makeText(EditWuHuActivity.this,"点击弹窗外部关闭窗口",Toast.LENGTH_SHORT).show();

    }

}
*/

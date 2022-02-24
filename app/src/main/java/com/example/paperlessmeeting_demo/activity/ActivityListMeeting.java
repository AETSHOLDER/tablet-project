package com.example.paperlessmeeting_demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.MeetingListAdapter;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/10/6.
 */

public class ActivityListMeeting extends BaseActivity {
    @BindView(R.id.number_tv)
    TextView numberTv;
    @BindView(R.id.close_image)
    ImageView closeImage;
    @BindView(R.id.search_icon)
    ImageView searchIcon;
    @BindView(R.id.search_edittext)
    EditText searchEdittext;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.no_data)
    TextView noData;
    @BindView(R.id.search_btn)
    TextView searchBtn;
    private MeetingListAdapter meetingListAdapter;
    private List<AttendeBean> attendeBeanList = new ArrayList<>();
    private String contenStr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_meeting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
                /*
* 统计用户行为日志
* */
        if (Hawk.contains("UserBehaviorBean")) {
            UserBehaviorBean userBehaviorBean = Hawk.get("UserBehaviorBean");
            UserBehaviorBean.DataBean dataBean = new UserBehaviorBean.DataBean();
            dataBean.setTittile(this.getClass().getName());
            dataBean.setTime(TimeUtils.getTime(System.currentTimeMillis()));
            List<UserBehaviorBean.DataBean> dataBeanList = userBehaviorBean.getData();
            dataBeanList.add(dataBean);
            Hawk.put("UserBehaviorBean", userBehaviorBean);
        }

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListMeeting.this, ActivitySearchListMeeting.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

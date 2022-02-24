package com.example.paperlessmeeting_demo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.MeetingListAdapter;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 梅涛 on 2020/10/6.
 */

public class ActivitySearchListMeeting extends BaseActivity {
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
    @BindView(R.id.tittel_ll)
    LinearLayout tittelLl;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.list_ll)
    LinearLayout listLl;
    private MeetingListAdapter meetingListAdapter;
    private List<AttendeBean> attendeBeanList = new ArrayList<>();
    private String contenStr;
    private List<AttendeBean> getAttendeBeanList = new ArrayList<>();//前一页带回来的

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_list_meeting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        noData.setVisibility(View.GONE);
        listLl.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                attendeBeanList.clear();
                contenStr = searchEdittext.getText().toString().trim();
                if (contenStr.isEmpty()) {
                    Toast.makeText(ActivitySearchListMeeting.this, "搜索内容不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                getData(contenStr);
            }
        });
    }

    private void getData(String edtxtStr) {
        NetWorkManager.getInstance().getNetWorkApiService().getSearchMeetingUser(edtxtStr).compose(this.<BasicResponse<List<AttendeBean>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<AttendeBean>>>() {
                    @Override
                    protected void onSuccess(BasicResponse<List<AttendeBean>> response) {
                        Log.d("RigthConteagment2222", response.getData().toString() + "======");
                        if (response != null) {
                            setData(response);
                        }

                    }
                });
    }

    private void setData(BasicResponse<List<AttendeBean>> response) {
        attendeBeanList = (List<AttendeBean>) response.getData();
        if (attendeBeanList.size() > 0) {
            noData.setVisibility(View.GONE);
            listLl.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.VISIBLE);
            listLl.setVisibility(View.GONE);
        }
        meetingListAdapter = new MeetingListAdapter(ActivitySearchListMeeting.this, attendeBeanList);
        listView.setAdapter(meetingListAdapter);
        meetingListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

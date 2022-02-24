package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.ActivityBrowser;
import com.example.paperlessmeeting_demo.activity.ActivitySearchListMeeting;
import com.example.paperlessmeeting_demo.activity.CalculatorActivity;
import com.example.paperlessmeeting_demo.activity.PlayerLiveActivity;
import com.example.paperlessmeeting_demo.activity.WhiteBoardActivity;
import com.example.paperlessmeeting_demo.activity.WhiteBoardActivity2;
import com.example.paperlessmeeting_demo.adapter.GridDailtsAdapter;
import com.example.paperlessmeeting_demo.adapter.MeetingListAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.HomeGridBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 梅涛 on 2020/9/21.
 */
@SuppressLint("ValidFragment")
public class RigthContentFragment extends BaseFragment implements MeetingListAdapter.InterfaceSign {

    @BindView(R.id.gridview)
    GridView gridview;
    Unbinder unbinder;
    @BindView(R.id.file_fragment)
    FrameLayout fileFragmentLayout;
    @BindView(R.id.root_ll)
    RelativeLayout root_ll;

    private GridDailtsAdapter gridDailtsAdapter;
    private List<HomeGridBean> homeGridBeanList = new ArrayList<>();
    private String titles;
    private Context context;
    private AlertDialog serviceDialog;
    private AlertDialog results;
    private AlertDialog meetDialog;
    private MeetingListAdapter meetingListAdapter;
    private List<AttendeBean> attendeBeanList = new ArrayList<>();
    private InformationFragment fileFragment;
    private VoteListFragment voteListFragment;
    private MyReceiver myReceiver;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView numberTv;

    public RigthContentFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public RigthContentFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rigth_content;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initData() {
        /*
        * 白色透明度
        * */
        root_ll.getBackground().setAlpha(230);
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
        meetingListAdapter = new MeetingListAdapter(getActivity(), attendeBeanList);//人员列表
        meetingListAdapter.setInterfaceSign(this);
        //  getUserList();//获取人员列表
        HomeGridBean homeGridBean1 = new HomeGridBean(R.mipmap.ic_meeting_file, "会议文件", 1);
        HomeGridBean homeGridBean2 = new HomeGridBean(R.mipmap.ic_decide_ballot, "会议投票", 2);
        HomeGridBean homeGridBean3 = new HomeGridBean(R.mipmap.ic_attendees, "参会名单", 3);
        HomeGridBean homeGridBean4 = new HomeGridBean(R.mipmap.ic_meeting_services, "会议服务", 4);
        HomeGridBean homeGridBean5 = new HomeGridBean(R.mipmap.ic_browser, "浏览器", 5);
        HomeGridBean homeGridBean6 = new HomeGridBean(R.mipmap.ic_whiteboard, "白板", 6);
        HomeGridBean homeGridBean7 = new HomeGridBean(R.mipmap.ic_video_services, "视频服务", 7);
        HomeGridBean homeGridBean8 = new HomeGridBean(R.mipmap.ic_tools, "工具", 8);
        homeGridBeanList.add(homeGridBean1);
        homeGridBeanList.add(homeGridBean2);
        homeGridBeanList.add(homeGridBean3);
        homeGridBeanList.add(homeGridBean4);
        homeGridBeanList.add(homeGridBean5);
        homeGridBeanList.add(homeGridBean6);
        homeGridBeanList.add(homeGridBean7);
        homeGridBeanList.add(homeGridBean8);
        gridDailtsAdapter = new GridDailtsAdapter(getActivity(), homeGridBeanList);
        gridview.setAdapter(gridDailtsAdapter);
        gridDailtsAdapter.notifyDataSetChanged();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeGridBean homeGridBean = (HomeGridBean) adapterView.getAdapter().getItem(i);
                if ("会议文件".equals(homeGridBean.getName())) {
                  /*  fileFragment = new InformationFragment("文件", getActivity());
                    getFragmentManager()   //所得到的是所在fragment 的父容器的管理器
                            .beginTransaction()
                            .addToBackStack(null)   //添加到后退栈
                            //此处的id，是FrameLayout的id
                            .add(R.id.file_fragment, fileFragment)   //anotherFragment()为新建的fragment的java文件，OnCreateView里面把.xml文件添加
                            .commit();*/
                /*    fileFragment = new FileFragment(fragmentManager);
                    fragmentTransaction.add(R.id.file_fragment, fileFragment);
                    //事务处理完需要提交
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("tab", "file");
                    intent.putExtras(bundle);
                    intent.setAction(constant.FRESH_TAB);
                    getActivity().sendBroadcast(intent);
                } else if ("会议投票".equals(homeGridBean.getName())) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("tab", "vote");
                    intent.putExtras(bundle);
                    intent.setAction(constant.FRESH_TAB);
                    getActivity().sendBroadcast(intent);
                  /*  voteListFragment = new VoteListFragment(fragmentManager);
                    fragmentTransaction.add(R.id.file_fragment, voteListFragment);
                    //事务处理完需要提交
                    fragmentTransaction.commit();*/
                   /* voteListFragment = new VoteListFragment("投票", getActivity());
                    getFragmentManager()   //所得到的是所在fragment 的父容器的管理器
                            .beginTransaction()
                            .addToBackStack(null)   //添加到后退栈
                            //此处的id，是FrameLayout的id
                            .add(R.id.file_fragment, voteListFragment)   //anotherFragment()为新建的fragment的java文件，OnCreateView里面把.xml文件添加
                            .commit();*/
                } else if ("白板".equals(homeGridBean.getName())) {
                   /* Intent intent = new Intent();
                    //设置intent的动作为com.example.broadcast，可以任意定义
                    intent.setAction(constant.WHITEBOARD_BROADCAST);
                    //发送无序广播
                    context.sendBroadcast(intent);*/
                    Intent intent = null;
                    if (UserUtil.isTempMeeting) {
                        intent = new Intent(getActivity(), WhiteBoardActivity2.class);
                    } else {
                        intent = new Intent(getActivity(), WhiteBoardActivity.class);
                    }
//                    intent = new Intent(getActivity(), WhiteBoardActivity2.class);
//                    Intent intent = new Intent(getActivity(), WhiteBroardBrowser.class);
                    startActivity(intent);
                } else if ("参会名单".equals(homeGridBean.getName())) {
                  /*  Intent intent = new Intent(getActivity(), ActivityListMeeting.class);
                    startActivity(intent);*/
                    showMeetDialog();
                } else if ("会议服务".equals(homeGridBean.getName())) {
                    showDialog();
                } else if ("视频服务".equals(homeGridBean.getName())) {
                    Intent intent = new Intent(getActivity(), PlayerLiveActivity.class);
                    startActivity(intent);
                } else if ("浏览器".equals(homeGridBean.getName())) {
                    Intent intent = new Intent(getActivity(), ActivityBrowser.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if ("工具".equals(homeGridBean.getName())) {
                    Intent intent = new Intent(getActivity(), CalculatorActivity.class);
                    startActivity(intent);
/*                    Intent intent = new Intent(getActivity(), ActivityMemberSsignApproval.class);
                    startActivity(intent);*/
                /*    Intent intent = new Intent(getActivity(), ActivityMinuteMeeting.class);
                    startActivity(intent);*/
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        if(!UserUtil.isTempMeeting){
            return;
        }
        if(message.getType().equals(MessageReceiveType.MessageServer)){
            return;
        }
        if (message.getMessage().contains(constant.QUERYATTEND)) {
            try {
                TempWSBean<ArrayList<AttendeBean>> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<ArrayList<AttendeBean>>>() {
                }.getType());
                attendeBeanList = wsebean.getBody();

                if(attendeBeanList != null){
                    numberTv.setText("人员列表  " + attendeBeanList.size() + "/" + attendeBeanList.size());
                    meetingListAdapter.setGridViewBeanList(attendeBeanList);
                    meetingListAdapter.notifyDataSetChanged();
                    Log.d("dvgrg222", "路过~~~~attendeBeanList=" + attendeBeanList.size());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            return;
        }

    }


    private void getUserList() {
       /* if (Hawk.contains("_id")) {
            _id = Hawk.get("_id");
        }*/
       // 临时会议直接显示机号
       if(UserUtil.isTempMeeting){
           // 查询参会人员
           TempWSBean bean = new TempWSBean();
           bean.setReqType(0);
           bean.setUserMac_id(FLUtil.getMacAddress());
           bean.setPackType(constant.QUERYATTEND);
           bean.setBody("");
           String strJson = new Gson().toJson(bean);
           JWebSocketClientService.sendMsg(strJson);
       }else {
           if (attendeBeanList != null) {
               attendeBeanList.clear();
           }
           String _id = UserUtil.meeting_record_id;
           NetWorkManager.getInstance().getNetWorkApiService().getMeetingUserList(_id).compose(this.<BasicResponse<List<AttendeBean>>>bindToLifecycle())
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new DefaultObserver<BasicResponse<List<AttendeBean>>>() {
                       @Override
                       protected void onSuccess(BasicResponse<List<AttendeBean>> response) {
                           Log.d("RigthContragment2222", response.getData().toString() + "======路过");
                           attendeBeanList = (List<AttendeBean>) response.getData();
                           numberTv.setText("人员列表  " + attendeBeanList.size() + "/" + attendeBeanList.size());
                           meetingListAdapter.setGridViewBeanList(attendeBeanList);
                           meetingListAdapter.notifyDataSetChanged();
                           Log.d("dvgrg222", "路过~~~~attendeBeanList=" + attendeBeanList.size());
                       }
                   });
       }

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.InfoDialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_service, null);
        ImageView close_image = view.findViewById(R.id.close_image);
        LinearLayout root_ll = view.findViewById(R.id.root_ll);
        RelativeLayout water_rl = view.findViewById(R.id.water_rl);
        RelativeLayout pen_rl = view.findViewById(R.id.pen_rl);
        RelativeLayout other_rl = view.findViewById(R.id.other_rl);
        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceDialog.dismiss();
            }
        });
        water_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResualtDialog();
            }
        });
        pen_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResualtDialog();
            }
        });
        other_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResualtDialog();
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        serviceDialog = builder.create();
        serviceDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = serviceDialog.getWindow().getAttributes();
        lp.width = (int) (display.getHeight() * 0.8); //设置宽度
        serviceDialog.getWindow().setAttributes(lp);
    }

    private void showResualtDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.InfoDialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_results, null);
        builder.setView(view);
        builder.setCancelable(true);
        results = builder.create();
        results.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                results.dismiss();
            }
        }, 1500);
    }

    private void showMeetDialog() {
        getUserList();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_list_meeting, null);
        LinearLayout root_ll = view.findViewById(R.id.root_ll);
        numberTv = view.findViewById(R.id.number_tv);
        ImageView closeImage = view.findViewById(R.id.close_image);
        ImageView searchIcon = view.findViewById(R.id.search_icon);
        EditText searchEdittext = view.findViewById(R.id.search_edittext);
        ListView listView = view.findViewById(R.id.listView);
        TextView noData = view.findViewById(R.id.no_data);
        TextView searchBtn = view.findViewById(R.id.search_btn);
        RelativeLayout search_rl = view.findViewById(R.id.search_rl);
        builder.setView(view);
        builder.setCancelable(true);
        meetDialog = builder.create();
        listView.setAdapter(meetingListAdapter);
        meetingListAdapter.notifyDataSetChanged();
        //     Hawk.put("attendeBeanList", attendeBeanList);
        Log.d("dvgrg111", "路过~~~~attendeBeanList=" + attendeBeanList.size());
        meetDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = meetDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
        meetDialog.getWindow().setAttributes(lp);

        if(UserUtil.isTempMeeting){
            search_rl.setVisibility(View.GONE);
        }
        search_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySearchListMeeting.class);
                startActivity(intent);

            }
        });
        searchEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySearchListMeeting.class);
                startActivity(intent);

            }
        });

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meetDialog.dismiss();

            }
        });
    }

    @Override
    public void Sign(String user_id, String meeting_record_id) {
        if(UserUtil.isTempMeeting){
            ToastUtil.makeText(getActivity(),"临时会议暂不能签到!");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("meeting_record_id", meeting_record_id);
        NetWorkManager.getInstance().getNetWorkApiService().sign(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onFail(BasicResponse response) {
                        Toast.makeText(getActivity(), response.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {
                        getUserList();
                    }
                });
    }

    @Override
    protected void initView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.REMOVE_BROADCAST);
        getActivity().registerReceiver(myReceiver, filter);
        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (myReceiver != null) {
            getActivity().unregisterReceiver(myReceiver);
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("voteListFragment", "移除广播");
            // fileFragmentLayout.removeAllViews();
            if (fileFragment != null) {
                getFragmentManager().popBackStack();
            }
            if (voteListFragment != null) {
                getFragmentManager().popBackStack();
              /*  getFragmentManager()   //所得到的是所在fragment 的父容器的管理器
                        .beginTransaction().remove(voteListFragment);
                getFragmentManager()   //所得到的是所在fragment 的父容器的管理器
                        .beginTransaction().hide(voteListFragment);*/
                //   fragmentTransaction.remove(voteListFragment);
                //   voteListFragment.onDestroyView();

            }
        }
    }
}

package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.Sign.MyImageDialog;
import com.example.paperlessmeeting_demo.activity.WuHuActivity;
import com.example.paperlessmeeting_demo.adapter.RecycleAdapter;
import com.example.paperlessmeeting_demo.adapter.VoteAdapter;
import com.example.paperlessmeeting_demo.adapter.VoteChairmanAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuImaResultAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuMultipleGridleAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuRecycleAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuSingleGridleAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuTextResultAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuVoteAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.ChoseBean;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.ItemBean;
import com.example.paperlessmeeting_demo.bean.PieEntry;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean.VoteBean;
import com.example.paperlessmeeting_demo.bean.WuHuDeleteFragmentBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.WuHuNetFileBean;
import com.example.paperlessmeeting_demo.bean.WuHuVoteResultBean;
import com.example.paperlessmeeting_demo.dialog.CheckBoxDialog;
import com.example.paperlessmeeting_demo.dialog.PieChartDialog;
import com.example.paperlessmeeting_demo.dialog.RadioDialog;
import com.example.paperlessmeeting_demo.dialog.VoteInfoDialog;
import com.example.paperlessmeeting_demo.dialog.WuHuCheckBoxDialog;
import com.example.paperlessmeeting_demo.dialog.WuHuPieChartDialog;
import com.example.paperlessmeeting_demo.dialog.WuHuRadioDialog;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.sharefile.SocketShareFileManager;
import com.example.paperlessmeeting_demo.tool.Base642BitmapTool;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.tool.DownloadUtil;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.PermissionManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.ServerManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.widgets.DrawableTextView;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.example.paperlessmeeting_demo.widgets.ZoomImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrgao.luckly_popupwindow.LucklyPopopWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.hawk.Hawk;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.vudroid.core.utils.MD5StringUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressLint("ValidFragment")
public class WuHUVoteListFragment extends BaseFragment implements VoteAdapter.voteClickListener, WuHuVoteAdapter.voteClickListener, EasyPermissions.PermissionCallbacks, WuHuRecycleAdapter.ChoseOptionItemImaListener, WuHuRecycleAdapter.SeePhotosItemImaListener, View.OnClickListener,WuHuVoteAdapter.viewResultsInterface,WuHuVoteAdapter.voteInterFace,WuHuVoteAdapter.endInterFace,WuHuRecycleAdapter.deleteOpitionIntetface {
    private SocketShareFileManager socketShareFileManager;
    private boolean isOptionShow=false;
    private MyBroadcastReceiver  myBroadcastReceiver;
    private MyBroadcastReceiver  myVoteImagBroadcastReceiver;
    private  ArrayList<String> fileNames = new ArrayList<>();
    private  ArrayList<String> paths = new ArrayList<>();
    private List<String> stringListIp = new ArrayList<>();
    private String   votePath = Environment.getExternalStorageDirectory().getPath() + constant.VOTE_FILE;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            String selfIp = "";
            String stIp = "";
            switch (msg.what) {
                case 0:
                    progressBar_ll.setVisibility(View.GONE);
                    break;


            }


        }
    };




    @Override
    protected int getLayoutId() {
        if (UserUtil.ISCHAIRMAN) {
            return R.layout.activity_wuhu_vote_list_chairman;
        } else {
            return R.layout.activity_wuhu_vote_list_chairman;
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initView() {

        vote_type_ll.setVisibility(View.GONE);
        vote_imag_rl.setOnClickListener(this);
        vote_text_rl.setOnClickListener(this);
        no_vote_add_rl.setOnClickListener(this);
        no_vote_add_type.setOnClickListener(this);
        add_wuhu_vote_rl.setOnClickListener(this);
        no_vote_iam_rl.setOnClickListener(this);
        no_vote_text_rl.setOnClickListener(this);
        refresh_rl.setOnClickListener(this);
        RelativeLayout no_vote_text_rl;
        back_ll.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vote_imag_rl:
                vote_type_ll.setVisibility(View.GONE);
                add_wuhu_vote.setImageResource(R.mipmap.ic_add_wuhu_vote);
                showNewVoteItem("2");
                break;
            case R.id.vote_text_rl:
                vote_type_ll.setVisibility(View.GONE);
                add_wuhu_vote.setImageResource(R.mipmap.ic_add_wuhu_vote);
                showNewVoteItem("1");
                break;
            case R.id.add_wuhu_vote_rl:
                if (!isOptionShow){
                    vote_type_ll.setVisibility(View.VISIBLE);
                    isOptionShow=true;
                    add_wuhu_vote.setImageResource(R.mipmap.ic_add_wuhu_vote);
                }else {
                    vote_type_ll.setVisibility(View.GONE);
                    add_wuhu_vote.setImageResource(R.mipmap.ic_add_wuhu_vote);
                    isOptionShow=false;
                }

                break;
            case R.id.back_ll:
                Intent intent = new Intent();
                intent.setAction(constant.FINISH_WUHUVOTEACTIVITY_BROADCAST);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.no_vote_add_rl:
            no_vote_add_rl.setVisibility(View.GONE);
            no_vote_add_type.setVisibility(View.VISIBLE);
                break ;

            case R.id.no_vote_iam_rl:
               showNewVoteItem("2");
                no_vote_add_type.setVisibility(View.GONE);
            break ;
            case R.id.no_vote_text_rl:
                showNewVoteItem("1");
                no_vote_add_type.setVisibility(View.GONE);
                break;
            case R.id.refresh_rl:
                loadData();
                break;
        }

    }
    @Override
    protected void initData() {
        /**
         * ????????????????????????
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
        loadData();
    }

    @BindView(R.id.vote_refreshLayout)
    protected RefreshLayout home_refreshLayout;

    @BindView(R.id.recycle_view)
    protected RecyclerView rv;
    @BindView(R.id.vote_type_ll)
    LinearLayout vote_type_ll;

    @BindView(R.id.vote_text_rl)
    RelativeLayout vote_text_rl;
    @BindView(R.id.vote_imag_rl)
    RelativeLayout vote_imag_rl;

    @BindView(R.id.add_wuhu_vote_rl)
    RelativeLayout add_wuhu_vote_rl;

    @BindView(R.id.add_wuhu_vote)
    ImageView add_wuhu_vote;
    @BindView(R.id.back_ll)
    LinearLayout back_ll;
    @BindView(R.id.no_vote_add_rl)
    RelativeLayout no_vote_add_rl;

    @BindView(R.id.no_vote_add_type)
    RelativeLayout no_vote_add_type;

    @BindView(R.id.no_vote_iam_rl)
    RelativeLayout no_vote_iam_rl;

    @BindView(R.id.no_vote_text_rl)
    RelativeLayout no_vote_text_rl;
    @BindView(R.id.progressBar_ll)
    RelativeLayout progressBar_ll;

    @BindView(R.id.refresh_rl)
    RelativeLayout refresh_rl;
    List<VoteBean> voteList = new ArrayList<>();
    private LucklyPopopWindow mLucklyPopopWindow;
    private RecyclerView.LayoutManager mLayoutManager;
    //  ??????1
    WuHuVoteAdapter voteAdapter1;
    WuHuVoteAdapter voteAdapter2;

    private String[] popTitleArr;
    private String TAG = "WuHUVoteListFragment";
    //  ??????????????????
    private ImageView currentSelDanxuan;
    //  ??????????????????
    private ImageView currentSelNiming;
    private WuHuRecycleAdapter adapter;
    private List<ItemBean> list = new ArrayList<ItemBean>();
    LinearLayout layoutContent;
    RecyclerView mRecyclerView;
    ImageView add_chose;
    ImageView close_icon;
    Button btn_pos;
    Button btn_neg;
    ImageView ivCheckState1;
    ImageView ivCheckState2;
    ImageView ivCheckState3;
    ImageView ivCheckState4;

    EditText edit_content;

    EditText edit_text;
    EditText edit_text2;
    EditText edit_text3;
    TextView  text_title;
    private AlertDialog results;
    private AlertDialog newResults;
    private AlertDialog optionFormDialog;
    private AlertDialog imaDialog;
    private FragmentManager fragmentManager;
    private String titles;
    private Context context;
    private String currentEndDate="22222";
    private int positionIma = -1;
    private ArrayList<VoteBean> tempList = new ArrayList<>();//????????????
    private int a,b,c,d,e,f,g,h;
    private StringBuilder strA,strB,strC,strD,strE,strF,strG,strH;
    private int allVoteNumb;//?????????
    private boolean isJustComeIn=true;
    private String refreshDataFlag="0";
    public WuHUVoteListFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public WuHUVoteListFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public WuHUVoteListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.SEE_IMA_BROADCAST);
        getActivity().registerReceiver(myBroadcastReceiver, filter);


        myVoteImagBroadcastReceiver=new MyBroadcastReceiver();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(constant.RUSH_VOTE_LIST_BROADCAST);
        getActivity().registerReceiver(myVoteImagBroadcastReceiver, filter2);

        voteAdapter1 = new WuHuVoteAdapter(getActivity());
        voteAdapter2 = new WuHuVoteAdapter(getActivity());
        voteAdapter2.setEf(this);//??????
        voteAdapter2.setVf(this);//??????
        voteAdapter2.setVr(this);//????????????
        socketShareFileManager = new SocketShareFileManager(mHandler);
     //??????????????????
        voteAdapter1.setVf(this);//??????
        voteAdapter1.setVr(this);//????????????
        DrawableTextView new_vote = null;
        if (UserUtil.ISCHAIRMAN) {
            new_vote = rootView.findViewById(R.id.new_vote);
            voteAdapter2.setOnVoteListener(this);
            new_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptionForm();

                }
            });
        } else {

            voteAdapter1.setOnVoteListener(this);
        }

        checkWritePermission1();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager = layoutManager;
        rv.setLayoutManager(layoutManager);
        home_refreshLayout.setEnableLoadMore(false);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        rv.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(dividerItemDecoration);
        rv.setHasFixedSize(true);


        if (UserUtil.ISCHAIRMAN) {
            rv.setAdapter(voteAdapter2);
        } else {
            rv.setAdapter(voteAdapter1);
        }
        // ??????socket??????
        EventBus.getDefault().register(this);

        if (!UserUtil.ISCHAIRMAN) {
            no_vote_add_rl.setVisibility(View.GONE);
            add_wuhu_vote_rl.setVisibility(View.GONE);
            no_vote_add_type.setVisibility(View.GONE);
        }else {
            if (voteList.size()<1){
                add_wuhu_vote_rl.setVisibility(View.GONE);
                no_vote_add_rl.setVisibility(View.VISIBLE);
                no_vote_add_type.setVisibility(View.GONE);
            }else {
                add_wuhu_vote_rl.setVisibility(View.VISIBLE);
                no_vote_add_rl.setVisibility(View.GONE);
                no_vote_add_type.setVisibility(View.GONE);
            }

        }
        return rootView;
    }

    /**
     * ??????websocket ??????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        tempList.clear();
        //  client??????????????????
        if (message.getType().equals(MessageReceiveType.MessageClient)) {
            // ??????????????????
            if (message.getMessage().contains(constant.QUERYVOTE)) {
           /*     try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }*/
              //  voteList.clear();
                Log.d(TAG, "wuhu????????????: " + message.toString());
                Log.e(TAG, "wuhuonReceiveMsg11111: " + message.toString());
                try {
                    TempWSBean<List> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<List<VoteBean>>>() {
                    }.getType());
                    //  ??????vote???websocket?????????
                    if (wsebean != null) {

                        voteList = wsebean.getBody();
                        refreshDataFlag= wsebean.getFlag();
                        String flag = wsebean.getFlag();//???????????????????????????????????????
                        Log.d("gdgsdgsdgdgf444555",flag+"");
                        for (VoteListBean.VoteBean bean : voteList) {
                            bean.setStatus(bean.getStatus());
                        }

                        VoteListBean voteListBean=new VoteListBean();

                    /*    if (Hawk.contains("VOTE")){
                            List<VoteBean> voteBeanArrayList= new ArrayList<>();
                            Map<String,List<VoteBean>> sites2 = new HashMap<String,List<VoteBean>>();
                            if (Hawk.get("VOTE")!=null){
                                sites2=(HashMap<String,List<VoteBean>>)Hawk.get("VOTE");
                                if (sites2!=null&&sites2.size()>0) {
                                    Iterator<Map.Entry<String, List<VoteBean>>> iterator = sites2.entrySet().iterator();
                                    while (iterator.hasNext()) {
                                        Map.Entry<String, List<VoteBean>> entry = iterator.next();
                                        if (entry.getKey().equals(UserUtil.meeting_record_id) && entry.getKey() != null) {

                                            if (entry.getValue() != null && entry.getValue().size() > 0) {

                                                voteBeanArrayList= (List<VoteBean> )entry.getValue();
                                                Log.d("svcvcxvcv",voteBeanArrayList.size()+"");

                                                voteList.addAll(voteBeanArrayList);

                                            }

                                        }

                                    }
                                }
                            }
                        }*/
                        List<VoteBean>  currentVoteBeanlist=new ArrayList<>();
                           currentVoteBeanlist.clear();
                        if (Hawk.contains("VOTE")){
                            List<VoteBean>  voteBeanlist=new ArrayList<>();
                            if (Hawk.get("VOTE")!=null){
                                voteBeanlist=Hawk.get("VOTE");

                                for (int i=0;i<voteBeanlist.size();i++){
                                    //Log.d("fgfffgff",voteBeanlist.get(i).getMeeting_record_id()+"   = "+UserUtil.meeting_record_id);
                                    if (voteBeanlist.get(i).getMeeting_record_id().equals(UserUtil.meeting_record_id)){
                                        Log.d("fgfffgff",voteBeanlist.get(i).getMeeting_record_id()+"   = "+UserUtil.meeting_record_id);
                                        currentVoteBeanlist.add(voteBeanlist.get(i));
                                    }

                                }

                            }

                        }
                        if (currentVoteBeanlist!=null&&currentVoteBeanlist.size()>0){

                            voteList.addAll(currentVoteBeanlist);

                        }

                        for (int y = 0; y < voteList.size() - 1; y++) {
                            for (int j = voteList.size() - 1; j > y; j--) {
                                if (voteList.get(j).getTopic().equals(voteList.get(y).getTopic())) {
                                    voteList.remove(j);
                                }
                            }
                        }


                        voteListBean.setData(voteList);

                        Hawk.put("VoteListBean",voteListBean);

                        refreshUI(voteList, flag);
                        if (UserUtil.ISCHAIRMAN){

                        }else {
                            File path = new File(votePath);
                            File[] files = path.listFiles();// ??????
                            if (files == null) {
                                return;
                            }
                            getVoteFile(files);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (message.getMessage().contains(constant.NEWVOTE)) {
             //   progressBar_ll.setVisibility(View.VISIBLE);
                Log.d(TAG, "wuhu??????????????????: " + message.toString());
                Log.e(TAG, "wuhuonReceiveMsg1111122222: " + message.toString());
                try {
                    TempWSBean<VoteBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<VoteBean>>() {
                    }.getType());
                    //  ??????vote???websocket?????????
                    if (wsebean != null) {
                        refreshDataFlag= wsebean.getFlag();
                        VoteBean  voteBean=wsebean.getBody();

                        if (voteBean!=null){
                            voteList.add(voteBean);
                            String flag = wsebean.getFlag();//???????????????????????????????????????
                            for (VoteListBean.VoteBean bean : voteList) {
                                bean.setStatus(bean.getStatus());
                            }
                            Log.d("gdgsdgsdgdgf444888",flag+"");

                          /*  Map<String,List<VoteBean>> sites2 = new HashMap<String,List<VoteBean>>();
                            sites2.put(UserUtil.meeting_record_id,voteList);*/
                            Hawk.put("VOTE",voteList);

                            refreshUI(voteList, flag);

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (message.getMessage().contains(constant.SURENAME)) {
                loadData();
            }
        } else {  //  ???????????????

        }

    }

    //????????????
    private void showOptionForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_option_form, null);
        RadioGroup rg = view.findViewById(R.id.rg_option_form);
        RadioButton tv = view.findViewById(R.id.rb_tv);
        RadioButton ima = view.findViewById(R.id.rb_ima);
        builder.setView(view);
        builder.setCancelable(true);
        optionFormDialog = builder.create();
        optionFormDialog.show();

        Window window = optionFormDialog.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.4);//?????????
        p.height = (int) (height * 0.3);//?????????
        window.setAttributes(p);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // ??????????????????????????????
                switch (checkedId) {
                    case R.id.rb_tv:
                        // ??????
                        showNewVoteItem("1");
                        optionFormDialog.dismiss();
                        break;
                    case R.id.rb_ima:
                        // ??????
                        showNewVoteItem("2");
                        optionFormDialog.dismiss();
                        break;
                }
            }
        });

    }

    //????????????
    private void showIma(int imaPath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_see_ima, null);
        ZoomImageView imageView = view.findViewById(R.id.see_ima);
        builder.setView(view);
        builder.setCancelable(true);
        imaDialog = builder.create();
        imaDialog.show();
        Window window = imaDialog.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.8);//?????????
        p.height = (int) (height * 0.8);//?????????
        window.setAttributes(p);
        ImageLoader.getInstance().displayImage("file://" + list.get(imaPath).getText(), imageView);
    }

    //flag  1:????????????  2???????????????
    private void showNewVoteItem(String flag) {
        fileNames.clear();
        paths.clear();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = null;
        if (flag.equals("1")) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_newvote, null);
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_newvote_ima, null);
        }

        layoutContent = view.findViewById(R.id.layout_newvote);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        add_chose = view.findViewById(R.id.add_chose);//???????????????
        close_icon = view.findViewById(R.id.close_icon);
        btn_pos = view.findViewById(R.id.btn_pos);//??????
        btn_neg = view.findViewById(R.id.btn_neg);//??????
        ivCheckState1 = view.findViewById(R.id.iv_check_state1);
        ivCheckState2 = view.findViewById(R.id.iv_check_state2);
        ivCheckState3 = view.findViewById(R.id.iv_check_state3);
        ivCheckState4 = view.findViewById(R.id.iv_check_state4);
        edit_content = view.findViewById(R.id.edit_content);
        edit_text = view.findViewById(R.id.edit_text);
        edit_text2 = view.findViewById(R.id.edit_text2);
        edit_text3 = view.findViewById(R.id.edit_text3);
        text_title= view.findViewById(R.id.text_title);
        edit_text3.setFocusable(false);
        edit_text3.setKeyListener(null);
        currentSelDanxuan = ivCheckState1;
        currentSelNiming = ivCheckState4;
        builder.setView(view);
        builder.setCancelable(false);
        results = builder.create();
        results.show();

        Window window = results.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.8);//?????????
        p.height = (int) (height * 0.8);//?????????
        window.setAttributes(p);
        if (flag.equals("1")) {
            text_title.setText("????????????-??????");
        } else {
            text_title.setText("????????????-??????");
        }

        initRecycle(flag);
        if (list.size()>0){
            add_chose.setVisibility(View.GONE);
        }
        edit_text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickDialog dialog = new DatePickDialog(getActivity());
                //????????????????????????
                dialog.setYearLimt(3);
                //????????????
                dialog.setTitle("????????????");
                //????????????
                dialog.setType(DateType.TYPE_ALL);
                //?????????????????????????????????????????????
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");
                //??????????????????
                dialog.setOnChangeLisener(null);
                //??????????????????????????????
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = format.format(date);
                        Log.d("DatePickDialog", str);
                        edit_text3.setText(str);
                        currentEndDate = str;
                    }
                });
                dialog.show();
            }
        });
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                results.dismiss();
            }
        });
        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!UserUtil.ISCHAIRMAN) {
                    no_vote_add_rl.setVisibility(View.GONE);
                    add_wuhu_vote_rl.setVisibility(View.GONE);
                    no_vote_add_type.setVisibility(View.GONE);
                }else {
                    if (voteList.size()<1){
                        add_wuhu_vote_rl.setVisibility(View.GONE);
                        no_vote_add_rl.setVisibility(View.VISIBLE);
                        no_vote_add_type.setVisibility(View.GONE);
                    }else {
                        add_wuhu_vote_rl.setVisibility(View.VISIBLE);
                        no_vote_add_rl.setVisibility(View.GONE);
                        no_vote_add_type.setVisibility(View.GONE);
                    }

                }
                results.dismiss();
            }
        });
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_content.getText())) {
                    Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edit_content.getText().length() < 5) {
                    Toast.makeText(getActivity(), "??????????????????5?????????", Toast.LENGTH_SHORT).show();
                    return;
                }


               /* if (TextUtils.isEmpty(currentEndDate)) {
                    Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                ArrayList<String> options_list = new ArrayList();
                ArrayList<VoteBean.TemporBean> temporBeanArrayList = new ArrayList();
                VoteBean.TemporBean  temporBean1=new VoteBean.TemporBean();
                VoteBean.TemporBean  temporBean2=new VoteBean.TemporBean();
                //?????????????????????
                if (flag.equals("1")) {
                    if (TextUtils.isEmpty(edit_text.getText())) {
                        Toast.makeText(getActivity(), "???????????????1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(edit_text2.getText())) {
                        Toast.makeText(getActivity(), "???????????????2", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    temporBean1.setContent(edit_text.getText().toString());
                    temporBean1.setChecked(false);

                    temporBean2.setContent(edit_text2.getText().toString());
                    temporBean2.setChecked(false);
                    temporBeanArrayList.add(temporBean1);
                    temporBeanArrayList.add(temporBean2);
                    options_list.add(edit_text.getText().toString());
                    options_list.add(edit_text2.getText().toString());
                    //  ?????????????????????
                    for (int i = 0; i < list.size(); i++) {
                        ItemBean bean = list.get(i);
                        VoteBean.TemporBean  temporBean3=new VoteBean.TemporBean();
                        if (TextUtils.isEmpty(bean.getText())){
                            Toast.makeText(getActivity(),"??????????????????",Toast.LENGTH_SHORT).show();
                            return;

                        }
                        if (!TextUtils.isEmpty(bean.getText())) {
                            bean.setFlag("1");
                            options_list.add(bean.getText());
                            temporBean3.setContent(bean.getText());
                            temporBean3.setChecked(false);
                            temporBeanArrayList.add(temporBean3);
                        }
                    }

                } else {
                    //?????????????????????

                    //  ?????????????????????
                    for (int i = 0; i < list.size(); i++) {
                        ItemBean bean = list.get(i);
                        VoteBean.TemporBean  temporBean4=new VoteBean.TemporBean();
                        if (TextUtils.isEmpty(bean.getText())){
                            Toast.makeText(getActivity(),"??????????????????",Toast.LENGTH_SHORT).show();
                            return;

                        }
                        if (!TextUtils.isEmpty(bean.getText())) {
                            try {
                                bean.setFlag("2");
                              //  temporBean4.setContent(byte2Base64(readStream(bean.getText())));
                                temporBean4.setContent(bean.getText());
                                temporBean4.setChecked(false);
                                temporBean4.setFileName(bean.getFileName());//??????Bitmap
                              // temporBean4.setVotePath(bean.);
                                temporBeanArrayList.add(temporBean4);
                                options_list.add(bean.getText());
                               // options_list.add(byte2Base64(readStream(bean.getText())));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }


                VoteBean voteBean = new VoteBean();
                VoteBean.FromBean fromBean = new VoteBean.FromBean();
               // fromBean.setName(Hawk.get(constant.myNumber));
                fromBean.setName(UserUtil.user_name);
                fromBean.set_id(FLUtil.getMacAddress());
                voteBean.setMeeting_record_id(UserUtil.meeting_record_id);
                voteBean.setTemporBeanList(temporBeanArrayList);
                voteBean.setTopic(edit_content.getText().toString());
                voteBean.setOptions(options_list);
                voteBean.setTemporBeanList(temporBeanArrayList);
                voteBean.setEnd_time(currentEndDate);
                voteBean.setType(currentSelDanxuan == ivCheckState1 ? "0" : "1");
                voteBean.setAnonymity(currentSelNiming == ivCheckState3 ? "1" : "0");
                voteBean.setFrom(fromBean);
                voteBean.setFlag(flag);
                // ??????????????????????????????
                voteBean.setUser_list(new ArrayList<>());
                voteBean.setStatus("ENABLE");
//                Map<String,Object> map = new HashMap<>();
//                map.put("topic",edit_content.getText().toString());
////                map.put("c_id",UserUtil.c_id);
//                map.put("status","ENABLE");
//                map.put("options",options_list);
//                map.put("end_time",currentEndDate);
//                map.put("type",currentSelDanxuan == ivCheckState1 ? "0" : "1");
//                map.put("anonymity",currentSelNiming == ivCheckState3 ? "1" : "0");
//                map.put("from", FLUtil.getMacAddress());
////                map.put("meeting_record_id",UserUtil.meeting_record_id);
                //?????????????????????
                if (flag.equals("1")) {
                    creatVote(voteBean, "1");
                } else {
                    //???????????????
                    /*
                     * ??????????????????Service?????????????????????
                     * */
                         for (int i=0;i<list.size();i++){
                             fileNames.add(list.get(i).getFileName());
                             paths.add(list.get(i).getText());
                         }
                    if (Hawk.contains("stringsIp")) {
                        stringListIp = Hawk.get("stringsIp");
                    }
                    for (int i = 0; i < stringListIp.size(); i++) {
                        Log.d("stringListIp=??????11",stringListIp.get(i));

                    }

                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {


                            //??????Ip?????????????????????????????????
                            for (int i = 0; i < stringListIp.size(); i++) {
                                Log.d("fgdggsg3333 ",stringListIp.get(i));
                                socketShareFileManager.SendNewVoteFile(fileNames, paths, stringListIp.get(i), constant.SHARE_PORT,"3");
                            }
                        }

                    });
                    for (int i = 0; i < stringListIp.size(); i++) {
                        Log.d("stringListIp=??????22",stringListIp.get(i));

                    }
                    sendThread.start();
                    creatVote(voteBean, "2");
                }

                results.dismiss();
            }
        });
        add_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addData(list.size());

                if (flag.equals("2")){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//???????????????
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, 1);
                    positionIma = 0;
                }
                add_chose.setVisibility(View.INVISIBLE);


            }
        });

        // ??????
        ivCheckState1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDanxuan(1);
            }
        });
        ivCheckState2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDanxuan(2);
            }
        });

        //  ??????
        ivCheckState3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNiming(1);
            }
        });
        ivCheckState4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNiming(2);
            }
        });

    }
    //????????????
    @Override
    public void deleteOpition(int i) {

        CVIPaperDialogUtils.showCustomDialog(getActivity(), "?????????????????????", "", "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
            @Override
            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                if (clickConfirm) {
                    list.remove(i);
                    adapter.notifyDataSetChanged();
                    if (list.size() > 0) {
                        add_chose.setVisibility(View.INVISIBLE);
                    } else {
                        add_chose.setVisibility(View.VISIBLE);
                    }
                   /* if (list.size() == 0) {
                        mchoseClickListener.clickListener(position,flag);
                    }
                    //????????????
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    itemObj.setText("");
                    removeData(position);*/

                }
            }
        });
    }
    //??????????????????
    @Override
    public void end(int a,String flag) {
        CVIPaperDialogUtils.showCustomDialog(getActivity(), "?????????????????????", "", "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
            @Override
            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                if (clickConfirm) {
                    Log.d("gsdrwerbiumr","??????????????????"+flag);
                    VoteBean model=voteList.get(a);
                    model.setStatus("FINISH");
                    finishVote(model, flag);
                }
            }
        });

    }
    //????????????
    @Override
    public void vote(int a,String flag) {
        Log.d("gsdrwerbiumr","????????????"+flag);
        List<ChoseBean> choseBeanList=new ArrayList<>();
        if (Hawk.contains("ChoseBean")){
            choseBeanList=Hawk.get("ChoseBean");
        }

        if (voteList==null||voteList.size()<0){
            return;
        }
           VoteBean model=voteList.get(a);
        ArrayList<String> chose = new ArrayList<>();
        ArrayList<VoteBean.UserListBean> userbeanList = new ArrayList<>(model.getUser_list());
        List<VoteBean.UserListBean.ChoseBean>  choseBeans=new ArrayList<>();
           List<VoteBean.TemporBean>temporBeanList=new ArrayList<>();
        for (int i=0;i<choseBeanList.size();i++){
            VoteBean.UserListBean.ChoseBean  choseBean=new VoteBean.UserListBean.ChoseBean();
            VoteBean.TemporBean  temporBean=new VoteBean.TemporBean();
            temporBean.setOrderNumb(choseBeanList.get(i).getOrderNumb());
            temporBean.setContent(choseBeanList.get(i).getContent());
            temporBean.setVotePath(choseBeanList.get(i).getVotePath());
            temporBeanList.add(temporBean);

        if (choseBeanList.get(i).isChecked()) {
                choseBean.setOrderNumb(choseBeanList.get(i).getOrderNumb());
                choseBean.setContent(choseBeanList.get(i).getContent());
                choseBean.setChecked(true);
                choseBean.setUserName(UserUtil.user_name);//???????????????
            choseBean.setVotePath(choseBeanList.get(i).getVotePath());
                choseBeans.add(choseBean);
                //???????????????
                chose.add(choseBeanList.get(i).getOrderNumb());
            }else {
            choseBean.setOrderNumb(choseBeanList.get(i).getOrderNumb());
            choseBean.setContent(choseBeanList.get(i).getContent());
            choseBean.setChecked(false);
            choseBean.setUserName(UserUtil.user_name);//???????????????
            choseBean.setVotePath(choseBeanList.get(i).getVotePath());
            choseBeans.add(choseBean);
            //???????????????
            chose.add(choseBeanList.get(i).getOrderNumb());

        }
        }

        Log.d("gdgfggf",chose.size()+"   "+choseBeans.size()+"   ");
        VoteBean.UserListBean userListBean = new VoteBean.UserListBean();

        userListBean.setUser_name(Hawk.get(constant.myNumber));
        userListBean.setStatus("FINISH");
        userListBean.setUser_id(FLUtil.getMacAddress());
        userListBean.setMeeting_vote_id(model.get_id());
        userListBean.setChoose(chose);
        userListBean.setChoseBeanList(choseBeans);
        userbeanList.add(userListBean);
        model.setTemporBeanList(temporBeanList);//????????????????????????
       // model.setUser_list(userbeanList);
       // voteList.add(model);
        requestToVote(userListBean, flag);

    }
    //??????????????????
    @Override
    public void vr(int position,String flag) {
      List<VoteListBean.VoteBean.TemporBean>temporBeanList=new ArrayList<>();
        Log.d("gsdrwerbiumr","??????????????????"+flag);
        if (position >= 0 && voteList.size() > position) {
            VoteListBean.VoteBean model = voteList.get(position);
            temporBeanList=model.getTemporBeanList();
            int status = model.getMvoteStatus();

            for (int p=0;p<temporBeanList.size();p++){
                switch (p){
                    case  0:
                        temporBeanList.get(p).setOrderNumb("A");
                        break;
                    case  1:
                        temporBeanList.get(p).setOrderNumb("B");
                        break;
                    case  2:
                        temporBeanList.get(p).setOrderNumb("C");
                        break;
                    case  3:
                        temporBeanList.get(p).setOrderNumb("D");
                        break;

                    case  4:
                        temporBeanList.get(p).setOrderNumb("E");
                        break;

                    case  5:
                        temporBeanList.get(p).setOrderNumb("F");
                        break;

                    case  6:
                        temporBeanList.get(p).setOrderNumb("G");
                        break;

                    case  7:
                        temporBeanList.get(p).setOrderNumb("H");
                        break;

                    case  8:
                        temporBeanList.get(p).setOrderNumb("I");
                        break;
                    case  9:
                        temporBeanList.get(p).setOrderNumb("L");

                        break;

                    case  10:
                        temporBeanList.get(p).setOrderNumb("M");

                        break;
                }

            }


            // ????????????
            if (status == Constants.VoteStatusEnum.hasFinshed) {
                boolean isNoUser = true;        //  ??????????????????user??????
                boolean isAllNoChoose = true;   //  ???????????????????????????????????????
                if (model.getUser_list().size() > 0) {
                    isNoUser = false;
                    for (VoteListBean.VoteBean.UserListBean bean : model.getUser_list()) {
                        if (bean.getChoose().size() > 0) {
                            isAllNoChoose = false;
                        }
                    }
                }

                if (isNoUser || isAllNoChoose) {
                    ToastUtils.showShort("???????????????!");
                    return;
                }
                newSeeResult(voteList.get(position),  flag);
             /*   WuHuPieChartDialog piechartDialog = new WuHuPieChartDialog(getActivity(), R.style.AlertDialogStyle, createData(model), voteList.get(position));
                piechartDialog.show();

                Window window = piechartDialog.getWindow();//??????dialog????????????
                window.setGravity(Gravity.CENTER);//??????????????????
                Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
                WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
                Point size = new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;
                p.width = (int) (width * 0.8);//?????????
                p.height = (int) (height * 0.85);//?????????
                window.setAttributes(p);*/
            }
            Log.d("fgsdfgsdg445555", flag);
            //  ??????
      /*      if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                if (model.getType().equals("0")) {
                    showRadioDialog(model, position, flag);
                } else {
                    showCheckBoxDialog(model, position, flag);
                }
            }*/
        }

    }
  //????????????????????????
    private void newSeeResult(VoteBean voteBean, String flag){
        allVoteNumb=0;
                a=0;
                b=0;
                c=0;
                d=0;
                e=0;
                f=0;
                g=0;
                h=0;
                strA=new StringBuilder();
                strB=new StringBuilder();
        strC=new StringBuilder();
        strD=new StringBuilder();
        strE=new StringBuilder();
        strF=new StringBuilder();
        strG=new StringBuilder();
        strH=new StringBuilder();
                /*String chooseChar;
                String chooseContent;
                String */
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view   = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wuhu_voting_results, null);
            Button danxaun=view.findViewById(R.id.danxaun);
            Button niming=view.findViewById(R.id.niming);
            TextView vote_topic=view.findViewById(R.id.vote_topic);
             MyListView myList_view=view.findViewById(R.id.myList_view);
            TextView  personnel=view.findViewById(R.id.personnel);
            View vv=view.findViewById(R.id.vv);
        if (voteBean.getType().equals("0")) {
            danxaun.setText("??????");
        }else {
            danxaun.setText("??????");
        }
        if (voteBean.getAnonymity().equals("1")) {
            niming.setText("??????");
            vv.setVisibility(View.VISIBLE);
            personnel.setVisibility(View.VISIBLE);
        } else {
            niming.setText("??????");
            vv.setVisibility(View.GONE);
            personnel.setVisibility(View.GONE);

        }
        vote_topic.setText("???????????????"+voteBean.getTopic());
       List<VoteBean.UserListBean>userListBeanList=new ArrayList<>();
        userListBeanList=voteBean.getUser_list();
       List<WuHuVoteResultBean>wuHuVoteResultBeanList=new ArrayList<>();

        for (int i=0;i<userListBeanList.size();i++){
            List<VoteBean.UserListBean.ChoseBean> choseBeanList=new ArrayList<>();
            choseBeanList=userListBeanList.get(i).getChoseBeanList();
            wuHuVoteResultBeanList.clear();
            for (int n=0;n<choseBeanList.size();n++){
                if (choseBeanList.get(n).isChecked()){
                    allVoteNumb++;
                }
                    WuHuVoteResultBean wuHuVoteResultBean=new WuHuVoteResultBean();
              switch (choseBeanList.get(n).getOrderNumb()){
                  case "A":
                      if (choseBeanList.get(n).isChecked()){
                      a++;
                          strA.append(choseBeanList.get(n).getUserName()+",");
                      }
                      wuHuVoteResultBean.setVoteNumb(a+"");
                      wuHuVoteResultBean.setChoosenNumb(choseBeanList.get(n).getOrderNumb());
                      wuHuVoteResultBean.setContent(choseBeanList.get(n).getContent());
                      wuHuVoteResultBean.setVotePath(choseBeanList.get(n).getVotePath());
                      wuHuVoteResultBean.setName(choseBeanList.get(n).getUserName());

                  break;
                  case "B":
                      if (choseBeanList.get(n).isChecked()){
                      b++;
                          strB.append(choseBeanList.get(n).getUserName()+",");
                      }
                      wuHuVoteResultBean.setVoteNumb(b+"");
                      wuHuVoteResultBean.setChoosenNumb(choseBeanList.get(n).getOrderNumb());
                      wuHuVoteResultBean.setContent(choseBeanList.get(n).getContent());
                      wuHuVoteResultBean.setName(choseBeanList.get(n).getUserName());
                      wuHuVoteResultBean.setVotePath(choseBeanList.get(n).getVotePath());
                      break;
                  case "C":
                      if (choseBeanList.get(n).isChecked()) {
                          c++;
                          strC.append(choseBeanList.get(n).getUserName()+",");
                      }
                      wuHuVoteResultBean.setVoteNumb(c+"");
                      wuHuVoteResultBean.setChoosenNumb(choseBeanList.get(n).getOrderNumb());
                      wuHuVoteResultBean.setContent(choseBeanList.get(n).getContent());
                      wuHuVoteResultBean.setVotePath(choseBeanList.get(n).getVotePath());
                      break;
                  case "D":
                      if (choseBeanList.get(n).isChecked()) {
                          d++;
                          strD.append(choseBeanList.get(n).getUserName()+",");
                      }
                      wuHuVoteResultBean.setVoteNumb(d+"");
                      wuHuVoteResultBean.setChoosenNumb(choseBeanList.get(n).getOrderNumb());
                      wuHuVoteResultBean.setContent(choseBeanList.get(n).getContent());
                      wuHuVoteResultBean.setName(choseBeanList.get(n).getUserName());
                      wuHuVoteResultBean.setVotePath(choseBeanList.get(n).getVotePath());
                      break;
                  case "E":
                      if (choseBeanList.get(n).isChecked()) {
                      e++;
                          strE.append(choseBeanList.get(n).getUserName()+",");
                      }
                      wuHuVoteResultBean.setVoteNumb(e+"");
                      wuHuVoteResultBean.setChoosenNumb(choseBeanList.get(n).getOrderNumb());
                      wuHuVoteResultBean.setContent(choseBeanList.get(n).getContent());
                      wuHuVoteResultBean.setName(choseBeanList.get(n).getUserName());
                      wuHuVoteResultBean.setVotePath(choseBeanList.get(n).getVotePath());
                      break;
                  case "F":
                      if (choseBeanList.get(n).isChecked()) {
                          f++;
                          strF.append(choseBeanList.get(n).getUserName()+",");
                      }
                      wuHuVoteResultBean.setVoteNumb(f+"");
                      wuHuVoteResultBean.setChoosenNumb(choseBeanList.get(n).getOrderNumb());
                      wuHuVoteResultBean.setContent(choseBeanList.get(n).getContent());
                      wuHuVoteResultBean.setName(choseBeanList.get(n).getUserName());
                      wuHuVoteResultBean.setVotePath(choseBeanList.get(n).getVotePath());
                      break;
                  case "G":
                      if (choseBeanList.get(n).isChecked()) {
                          g++;
                          strG.append(choseBeanList.get(n).getUserName()+",");
                      }
                      wuHuVoteResultBean.setVoteNumb(g+"");
                      wuHuVoteResultBean.setChoosenNumb(choseBeanList.get(n).getOrderNumb());
                      wuHuVoteResultBean.setContent(choseBeanList.get(n).getContent());
                      wuHuVoteResultBean.setName(choseBeanList.get(n).getUserName());
                      wuHuVoteResultBean.setVotePath(choseBeanList.get(n).getVotePath());
                      break;
                  case "H":
                      if (choseBeanList.get(n).isChecked()) {
                          h++;
                          strH.append(choseBeanList.get(n).getUserName()+",");
                      }
                      wuHuVoteResultBean.setVoteNumb(h+"");
                      wuHuVoteResultBean.setChoosenNumb(choseBeanList.get(n).getOrderNumb());
                      wuHuVoteResultBean.setContent(choseBeanList.get(n).getContent());
                      wuHuVoteResultBean.setName(choseBeanList.get(n).getUserName());
                      wuHuVoteResultBean.setVotePath(choseBeanList.get(n).getVotePath());
                      break;
              }
                      wuHuVoteResultBeanList.add(wuHuVoteResultBean);

            }
        }

        Log.d("sdas111",strA.length()+"  "+strB.length()+"  "+strC.length()+" "+strD.length()+"   "+strE.length()+"   "+strF.length()+"  "+strG.length()+"  "+strH.length());
        StringBuilder nameString=new StringBuilder();
      if (strA.length()>0){
          nameString.append("A:"+strA.substring(0,strA.length()-1)).append("\n"+"\n");
        }
        if (strB.length()>0){
            nameString.append("B:"+strB.substring(0,strB.length()-1)).append("\n"+"\n");
        }else {

        }
        if (strC.length()>0){
            nameString.append("C:"+strC.substring(0,strC.length()-1)).append("\n"+"\n");
        }else {


        }
        if (strD.length()>0){
            nameString.append("D:"+strD.substring(0,strD.length()-1)).append("\n"+"\n");
        }else {


        }
        if (strE.length()>0){
            nameString.append("E:"+strE.substring(0,strE.length()-1)).append("\n"+"\n");
        }else {


        }
        if (strF.length()>0){
            nameString.append("F:"+strF.substring(0,strF.length()-1)).append("\n"+"\n");
        }  else {


        }
        if (strG.length()>0){
            nameString.append("G:"+strG.substring(0,strG.length()-1)).append("\n"+"\n");
        }else {

        }
        if (strH.length()>0){
            nameString.append("H:"+strH.substring(0,strH.length()-1)).append("\n"+"\n");
        }else {

        }

        personnel.setText(nameString);
    /*    //????????????????????????
        for (int i=0;i<wuHuVoteResultBeanList.size();i++ ){
            int prossNumb=0;

        WuHuVoteResultBean  wuHuVoteResultBean=wuHuVoteResultBeanList.get(i);
         switch (wuHuVoteResultBean.getChoosenNumb()){
             case "A":
                 prossNumb=(Integer)(Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb)*100;
                 wuHuVoteResultBeanList.get(i).setProssBarNumb(prossNumb);
                 Log.d("ggsdfgg222",prossNumb+"  "+wuHuVoteResultBean.getVoteNumb()+"   "+Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb+"  allVoteNumb="+allVoteNumb);
                 break;
             case "B":
                 prossNumb=(Integer)(Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb)*100;
                 wuHuVoteResultBeanList.get(i).setProssBarNumb(prossNumb);
                 Log.d("ggsdfgg333",prossNumb+"    "+wuHuVoteResultBean.getVoteNumb()+"   "+Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb+"  allVoteNumb="+allVoteNumb);
                 break;
             case "C":
                 prossNumb=(Integer)(Integer.valueOf("1")/allVoteNumb)*100;
                 wuHuVoteResultBeanList.get(i).setProssBarNumb(prossNumb);
                 Log.d("ggsdfgg444",prossNumb+""+wuHuVoteResultBean.getVoteNumb()+"   "+Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb+"  allVoteNumb="+allVoteNumb);
                 break;
             case "D":
                 prossNumb=(Integer)(Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb)*100;
                 wuHuVoteResultBeanList.get(i).setProssBarNumb(prossNumb);
                 Log.d("ggsdfgg555",prossNumb+"   "+wuHuVoteResultBean.getVoteNumb()+"   "+Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb+"  allVoteNumb="+allVoteNumb);
                 break;
             case "E":
                 prossNumb=(Integer)(Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb)*100;
                 wuHuVoteResultBeanList.get(i).setProssBarNumb(prossNumb);
                 break;
             case "F":
                 prossNumb=(Integer)(Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb)*100;
                 wuHuVoteResultBeanList.get(i).setProssBarNumb(prossNumb);
                 break;
             case "G":
                 prossNumb=(Integer)(Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb)*100;
                 wuHuVoteResultBeanList.get(i).setProssBarNumb(prossNumb);
                 break;
             case "H":
                 prossNumb=(Integer)(Integer.valueOf(wuHuVoteResultBean.getVoteNumb())/allVoteNumb)*100;
                 wuHuVoteResultBeanList.get(i).setProssBarNumb(prossNumb);
                 break;
         }
            Log.d("ggsdfgg1111",prossNumb+"");
        }*/
        Log.d("ggsdfgg8888",allVoteNumb+"");

        //????????????
        for (int i=0;i<userListBeanList.size();i++){
            VoteBean.UserListBean  userListBean=userListBeanList.get(i);
            if (FLUtil.getMacAddress().equals(userListBean)){
                List<VoteBean.UserListBean.ChoseBean> choseBeanList=new ArrayList<>();
                choseBeanList=userListBeanList.get(i).getChoseBeanList();
                for (int n=0;n<choseBeanList.size();n++){
                    for (int k=0;k<wuHuVoteResultBeanList.size();k++){
                        if (wuHuVoteResultBeanList.get(k).getChoosenNumb().equals(choseBeanList.get(n).getOrderNumb())){
                            wuHuVoteResultBeanList.get(k).setVotePath(choseBeanList.get(n).getVotePath());
                            wuHuVoteResultBeanList.get(k).setContent(choseBeanList.get(n).getContent());

                        }

                    }

                }

            }




        }
        for (int i=0;i<wuHuVoteResultBeanList.size();i++){
            String numb=wuHuVoteResultBeanList.get(i).getVoteNumb();
            int  numbInt=Integer.parseInt(numb);

            float pressent = (float) numbInt / allVoteNumb * 100;
            int per=(int)pressent;
            wuHuVoteResultBeanList.get(i).setProssBarNumb(per);
        }


       if (flag.equals("1")){
        WuHuTextResultAdapter  wuHuTextResultAdapter=new WuHuTextResultAdapter(getActivity(),wuHuVoteResultBeanList);
        myList_view.setAdapter(wuHuTextResultAdapter);
        wuHuTextResultAdapter.notifyDataSetChanged();

       }else {
           WuHuImaResultAdapter wuHuImaResultAdapter = new WuHuImaResultAdapter(getActivity(), wuHuVoteResultBeanList);
           myList_view.setAdapter(wuHuImaResultAdapter);
           wuHuImaResultAdapter.notifyDataSetChanged();
       }

            builder.setView(view);
            builder.setCancelable(true);
            newResults = builder.create();
            newResults.show();
            Window window = newResults.getWindow();//??????dialog????????????
            window.setGravity(Gravity.CENTER);//??????????????????
            Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
            WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
            Point size = new Point();
            d.getSize(size);
            int width = size.x;
            int height = size.y;
            p.width = (int) (width * 0.8);//?????????
            p.height = (int) (height * 0.8);//?????????
            window.setAttributes(p);
        VoteBean  voteBean1=voteList.get(0);
        voteBean1.setWuHuVoteResultBeanArrayList(wuHuVoteResultBeanList);
      VoteListBean voteListBean=new VoteListBean();
        voteListBean.setData(voteList);

        Hawk.put("VoteListBean",voteListBean);
       /* if (Hawk.contains("WuHuFragment")){
            WuHuEditBean   wuHuEditBean=Hawk.get("WuHuFragment");
            wuHuEditBean.setVoteListBean(voteListBean);

            Hawk.put("WuHuFragmentData", wuHuEditBean);
        }*/

    }
    //  ??????????????????
    private void checkDanxuan(Integer tag) {

        if (currentSelDanxuan.equals(ivCheckState1)) {
            if (1 == tag) {
                return;
            }
            ivCheckState1.setImageResource(R.drawable.radio_unselected);
            ivCheckState2.setImageResource(R.drawable.radio_selected);
            currentSelDanxuan = ivCheckState2;
        } else {
            if (2 == tag) {
                return;
            }
            ivCheckState1.setImageResource(R.drawable.radio_selected);
            ivCheckState2.setImageResource(R.drawable.radio_unselected);
            currentSelDanxuan = ivCheckState1;
        }
    }

    //  ??????????????????
    private void checkNiming(Integer tag) {

        if (currentSelNiming.equals(ivCheckState3)) {
            if (1 == tag) {
                return;
            }
            ivCheckState3.setImageResource(R.drawable.radio_unselected);
            ivCheckState4.setImageResource(R.drawable.radio_selected);
            currentSelNiming = ivCheckState4;
        } else {
            if (2 == tag) {
                return;
            }
            ivCheckState3.setImageResource(R.drawable.radio_selected);
            ivCheckState4.setImageResource(R.drawable.radio_unselected);
            currentSelNiming = ivCheckState3;
        }
    }

    //  ???????????????
    private void initRecycle(String flag) {
        if (list != null) {
            list.clear();
        }

        //  ????????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //      ??????????????????????????????????????????????????????
//        list = initData();
        if (flag.equals("2")){
            ItemBean itemBean1=new ItemBean();
            ItemBean itemBean2=new ItemBean();
            list.add(itemBean1);
            list.add(itemBean2);
        }
        adapter = new WuHuRecycleAdapter(getActivity(), list, new WuHuRecycleAdapter.ChoseClickListener() {
            @Override
            public void clickListener(int position, String flag) {
                if (list.size() > 0) {
                    add_chose.setVisibility(View.INVISIBLE);
                } else {
                    add_chose.setVisibility(View.VISIBLE);
                }

            }
        }, flag);
        adapter.setChoseOptionItemImaListener(this);
        adapter.setSeePhotosItemImaListener(this);
        adapter.setDeleteOpition(this);
        mRecyclerView.setAdapter(adapter);
        //      ????????????
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    protected ArrayList<ItemBean> initDatas() {
        ArrayList<ItemBean> mDatas = new ArrayList<ItemBean>();
        for (int i = 0; i < 1; i++) {
            mDatas.add(new ItemBean());
        }
        return mDatas;
    }

    // ??????????????????????????????????????????
    protected void refreshUI(List<VoteBean> data, String flag) {
     if (data.size()>0){
         refresh_rl.setVisibility(View.VISIBLE);
     }else {
         refresh_rl.setVisibility(View.GONE);

     }
        Log.d("gdgsdgsdgdgf444",flag+"");
        if (UserUtil.ISCHAIRMAN) {
//            voteAdapter2.addFinalVotestate(data);
            voteAdapter2.setList(data);
            voteAdapter2.setFlag(flag);
            voteAdapter2.notifyDataSetChanged();
        } else {
//            voteAdapter1.addFinalVotestate(data);
            voteAdapter1.setList(data);
            voteAdapter1.setFlag(flag);
            voteAdapter1.notifyDataSetChanged();
        }
        home_refreshLayout.finishRefresh();
        if (!UserUtil.ISCHAIRMAN) {
            no_vote_add_rl.setVisibility(View.GONE);
            add_wuhu_vote_rl.setVisibility(View.GONE);
            no_vote_add_type.setVisibility(View.GONE);
        }else {
            if (data.size()<1){
                add_wuhu_vote_rl.setVisibility(View.GONE);
                no_vote_add_rl.setVisibility(View.VISIBLE);
                no_vote_add_type.setVisibility(View.GONE);
            }else {
                add_wuhu_vote_rl.setVisibility(View.VISIBLE);
                no_vote_add_rl.setVisibility(View.GONE);
                no_vote_add_type.setVisibility(View.GONE);
            }


        }


    }

    //????????????????????????????????????
    public void loadData() {
        voteList.clear();
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(constant.QUERYVOTE);
        bean.setBody("");
        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);

    }

    //????????????
    @Override
    public void seeImaistener(int position) {

        if(StringUtils.isEmpty(list.get(position).getText())){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//???????????????
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, 1);
            positionIma = position;

        }else {
        showImage(openImage( list.get(position).getText()));}
    }

    public  Bitmap openImage(String path){
        Bitmap bitmap = null;

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            try {
                bis.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }


        return bitmap;
    }

    //??????????????????
    private void showImage(Bitmap  bitmap){
        MyImageDialog myImageDialog = new MyImageDialog(getActivity(),R.style.mypopwindow_anim_style,0,-300,bitmap);
        myImageDialog.show();

    }
    //????????????-??????
    @Override
    public void choseImaistener(int position) {
       Log.d("dsaff",position+"");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//???????????????
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
        positionIma = position;
    }

    /**
     * ?????????byte?????????
     *
     * @param imagepath ?????????byte???????????????
     * @return ???????????????byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[3*1024*1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }

    private String byte2Base64(byte[] imageByte) {
        if (null == imageByte) return null;
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    private Bitmap base642Bitmap(String base64) {
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        Bitmap mBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        // bitmapList.add(mBitmap);
        Message ms = new Message();
        ms.what = 2;
        // handler.sendMessage(ms);//???????????????
        return mBitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                try {
                    File file = new File(getFilePath(getActivity(), uri));
                    Log.d("requestCodeUr00000", Environment.getExternalStorageDirectory().getAbsolutePath() + "===" + Environment.getExternalStorageDirectory().toString() + "====" + Environment.getStorageState(file));
                    Log.d("requestCodeUrl111", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName() + "++++++++++" + getFilePath(getActivity(), uri) + "===" + uri.getAuthority());
                    String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    if ("jpg".equals(endStr) || "gif".equals(endStr) || "png".equals(endStr) || "jpeg".equals(endStr) || "bmp".equals(endStr)) {
                        list.get(positionIma).setText(file.getPath());
                        list.get(positionIma).setFileName(file.getName());
                        adapter.notifyDataSetChanged();
                        Log.d("requestCodeUr779999??????", list.size()+"   ");
                    } else {

                        Toast.makeText(getActivity(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("requestCodeUr7788", "????????????=" + endStr + "????????????" + file.getName() + getFilePath(getActivity(), uri));

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else {
                //???????????????????????????
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    List<String> pathList = new ArrayList<>();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        try {
                            File file = new File(getFilePath(getActivity(), uri));
                          //  ToastUtil.makeText(getActivity(), "uri.getPath()=====" + uri.getPath());
                            Log.d("requestCodeUr2222", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName() + "++++++++++" + getFilePath(getActivity(), uri) + "===" + uri.getAuthority());
                            String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                            if ("jpg".equals(endStr) || "gif".equals(endStr) || "png".equals(endStr) || "jpeg".equals(endStr) || "bmp".equals(endStr)) {
                                list.get(positionIma).setText(file.getPath());
                                list.get(positionIma).setFileName(file.getName());
                                adapter.notifyDataSetChanged();
                            } else {

                                Toast.makeText(getActivity(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                            }
                            Log.d(TAG, "????????????=" + endStr + "????????????" + file.getName());
                            // fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                            //???????????????????????????
                       /*     new Thread() {
                                @Override
                                public void run() {
                                    copyFile(file.getPath(), fileStrPath + file.getName());
                                }
                            }.start();*/
                           /* fileBean.setResImage(getIamge(endStr));
                            fileBean.setFile_type(getType(endStr));
                            fileBeans.add(fileBean);*/
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

      /*    fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();*/
        }

    }

    @SuppressLint("NewApi")
    public String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        String path = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                for (int i = 0; i < split.length; i++) {
                    Log.d("requestCodeUr4444", split[i] + "i==" + i);
                }
                Log.d("requestCodeUr55555", Environment.getExternalStorageDirectory() + "/" + split[1]);
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    return "/storage/" + split[0] + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return  uri.getPath();

            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                for (int i = 0; i < split.length; i++) {
                    Log.d("requestCodeUr6666", split[i]);
                }
                Log.d("requestCodeUr7777", split[0]);
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {

            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            String[] projection = {
                    MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//============
                if (column_index<0||column_index>cursor.getColumnCount()){

                    return null;
                }
                if (cursor.moveToFirst()) {

                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    path = cursor.getString(columnIndex);

                }
                cursor.close();
                return path;



              /*  if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
                */

            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //   ????????????????????????????????????
    @Override
    public void clickListener(int position, String flag) {
        Log.d("gdgsdgsdgdgf444????????????","flag==="+flag);
        if (position >= 0 && voteList.size() > position) {
            VoteBean model = voteList.get(position);
            int status = model.getMvoteStatus();

            // ????????????
            if (status == Constants.VoteStatusEnum.hasFinshed) {
                boolean isNoUser = true;        //  ??????????????????user??????
                boolean isAllNoChoose = true;   //  ???????????????????????????????????????
                if (model.getUser_list().size() > 0) {
                    isNoUser = false;
                    for (VoteBean.UserListBean bean : model.getUser_list()) {
                        if (bean.getChoose().size() > 0) {
                            isAllNoChoose = false;
                        }
                    }
                }

                if (isNoUser || isAllNoChoose) {
                    ToastUtils.showShort("???????????????!");
                    return;
                }

                PieChartDialog piechartDialog = new PieChartDialog(getActivity(), R.style.AlertDialogStyle, createData(model), voteList.get(position));
                piechartDialog.show();

                Window window = piechartDialog.getWindow();//??????dialog????????????
                window.setGravity(Gravity.CENTER);//??????????????????
                Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
                WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
                Point size = new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;
                p.width = (int) (width * 0.9);//?????????
                p.height = (int) (height * 0.9);//?????????
                window.setAttributes(p);
            }

            //  ??????
            if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                if (model.getType().equals("0")) {
                    showRadioDialog(model, position, flag);
                } else {
                    showCheckBoxDialog(model, position, flag);
                }
            }
        }
    }

    //   ?????????????????????
    @Override
    public void chairmanClickListener(int position, String flag) {
        Log.d("gdgsdgsdgdgf444??????","flag==="+flag);
        if (position >= 0 && voteList.size() > position) {
            VoteBean model = voteList.get(position);
            int status = model.getMvoteStatus();

            // ????????????
            if (status == Constants.VoteStatusEnum.hasFinshed) {
                boolean isNoUser = true;        //  ??????????????????user??????
                boolean isAllNoChoose = true;   //  ???????????????????????????????????????
                if (model.getUser_list().size() > 0) {
                    isNoUser = false;
                    for (VoteBean.UserListBean bean : model.getUser_list()) {
                        if (bean.getChoose().size() > 0) {
                            isAllNoChoose = false;
                        }
                    }
                }

                if (isNoUser || isAllNoChoose) {
                    ToastUtils.showShort("???????????????!");
                    return;
                }
                if(flag.equals("1")){
                    ToastUtils.showShort("????????????????????????!");
                    return;
                }

                PieChartDialog piechartDialog = new PieChartDialog(getActivity(), R.style.AlertDialogStyle, createData(model), voteList.get(position));
                piechartDialog.show();

                Window window = piechartDialog.getWindow();//??????dialog????????????
                window.setGravity(Gravity.CENTER);//??????????????????
                Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
                WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
                Point size = new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;
                p.width = (int) (width * 0.8);//?????????
                p.height = (int) (height * 0.85);//?????????
                window.setAttributes(p);
            }
            Log.d("fgsdfgsdg445555", flag);
            //  ??????
            if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                if (model.getType().equals("0")) {
                    showRadioDialog(model, position, flag);
                } else {
                    showCheckBoxDialog(model, position, flag);
                }
            }
        }
    }

    //  ????????????
    @Override
    public void operationclickListener(int position, String flag) {
        Log.d("operation","flag==="+flag);
        int status = voteList.get(position).getMvoteStatus();
        if (status == Constants.VoteStatusEnum.hasStartUnVote) {
            popTitleArr = new String[]{"??????", "??????"};
        } else if (status == Constants.VoteStatusEnum.hasFinshed) {
            return;
        } else {
            popTitleArr = new String[]{"??????"};
        }

        mLucklyPopopWindow = new LucklyPopopWindow(getActivity());
        //???popupWindow????????????
//        mLucklyPopopWindow.setData(new String[]{"??????","??????"}, new int[]{ R.mipmap.operation_end, R.mipmap.operation_delet});
        mLucklyPopopWindow.setData(popTitleArr);

        //??????????????????
        mLucklyPopopWindow.setWidth(160);
        mLucklyPopopWindow.setRadius(0);
        //????????????
        mLucklyPopopWindow.setOnItemClickListener(new LucklyPopopWindow.OnItemClickListener() {
            @Override
            public void onItemClick(int popPosition) {
                VoteBean model = voteList.get(position);

                if (popTitleArr[popPosition].equals("??????")) {
                    int status = model.getMvoteStatus();

                    //  ?????????????????????
                    if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                        if (model.getType().equals("0")) {
                            showRadioDialog(model, position, flag);
                        } else {
                            showCheckBoxDialog(model, position, flag);
                        }
                    }
                } else if (popTitleArr[popPosition].equals("??????")) {
                    model.setStatus("FINISH");


                    finishVote(model, flag);

                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", model.get_id());
                    deleatVote(map);
                }
                mLucklyPopopWindow.dismiss();
            }
        });

        //???????????????(??????)
        mLucklyPopopWindow.addItemDecoration(LucklyPopopWindow.VERTICAL, Color.GRAY, 1);
        //??????image?????????(??????)
        mLucklyPopopWindow.setImageDisable(true);
        //??????image?????????(??????)
        mLucklyPopopWindow.setTextSize(20);
        mLucklyPopopWindow.setAnimationStyle(0);
//        mLucklyPopopWindow.setImageSize(15,15);
        //??????popopWindow
        mLucklyPopopWindow.setTrianleHeight(0);
        //  ???????????????view
        View view = mLayoutManager.findViewByPosition(position);
        //mLayoutManager???recyclre??????????????????
        RelativeLayout layout = (RelativeLayout) view;        //?????????????????????????????????
        ImageView operation = layout.findViewById(R.id.operation);
        mLucklyPopopWindow.showAtLocation(getActivity().getWindow().getDecorView(), operation);

    }

    //  ????????????????????????
    private ArrayList<PieEntry> createData(VoteBean model) {
        ArrayList<PieEntry> pieLists = new ArrayList<>();

        String entryname = "";
        ArrayList<String> optionAll = new ArrayList<>();
        Map<String, Integer> map = new HashMap();
        //  ??????????????????????????????????????????
        for (int i = 0; i < model.getUser_list().size(); i++) {
            VoteBean.UserListBean userbean = model.getUser_list().get(i);
            for (int j = 0; j < userbean.getChoose().size(); j++) {

                optionAll.add(userbean.getChoose().get(j));
            }
        }

        // ???????????????????????????????????????
        float totalNum = optionAll.size();
        if (optionAll.size() > 0) {
            if (optionAll.size() > 1) {
                int count = 1;// ????????????????????????1
                for (int i = 0; i < optionAll.size(); i++) {
                    count = 1;
                    for (int j = i + 1; j < optionAll.size(); j++) {
                        if (optionAll.get(i).equals(optionAll.get(j))) {
                            count++;// ??????+1
                            optionAll.remove(j);
                            j--;// ??????????????????????????????remove???????????????????????????????????????????????????????????????????????????//????????????
                        }
                    }
                    System.out.println(optionAll.get(i) + "??????????????????" + count);
                    map.put(optionAll.get(i), count);

                }
            } else {
                // ??????1?????????
                totalNum = 1;
                map.put(optionAll.get(0), 1);
            }
        } else {
            //   ?????????,???????????????????????????????????????
        }

        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            List<String> chooseUserList = new ArrayList();
            for (int i = 0; i < model.getUser_list().size(); i++) {
                VoteBean.UserListBean userbean = model.getUser_list().get(i);
                if (userbean.getChoose().contains(entry.getKey())) {
                    chooseUserList.add(userbean.getUser_name());
                }
            }

            float percent = (entry.getValue() / totalNum) * 100;
            PieEntry pieentry = new PieEntry(percent, entry.getKey());
            pieentry.setChooseUserList(chooseUserList);
            pieLists.add(pieentry);
        }
        return pieLists;
    }


    /*????????????*/
    private void voteSucessDialog() {
        VoteInfoDialog infoDialog = new VoteInfoDialog.Builder(getActivity())
                .setTitle("????????????")
                .setMessage("??????????????????????????????")
                .setButton(view -> {
                        }
                ).create();
        infoDialog.show();
    }

    //  ?????????
    private void showRadioDialog(VoteBean model, int index, String flag) {
        final List<ChoseBean> list = new ArrayList<>();
        for (int i = 0; i < model.getOptions().size(); i++) {
            ChoseBean object = new ChoseBean();
            object.setContent(model.getOptions().get(i));
            object.setChecked(false);
            list.add(object);
        }

        WuHuRadioDialog dialog = new WuHuRadioDialog(getActivity(), R.style.AlertDialogStyle, list, flag);
        dialog.show();

       Window window = dialog.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.8);//?????????
        p.height = (int) (height * 0.6);//?????????
        window.setAttributes(p);

        dialog.setTitle(model.getTopic());
        dialog.setEndTime("??????????????????:" + model.getEnd_time());
        dialog.setCreator("????????????" + model.getFrom().getName());
        dialog.setNegBtn("??????", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (model.getAnonymity().equals("1")) {
            dialog.setNiming("??????");
        } else {
            dialog.setNiming("??????");
        }
        dialog.setPosBtn("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> chose = new ArrayList<>();
                ArrayList<VoteBean.UserListBean> userbeanList = new ArrayList<>(model.getUser_list());

                for (int i = 0; i < list.size(); i++) {
                    ChoseBean object = list.get(i);
                    if (object.isChecked()) {
                        chose.add(object.getContent());
                    }
                }
                VoteBean.UserListBean userListBean = new VoteBean.UserListBean();
                userListBean.setStatus("FINISH");
                userListBean.setUser_name(Hawk.get(constant.myNumber));
                userListBean.setUser_id(FLUtil.getMacAddress());
                userListBean.setMeeting_vote_id(model.get_id());
                userListBean.setChoose(chose);
                requestToVote(userListBean, flag);
            }
        });
        dialog.setOnItemClickEvent(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * websocket??????????????????
     */
    private void wsUpdataVote(Object obj, String packType, String flag) {
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setFlag(flag);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(packType);
        bean.setBody(obj);

        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("gdgsdgsdgdgf444666",flag+"");

            }
        }).start();*/

    }


    /**
     * ??????????????????
     */
    private void creatVote(VoteBean bean, String flag) {
        if (UserUtil.isTempMeeting) {
            wsUpdataVote(bean, constant.NEWVOTE, flag);

            VoteInfoDialog infoDialog = new VoteInfoDialog.Builder(getActivity())
                    .setTitle("??????????????????")
                    .create();
            infoDialog.show();
        }
    }

    /**
     * ??????????????????(??????????????????)??????
     */
    private void requestToVote(VoteBean.UserListBean bean, String flag) {
        wsUpdataVote(bean, constant.UPDATEVOTE, flag);
         voteAdapter2.notifyDataSetChanged();
    }

    /**
     * ??????????????????
     */
    private void deleatVote(Map<String, Object> map) {
        NetWorkManager.getInstance().getNetWorkApiService().removeMeetingVote(map).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onSuccess(BasicResponse response) {
                        loadData();
                    }
                });
    }

    /**
     * ??????????????????
     */
    private void updateMeetingVote(Map<String, Object> map) {
        NetWorkManager.getInstance().getNetWorkApiService().updateMeetingVote(map).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onSuccess(BasicResponse response) {
                        loadData();
                    }
                });
    }

    /**
     * ??????????????????
     */
    private void finishVote(VoteBean voteBean, String flag) {

        wsUpdataVote(voteBean, constant.FINISHVOTE, flag);
    }


    //  ?????????
    private void showCheckBoxDialog(VoteBean model, int index, String flag) {
        final List<ChoseBean> list = new ArrayList<>();
        for (int i = 0; i < model.getOptions().size(); i++) {
            ChoseBean object = new ChoseBean();
            object.setContent(model.getOptions().get(i));
            object.setChecked(false);
            list.add(object);
        }

        WuHuCheckBoxDialog dialog = new WuHuCheckBoxDialog(getActivity(), R.style.AlertDialogStyle, list, flag);
        dialog.show();
        Window window = dialog.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.8);//?????????
        p.height = (int) (height * 0.6);//?????????
        window.setAttributes(p);

        dialog.setTitle(model.getTopic());
        dialog.setEndTime("??????????????????:" + model.getEnd_time());
        dialog.setCreator("????????????" + model.getFrom().getName());
        dialog.setNegBtn("??????", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (model.getAnonymity().equals("1")) {
            dialog.setNiming("??????");
        } else {
            dialog.setNiming("??????");
        }
        dialog.setPosBtn("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> chose = new ArrayList<>();
                ArrayList<VoteBean.UserListBean> userbeanList = new ArrayList<>(model.getUser_list());

                for (int i = 0; i < list.size(); i++) {
                    ChoseBean object = list.get(i);
                    if (object.isChecked()) {
                        chose.add(object.getContent());
                    }
                }

                VoteBean.UserListBean userListBean = new VoteBean.UserListBean();
                userListBean.setUser_name(Hawk.get(constant.myNumber));
                userListBean.setStatus("FINISH");
                userListBean.setUser_id(FLUtil.getMacAddress());
                userListBean.setMeeting_vote_id(model.get_id());
                userListBean.setChoose(chose);
                requestToVote(userListBean, flag);
            }
        });
        dialog.setOnItemClickEvent(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
/*
        Hawk.put(UserUtil.meeting_record_id,"");
        if (Hawk.contains("allVote")){
            HashMap<String,ArrayList<VoteListBean.VoteBean>> sites2 = new HashMap<String,ArrayList<VoteListBean.VoteBean>>();
            if (Hawk.get("allVote")!=null){
                sites2=Hawk.get("allVote");
                Iterator <Map.Entry< String, ArrayList<VoteListBean.VoteBean> >> iterator = sites2.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry< String, ArrayList<VoteListBean.VoteBean> > entry = iterator.next();

                    if (entry.getKey().equals(UserUtil.meeting_record_id)&&entry.getKey()!=null){
                        if (entry.getValue()!=null&&entry.getValue().size()>0){
                            ArrayList<VoteListBean.VoteBean>  arrayList=new ArrayList<>();

                            arrayList.addAll((ArrayList<VoteListBean.VoteBean>)entry.getValue());
                            for (int k=0;k<arrayList.size();k++){
                                if (UserUtil.isTempMeeting) {
                                    VoteListBean.VoteBean  voteBean=arrayList.get(k);
                                   // wsUpdataVote(voteBean, constant.NEWVOTE, voteBean.getFlag());

                                }

                            }

                        }

                    }


                }

            }



        }
*/





    }

    /*
     * ????????????????????????
     * */
    private void getVoteFile(File[] files) {
       //shareFileBeans.clear();
        Log.d(TAG, "??????~~~~~11");
        String content = "";
        if (files != null) {// ???????????????????????????????????????????????????
            for (File file : files) {
                Log.d(TAG, "??????~~~~~11" + file.getPath());

                if (file.isDirectory()) {
                    Log.d(TAG, "??????????????????????????????1" + file.getName()
                            + file.getPath());

                    getVoteFile(file.listFiles());
                    Log.d(TAG, "??????????????????????????????2" + file.getName()
                            + file.getPath());
                } else {
                    String fileName = file.getName();
                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    Log.d(TAG, "????????????=" + endStr + "????????????" + fileName);
                    Uri uri = Uri.fromFile(file);

                    for (int i=0;i<voteList.size();i++){
                       List <VoteBean.TemporBean>temporBeanList=new ArrayList<>();
                        temporBeanList=voteList.get(i).getTemporBeanList();
                       for (int  n=0;n<temporBeanList.size();n++){
                           if (fileName.equals(temporBeanList.get(n).getFileName())){
                               temporBeanList.get(n).setVotePath(file.getPath());

                           }

                       }

                    }
                    Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                  /*  fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setNet(false);
                    fileBean.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
                    //  fileBean.setType(endStr);
                    fileBean.setType(getFileType(endStr));
                    shareFileBeans.add(fileBean);*/

                }
            }
            /*fileBeans.addAll(shareFileBeans);
            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();*/
        }
    }
    /**
     * ????????????????????????
     */
    @AfterPermissionGranted(constant.WRITE_PERMISSION_CODE1)
    private void checkWritePermission1() {
        if (!PermissionManager.checkPermission(getActivity(), constant.PERMS_WRITE1)) {
            PermissionManager.requestPermission(getActivity(), constant.WRITE_PERMISSION_TIP, constant.WRITE_PERMISSION_CODE1, constant.PERMS_WRITE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //?????????????????????EasyPermission?????????
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Toast.makeText(getActivity(), "????????????", Toast.LENGTH_LONG).show();

    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(getActivity(), "??????????????????", Toast.LENGTH_LONG).show();
        /**
         * ??????????????????????????????????????????'NEVER ASK AGAIN.'??????'????????????'?????????????????????
         * ?????????????????????????????????????????????????????????????????????
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        if (myBroadcastReceiver!=null){
            getActivity().unregisterReceiver(myBroadcastReceiver);

        }
        if (myVoteImagBroadcastReceiver!=null){

            getActivity().unregisterReceiver(myVoteImagBroadcastReceiver);
        }
    }
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent in) {
            if (in.getAction().equals(constant.RUSH_VOTE_LIST_BROADCAST)){
                //???????????????????????????????????????
                loadData();
            }else if (in.getAction().equals(constant.SEE_IMA_BROADCAST)){
               String path= in.getStringExtra("votefilePath");
               if(StringUtils.isEmpty(path)){
                   return;

               }
                showImage(openImage( path));
            }

        }

    }
}


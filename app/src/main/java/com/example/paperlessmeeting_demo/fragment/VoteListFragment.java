package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.RecycleAdapter;
import com.example.paperlessmeeting_demo.adapter.VoteAdapter;
import com.example.paperlessmeeting_demo.adapter.VoteChairmanAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.ChoseBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.bean.ItemBean;
import com.example.paperlessmeeting_demo.bean.PieEntry;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.WSBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.dialog.CheckBoxDialog;
import com.example.paperlessmeeting_demo.dialog.PieChartDialog;
import com.example.paperlessmeeting_demo.dialog.RadioDialog;
import com.example.paperlessmeeting_demo.dialog.VoteInfoDialog;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.PermissionManager;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.widgets.DrawableTextView;
import com.google.gson.Gson;
import com.mrgao.luckly_popupwindow.LucklyPopopWindow;
import com.orhanobut.hawk.Hawk;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class VoteListFragment extends BaseFragment implements VoteAdapter.voteClickListener, VoteChairmanAdapter.voteClickListener, EasyPermissions.PermissionCallbacks {

    @Override
    protected int getLayoutId() {
        if(UserUtil.ISCHAIRMAN){
            return R.layout.activity_vote_list_chairman;
        }else{
            return R.layout.activity_vote_list;
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initView() {}

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

    }

    @BindView(R.id.vote_refreshLayout)
    protected RefreshLayout home_refreshLayout;

    @BindView(R.id.recycle_view)
    protected RecyclerView rv;

    ArrayList<VoteListBean.VoteBean> voteList = new ArrayList<>();
    private LucklyPopopWindow mLucklyPopopWindow;

    private RecyclerView.LayoutManager mLayoutManager;
    //  修改1
    VoteAdapter voteAdapter1;
    VoteChairmanAdapter voteAdapter2;

    private String[] popTitleArr;
    private String TAG = "VoteListFragment";
    //  当前选中单选
    private ImageView currentSelDanxuan;
    //  当前选中匿名
    private ImageView currentSelNiming;
    private RecycleAdapter adapter;
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

    private AlertDialog results;

    private FragmentManager fragmentManager;
    private String titles;
    private Context context;
    private String currentEndDate;

    public VoteListFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public VoteListFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public VoteListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        voteAdapter1 = new VoteAdapter(getActivity());
        voteAdapter2 = new VoteChairmanAdapter(getActivity());
        DrawableTextView new_vote = null;
        if(UserUtil.ISCHAIRMAN){
            new_vote = rootView.findViewById(R.id.new_vote);

            voteAdapter2.setOnVoteListener(this);
            new_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNewVoteItem();

                }
            });
        }else{

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
        if(UserUtil.ISCHAIRMAN){
            rv.setAdapter(voteAdapter2);
        }else {
            rv.setAdapter(voteAdapter1);
        }

        home_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        loadData();
        // 监听socket信息
        EventBus.getDefault().register(this);
        return rootView;
    }

    /**
     *  收到websocket 信息
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        Log.e("onReceiveMsg", TAG+"onReceiveMsg: " + message.toString());
        if(message.getMessage().contains("vote")){
            try {
                WSBean wsebean = new Gson().fromJson(message.getMessage(), WSBean.class);
                //  收到vote的websocket的信息
                if(wsebean!=null){
                    if( wsebean.getPackType().equals("vote")){
                        loadData();
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void showNewVoteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_newvote, null);
        layoutContent = view.findViewById(R.id.layout_newvote);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        add_chose = view.findViewById(R.id.add_chose);//新增选项目
        close_icon = view.findViewById(R.id.close_icon);
        btn_pos = view.findViewById(R.id.btn_pos);//确定
        btn_neg = view.findViewById(R.id.btn_neg);//取消
        ivCheckState1 = view.findViewById(R.id.iv_check_state1);
        ivCheckState2 = view.findViewById(R.id.iv_check_state2);
        ivCheckState3 = view.findViewById(R.id.iv_check_state3);
        ivCheckState4 = view.findViewById(R.id.iv_check_state4);
        edit_content = view.findViewById(R.id.edit_content);
        edit_text = view.findViewById(R.id.edit_text);
        edit_text2 = view.findViewById(R.id.edit_text2);
        edit_text3 = view.findViewById(R.id.edit_text3);

        edit_text3.setFocusable(false);
        edit_text3.setKeyListener(null);
        currentSelDanxuan = ivCheckState1;
        currentSelNiming = ivCheckState4;
        builder.setView(view);
        builder.setCancelable(false);
        results = builder.create();
        results.show();
        initRecycle();
        edit_text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickDialog dialog = new DatePickDialog(getActivity());
                //设置上下年分限制
                dialog.setYearLimt(3);
                //设置标题
                dialog.setTitle("选择时间");
                //设置类型
                dialog.setType(DateType.TYPE_ALL);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");
                //设置选择回调
                dialog.setOnChangeLisener(null);
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = format.format(date);
                        Log.d("DatePickDialog",str);
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
                results.dismiss();
            }
        });
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_content.getText())) {
                    Toast.makeText(getActivity(), "请输入投票内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edit_content.getText().length() < 5) {
                    Toast.makeText(getActivity(), "投票内容至少5个字符", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edit_text.getText())) {
                    Toast.makeText(getActivity(), "请输入选项1", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edit_text2.getText())) {
                    Toast.makeText(getActivity(), "请输入选项2", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(currentEndDate)) {
                    Toast.makeText(getActivity(), "请选择结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> options_list = new ArrayList();
                options_list.add(edit_text.getText().toString());
                options_list.add(edit_text2.getText().toString());
                //  拼接选项字符串
                for (int i = 0; i < list.size(); i++) {
                    ItemBean bean = list.get(i);
                    if (!TextUtils.isEmpty(bean.getText())) {
                        options_list.add(bean.getText().toString());
                    }
                }

                Map<String,Object> map = new HashMap<>();
                map.put("topic",edit_content.getText().toString());
                String c_id = Hawk.contains(constant.c_id) ? Hawk.get(constant.c_id) : "";
                map.put("c_id",c_id);
                map.put("status","ENABLE");
                map.put("options",options_list);
                map.put("end_time",currentEndDate);
                map.put("type",currentSelDanxuan == ivCheckState1 ? "0" : "1");
                map.put("anonymity",currentSelNiming == ivCheckState3 ? "1" : "0");
                map.put("from",UserUtil.user_id);
                map.put("meeting_record_id",UserUtil.meeting_record_id);

                creatVote(map);

                results.dismiss();
            }
        });
        add_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addData(list.size());
                add_chose.setVisibility(View.INVISIBLE);
            }
        });

        // 单选
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

        //  匿名
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

    //  单选按钮选择
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

    //  单选按钮选择
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

    //  初始化列表
    private void initRecycle() {
        if (list != null) {
            list.clear();
        }

        //  纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //      获取数据，向适配器传数据，绑定适配器
//        list = initData();
        adapter = new RecycleAdapter(getActivity(), list, new RecycleAdapter.ChoseClickListener() {
            @Override
            public void clickListener(int position,String flag) {
                if (list.size() > 0) {
                    add_chose.setVisibility(View.INVISIBLE);
                } else {
                    add_chose.setVisibility(View.VISIBLE);
                }

            }
        },"1");
        mRecyclerView.setAdapter(adapter);
        //      添加动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    protected ArrayList<ItemBean> initDatas() {
        ArrayList<ItemBean> mDatas = new ArrayList<ItemBean>();
        for (int i = 0; i < 1; i++) {
            mDatas.add(new ItemBean());
        }
        return mDatas;
    }

    // 刷新页面，无数据显示空白页面
    protected void refreshUI(ArrayList<VoteListBean.VoteBean> data) {
//        if (tempArr.size() == 0) {
//            emptyView.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.INVISIBLE);
//        }else{
//            emptyView.setVisibility(View.INVISIBLE);
//            recyclerView.setVisibility(View.VISIBLE);
//        }
        if(UserUtil.ISCHAIRMAN){
//            voteAdapter2.addFinalVotestate(data);
            voteAdapter2.setList(data);
            voteAdapter2.notifyDataSetChanged();
        }else {
//            voteAdapter1.addFinalVotestate(data);
            voteAdapter1.setList(data);
            voteAdapter1.notifyDataSetChanged();
        }

        home_refreshLayout.finishRefresh();
    }

    //获取投票数据
    public void loadData() {
        voteList.clear();
        if(UserUtil.isTempMeeting){
            return;
        }
        NetWorkManager.getInstance().getNetWorkApiService().findMeetingVoteList(UserUtil.meeting_record_id).compose(this.<BasicResponse<ArrayList<VoteListBean.VoteBean>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<ArrayList<VoteListBean.VoteBean>>>() {
                    @Override
                    protected void onSuccess(BasicResponse<ArrayList<VoteListBean.VoteBean>> response) {
                        Log.d(TAG, "loadData="+response.getData());
                        voteList = response.getData();

                        //  赋值一下mvotestatus
                        for(VoteListBean.VoteBean bean : voteList){
                            bean.setStatus(bean.getStatus());
                        }
                        refreshUI(voteList);
                    }

                    @Override
                    protected void onFail(BasicResponse<ArrayList<VoteListBean.VoteBean>> response) {
                        super.onFail(response);
                        home_refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        home_refreshLayout.finishRefresh();
                    }
                });

    }


    //   普通用户点击投票或者查看
    @Override
    public void clickListener(int position,String flag) {
        if (position >= 0 && voteList.size() > position) {
            VoteListBean.VoteBean model = voteList.get(position);
            int status = model.getMvoteStatus();

            // 已有结果
            if (status == Constants.VoteStatusEnum.hasFinshed) {
                boolean isNoUser = true;        //  数据结构下无user列表
                boolean isAllNoChoose = true;   //  是否所有人都没有投任何选项
                if(model.getUser_list().size()>0){
                    isNoUser = false;
                    for (VoteListBean.VoteBean.UserListBean bean : model.getUser_list()){
                        if(bean.getChoose().size()>0){
                            isAllNoChoose = false;
                        }
                    }
                }

                if(isNoUser || isAllNoChoose){
                    ToastUtils.showShort("暂无人投票!");
                    return;
                }
                PieChartDialog piechartDialog = new PieChartDialog(getActivity(), R.style.AlertDialogStyle, createData(model), voteList.get(position));
                piechartDialog.show();

                Window window = piechartDialog.getWindow();//获取dialog屏幕对象
                window.setGravity(Gravity.CENTER);//设置展示位置
                Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
                WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
                Point size=new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;
                p.width = (int) (width * 0.8);//设置宽
                p.height = (int) (height * 0.85);//设置宽
                window.setAttributes(p);
            }

            //  投票
            if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                if (model.getType().equals("0")) {
                    showRadioDialog(model, position);
                } else {
                    showCheckBoxDialog(model, position);
                }
            }
        }
    }

    //   主席台点击查看
    @Override
    public void chairmanClickListener(int position,String flag) {
        if (position >= 0 && voteList.size() > position) {
            VoteListBean.VoteBean model = voteList.get(position);
            int status = model.getMvoteStatus();

            // 已有结果
            if (status == Constants.VoteStatusEnum.hasFinshed) {
                boolean isNoUser = true;        //  数据结构下无user列表
                boolean isAllNoChoose = true;   //  是否所有人都没有投任何选项
                if(model.getUser_list().size()>0){
                    isNoUser = false;
                    for (VoteListBean.VoteBean.UserListBean bean : model.getUser_list()){
                        if(bean.getChoose().size()>0){
                            isAllNoChoose = false;
                        }
                    }
                }

                if(isNoUser || isAllNoChoose){
                    ToastUtils.showShort("暂无人投票!");
                    return;
                }
                PieChartDialog piechartDialog = new PieChartDialog(getActivity(), R.style.AlertDialogStyle, createData(model), voteList.get(position));
                piechartDialog.show();

                Window window = piechartDialog.getWindow();//获取dialog屏幕对象
                window.setGravity(Gravity.CENTER);//设置展示位置
                Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
                WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
                Point size=new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;
                p.width = (int) (width * 0.8);//设置宽
                p.height = (int) (height * 0.85);//设置宽
                window.setAttributes(p);
            }

            //  投票
            if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                if (model.getType().equals("0")) {
                    showRadioDialog(model, position);
                } else {
                    showCheckBoxDialog(model, position);
                }
            }
        }
    }

    //  点击操作
    @Override
    public void operationclickListener(int position,String flag) {
        int status = voteList.get(position).getMvoteStatus();
        if (status == Constants.VoteStatusEnum.hasStartUnVote) {
            popTitleArr = new String[]{"投票", "结束"};
        } else if (status == Constants.VoteStatusEnum.hasFinshed) {
            return;
        } else {
            popTitleArr = new String[]{"结束"};
        }

        mLucklyPopopWindow = new LucklyPopopWindow(getActivity());
        //给popupWindow添加数据
//        mLucklyPopopWindow.setData(new String[]{"结束","删除"}, new int[]{ R.mipmap.operation_end, R.mipmap.operation_delet});
        mLucklyPopopWindow.setData(popTitleArr);

        //必须设置宽度
        mLucklyPopopWindow.setWidth(160);
        mLucklyPopopWindow.setRadius(0);
        //监听事件
        mLucklyPopopWindow.setOnItemClickListener(new LucklyPopopWindow.OnItemClickListener() {
            @Override
            public void onItemClick(int popPosition) {
                VoteListBean.VoteBean model = voteList.get(position);

                if (popTitleArr[popPosition].equals("投票")) {
                    int status = model.getMvoteStatus();
                    //  未开始点击投票
                    if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                        if (model.getType().equals("0")) {
                            showRadioDialog(model, position);
                        } else {
                            showCheckBoxDialog(model, position);
                        }
                    }
                } else if (popTitleArr[popPosition].equals("结束")) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("id",model.get_id());
                    finishVote(map);

                } else {
                    Map<String,Object> map = new HashMap<>();
                    map.put("id",model.get_id());
                    deleatVote(map);
                }
                mLucklyPopopWindow.dismiss();
            }
        });

        //添加分割线(可选)
        mLucklyPopopWindow.addItemDecoration(LucklyPopopWindow.VERTICAL, Color.GRAY, 1);
        //设置image不显示(可选)
        mLucklyPopopWindow.setImageDisable(true);
        //设置image的大小(可选)
        mLucklyPopopWindow.setTextSize(20);
        mLucklyPopopWindow.setAnimationStyle(0);
//        mLucklyPopopWindow.setImageSize(15,15);
        //显示popopWindow
        mLucklyPopopWindow.setTrianleHeight(0);
        //  获取对应的view
        View view = mLayoutManager.findViewByPosition(position);
        //mLayoutManager为recyclre的布局管理器
        RelativeLayout layout = (RelativeLayout) view;        //获取布局中任意控件对象
        ImageView operation = (ImageView) layout.findViewById(R.id.operation);
        mLucklyPopopWindow.showAtLocation(getActivity().getWindow().getDecorView(), operation);

    }

    //  创建饼状图的数据
    private ArrayList<PieEntry> createData(VoteListBean.VoteBean model) {
        ArrayList<PieEntry> pieLists = new ArrayList<>();

        String entryname = "";
        ArrayList<String> optionAll = new ArrayList<>();
        Map<String, Integer> map = new HashMap();
        //  不同用户所有选项加入一个集合
        for (int i = 0; i < model.getUser_list().size(); i++) {
            VoteListBean.VoteBean.UserListBean userbean = model.getUser_list().get(i);
            for (int j = 0; j < userbean.getChoose().size(); j++) {

                optionAll.add(userbean.getChoose().get(j));
            }
        }

        // 小算法，找到集合中重复数据
        float totalNum = optionAll.size();
        if (optionAll.size() > 0) {
            if (optionAll.size() > 1) {
                int count = 1;// 默认出现的次数为1
                for (int i = 0; i < optionAll.size(); i++) {
                    count = 1;
                    for (int j = i + 1; j < optionAll.size(); j++) {
                        if (optionAll.get(i).equals(optionAll.get(j))) {
                            count++;// 次数+1
                            optionAll.remove(j);
                            j--;// 这里是重点，因为集合remove（）后，长度改变了，对应的下标也不再是原来的下标，//仔细体会
                        }
                    }
                    System.out.println(optionAll.get(i) + "》》》出现了" + count);
                    map.put(optionAll.get(i), count);

                }
            } else {
                // 只有1个数据
                totalNum = 1;
                map.put(optionAll.get(0), 1);
            }
        } else {
            //   无数据,前面已做处理，这边只是严谨
        }

        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            List<String> chooseUserList = new ArrayList();
            for (int i = 0; i < model.getUser_list().size(); i++) {
                VoteListBean.VoteBean.UserListBean userbean = model.getUser_list().get(i);
                if(userbean.getChoose().contains(entry.getKey()) ){
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


    /*投票成功*/
    private void voteSucessDialog() {
        VoteInfoDialog infoDialog = new VoteInfoDialog.Builder(getActivity())
                .setTitle("投票成功")
                .setMessage("投票结果正在统计中…")
                .setButton(view -> {
                        }
                ).create();
        infoDialog.show();
    }

    //  单选框
    private void showRadioDialog(VoteListBean.VoteBean model, int index) {
        final List<ChoseBean> list = new ArrayList<>();
        for (int i = 0; i < model.getOptions().size(); i++) {
            ChoseBean object = new ChoseBean();
            object.setContent(model.getOptions().get(i));
            object.setChecked(false);
            list.add(object);
        }

        RadioDialog dialog = new RadioDialog(getActivity(), R.style.AlertDialogStyle, list,"1");
        dialog.show();
        dialog.setTitle(model.getTopic());
        dialog.setEndTime("投票截止时间:" + model.getEnd_time());
        dialog.setCreator("发起人：" + model.getFrom().getName());
        dialog.setAvater_img(model.getFrom().getAvatar());
        dialog.setNegBtn("弃权", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (model.getAnonymity().equals("1")) {
            dialog.setNiming("实名");
        } else {
            dialog.setNiming("匿名");
        }
        dialog.setPosBtn("投票", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> chose = new ArrayList<>();
                ArrayList<VoteListBean.VoteBean.UserListBean> userbeanList = new ArrayList<>(model.getUser_list());

                for (int i = 0; i < list.size(); i++) {
                    ChoseBean object = list.get(i);
                    if (object.isChecked()) {
                        chose.add(object.getContent());
                    }
                }

                Map<String,Object> map = new HashMap<>();
                map.put("meeting_vote_id",model.get_id());
                map.put("user_id",UserUtil.user_id);
                map.put("choose",chose);
                requestToVote(map);
            }
        });
        dialog.setOnItemClickEvent(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * websocket发送投票更新,暂时不需要发socket,调用接口后台直接发送
     */
    private void wsUpdataVote() {
        WSBean bean = new WSBean();
        bean.setReqType(0);
        bean.setUser_id(UserUtil.user_id);
        bean.setPackType("vote");
        bean.setBody("");
        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);
    }


    /**
     * 新增投票请求
     */
    private void creatVote(Map<String,Object> map) {
        if(UserUtil.isTempMeeting){

            String strJson = new Gson().toJson(map);
            JWebSocketClientService.sendMsg(strJson);
        }
        NetWorkManager.getInstance().getNetWorkApiService().createMeetingVote(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onSuccess(BasicResponse response) {
                        loadData();
                        VoteInfoDialog infoDialog = new VoteInfoDialog.Builder(getActivity())
                                .setTitle("新增投票成功")
                                .create();
                        infoDialog.show();
                    }
                });
    }

    /**
     * 投票操作请求
     */
    private void requestToVote(Map<String,Object> map) {
        NetWorkManager.getInstance().getNetWorkApiService().toVote(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onSuccess(BasicResponse response) {
                        voteSucessDialog();
                        loadData();
                    }
                });
    }

    /**
     * 删除投票请求
     */
    private void deleatVote(Map<String,Object> map){
        NetWorkManager.getInstance().getNetWorkApiService().removeMeetingVote(map).compose(this.<BasicResponse>bindToLifecycle())
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
     * 更新投票请求
     */
    private void updateMeetingVote(Map<String,Object> map){
        NetWorkManager.getInstance().getNetWorkApiService().updateMeetingVote(map).compose(this.<BasicResponse>bindToLifecycle())
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
     * 结束投票请求
     */
    private void finishVote(Map<String,Object> map){
        NetWorkManager.getInstance().getNetWorkApiService().finishVote(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onSuccess(BasicResponse response) {
                        loadData();
                    }
                });
    }


    //  复选框
    private void showCheckBoxDialog(VoteListBean.VoteBean model, int index) {
        final List<ChoseBean> list = new ArrayList<>();
        for (int i = 0; i < model.getOptions().size(); i++) {
            ChoseBean object = new ChoseBean();
            object.setContent(model.getOptions().get(i));
            object.setChecked(false);
            list.add(object);
        }

        CheckBoxDialog dialog = new CheckBoxDialog(getActivity(), R.style.AlertDialogStyle, list,"1");
        dialog.show();
        dialog.setTitle(model.getTopic());
        dialog.setEndTime("投票截止时间:" + model.getEnd_time());
        dialog.setCreator("发起人：" + model.getFrom().getName());
        dialog.setAvater_img(model.getFrom().getAvatar());
        dialog.setNegBtn("弃权", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (model.getAnonymity().equals("1")) {
            dialog.setNiming("实名");
        } else {
            dialog.setNiming("匿名");
        }
        dialog.setPosBtn("投票", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> chose = new ArrayList<>();
                ArrayList<VoteListBean.VoteBean.UserListBean> userbeanList = new ArrayList<>(model.getUser_list());

                for (int i = 0; i < list.size(); i++) {
                    ChoseBean object = list.get(i);
                    if (object.isChecked()) {
                        chose.add(object.getContent());
                    }
                }

                Map<String,Object> map = new HashMap<>();
                map.put("meeting_vote_id",model.get_id());
                map.put("user_id",UserUtil.user_id);
                map.put("choose",chose);
                requestToVote(map);
            }
        });
        dialog.setOnItemClickEvent(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    /**
     * 检查读写权限权限
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
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 请求权限成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_LONG).show();

    }

    /**
     * 请求权限失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(getActivity(), "用户授权失败", Toast.LENGTH_LONG).show();
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}

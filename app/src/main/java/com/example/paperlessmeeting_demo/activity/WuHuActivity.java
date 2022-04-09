package com.example.paperlessmeeting_demo.activity;

import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.activity.Sign.SignActivity;
import com.example.paperlessmeeting_demo.activity.Sign.SignListActivity;
import com.example.paperlessmeeting_demo.adapter.WuHuListAdapter;
import com.example.paperlessmeeting_demo.base.BaseActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v4.view.ViewPager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.adapter.PagerAdapter;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.fragment.WuHuFragment;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.ServerManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WuHuActivity  extends BaseActivity implements View.OnClickListener,WuHuListAdapter.saveSeparatelyInterface{
    @BindView(R.id.edit_ll)
    RelativeLayout edit_ll;
    @BindView(R.id.edit_rl)
    RelativeLayout edit_rl;
    @BindView(R.id.edit_name_rl)
    RelativeLayout edit_name_rl;
    @BindView(R.id.vote_ll)
    RelativeLayout vote_ll;
    @BindView(R.id.consult_ll)
    RelativeLayout consult_ll;

    private Dialog dialog;
    @BindView(R.id.comfirm)
    ImageView comfirm;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.name)
    TextView name;

  /*  @BindView(R.id.tittle2)*/
  private   EditText tittle2;
   /* @BindView(R.id.company_name)*/
  private   EditText company_name;
    private MyListView myListView;
    private WuHuListAdapter wuHuListAdapter;
    private  int  k=0;
    private View inflate;
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList=new ArrayList<>();

    private RelativeLayout  add_topic_rl;
    private RelativeLayout  dialg_rl_root;
    private RelativeLayout sava_all;
    private Button mBtnDelete;
    private Button mBtnAdd;
    private ViewPager mViewPager;
    private SparseArray<WuHuFragment>mTestFragments;
    private PagerAdapter mPagerAdapter;
    private int key;
    private int mCurPos;
    private int fragmentPos;
     private MyBroadcastReceiver myBroadcastReceiver;
    private MyBroadcastReceiver myRefreshBroadcastReceiver;
    private  WuHuEditBean wuHuEditBean;
    //表示红线的flag
    private String lineFlag;
    //标识主题的颜色
    private String themFlag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.ADD_FRAGMENT_BROADCAST);
        registerReceiver(myBroadcastReceiver, filter);

      /*  myRefreshBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(constant.REFRESH_BROADCAST);
        registerReceiver(myRefreshBroadcastReceiver, filter2);*/

        // 如果是临时会议,判断是否是主席
        if (UserUtil.isTempMeeting) {
            if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageClient)) {
                //  客户端默认非主席
                UserUtil.ISCHAIRMAN = false;
                String ipUrl = getIntent().getStringExtra("ip");
                if (!StringUtils.isEmpty(ipUrl)) {
                    UrlConstant.TempWSIPString = "ws://" + ipUrl + ":" + UrlConstant.port;
                    Log.e("临时会议", "临时会议的server IP ======" + UrlConstant.TempWSIPString);
                }

                //  连接websocket(server，client均要连)
                JWebSocketClientService.initSocketClient();

            } else if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer)) {
                UserUtil.ISCHAIRMAN = true;
                String code = getIntent().getStringExtra("code");
              //  alertCodeDialog(code);

                //  广播四位数code以及IP地址
                UDPBroadcastManager.getInstance().sendUDPWithCode(code);
                // 开启socket服务器
                ServerManager.getInstance().startMyWebsocketServer(UrlConstant.port);
                UrlConstant.TempWSIPString = "ws://" + FLUtil.getIPAddress() + ":" + UrlConstant.port;
                // 主席的client在收到server创建成功后连接
            }
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wuhu_main;
    }

    @Override
    protected void initView() {

        if ( Hawk.contains(constant.myNumber)){
            String  n=Hawk.get(constant.myNumber);
            name.setText(n);
        }
        edit_rl.setOnClickListener(this);
        edit_ll.setOnClickListener(this);
        comfirm.setOnClickListener(this);
        vote_ll.setOnClickListener(this);
        consult_ll.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mBtnDelete = (Button) findViewById(R.id.btn_delete);
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        edit_name_rl.setVisibility(View.GONE);
        mTestFragments = new SparseArray<>();
        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
        fragmentPos++;
        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
        fragmentPos++;

      /*  mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
        fragmentPos++;
        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
        fragmentPos++;
        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
        fragmentPos++;*/
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mTestFragments);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurPos = position;
                Log.d("sort:", "onPageSelected: " + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestFragments.removeAt(mCurPos);
                mPagerAdapter.notifyDataSetChanged();
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("aa","aa");
                intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);

            }
        });
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case  R.id.consult_ll:
                 intent = new Intent(WuHuActivity.this, SignListActivity.class);
                startActivity(intent);

                break;
            case R.id.edit_rl:
                edit_name_rl.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_ll:
                showRightDialog();
                break;
            case R.id.comfirm:
                edit_name_rl.setVisibility(View.GONE);
                if ( edit_name.getText().toString().isEmpty()){
                    Toast.makeText(WuHuActivity.this,"名字不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                Hawk.put(constant.user_name, edit_name.getText().toString());
                UserUtil.user_name = edit_name.getText().toString();
                name.setText(edit_name.getText().toString());

                break;
            case R.id.vote_ll:
                intent=new Intent(WuHuActivity.this, WuHuVoteActivity.class);
                startActivity(intent);
                break;

        }

    }
    //保存单个数据
    @Override
    public void saveData(int position) {

        if (Hawk.contains("WuHuFragmentData")) {
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            wuHuEditBean.setTopics(company_name.getText().toString());
            wuHuEditBean.setTopic_type(tittle2.getText().toString());
            wuHuEditBean.setLine_color(lineFlag);
            wuHuEditBean.setThem_color(themFlag);
            wuHuEditBean.setPosition(position+"");
            wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
            Hawk.put("WuHuFragmentData",wuHuEditBean);
            //更新单个数据
            wsUpdata(wuHuEditBean,constant.REFRASHWuHUSIGLEDATA);
        }
    }


    /**
     * 收到websocket 信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        Log.e("onReceiveMsg0000: " , message.toString());

        if (UserUtil.isTempMeeting) {
            //  名称已经确定
            if (message.getMessage().contains(constant.SURENAME)) {
                UserUtil.user_name = Hawk.get(constant.myNumber);
               // ttendeesName.setText(Hawk.get(constant.myNumber) + "号   您好");
            }

            if (UserUtil.ISCHAIRMAN) {
                // 服务端开启成功 服务端上的client只可以在此连接
                if (message.getMessage().equals(constant.SERVERSTART)) {
                    //  连接websocket(server，client均要连)
                    JWebSocketClientService.initSocketClient();
                }
            }

        }
        //  client端接收到信息
        if (message.getType().equals(MessageReceiveType.MessageClient)) {
            // 查询投票信息
            if (message.getMessage().contains(constant.WUHUADDFRAGMENT)) {
                Log.e("onReceiveMsg11111: " , message.toString());
                try {
                    TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                    }.getType());
                    //  收到vote的websocket的信息
                    if (wsebean != null) {
                        WuHuEditBean     wuHuEditBean = wsebean.getBody();
                        Log.d("onReceiveMsg11111大小=", wuHuEditBean.getEditListBeanList().size()+ "");
                        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
                        fragmentPos++;
                        mPagerAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(message.getMessage().contains(constant.REFRASHWuHUSIGLEDATA)){
                Log.e("onReceiveMsg2222: " , message.toString());

            }

        }
    }
    public void showRightDialog( ) {
        //自定义dialog显示布局
        inflate = LayoutInflater.from(WuHuActivity.this).inflate(R.layout.dialog_edit_wuhu, null);
        //自定义dialog显示风格
        dialog = new Dialog(WuHuActivity.this, R.style.DialogRight);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        dialog.setCanceledOnTouchOutside(true);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        View line=inflate.findViewById(R.id.line);
        RadioGroup line_colors=inflate.findViewById(R.id.line_colors);
        RadioGroup theme_colors=inflate.findViewById(R.id.theme_colors);
        myListView=inflate.findViewById(R.id.myList_view);
        company_name=inflate.findViewById(R.id.company_name);
        tittle2=inflate.findViewById(R.id.tittle2);

        add_topic_rl=inflate.findViewById(R.id.add_topic_rl);
        dialg_rl_root=inflate.findViewById(R.id.dialg_rl_root);
        sava_all=inflate.findViewById(R.id.sava_all);
        Log.d("reyeyrty",UserUtil.ISCHAIRMAN+"");
        if (UserUtil.ISCHAIRMAN) {
            if (Hawk.contains("WuHuFragmentData")){
                 wuHuEditBean= Hawk.get("WuHuFragmentData");
                wuHuEditBean.setTopics("2022年临时会议");
                wuHuEditBean.setTopic_type("会议记录");
                WuHuEditBean.EditListBean editListBean=new WuHuEditBean.EditListBean();
                editListBean.setSubTopics("总结2022年");
                editListBean.setAttendeBean("王二狗，李狐狸，张太郎，刘毛毛");
                WuHuEditBean.EditListBean editListBean1=new WuHuEditBean.EditListBean();
                editListBean1.setSubTopics("总结2022年");
                editListBean1.setAttendeBean("王二发狗，李狐请问狸，张恶热太郎，刘恶热毛毛");
                wuHuEditBeanList.add(editListBean1);
                wuHuEditBeanList.add(editListBean);
                Hawk.put("WuHuFragmentData",wuHuEditBean);
            }
        }

        wuHuListAdapter=new WuHuListAdapter(WuHuActivity.this,wuHuEditBeanList);
        wuHuListAdapter.setSaveSeparatelyInterface(this);
        myListView.setAdapter(wuHuListAdapter);
        wuHuListAdapter.notifyDataSetChanged();

        line_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                switch (checkedId){

                    case R.id.color_rb1:
                        bundle.putString("refreshType","1");
                        line.setBackgroundColor(Color.parseColor("#EA4318"));
                        lineFlag="1";
                        break;
                    case R.id.color_rb2:
                        bundle.putString("refreshType","2");
                        line.setBackgroundColor(Color.parseColor("#1D1D1D"));
                        lineFlag="2";
                        break;

                }
                intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);
            }
        });

        theme_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkId) {
                Intent intent = new Intent();
                Bundle bundle=new Bundle();

                switch (checkId){
                    case R.id.color_rb3:
                        bundle.putString("refreshType","3");
                        themFlag="3";

                        break;
                    case R.id.color_rb4:
                        bundle.putString("refreshType","4");
                        themFlag="4";

                        break;
                    case R.id.color_rb5:
                        bundle.putString("refreshType","5");
                        themFlag="5";

                        break;
                    case R.id.color_rb6:
                        bundle.putString("refreshType","6");
                        themFlag="6";

                        break;
                    case R.id.color_rb7:
                        bundle.putString("refreshType","7");
                        themFlag="7";

                        break;

                }
                intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);
            }
        });


        add_topic_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WuHuEditBean.EditListBean editListBean=new WuHuEditBean.EditListBean();
                editListBean.setSubTopics(wuHuEditBeanList.get(wuHuEditBeanList.size()-1).getSubTopics());
                editListBean.setAttendeBean(wuHuEditBeanList.get(wuHuEditBeanList.size()-1).getAttendeBean());
                wuHuEditBeanList.add(editListBean);
                wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                wuHuListAdapter.notifyDataSetChanged();

                if (Hawk.contains("WuHuFragmentData")){
                    WuHuEditBean wuHuEditBean= Hawk.get("WuHuFragmentData");
                    wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                    Hawk.put("WuHuFragmentData",wuHuEditBean);
                    //通知其他设备增加fragment
                    wsUpdata(wuHuEditBean,constant.WUHUADDFRAGMENT);
                }
                //自己增加frgament
                mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
                fragmentPos++;
                mPagerAdapter.notifyDataSetChanged();

              /*  Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("aa","filePath");
                intent.putExtras(bundle);
                intent.setAction(constant.REFRESH_BROADCAST);
                  sendBroadcast(intent);*/

            }
        });
        //获取当前Activity所在的窗体
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
        wlp.gravity= Gravity.RIGHT;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Point size=new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.4);//设置宽
        wlp.height = 1600;//设置宽
        window.setAttributes(wlp);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBroadcastReceiver!=null){
            unregisterReceiver(myBroadcastReceiver);
        }
    }
    /**
     * websocket发送数据至其他设备
     */
    private void wsUpdata(Object obj, String packType) {
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(packType);
        bean.setBody(obj);
        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);
    }
    @Override
    protected void initData() {

    }
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent in) {
            if (in.getAction().equals(constant.ADD_FRAGMENT_BROADCAST)){
                mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
                fragmentPos++;
                mPagerAdapter.notifyDataSetChanged();

            }else if(in.getAction().equals(constant.REFRESH_BROADCAST)){



            }


        }
    }
}

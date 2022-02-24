package com.example.paperlessmeeting_demo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.enums.ToFragmentIsConnectedType;
import com.example.paperlessmeeting_demo.enums.ToFragmentType;
import com.example.paperlessmeeting_demo.fragment.WhiteBoard.MyShareWhiteFragment;
import com.example.paperlessmeeting_demo.fragment.WhiteBoard.MyTeamWhiteFragment;
import com.example.paperlessmeeting_demo.fragment.WhiteBoard.MyWhiteFragment;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.widgets.MyWhiteTabLayout;
import com.example.paperlessmeeting_demo.widgets.NoScrollViewPager;
import com.github.guanpy.library.ann.ReceiveEvents;
import com.github.guanpy.wblib.utils.Events;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 白板工具
 * Created by gpy on 2015/6/2.
 */
public class WhiteBoardActivity2 extends BaseActivity {
    @Override
    protected void initData() {}
    @BindView(R.id.tabLayout)
    MyWhiteTabLayout tabLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.fullscreen_back)
    ImageView back;
    private ToFragmentListener mFragmentListener1;
    private ToFragmentListener mFragmentListener2;
    private ToFragmentListener mFragmentListener3;

    private String[] titleArr = {"同屏", "白板", "协作"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private boolean isShareConnected = false;
    private boolean isTeamConnected = false;
    private int currentSelTab = 1;  // 默认先选中index=1
    private final String TAG = "WhiteBoardActivity2";

    @Override
    protected void initView() {
        initViewPagerFragment();
    }

    private void initEvent() {
        // 非主席或者是点击邀请进来，禁止点击tab切换
        if(!UserUtil.ISCHAIRMAN || Hawk.get(constant.team_share) != null){
            tabLayout.setClickable(false);


            LinearLayout tabStrip = (LinearLayout) tabLayout.getChildAt(0);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View tabView = tabStrip.getChildAt(i);
                if (tabView != null) {
                    tabView.setClickable(false);
                }
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // 离开先保存数据，跳转的fragment加载数据
                if(tab.getPosition() != currentSelTab){
                    if(currentSelTab==0){
                        mFragmentListener1.onTypeClick(ToFragmentType.SaveData,ToFragmentIsConnectedType.nonsense);
                    }else if(currentSelTab==1){
                        mFragmentListener2.onTypeClick(ToFragmentType.SaveData,ToFragmentIsConnectedType.nonsense);
                    }else {
                        mFragmentListener3.onTypeClick(ToFragmentType.SaveData,ToFragmentIsConnectedType.nonsense);
                    }
                    currentSelTab = tab.getPosition();
                }

                switch (tab.getPosition()){
                    case 0:
                        if(!isShareConnected){
                            CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "是否发送同屏信息?", null, "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if(clickConfirm){
                                        sendShareReqMsg();
                                    }else {
                                        tabLayout.getTabAt(1).select(); //默认选中第一个tab
                                        viewPager.setCurrentItem(1);
                                    }
                                }
                            });
                            return;
                        }
                        break;
                    case 1:
                        if(isShareConnected || isTeamConnected){
                            CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "是否断开同屏|协作?", null, "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if(clickConfirm){
                                        //  协作断开会直接退出，先判断协作
                                        if (isTeamConnected){
                                            // 清屏
                                            mFragmentListener3.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                            isTeamConnected = false;
                                            sendDisconnectTeamMsg();
                                        }else if(isShareConnected){
                                            // 清屏
                                            mFragmentListener1.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                            isShareConnected = false;
                                            sendDisconnectShareMsg();
                                        }
                                        isShareConnected = false;
                                        isTeamConnected  = false;

                                    }else {
                                        if(isShareConnected){
                                            tabLayout.getTabAt(0).select(); //选中第一个tab
                                            viewPager.setCurrentItem(0);
                                        }else if(isTeamConnected){
                                            tabLayout.getTabAt(2).select(); //选中第三个tab
                                            viewPager.setCurrentItem(2);
                                        }

                                    }
                                }
                            });
                        }

                        break;
                    case 2:
                        if(!isTeamConnected){
                            CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "是否发送协作信息?", null, "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if(clickConfirm){
                                        sendTeamReqMsg();
                                    }else {
                                        tabLayout.getTabAt(1).select(); //默认选中第一个tab
                                        viewPager.setCurrentItem(1);
                                    }
                                }
                            });
                            return;
                        }
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        if(isShareConnected){
                            //如果已经连接发送，询问是否重发
                            CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "是否重新发送同屏信息?", null, "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if(clickConfirm){
                                        sendShareReqMsg();
                                    }
                                }
                            });
                            return;
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        if(isTeamConnected){
                        //如果已经连接发送，询问是否重发
                        CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "是否重新发送协作信息?", null, "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                            @Override
                            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                if(clickConfirm){
                                    sendTeamReqMsg();
                                }
                            }
                        });
                        return;
                    }
                        break;
                }

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Hawk.get(constant.team_share) != null) {
                    if(Hawk.get(constant.team_share).equals("share")) {
                        ToastUtil.makeText(WhiteBoardActivity2.this,"不允许退出!");
                        return;
                    }
                }

                // 点击返回，如果有同屏，提示断开
                if(isShareConnected || isTeamConnected){
                    CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "是否断开同屏|协作?", null, "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if(clickConfirm){
                                //  协作断开会直接退出，先判断协作
                                if (isTeamConnected){
                                    // 清屏
                                    mFragmentListener3.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                    isTeamConnected = false;
                                    sendDisconnectTeamMsg();
                                }else if(isShareConnected){
                                    // 清屏
                                    mFragmentListener1.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                    isShareConnected = false;
                                    sendDisconnectShareMsg();
                                }
                                finish();
                            }
                        }
                    });
                }else {
                    finish();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_white_board;
    }

    private void initViewPagerFragment() {
        FmPagerAdapter pagerAdapter = new FmPagerAdapter(getSupportFragmentManager());

        MyShareWhiteFragment myShareWhiteFrag = new MyShareWhiteFragment();
        MyWhiteFragment      myWhiteFrag      = new MyWhiteFragment();
        MyTeamWhiteFragment  myTeamWhiteFrag  = new MyTeamWhiteFragment();


        if(!UserUtil.ISCHAIRMAN && !Hawk.contains(constant.team_share)){
            fragments.add(myWhiteFrag);
            mFragmentListener2 = myWhiteFrag;

            String[] mTitleArr = {"白板"};
            List<String> titles = Arrays.asList(mTitleArr);
            tabLayout.refreshTitles(titles);
            tabLayout.getTabAt(0).select(); //默认选中第二个tab
            viewPager.setCurrentItem(0);

            pagerAdapter.setFragmentList(fragments);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setNoScroll(true);
        }else {
            fragments.add(myShareWhiteFrag);
            fragments.add(myWhiteFrag);
            fragments.add(myTeamWhiteFrag);

            mFragmentListener1 = myShareWhiteFrag;
            mFragmentListener2 = myWhiteFrag;
            mFragmentListener3 = myTeamWhiteFrag;

            pagerAdapter.setFragmentList(fragments);
            // 需要在viewpage默认选中前设置,代码位置不可以动
            viewPager.setAdapter(pagerAdapter);
            viewPager.setNoScroll(true);

            List<String> titles = Arrays.asList(titleArr);
            tabLayout.setTitle(titles);
            // 接受邀请进来
            if(Hawk.get(constant.team_share) != null){
                if(Hawk.get(constant.team_share).equals("share")){
                    tabLayout.getTabAt(0).select(); //选中第一个tab
                    viewPager.setCurrentItem(0);
                }else if(Hawk.get(constant.team_share).equals("team")){
                    tabLayout.getTabAt(2).select(); //选中第三个tab
                    viewPager.setCurrentItem(2);
                }
            }else {
                tabLayout.getTabAt(1).select(); //默认选中第二个tab
                viewPager.setCurrentItem(1);
            }
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                fragments.get(i).onResume();//关键方法
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    class FmPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        public FmPagerAdapter( FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return fragmentList != null && !fragmentList.isEmpty() ? fragmentList.size() : 0;
        }
        public void setFragmentList(List<Fragment> fragmentList) {
            this.fragmentList = fragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }


    /**
     * 发送断开同屏信息
     */
    private void sendDisconnectShareMsg() {
        if (JWebSocketClientService.client != null && !JWebSocketClientService.client.isOpen()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPSHAREDIS);
                bean.setBody("");
                String strJson = new Gson().toJson(bean);
                send(strJson);

            }
        }.start();
    }
    /**
     * 发送断开协作信息
     */
    private void sendDisconnectTeamMsg() {
        if (JWebSocketClientService.client != null && !JWebSocketClientService.client.isOpen()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPTEAMDIS);
                bean.setBody("");
                String strJson = new Gson().toJson(bean);
                send(strJson);

            }
        }.start();
    }
    /**
     * 发送协作请求信息
     */
    private void sendTeamReqMsg() {
        if (JWebSocketClientService.client != null && !JWebSocketClientService.client.isOpen()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPTEAMREQ);
                bean.setBody("");
                String strJson = new Gson().toJson(bean);
                send(strJson);

            }
        }.start();
        isTeamConnected = true;
        mFragmentListener3.onTypeClick(ToFragmentType.IsConnected,ToFragmentIsConnectedType.isConnected);
        ToastUtil.makeText(this,"协作信息发送成功!");
    }
    /**
     * 发送同屏请求信息,清屏
     */
    private void sendShareReqMsg() {
        if (JWebSocketClientService.client != null && !JWebSocketClientService.client.isOpen()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPSHAREREQ);
                bean.setBody("");
                String strJson = new Gson().toJson(bean);
                send(strJson);

            }
        }.start();
        isShareConnected = true;
        mFragmentListener1.onTypeClick(ToFragmentType.IsConnected,ToFragmentIsConnectedType.isConnected);
        // 清屏
//        mDbView.clearImage();
        ToastUtil.makeText(this,"同屏信息发送成功!");
    }


    private void send(final String str) {
        if (str.length() == 0) {
            return;
        }
        JWebSocketClientService.sendMsg(str);
    }

    @Override
    public void onBackPressed() {
        if(!UserUtil.ISCHAIRMAN){
            if(Hawk.contains(constant.team_share) && Hawk.get(constant.team_share).equals("share")) {
                ToastUtil.makeText(this,"不允许退出!");
                return;
            }else {
                finish();
            }
        }else {
            // 点击返回，如果有同屏，提示断开
            if(isShareConnected || isTeamConnected){
                CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "是否断开同屏|协作?", null, "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                    @Override
                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                        if(clickConfirm){
                            //  协作断开会直接退出，先判断协作
                            if (isTeamConnected){
                                // 清屏
                                mFragmentListener3.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                isTeamConnected = false;
                                sendDisconnectTeamMsg();
                            }else if(isShareConnected){
                                // 清屏
                                mFragmentListener1.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                isShareConnected = false;
                                sendDisconnectShareMsg();
                            }
                            finish();
                        }
                    }
                });
            }else {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (Hawk.contains(constant.team_share)) {
            Hawk.delete(constant.team_share);
        }
    }

    @ReceiveEvents(name = Events.WHITE_BOARD_TEXT_EDIT)
    private void textEdit() {//文字编辑

    }

    @ReceiveEvents(name = Events.WHITE_BOARD_UNDO_REDO)
    private void showUndoRedo() {//是否显示撤销、重装按钮

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initEvent();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.SHOW_UI);
        //  监听websocket消息事件
        EventBus.getDefault().register(this);
    }

    /**
     *  收到websocket 信息
     * */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onReceiveMsg(EventMessage message) {

    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (constant.SHOW_UI.equals(intent.getAction())) {
                showUndoRedo();
            }
        }
    }

    /**
     *  isConnected : 0 未连接   1 ：已连接
     * */
    public interface ToFragmentListener {
        void onTypeClick(ToFragmentType toFragmentType, ToFragmentIsConnectedType isConnected);
    }
}

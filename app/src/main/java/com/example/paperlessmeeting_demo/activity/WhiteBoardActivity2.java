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
 * ????????????
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

    private String[] titleArr = {"??????", "??????", "??????"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private boolean isShareConnected = false;
    private boolean isTeamConnected = false;
    private int currentSelTab = 1;  // ???????????????index=1
    private final String TAG = "WhiteBoardActivity2";

    @Override
    protected void initView() {
        initViewPagerFragment();
    }

    private void initEvent() {
        // ???????????????????????????????????????????????????tab??????
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

                // ?????????????????????????????????fragment????????????
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
                            CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "?????????????????????????", null, "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if(clickConfirm){
                                        sendShareReqMsg();
                                    }else {
                                        tabLayout.getTabAt(1).select(); //?????????????????????tab
                                        viewPager.setCurrentItem(1);
                                    }
                                }
                            });
                            return;
                        }
                        break;
                    case 1:
                        if(isShareConnected || isTeamConnected){
                            CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "??????????????????|???????", null, "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if(clickConfirm){
                                        //  ?????????????????????????????????????????????
                                        if (isTeamConnected){
                                            // ??????
                                            mFragmentListener3.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                            isTeamConnected = false;
                                            sendDisconnectTeamMsg();
                                        }else if(isShareConnected){
                                            // ??????
                                            mFragmentListener1.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                            isShareConnected = false;
                                            sendDisconnectShareMsg();
                                        }
                                        isShareConnected = false;
                                        isTeamConnected  = false;

                                    }else {
                                        if(isShareConnected){
                                            tabLayout.getTabAt(0).select(); //???????????????tab
                                            viewPager.setCurrentItem(0);
                                        }else if(isTeamConnected){
                                            tabLayout.getTabAt(2).select(); //???????????????tab
                                            viewPager.setCurrentItem(2);
                                        }

                                    }
                                }
                            });
                        }

                        break;
                    case 2:
                        if(!isTeamConnected){
                            CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "?????????????????????????", null, "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if(clickConfirm){
                                        sendTeamReqMsg();
                                    }else {
                                        tabLayout.getTabAt(1).select(); //?????????????????????tab
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
                            //?????????????????????????????????????????????
                            CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "???????????????????????????????", null, "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
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
                        //?????????????????????????????????????????????
                        CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "???????????????????????????????", null, "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
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
                        ToastUtil.makeText(WhiteBoardActivity2.this,"???????????????!");
                        return;
                    }
                }

                // ?????????????????????????????????????????????
                if(isShareConnected || isTeamConnected){
                    CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "??????????????????|???????", null, "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if(clickConfirm){
                                //  ?????????????????????????????????????????????
                                if (isTeamConnected){
                                    // ??????
                                    mFragmentListener3.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                    isTeamConnected = false;
                                    sendDisconnectTeamMsg();
                                }else if(isShareConnected){
                                    // ??????
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

            String[] mTitleArr = {"??????"};
            List<String> titles = Arrays.asList(mTitleArr);
            tabLayout.refreshTitles(titles);
            tabLayout.getTabAt(0).select(); //?????????????????????tab
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
            // ?????????viewpage?????????????????????,????????????????????????
            viewPager.setAdapter(pagerAdapter);
            viewPager.setNoScroll(true);

            List<String> titles = Arrays.asList(titleArr);
            tabLayout.setTitle(titles);
            // ??????????????????
            if(Hawk.get(constant.team_share) != null){
                if(Hawk.get(constant.team_share).equals("share")){
                    tabLayout.getTabAt(0).select(); //???????????????tab
                    viewPager.setCurrentItem(0);
                }else if(Hawk.get(constant.team_share).equals("team")){
                    tabLayout.getTabAt(2).select(); //???????????????tab
                    viewPager.setCurrentItem(2);
                }
            }else {
                tabLayout.getTabAt(1).select(); //?????????????????????tab
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
                fragments.get(i).onResume();//????????????
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
     * ????????????????????????
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
     * ????????????????????????
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
     * ????????????????????????
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
        ToastUtil.makeText(this,"????????????????????????!");
    }
    /**
     * ????????????????????????,??????
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
        // ??????
//        mDbView.clearImage();
        ToastUtil.makeText(this,"????????????????????????!");
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
                ToastUtil.makeText(this,"???????????????!");
                return;
            }else {
                finish();
            }
        }else {
            // ?????????????????????????????????????????????
            if(isShareConnected || isTeamConnected){
                CVIPaperDialogUtils.showCustomDialog(WhiteBoardActivity2.this, "??????????????????|???????", null, "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                    @Override
                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                        if(clickConfirm){
                            //  ?????????????????????????????????????????????
                            if (isTeamConnected){
                                // ??????
                                mFragmentListener3.onTypeClick(ToFragmentType.Close,ToFragmentIsConnectedType.nonsense);
                                isTeamConnected = false;
                                sendDisconnectTeamMsg();
                            }else if(isShareConnected){
                                // ??????
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
    private void textEdit() {//????????????

    }

    @ReceiveEvents(name = Events.WHITE_BOARD_UNDO_REDO)
    private void showUndoRedo() {//?????????????????????????????????

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initEvent();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.SHOW_UI);
        //  ??????websocket????????????
        EventBus.getDefault().register(this);
    }

    /**
     *  ??????websocket ??????
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
     *  isConnected : 0 ?????????   1 ????????????
     * */
    public interface ToFragmentListener {
        void onTypeClick(ToFragmentType toFragmentType, ToFragmentIsConnectedType isConnected);
    }
}

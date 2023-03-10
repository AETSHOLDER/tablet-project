package com.example.paperlessmeeting_demo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;
import com.example.paperlessmeeting_demo.fragment.SweepCodeFragment;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.PermissionManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.tool.VideoEncoderUtil;
import com.example.paperlessmeeting_demo.widgets.NoScrollViewPager;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by ?????? on 2020/9/18.
 */

public class LoginA extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.entry_meet)
    TextView entryMeet;
    @BindView(R.id.tt)
    TextView tt;
    @BindView(R.id.visitor)
    ImageView visitor;
    //    private String[] titles = {"????????????", "????????????", "?????????"};
    private String[] titles = {"????????????"};
    private List<Fragment> fragments = new ArrayList<>();
    AlertDialog.Builder inputDialog;
    public String targetip = "192.168.0.106";
    private AlertDialog serviceDialog;
    //    private boolean isFirstInit = true;
    private static final int ACTIVITY_RESULT_CODE = 110;
    private MediaProjectionManager projectionManager;
    private VideoEncoderUtil videoEncoder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initViewPagerFragment();
        name.setText("@" + UserUtil.user_name);
        Log.d("22", "mac===" + FLUtil.getMacAddress());
//        if(!Hawk.contains(constant.isFirstInit)) {
//            Intent intent = new Intent(LoginActivity.this, InitiaActivity.class);
//            startActivity(intent);
//        }else {
//            if(Hawk.get(constant.isFirstInit)){
//                Intent intent = new Intent(LoginActivity.this, InitiaActivity.class);
//                startActivity(intent);
//            }
//        }
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetStickyEvent(EventMessage message) {
        if (message.getMessage().equals(constant.start_meeting)) {
            String aa = String.format("???????????????????????????id=%s,????????????=%s", UserUtil.meeting_record_id, UserUtil.user_name);

            name.setText("@" + UserUtil.user_name);
            Toast.makeText(this, aa, Toast.LENGTH_SHORT).show();
        }
        if (message.getMessage().equals(constant.continusClick)) {
            String tips = "????????????????????????8????????????????????????????????????";
            Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
            visitor.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        entryMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginA.this, MainActivity.class);
                startActivity(intent);
            }
        });
        /**
         *  ?????????????????????????????????
         */
        visitor.setVisibility(View.GONE);
        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginA.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent captureIntent = projectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE);
            }
        });
    }

    private void initViewPagerFragment() {
        if (titles.length < 4) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }//tab?????????????????????????????????MODE_SCROLLABLE  ????????????MODE_FIXED
        FragmentManager fm = getSupportFragmentManager();
        FragmentAdapter pagerAdapter = new FragmentAdapter(fm);
        pagerAdapter.addFragment(SweepCodeFragment.newInstance("????????????"), "????????????");
//        pagerAdapter.addFragment(WhiteboardFragment.newInstance("????????????"), "????????????");
//        pagerAdapter.addFragment(InvitedFragment.newInstance("?????????"), "?????????");
        //  pagerAdapter.addFragment(InvitedFragment.newInstance("????????????"), "????????????");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getBackground().setAlpha(175);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);

        /**
         * ??????tablayout?????????????????????????????????
         */
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
              /*  if (tab.getText().equals("??????")) {
                    Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
                }
                if (tab.getPosition() == 1) {
                    Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
                }*/
             /*   if ("????????????".equals(tab.getText().toString())) {
                    entryMeet.setVisibility(View.GONE);

                } else if ("????????????".equals(tab.getText().toString())) {
                    entryMeet.setVisibility(View.VISIBLE);
                } else if ("?????????".equals(tab.getText().toString())) {
                    entryMeet.setVisibility(View.VISIBLE);
                }*/
                ;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //??????????????????
            }
        });
    }


    private void getMeeingInfo() {
        //??????????????????
        NetWorkManager.getInstance().getNetWorkApiService().getMeetingInfo("5fa90f4faac1a70864c41ff2").compose(this.<BasicResponse<MeetingInfoBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<MeetingInfoBean>>() {
                    @Override
                    protected void onSuccess(BasicResponse<MeetingInfoBean> response) {
                        if (response != null) {
                            MeetingInfoBean meetingInfoBean = response.getData();
                           /* Hawk.put("c_id", meetingInfoBean.getC_id());
                            Hawk.put("user_id", meetingInfoBean.getUser_list().get(0).getUser_id());*/

                        }
                    }
                });
    }


    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginA.this);
        View view = LayoutInflater.from(LoginA.this).inflate(R.layout.dialo_inputadreass1, null);
        EditText edit_text =
                (EditText) view.findViewById(R.id.editText_ip);

        Button confirm =
                (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (edit_text.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "???????????????", Toast.LENGTH_LONG).show();
                    return;
                }*/
                Hawk.put("ip", edit_text.getText().toString().trim());
                serviceDialog.dismiss();
            }
        });

        builder.setView(view);
        builder.setCancelable(false);
        serviceDialog = builder.create();
        serviceDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        checkWritePermission1();
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        PermissionManager.RequestOverlayPermission(this);
        //  buildDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * Activity????????????
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionManager.onActivityResult(requestCode, resultCode, data, this);
       if (requestCode == ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            MediaProjection mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            videoEncoder = new VideoEncoderUtil(mediaProjection, "");
            videoEncoder.start();

        }
    }

    /**
     * ????????????????????????
     */
    @AfterPermissionGranted(constant.WRITE_PERMISSION_CODE1)
    private void checkWritePermission1() {
        if (!PermissionManager.checkPermission(this, constant.PERMS_WRITE1)) {
            PermissionManager.requestPermission(this, constant.WRITE_PERMISSION_TIP, constant.WRITE_PERMISSION_CODE1, constant.PERMS_WRITE);
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

        Toast.makeText(LoginA.this, "????????????", Toast.LENGTH_LONG).show();


    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(LoginA.this, "??????????????????", Toast.LENGTH_LONG).show();
        /**
         * ??????????????????????????????????????????'NEVER ASK AGAIN.'??????'????????????'?????????????????????
         * ?????????????????????????????????????????????????????????????????????
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mFragmentTitle = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);//?????????List???
            mFragmentTitle.add(title);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        //???????????????????????????tab???name???
        @Override
        public CharSequence getPageTitle(int position) {
            //???????????????list?????????????????????
            return mFragmentTitle.get(position);
        }
    }
}

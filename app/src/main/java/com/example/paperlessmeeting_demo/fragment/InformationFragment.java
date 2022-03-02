package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.ActivitySearchFile;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.Linear_Image_Radius;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.orhanobut.hawk.Hawk;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 梅涛 on 2020/10/20.
 */
@SuppressLint("ValidFragment")
public class InformationFragment extends BaseFragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.search_icon)
    ImageView searchIcon;
    @BindView(R.id.search_edittext)
    EditText searchEdittext;
    @BindView(R.id.search)
    CardView search;
    @BindView(R.id.search_btn)
    TextView searchBtn;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.search_rl)
    RelativeLayout searchRl;
    @BindView(R.id.first_tabLayout)
    TabLayout firstTabLayout;
    @BindView(R.id.file_fragment_ly)
    LinearLayout fileFragmentLy;
    private String titles;
    private Context context;
    private List<Fragment> fragmentsList;//fragment容器
    private List<String> titleList;//标签容器

    public InformationFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public InformationFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_information;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initData() {
        Drawable drawable = getResources().getDrawable(R.drawable.file_bg);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        BitmapDrawable bbb = new BitmapDrawable(Linear_Image_Radius.toRoundCorner(bitmap, 30));
        bbb.setAlpha(200); //设置透明度 有效
        fileFragmentLy.setBackgroundDrawable(bbb);
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
        searchRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySearchFile.class);
                startActivity(intent);
            }
        });
        fragmentsList = new ArrayList<>();
        titleList = new ArrayList<>();
        if (UserUtil.isTempMeeting) {
            titleList.add("本地文件");
            FileFragment fileFragment = new FileFragment(titleList.get(0), getActivity());
            fragmentsList.add(fileFragment);
        } else {
            titleList.add("公开文件");
            titleList.add("私人文件");
            titleList.add("本地文件");
            NetFileFragment netFileFragment = new NetFileFragment(titleList.get(0), getActivity());
            PrivateFileFragment privateFileFragment = new PrivateFileFragment(titleList.get(1), getActivity());
            FileFragment fileFragment = new FileFragment(titleList.get(2), getActivity());
            fragmentsList.add(netFileFragment);
            fragmentsList.add(privateFileFragment);
            fragmentsList.add(fileFragment);
        }


        if (titleList.size() < 4) {
            firstTabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            firstTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }//tab的模式如果标签多的话用MODE_SCROLLABLE  少的话用MODE_FIXED
        //tabLayout.setBackgroundColor(Color.BLUE);
        FragViewAdapter adapter = new FragViewAdapter(getChildFragmentManager(), fragmentsList, titleList);
        viewPager.setAdapter(adapter);
        firstTabLayout.setupWithViewPager(viewPager);
//        firstTabLayout.setTabsFromPagerAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragmentsList.size());
    }

    @Override
    protected void initView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 创建适配器，主要是为了fragmet与标题进行匹配的 继承FragmentPagerAdapter
     */
    class FragViewAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList_;
        List<String> titleList_;

        public FragViewAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
            super(fm);
            fragmentList_ = fragmentList;
            titleList_ = titleList;
        }

        @Override//fragment匹配
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem  " + fragmentList_.get(position));
            return fragmentList_.get(position);
        }

        @Override//获取fragment的数量
        public int getCount() {
            return titleList_.size();
        }

        @Override//对标题进行匹配
        public CharSequence getPageTitle(int position) {
            Log.i(TAG, "getPageTitle  " + titleList_.get(position));
            return titleList_.get(position);
        }

        @Override//销毁 不知道这样做行不行
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            fragmentList_.get(position).onDestroy();
            fragmentList_.get(position).onStop();
        }
    }
}

package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.MenuBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import q.rorbin.badgeview.Badge;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * Created by 梅涛 on 2020/9/21.
 */
@SuppressLint("ValidFragment")
public class LeftSidebaFragment extends BaseFragment {
    @BindView(R.id.tablayout)
    VerticalTabLayout tablayout;
    /*    @BindView(R.id.rigth_fragment)
        FrameLayout rigthFragment;*/
    Unbinder unbinder;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragViewAdapter myTabAdapter;
    private List<MenuBean> menuBeanList = new ArrayList<>();

    public LeftSidebaFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_left_sidebar;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
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
        fragmentList.add(new RigthContentFragment("首页", getActivity()));
        if (UserUtil.ISCHAIRMAN) {
            fragmentList.add(new NewFragmentCentralControl("中控", getActivity()));
            menuBeanList.add(new MenuBean(0, 0, "中控"));
        }
        fragmentList.add(new SetingFragment("设置", getActivity()));
      /*  menuBeanList.add(new MenuBean(R.mipmap.ic_home_sele, R.mipmap.ic_home_unsele, "首页"));
        menuBeanList.add(new MenuBean(R.mipmap.ic_seting_sele, R.mipmap.ic_seting_unsele, "设置"));*/
        menuBeanList.add(new MenuBean(0, 0, "首页"));
        menuBeanList.add(new MenuBean(0, 0, "设置"));
        initViewPagerFragment();
    }

    @Override
    protected void initView() {
    }

    private void initViewPagerFragment() {
        myTabAdapter = new FragViewAdapter(getChildFragmentManager(), fragmentList, menuBeanList);
        //  tablayout.setTabAdapter(new MyTabAdapter());C
        viewPager.setAdapter(myTabAdapter);

        tablayout.setupWithViewPager(viewPager);
        // tablayout.setTabsFromPagerAdapter(myTabAdapter);
        //  tablayout.setTabAdapte(myTabAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        //  tablayout.setupWithFragment(getChildFragmentManager(), R.id.rigth_fragment, fragmentList, myTabAdapter);
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
      /*  if (unbinder!=null){
            unbinder.unbind();
        }*/
    }

    /* 创建适配器，主要是为了fragmet与标题进行匹配的 继承FragmentPagerAdapter
       */
    class FragViewAdapter extends FragmentPagerAdapter implements TabAdapter {
        @Override
        public TabView.TabBadge getBadge(int position) {
            return new TabView.TabBadge.Builder().setBadgeNumber(0).setBackgroundColor(0xff2faae5)
                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                        @Override
                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                            // targetView.setBackgroundColor(Color.parseColor("#fc6500"));
                        }
                    }).build();
        }

        @Override
        public TabView.TabIcon getIcon(int position) {
            MenuBean menu = menuBeanList.get(position);
            return new TabView.TabIcon.Builder()
                    .setIcon(menu.mSelectIcon, menu.mNormalIcon)
                    .setIconGravity(Gravity.START)
                    .setIconMargin(dp2px(5))
                    .setIconSize(dp2px(15), dp2px(15))
                    .build();
        }

        @Override
        public TabView.TabTitle getTitle(int position) {
            MenuBean menu = menuBeanList.get(position);
            return new TabView.TabTitle.Builder()
                    .setContent(menu.mTitle)
                    .setTextColor(0xFFFF7400, 0xFF0071FF)
                    .setTextSize(22)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return -1;
        }

        List<Fragment> fragmentList_;
        List<MenuBean> titleList_;

        public FragViewAdapter(FragmentManager fm, List<Fragment> fragmentList, List<MenuBean> titleList) {
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
            return titleList_.get(position).mTitle;
        }

        @Override//销毁 不知道这样做行不行
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            fragmentList_.get(position).onDestroy();
            fragmentList_.get(position).onStop();
        }
    }

/*    class MyTabAdapter extends PagerAdapter implements TabAdapter {


        List<MenuBean> menus;

        public MyTabAdapter() {
            menus = new ArrayList<>();
            Collections.addAll(menus, new MenuBean(R.mipmap.ic_home_sele, R.mipmap.ic_home_unsele, "首页")
                    , new MenuBean(R.mipmap.ic_seting_sele, R.mipmap.ic_seting_unsele, "设置"));
            Log.d("sdvst", "添加路过");

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return menus.size();
        }

        @Override
        public TabView.TabBadge getBadge(int position) {
            return new TabView.TabBadge.Builder().setBadgeNumber(0).setBackgroundColor(0xff2faae5)
                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                        @Override
                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                            // targetView.setBackgroundColor(Color.parseColor("#fc6500"));
                        }
                    }).build();
        }

        @Override
        public TabView.TabIcon getIcon(int position) {
            MenuBean menu = menus.get(position);
            return new TabView.TabIcon.Builder()
                    .setIcon(menu.mSelectIcon, menu.mNormalIcon)
                    .setIconGravity(Gravity.START)
                    .setIconMargin(dp2px(5))
                    .setIconSize(dp2px(20), dp2px(20))
                    .build();
        }

        @Override
        public TabView.TabTitle getTitle(int position) {
            MenuBean menu = menus.get(position);
            return new TabView.TabTitle.Builder()
                    .setContent(menu.mTitle)
                    .setTextColor(0xFF36BC9B, 0xFF757575)
                    .setTextSize(8)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return -1;
        }

    }*/

    protected int dp2px(float dp) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}

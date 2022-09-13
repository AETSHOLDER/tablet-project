package com.example.paperlessmeeting_demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.paperlessmeeting_demo.fragment.WuHuFragment2;
import com.example.paperlessmeeting_demo.fragment.WuHuFragment3;

/**
 * Description：
 * Author：LiuYM 原来：FragmentPagerAdapter
 * Date： 2017-05-10 10:44 FragmentStatePagerAdapter
 *
 */
/*
 *去掉所有广播
 */
public class PagerAdapter3 extends FragmentPagerAdapter {
    private static final String TAG = "PagerAdapter";

    SparseArray<WuHuFragment3> mTestFragments;

    public SparseArray<WuHuFragment3> getmTestFragments() {
        return mTestFragments;
    }

    public void setmTestFragments(SparseArray<WuHuFragment3> mTestFragments) {
        this.mTestFragments = mTestFragments;
    }

    public PagerAdapter3(FragmentManager fm, SparseArray<WuHuFragment3> testFragments) {
        super(fm);
        this.mTestFragments = testFragments;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: "+position);

        WuHuFragment3 wuHuFragment =mTestFragments.valueAt(position);

        return wuHuFragment;
    }

    @Override
    public int getCount() {
        return mTestFragments.size();
    }

    /**
     * 返回每一个item所有对应的key，在instantiateItem中 会根据这个item去查找是否已经存在这个item
     * 如果不存在则调用getItem创建
     * 存在则在FragmentManager中去find
     * 详情查看instantiateItem源码
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        Log.d(TAG, "position: "+position+"  getItemId:"+mTestFragments.keyAt(position));
        return mTestFragments.keyAt(position);
    }

 /*   @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem: "+position);
        return super.instantiateItem(container, position);
    }*/

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        WuHuFragment3 fragment = (WuHuFragment3) super.instantiateItem(container, position);

        fragment.updateArguments(position+"");

        return fragment;

    }





    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
            return POSITION_NONE;


    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }
}

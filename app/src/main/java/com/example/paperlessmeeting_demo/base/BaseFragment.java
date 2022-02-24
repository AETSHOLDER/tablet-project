package com.example.paperlessmeeting_demo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by WP on 2020/7/20.
 */

public abstract class BaseFragment extends RxFragment {

    protected static String TAG = null;

    protected Context mContext = null;

    protected View rootView = null;

    protected Unbinder unbinder = null;

//    private boolean isLoad = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getSimpleName();

        mContext = getActivity();//获取Activity的Context


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            if (getLayoutId() != 0) {
                rootView = inflater.inflate(getLayoutId(), null);
            }
        }

     /*   ViewGroup parent = (ViewGroup) rootView.getParent();

        if (parent != null){
            parent.removeView(rootView);
        }*/

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

//        if(isLoad) {
        initView();

        initData();

//            isLoad = false;
//        }
    }

    /**
     * 跳转一个Activity
     *
     * @param clazz
     */
    protected void go(Class<?> clazz) {
        Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
        startActivity(intent);
    }

    /**
     * 跳转一个Activity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void go(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转一个Activity then kill
     *
     * @param clazz
     */
    protected void goThenKill(Class<?> clazz) {
        Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * 跳转一个Activity with bundle then kill
     *
     * @param clazz
     * @param bundle
     */
    protected void goThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * 跳转一个Activity并等待返回结果
     *
     * @param clazz
     * @param requestCode
     */
    protected void goForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转一个Activity并等待返回结果 with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void goForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onDestroyView() {
      /*  if (unbinder != null) {
            unbinder.unbind();
        }*/
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      /*  RefWatcher refWatcher = MeetingAPP.getRefWatcher(getActivity());
        refWatcher.watch(this);*/
    }

    protected abstract int getLayoutId();

    protected abstract boolean isBindEventBus();

    protected abstract void initData();

    protected abstract void initView();

}

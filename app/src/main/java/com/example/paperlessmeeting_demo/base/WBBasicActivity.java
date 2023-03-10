package com.example.paperlessmeeting_demo.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.widgets.NavigationBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class WBBasicActivity extends RxAppCompatActivity {

    protected Context mContext;

    private NavigationBar navigationBar;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        FLUtil.requestfullScreen(this);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());//设置布局
        }
        init();
        initView();
        initData();
    }

    protected void showMessage(CharSequence msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected abstract int getLayoutId();

    private void init() {
        mContext = this;
        navigationBar = (NavigationBar) findViewById(R.id.navigation_bar);
        if (navigationBar != null) {
            navigationBar.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBack();
                }
            });
        }
        unbinder = ButterKnife.bind(this);
    }

    protected abstract void initView();

    protected abstract void initData();

    public void openActivity(Class aClass) {

        openActivity(aClass, null);
    }

    public void openActivity(Class aClass, Bundle bundle) {

        Intent intent = new Intent(this, aClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);

    }

    protected void setTitle(String title) {

        if (navigationBar != null) navigationBar.setTitle(title);
    }

    protected <T> View addRight(T item, NavigationBar.clickCallBack callBack) {
        if (navigationBar != null) return navigationBar.addRight(item, callBack);
        return null;
    }

    protected <T> View addLeft(T item, NavigationBar.clickCallBack callBack) {
        if (navigationBar != null) return navigationBar.addLeft(item, callBack);
        return null;
    }

    protected void hasBack(boolean has) {

        if (navigationBar != null) navigationBar.setHasBack(has);
    }

    protected void goBack() {
        finish();
    }

}
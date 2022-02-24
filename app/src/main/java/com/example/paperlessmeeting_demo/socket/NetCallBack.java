package com.example.paperlessmeeting_demo.socket;

/**
 * Created by fengli on 2018/2/6.
 */

public abstract class NetCallBack {

    public abstract void onSuccess(String data);

    public abstract void onError();

    public abstract void closeProgressHud();
}

package com.example.paperlessmeeting_demo.network;

import android.net.ParseException;
import android.text.TextUtils;
import android.util.Log;

import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;


import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.example.paperlessmeeting_demo.network.DefaultObserver.ExceptionReason.PARSE_ERROR;
import static com.example.paperlessmeeting_demo.network.DefaultObserver.ExceptionReason.UNKNOWN_ERROR;

/**
 * Created by window on 2018/3/13.
 */

public abstract class DefaultObserver<T extends BasicResponse> implements Observer<T> {

    public DefaultObserver() {
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T response) {
        if (response.getCode() == 1){
            onSuccess(response);
        }else{
            onFail(response);
        }
    }

    //请求成功后的处理
    protected abstract void onSuccess(T response);

    //服务器返回失败的处理
    protected void onFail(T response){
        String message = response.getMsg();
        if (TextUtils.isEmpty(message)) {
            ToastUtils.showShort(R.string.response_return_error);
        } else {
            ToastUtils.showShort(message);
        }
    }

    //网络连接失败、网络连接超时、数据解析异常调用
    @Override
    public void onError(@NonNull Throwable e) {

        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(PARSE_ERROR);
        } else {
            onException(UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }

    /**
     * 请求异常
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                ToastUtils.showShort(R.string.connect_error);
                break;

            case CONNECT_TIMEOUT:
                ToastUtils.showShort(R.string.connect_timeout);
                break;

            case BAD_NETWORK:
                //ToastUtils.showShort(R.string.bad_network);
                break;

            case PARSE_ERROR:
                Log.e("DefaultObserver","解析问题");
//                ToastUtils.showShort(R.string.parse_error);
                break;

            case UNKNOWN_ERROR:
            default:
                Log.e("DefaultObserver","未知问题");
//                Log.e("DefaultObserver",""+R.string.parse_error);
//                ToastUtils.showShort(R.string.unknown_error);
                break;
        }
    }
}

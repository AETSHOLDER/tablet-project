package com.example.paperlessmeeting_demo.network;

import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.tool.NetWorkUtils;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by window on 2018/3/7.
 * 中控摄像头HTTP协议管理类
 */

public class NetWorkUuIdManager {


    private NetWorkUuIdManager() {
    }

    private static NetWorkUuIdManager netWorkManager = new NetWorkUuIdManager();

    public static NetWorkUuIdManager getInstance() {
        return netWorkManager;
    }


    //Log信息拦截器，打印网络请求的body部分
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    //设置网络缓存大小
    private static File httpCacheDirectory = new File(MeetingAPP.getInstance().getCacheDir(), "cache");
    private static int cacheSize = 10 * 1024 * 1024;//10M
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);


    //读写控制拦截器
    private static final Interceptor httpCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtils.isNetWorkAvailable(MeetingAPP.getInstance())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)//强制使用缓存
                        .build();
            }
            // 临时会议,取消所有请求
            if (UserUtil.isTempMeeting) {
                chain.call().cancel();
            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetWorkAvailable(MeetingAPP.getInstance())) {
                int maxAge = 3; //maxAge 设置最大失效时间为60秒，失效则不使用
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader("Cache-Control")//移除网络请求Cache-Control请求头，因为请求头不一定支持缓存。okhttp缓存是基于请求头的
                        .header("Cache-Control", "public, max-age=" + maxAge)//添加返回网络返回数据的请求头，支持缓存
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周 maxStale 设置最大失效时间，失效则不使用
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    //配置客户端信息
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)//设置应用拦截器，可用于设置公共参数，头信息，日志拦截等
            .addNetworkInterceptor(httpCacheInterceptor)
            .addInterceptor(new AddTokenInterceptor())
            .cache(cache)
            .retryOnConnectionFailure(true)//错误重连
            .build();
    private NetWorkApi netWorkApi;
    private Object monitor = new Object();

    public NetWorkApi getNetWorkApiService() {
        if (netWorkApi == null) {
            synchronized (monitor) {
                if (netWorkApi == null) {
                    netWorkApi = new Retrofit.Builder()
                            .baseUrl(UrlConstant.baseUuIdUrl)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(client)
                            //好像不好同时支持解析json或者String
                            .addConverterFactory(GsonConverterFactory.create()) //支持json解析
                            .addConverterFactory(ScalarsConverterFactory.create()) //支持String解析
                            .build().create(NetWorkApi.class);
                }
            }
        }
        return netWorkApi;
    }
}

package com.example.paperlessmeeting_demo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.NetWorkUtils;
import com.example.paperlessmeeting_demo.tool.SdCardStatus;
import com.example.paperlessmeeting_demo.tool.SerialPortUtils.HexadecimalConversion;
import com.example.paperlessmeeting_demo.tool.SerialPortUtils.SerialPortClient;
import com.example.paperlessmeeting_demo.tool.StoreUtil;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.github.guanpy.wblib.utils.AppContextUtil;
import com.github.guanpy.wblib.utils.OperationUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.hawk.Hawk;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.dreamtobe.threaddebugger.IThreadDebugger;
import cn.dreamtobe.threaddebugger.ThreadDebugger;
import cn.dreamtobe.threaddebugger.ThreadDebuggers;
import io.realm.Realm;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 梅涛 on 2020/9/8.
 */

public class MeetingAPP extends Application {

    private RefWatcher mRefWatcher;
    private static Context context;
    private static MeetingAPP meetingAPP = null;

    private SerialPortClient serialPortClient;
    private HexadecimalConversion hexadecimal;
    private String ipStr;
    //static 代码段可以防止内存泄露
    static {
    /* //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.locationtextcolor, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });*/
    }
    public static Context getContext() {
        return context;
    }

    public HexadecimalConversion getHexadecimal() {
        return hexadecimal;
    }

    public SerialPortClient getSerialPortClient() {
        return serialPortClient;
    }

    Handler mHander = new Handler() {

        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    String str = (String) msg.obj;
                    Log.d("dsff", str);
                    ipStr = str.substring(4, str.length() - 4);
                    break;

            }


        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        meetingAPP = this;
        context = getApplicationContext();

        //  串口相关
        hexadecimal = new HexadecimalConversion();
//        serialPortClient = new SerialPortClient();
//        serialPortClient.openPort();
        // 初始化socket连接 ，寻址广播
        FLUtil.receiveBroadcast();

        Utils.init(this);
        initOkHttpUtil();
        Realm.init(this);
        AppContextUtil.init(this);
        SdCardStatus.init(StoreUtil.CACHE_DIR);
        OperationUtils.getInstance().init();
        Hawk.init(this).build();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                UserUtil.isNetworkOnline =  NetWorkUtils.isNetworkOnline();
            }
        });
        //  TBS 初始化
        initX5();
        //内存泄漏-start
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//
//        // 异常处理，不需要处理时注释掉这两句即可！
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        // 注册crashHandler
//        crashHandler.init(getApplicationContext());
//         mRefWatcher = LeakCanary.install(this);
        //内存泄漏-end
        //下载文件
        FileDownloadLog.NEED_LOG = BuildConfig.DOWNLOAD_NEED_LOG;

        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();

        // below codes just for monitoring thread pools in the FileDownloader:
        IThreadDebugger debugger = ThreadDebugger.install(
                ThreadDebuggers.create() /** The ThreadDebugger with known thread Categories **/
                        // add Thread Category
                        .add("OkHttp").add("okio").add("Binder")
                        .add(FileDownloadUtils.getThreadPoolName("Network"), "Network")
                        .add(FileDownloadUtils.getThreadPoolName("Flow"), "FlowSingle")
                        .add(FileDownloadUtils.getThreadPoolName("EventPool"), "Event")
                        .add(FileDownloadUtils.getThreadPoolName("LauncherTask"), "LauncherTask")
                        .add(FileDownloadUtils.getThreadPoolName("ConnectionBlock"), "Connection")
                        .add(FileDownloadUtils.getThreadPoolName("RemitHandoverToDB"), "RemitHandoverToDB")
                        .add(FileDownloadUtils.getThreadPoolName("BlockCompleted"), "BlockCompleted"),

                2000, /** The frequent of Updating Thread Activity information **/

                new ThreadDebugger.ThreadChangedCallback() {
                    /**
                     * The threads changed callback
                     **/
                    @Override
                    public void onChanged(IThreadDebugger debugger) {
                        // callback this method when the threads in this application has changed.

                    }
                });


    }

    private void initX5() {
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("MeetingAPP", " x5内核加载" + (arg0 ? "成功!":"失败!"));
            }

            @Override
            public void onCoreInitFinished() {
                Log.d("MeetingAPP22", " onViewInitFinished is " + "路过~~~~~");
            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("MeetingAPP33", " onViewInitFinished is " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("MeetingAPP444", " onViewInitFinished is " + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("MeetingAPP555", " onViewInitFinished is " + i);
            }
        });

        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

   /* public static RefWatcher getRefWatcher(Context context) {
        MeetingAPP application = (MeetingAPP) context.getApplicationContext();
        return application.mRefWatcher;
    }*/

    public static MeetingAPP getInstance() {
        return meetingAPP;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /*
    * 初始化网络请求管理器
    * */
    private void initOkHttpUtil() {

        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        request = request.newBuilder().addHeader("Connection", "keep-alive").build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();
        OkHttpUtils.initClient(client);
    }
}


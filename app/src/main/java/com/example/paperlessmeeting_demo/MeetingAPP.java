package com.example.paperlessmeeting_demo;

import static com.example.paperlessmeeting_demo.tool.FLUtil.getNetworkType;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.tool.CrashHandler;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.NetWorkUtils;
import com.example.paperlessmeeting_demo.tool.PCScreen.Netty.NettyClient;
import com.example.paperlessmeeting_demo.tool.SdCardStatus;
import com.example.paperlessmeeting_demo.tool.SerialPortUtils.HexadecimalConversion;
import com.example.paperlessmeeting_demo.tool.SerialPortUtils.SerialPortClient;
import com.example.paperlessmeeting_demo.tool.StoreUtil;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.WuHuCrashHandler;
import com.example.paperlessmeeting_demo.tool.constant;
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
//import com.umeng.commonsdk.UMConfigure;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
    public static Handler mHandler;
    private SerialPortClient serialPortClient;
    private HexadecimalConversion hexadecimal;
    private NettyClient nettyClient;//socket操作连接对象
    private String selfIp = "";
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

    public void reinitSocket(String ip){
        nettyClient = new NettyClient(ip, constant.TCP_PORT);
    }


    public NettyClient getNettyClient() {
        return nettyClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        meetingAPP = this;
        context = getApplicationContext();

        //让代码执行在主线程
        mHandler = new Handler(getMainLooper());
        //  串口相关
        hexadecimal = new HexadecimalConversion();
//        serialPortClient = new SerialPortClient();
//        serialPortClient.openPort();
        // 初始化socket连接 ，寻址广播
        FLUtil.receiveBroadcast();
        //友盟
     /*   UMConfigure.setLogEnabled(true);
      //  UMConfigure.preInit();
        UMConfigure.init(this, "63202ec788ccdf4b7e2b8f7f", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");*/

        Utils.init(this);
        initOkHttpUtil();
        Realm.init(this);
        AppContextUtil.init(this);
        SdCardStatus.init(StoreUtil.CACHE_DIR);
        OperationUtils.getInstance().init();
        Hawk.init(this).build();
        Hawk.put(constant.isLaunch,true);
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
        // 异常处理，不需要处理时注释掉这两句即可！
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());

        //芜湖版本
        WuHuCrashHandler.getInstance().init(this);
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
        //发送 ip
        sendIp();

    }

    public void initX5() {
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
                Hawk.put("TBS",arg0);
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
    private void sendIp() {
        /*
         * 临时会议时，全局发送本设备Ip地址到其他设备
         * */
        selfIp = getNetworkType();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);//休眠3
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        String str = constant.SHARE_FILE_IP +FLUtil. getIpAddressString();
                        Hawk.put("SelfIpAddress", selfIp);//自身Ip
                        byte[] sendStr = str.getBytes();
                        InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                        DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                        socket.send(packet);
                        socket.close();
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}


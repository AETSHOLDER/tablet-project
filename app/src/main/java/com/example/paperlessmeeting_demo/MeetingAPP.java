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
 * Created by ?????? on 2020/9/8.
 */

public class MeetingAPP extends Application {

    private RefWatcher mRefWatcher;
    private static Context context;
    private static MeetingAPP meetingAPP = null;
    public static Handler mHandler;
    private SerialPortClient serialPortClient;
    private HexadecimalConversion hexadecimal;
    private NettyClient nettyClient;//socket??????????????????
    private String selfIp = "";
    //static ?????????????????????????????????
    static {
    /* //???????????????Header?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.locationtextcolor, android.R.color.white);//????????????????????????
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("????????? %s"));//???????????????Header???????????? ???????????????Header
            }
        });
        //???????????????Footer?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //???????????????Footer???????????? BallPulseFooter
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

        //???????????????????????????
        mHandler = new Handler(getMainLooper());
        //  ????????????
        hexadecimal = new HexadecimalConversion();
//        serialPortClient = new SerialPortClient();
//        serialPortClient.openPort();
        // ?????????socket?????? ???????????????
        FLUtil.receiveBroadcast();
        //??????
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
        //  TBS ?????????
        initX5();
        //????????????-start
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//
        // ????????????????????????????????????????????????????????????
        CrashHandler crashHandler = CrashHandler.getInstance();
        // ??????crashHandler
        crashHandler.init(getApplicationContext());

        //????????????
        WuHuCrashHandler.getInstance().init(this);
//         mRefWatcher = LeakCanary.install(this);
        //????????????-end
        //????????????
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
        //?????? ip
        sendIp();

    }

    public void initX5() {
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //???wifi????????????????????????x5??????
        QbSdk.setDownloadWithoutWifi(true);
        //????????????tbs?????????????????????????????????????????????????????????????????????????????????
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5????????????????????????????????????true??????x5?????????????????????????????????x5??????????????????????????????????????????????????????
                Log.d("MeetingAPP", " x5????????????" + (arg0 ? "??????!":"??????!"));
                Hawk.put("TBS",arg0);
            }

            @Override
            public void onCoreInitFinished() {
                Log.d("MeetingAPP22", " onViewInitFinished is " + "??????~~~~~");
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

        //x5?????????????????????
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
    * ??????????????????????????????
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
         * ???????????????????????????????????????Ip?????????????????????
         * */
        selfIp = getNetworkType();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);//??????3
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        String str = constant.SHARE_FILE_IP +FLUtil. getIpAddressString();
                        Hawk.put("SelfIpAddress", selfIp);//??????Ip
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


package com.example.paperlessmeeting_demo.tool;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.InitiaBean.InitiaMeeting;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.socket.InitSocketManager;
import com.example.paperlessmeeting_demo.tool.SerialPortUtils.SerialPortListener;
import com.example.paperlessmeeting_demo.util.SerialPortUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import tp.xmaihh.serialport.utils.ByteUtil;

import static android.content.Context.WIFI_SERVICE;
import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by fengli on 2018/2/5.
 */

public class FLUtil {
    public static String BroadCastIP = "192.168.00.000";  //  广播得到的Ip,只用于判断
    public static Handler handler = new Handler();
    /*
    * 网络是否可用
    *
    * */
    public static boolean netIsConnect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * 自定义裁剪，根据第一个像素点(左上角)X和Y轴坐标和需要的宽高来裁剪
     * @param srcBitmap
     * @param firstPixelX
     * @param firstPixelY
     * @param needWidth
     * @param needHeight
     * @param recycleSrc
     * @return
     */
    public static Bitmap cropBitmapCustom(Bitmap srcBitmap, int firstPixelX, int firstPixelY, int needWidth, int needHeight, boolean recycleSrc) {

        Log.d("danxx", "cropBitmapRight before w : "+srcBitmap.getWidth());
        Log.d("danxx", "cropBitmapRight before h : "+srcBitmap.getHeight());

        if(firstPixelX + needWidth > srcBitmap.getWidth()){
            needWidth = srcBitmap.getWidth() - firstPixelX;
        }

        if(firstPixelY + needHeight > srcBitmap.getHeight()){
            needHeight = srcBitmap.getHeight() - firstPixelY;
        }

        /**裁剪关键步骤*/
        Bitmap cropBitmap = Bitmap.createBitmap(srcBitmap, firstPixelX, firstPixelY, needWidth, needHeight);

        Log.d("danxx", "cropBitmapRight after w : "+cropBitmap.getWidth());
        Log.d("danxx", "cropBitmapRight after h : "+cropBitmap.getHeight());

        return cropBitmap;
    }


    /*
    *  显示
    * */
    public static void showShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } else {
            return 0;
        }
    }

    public static String createUUID() {

        return UUID.randomUUID().toString();
    }

    /*
    * 图片保存路径
    * */
    public static String imageSavePath() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/images/";
        // FileUtils.createOrExistsDir(path);
        return path;
    }

    /*
    * 音频保存路径
    * */
    public static String audioSavePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audios/";
        // FileUtils.createOrExistsDir(path);
        return path;
    }

    public static String videoSavePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/videos/";
        //  FileUtils.createOrExistsDir(path);
        return path;
    }

    // 毫秒转秒
    public static String long2String(long time) {
        int sec = (int) time;
        int min = sec / 60;
        sec = sec % 60;
        if (min < 10) {
            if (sec < 10) {
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }
    }

    public static String getMacAddress() {
        /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        //    String macAddress= "";
//    WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
//    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//    macAddress = wifiInfo.getMacAddress();
//    return macAddress;
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth0");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "020000000002";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X", b));
            }
//            if (buf.length() > 0) {
//                buf.deleteCharAt(buf.length() - 1);
//            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "020000000002";
        }
        return macAddress;
    }

    /**
     * 通过串口获取信息  4E080F 149B2F32003A C0A8012E 02 01 0000 01  000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
     */
    public static void dealMacInfo(String portInfo) {
        if (!SerialPortUtil.isHeartBeatOK(portInfo)) {
            return;
        }

        if (!Hawk.contains(constant.micMac) || !Hawk.get(constant.micMac).equals(portInfo.substring(6, 18).toLowerCase())) {
            Hawk.put(constant.micMac, portInfo.substring(6, 18).toLowerCase());
        }

        if (!Hawk.contains(constant.micNumber) || !Hawk.get(constant.micNumber).equals(portInfo.substring(26, 28).toLowerCase())) {
            Hawk.put(constant.micNumber, portInfo.substring(26, 28).toLowerCase());
        }

        String ip = convertIP(portInfo.substring(18, 26).toLowerCase());
        if (!Hawk.contains(constant.micIP) || !Hawk.get(constant.micIP).equals(ip)) {
            Hawk.put(constant.micIP, ip);
        }

        if (!Hawk.contains(constant.micSta) || !Hawk.get(constant.micSta).equals(portInfo.substring(34, 36).toLowerCase())) {
            Hawk.put(constant.micSta, portInfo.substring(34, 36).toLowerCase());

        }

        Activity topActivity = (Activity) ActivityUtils.getTopActivity();
        if (topActivity != null) {
            topActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //  发黏性事件
                    EventMessage msg = new EventMessage(MessageReceiveType.MessageRefreshMicSta, "");
                    EventBus.getDefault().post(msg);
                }
            });
        }


        Log.d("micMac", portInfo.substring(6, 18).toLowerCase());
        Log.d("micIP", ip);
        Log.d("micNumber", portInfo.substring(26, 28).toLowerCase());
        Log.d("micSta", portInfo.substring(34, 36));
    }

    public static void findSerialPortInfo() {
        MeetingAPP.getInstance().getHexadecimal().setQUERYStatus(new SerialPortListener() {
            @Override
            public void onMessageResponse(String msg, boolean isComplete) {
                if (isComplete && !StringUtils.isEmpty(msg)) {
                    Log.d("findSerialPortInfo", "收到消息" + msg);
                    FLUtil.dealMacInfo(msg);
                }
            }
        });
    }

    /**
     * 转换IP
     */

    public static String convertIP(String data) {
        String ip1 = ByteUtil.HexToInt(data.substring(0, 2)) + "";
        String ip2 = ByteUtil.HexToInt(data.substring(2, 4)) + "";
        String ip3 = ByteUtil.HexToInt(data.substring(4, 6)) + "";
        String ip4 = ByteUtil.HexToInt(data.substring(6, 8)) + "";
        return ip1 + "." + ip2 + "." + ip3 + "." + ip4;
    }

    public static void receiveBroadcast() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] message = new byte[1024];
                try {
                    // 建立Socket连接
                    try {
                        InetAddress address = InetAddress.getByName("255.255.255.255");
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    DatagramSocket datagramSocket = new DatagramSocket(12345);
                    datagramSocket.setBroadcast(true);
                    DatagramPacket datagramPacket = new DatagramPacket(message,
                            message.length);

                    try {
                        while (true) {
                            int p = datagramPacket.getPort();
                            // 准备接收数据
                            datagramSocket.receive(datagramPacket);
                            String strMsg = new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength(), "UTF-8");
                            Message msg = new Message();
                            Log.d("Broadcast", "strMsg==" + strMsg);
                            if (strMsg.contains("fang wei biao shi")) {
                                String ip = datagramPacket.getAddress().getHostAddress();
                                Log.d("ip", "ip==" + ip);

                                /**
                                 *   第一次进来肯定不相等，初始化一次即可
                                 * */
                                if (!ip.equals(BroadCastIP)) {
//                                    UrlConstant.initSocketUrl = "http://" + ip + ":3006" ;
                                    UrlConstant.baseUrl = "http://"+ip + ":3006";
//                                    UrlConstant.wsUrl = "ws://"+ip + ":8010";

//                                    FLUtil.initSocketIO();
                                    BroadCastIP = ip;

                                    NetWorkManager.getInstance().resetNetworkApi();

                                    Activity topActivity = (Activity) ActivityUtils.getTopActivity();
                                    if(topActivity!=null){
                                        topActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //  发黏性事件
                                                EventMessage msg = new EventMessage(MessageReceiveType.MessageClient, constant.get_server_ip);
                                                EventBus.getDefault().postSticky(msg);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void initSocketIO() {
        InitSocketManager.SocketCallBack callBack = new InitSocketManager.SocketCallBack() {
            @Override
            public void success() {
                if(InitSocketManager.socket == null){
                    return;
                }
                //  监听会议开启
                InitSocketManager.socket.on(constant.start_meeting, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject msg = (JSONObject) args[0];
                        Log.e("Flutil", msg.toString());
                        Activity topActivity = (Activity) ActivityUtils.getTopActivity();
                        Gson gson = new Gson();
                        try {
                            InitiaMeeting Meeting = gson.fromJson(msg.toString(), InitiaMeeting.class);
//                            Hawk.put(constant.InitiaMeeting,Meeting);

                            Hawk.put(constant.InitiaMeeting,Meeting);

                            //  赋值会议ID
                            UserUtil.meeting_record_id = Meeting.getMeeting_id();
                            Hawk.put(constant._id, UserUtil.meeting_record_id);
                            Hawk.put(constant.user_name, UserUtil.user_name);


//                            String aa = String.format("收到会议消息，会议id=%s", UserUtil.meeting_record_id);
                            if (topActivity != null) {
                                topActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //  发黏性事件
                                        EventMessage msg = new EventMessage(MessageReceiveType.MessageClient, constant.start_meeting);
                                        EventBus.getDefault().postSticky(msg);
                                    }
                                });
                            }
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void fail() {

            }
        };
        InitSocketManager.connect(callBack);
    }



    /**
     * 全屏
     */
    public static void requestfullScreen(@NonNull Activity activity) {

        // 隐藏下面系统返回
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        activity.getWindow().setAttributes(params);

        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    /**
     * 获取IP地址
     */
    public static String getIPAddress() {
        String ipAddress;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            ipAddress = inetAddress.getHostAddress().toString();
                            if (!ipAddress.contains("::")) {//ipV6的地址
                                return ipAddress;
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPrefe", ex.toString());
        }

        return null;
    }

    public static List<String> dealUDPMsg(String msg) {
        List<String> list = new ArrayList<>();
//        "CVI.PaperLess.[" + getIPAddress() + "].[" + code + "]"
        if (msg.contains("CVI.PaperLess.")) {
            String ipcodeInfo = msg.substring(msg.indexOf("[") + 1, msg.indexOf("]"));
            if (ipcodeInfo.length() > 0) {
                list = extractMessage(msg);
            }
        }
        return list;
    }

    /**
     * 提取白板需要的服务器ip地址
     * "http://192.168.8.141:3000"
     * */
    public static String getWhiteIPAdress() {
        String ip = UrlConstant.baseUrl;
       if(ip.contains("http://")){
           ip = ip.replace("http://","");
       }
       String[] spArr = ip.split(":");
       return  spArr[0];
    }

    /**
     * 提取中括号中内容，忽略中括号中的中括号
     *
     * @param msg
     * @return
     */
    public static List<String> extractMessage(String msg) {

        List<String> list = new ArrayList<String>();
        int start = 0;
        int startFlag = 0;
        int endFlag = 0;
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == '[') {
                startFlag++;
                if (startFlag == endFlag + 1) {
                    start = i;
                }
            } else if (msg.charAt(i) == ']') {
                endFlag++;
                if (endFlag == startFlag) {
                    list.add(msg.substring(start + 1, i));
                }
            }
        }
        return list;
    }

    /**
     * 查询第一个不是 0 的字符的位置
     *
     * @param str   检测的字符串
     * @param regex 检测的字符
     * @return
     */
    public static int getFirst(String str, String regex) {
        int i = 0;
        for (int index = 0; index <= str.length() - 1; index++) {
            //将字符串拆开成单个的字符
            String w = str.substring(index, index + 1);
            if (!regex.equals(w)) {//
                i = index;
//                Log.e("查询第一个不是 0 的字符的位置","第一个不是0的索引："+index+",值为："+w);
                break;
            }
        }
        return i;
    }

    /**
     * 10进制转16进制并补零补成两位
     */
    public static String TenToHexAndAppendZeroTo2(Integer parameter) {
        String dl = valueTenToValueHex(parameter);
        if (dl.length() == 1) {
            dl = "0" + dl;
        }
        return dl;
    }

    public static String valueTenToValueHex(Integer valueTen) {
        String v = valueTen + "";
        Integer value = Integer.parseInt(v);
        return Integer.toHexString(value);
    }

    /**
     * 临时会议获取IP地址 方法
     * 临时会议时获取设备是有线网还是无线网
     * */
    public static String getNetworkType() {
        ConnectivityManager manager = (ConnectivityManager) MeetingAPP.getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                return getIpAddressString();
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return getIpAddress();
            }
        }
        return "0.0.0.0";
    }

    /**
     * 临时会议时获取设备有线网IP地址
     * */
    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    /**
     * 临时会议时获取设备无线网IP地址
     * */
    public static String getIpAddress() {
        WifiManager wifiManager = (WifiManager) MeetingAPP.getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int i = wifiInfo.getIpAddress();
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
    }

    public static boolean checkPackage(Context context, String packageName)
    {
        if (packageName == null || "".equals(packageName))
            return false;

        try
        {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}





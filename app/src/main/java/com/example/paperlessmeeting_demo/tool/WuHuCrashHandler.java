package com.example.paperlessmeeting_demo.tool;

import android.content.Context;

import android.content.pm.PackageInfo;

import android.content.pm.PackageManager;

import android.os.Build;

import android.os.Environment;

import android.os.Looper;

import android.util.Log;

import android.widget.Toast;

import java.io.File;

import java.io.FileOutputStream;

import java.io.PrintWriter;

import java.io.StringWriter;

import java.io.Writer;

import java.lang.reflect.Field;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.HashMap;

import java.util.Map;

public class WuHuCrashHandler implements Thread.UncaughtExceptionHandler {

    /**

     * 系统默认UncaughtExceptionHandler

     */

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**

     * context

     */

    private Context mContext;

    /**

     * 存储异常和参数信息

     */

    private Map<Object,Object> paramsMap = new HashMap<>();
    /**

     * 格式化时间

     */

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private String TAG = this.getClass().getSimpleName();

    private static WuHuCrashHandler mInstance;

    private WuHuCrashHandler() {

    }

    /**

     * 获取CrashHandler实例

     */

    public static synchronized WuHuCrashHandler getInstance() {

        if (null == mInstance) {

            mInstance = new WuHuCrashHandler();

        }

        return mInstance;

    }

    public void init(Context context) {

        mContext = context;

        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

//设置该CrashHandler为系统默认的

        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    /**

     * uncaughtException 回调函数

     */

    @Override

    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {

//如果自己没处理交给系统处理

            mDefaultHandler.uncaughtException(thread, ex);

        } else {

//自己处理

            try {//延迟3秒杀进程

                Thread.sleep(2000);

            } catch (InterruptedException e) {

                Log.e(TAG, "error : ", e);

            }

//退出程序

            System.exit(0);

        }

    }

    /**

     * 收集错误信息.发送到服务器

     *

     * @return 处理了该异常返回true, 否则false

     */

    private boolean handleException(Throwable ex) {

        if (ex == null) {

            return false;

        }

//收集设备参数信息

        collectDeviceInfo(mContext);

//添加自定义信息

        addCustomInfo();

//使用Toast来显示异常信息

        new Thread() {

            @Override

            public void run() {

                Looper.prepare();

//在此处处理出现异常的情况

                Toast.makeText(mContext, "程序开小差了呢..", Toast.LENGTH_SHORT).show();

                Looper.loop();

            }

        }.start();

//保存日志文件

        saveCrashInfo2File(ex);

        return true;

    }

    /**

     * 收集设备参数信息

     *

     * @param ctx

     */

    public void collectDeviceInfo(Context ctx) {

//获取versionName,versionCode

        try {

            PackageManager pm = ctx.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

            if (pi != null) {

                String versionName = pi.versionName == null ? "null" : pi.versionName;

                String versionCode = pi.versionCode + "";

                paramsMap.put("versionName", versionName);

                paramsMap.put("versionCode", versionCode);

            }

        } catch (PackageManager.NameNotFoundException e) {

            Log.e(TAG, "an error occured when collect package info", e);

        }

//获取所有系统信息

        Field[] fields = Build.class.getDeclaredFields();

        for (Field field : fields) {

            try {

                field.setAccessible(true);

                paramsMap.put(field.getName(), field.get(null).toString());

            } catch (Exception e) {

                Log.e(TAG, "an error occured when collect crash info", e);

            }

        }

    }

    /**

     * 添加自定义参数

     */

    private void addCustomInfo() {

        Log.i(TAG, "addCustomInfo: 程序出错了...");

    }

    /**

     * 保存错误信息到文件中

     *

     * @param ex

     * @return 返回文件名称, 便于将文件传送到服务器

     */

    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();

        for (Map.Entry entry : paramsMap.entrySet()) {

            String key = (String) entry.getKey();

            String value = (String)entry.getValue();

            sb.append(key + "=" + value + "\n");

        }

        Writer writer = new StringWriter();

        PrintWriter printWriter = new PrintWriter(writer);

        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();

        while (cause != null) {

            cause.printStackTrace(printWriter);

            cause = cause.getCause();

        }

        printWriter.close();

        String result = writer.toString();

        sb.append(result);

        try {

            long timestamp = System.currentTimeMillis();

            String time = format.format(new Date());

            String fileName = "crash-" + time + "-" + timestamp + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crash/";

                File dir = new File(path);

                if (!dir.exists()) {

                    dir.mkdirs();

                }

                FileOutputStream fos = new FileOutputStream(path + fileName);

                fos.write(sb.toString().getBytes());

                Log.i(TAG, "saveCrashInfo2File: "+sb.toString());

                fos.close();

            }

            return fileName;

        } catch (Exception e) {

            Log.e(TAG, "an error occured while writing file...", e);

        }

        return null;

    }

}

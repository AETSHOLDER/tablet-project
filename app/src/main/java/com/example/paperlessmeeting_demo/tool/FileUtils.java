package com.example.paperlessmeeting_demo.tool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.paperlessmeeting_demo.BuildConfig;

import java.io.File;
import java.util.HashMap;

/**
 * Created by 梅涛 on 2020/9/4.
 */

public class FileUtils {

    public static Intent openFile(String filePath, Context context) {
        Log.d("dsaffaaf3333", filePath);
        File file = new File(filePath);
        if (!file.exists()) return null;
            /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
            /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath, context);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(filePath, context);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath, context);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("pptx")) {
            return getPptFileIntent(filePath, context);
        } else if (end.equals("xls")||end.equals("xlsx")) {
            return getExcelFileIntent(filePath, context);
        } else if (end.equals("doc")||end.equals("docx")) {
            return getWordFileIntent(filePath, context);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath, context);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, context);
        } else {
            return getAllIntent(filePath);
        }
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param, Context context) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);

        Uri uriForFile;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(param));
            intent.setDataAndType(uriForFile, "video/*");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "video/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param, Context context) {
        Log.d("dsaffaaf2222", param);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);

        Uri uriForFile;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(param));
            intent.setDataAndType(uriForFile, "audio/*");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "audio/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param, Context context) {
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uriForFile;
        if (Build.VERSION.SDK_INT > 23) {
            //Android 7.0之后
            uriForFile = FileProvider.getUriForFile(context, "com.example.paperlessmeeting.fileProvider", new File(param));
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);//给目标文件临时授权
        } else {
            uriForFile = Uri.fromFile(new File(param));
        }
        //   uriForFile = Uri.fromFile(new File(param));
        intent.setDataAndType(uriForFile, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param, Context context) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uriForFile;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(param));
            intent.setDataAndType(uriForFile, "application/vnd.ms-powerpoint");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.ms-powerpoint");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

/*
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");*/
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//系统会检查当前所有已创建的Task中是否有该要启动的Activity的Task;
        // 若有，则在该Task上创建Activity；若没有则新建具有该Activity属性的Task，并在该新建的Task上创建Activity。
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uriForFile = null;
     /*   if (Build.VERSION.SDK_INT > 23) {
            //Android 7.0之后
            uriForFile = FileProvider.getUriForFile(context, "com.example.paperlessmeeting.fileProvider", new File(param));
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);//给目标文件临时授权
        } else {
            uriForFile = Uri.fromFile(new File(param));
        }*/

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(param));
            intent.setDataAndType(uriForFile, "application/vnd.ms-excel");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.ms-excel");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //  Uri uri = Uri.fromFile(new File(param));
        //  intent.setDataAndType(uriForFile, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param, Context context) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//系统会检查当前所有已创建的Task中是否有该要启动的Activity的Task;
        // 若有，则在该Task上创建Activity；若没有则新建具有该Activity属性的Task，并在该新建的Task上创建Activity。
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uriForFile = null;
     /*   if (Build.VERSION.SDK_INT > 23) {
            //Android 7.0之后
            uriForFile = FileProvider.getUriForFile(context, "com.example.paperlessmeeting.fileProvider", new File(param));
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);//给目标文件临时授权
        } else {
            uriForFile = Uri.fromFile(new File(param));
        }*/

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(param));
            intent.setDataAndType(uriForFile, "application/msword");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "application/msword");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //  uriForFile = Uri.fromFile(new File(param));
        //    intent.setDataAndType(uriForFile, "application/msword");
        //  intent.setAction(Intent.ACTION_VIEW);
        Log.d("MainActivity11", "打开word路过~~~" + uriForFile.getPath());
        return intent;
    }

    private void getFileName(File[] files) {
        Log.d("sjshgha", "路过~~~~~11");
        String content = "";
        if (files != null) {// 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                if (file.isDirectory()) {
                    Log.d("zeng", "若是文件目录。继续读1" + file.getName().toString()
                            + file.getPath().toString());

                    getFileName(file.listFiles());
                    Log.d("zeng", "若是文件目录。继续读2" + file.getName().toString()
                            + file.getPath().toString());
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(".pre")) {
                        HashMap map = new HashMap();
                        String s = fileName.substring(0,
                                fileName.lastIndexOf(".")).toString();
                        Log.i("zeng", "文件名txt：：   " + s);
                        Log.d("sjshgha", "路过~~~~~22");
                      /*  map.put("Name", fileName.substring(0,
                                fileName.lastIndexOf(".")))*name.add(s);
                        if (fileName.contains(s)) {
                            try {
                                InputStream instream = new FileInputStream(file);
                                if (instream != null) {
                                    InputStreamReader inputreader = new InputStreamReader(instream);
                                    BufferedReader buffreader = new BufferedReader(inputreader);
                                    String line = "";
                                    //分行读取
                                    while ((line = buffreader.readLine()) != null) {
                                        content += line + "\n";
                                    }
                                    instream.close();
                                }
                            } catch (java.io.FileNotFoundException e) {
                                Log.d("TestFile", "The File doesn't not exist.");
                            } catch (IOException e) {
                                Log.d("TestFile", e.getMessage());
                            }
                          /*  Gson gson = new Gson();
                            UserBean userBean = gson.fromJson(content.toString(), UserBean.class);*/
                         /*   for (int i = 0; i < userBean.getUserlist().size(); i++) {
                                Log.d("vvvvxx", userBean.getUserlist().get(i).toString());
                            }*/

                    }

                }

            }
        }
//
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, Context context) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("cn.wps.moffice_eng","cn.wps.moffice.documentmanager.PreStartActivity2");

        Uri uriForFile;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(param));
            intent.setDataAndType(uriForFile, "*/*");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "*/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }


//        if (paramBoolean) {
//            Uri uri1 = Uri.parse(param);
//            intent.setDataAndType(uri1, "text/plain");
//        } else {
//            if (Build.VERSION.SDK_INT > 23) {
//                //Android 7.0之后
//                uriForFile = FileProvider.getUriForFile(context, "com.example.paperlessmeeting.fileProvider", new File(param));
//                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);//给目标文件临时授权
//            } else {
//                uriForFile = Uri.fromFile(new File(param));
//            }
//
//            intent.setDataAndType(uriForFile, "text/plain");
//        }
        return intent;
    }


    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param, Context context) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uriForFile;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(param));
            intent.setDataAndType(uriForFile, "application/pdf");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        /*Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");*/
        return intent;
    }

}

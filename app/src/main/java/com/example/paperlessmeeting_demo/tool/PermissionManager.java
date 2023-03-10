package com.example.paperlessmeeting_demo.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by ${xinGen} on 2017/11/1.
 * blog: http://blog.csdn.net/hexingen
 *
 * 权限工具类
 *
 *
 */

public class PermissionManager {

    /**
     *
     * @param context
     * return true:已经获取权限
     * return false: 未获取权限，主动请求权限
     */
   // @AfterPermissionGranted 是可选的
    public static boolean checkPermission(Activity context,String[] perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }
    /**
     * 请求权限
     * @param context
     */
    public static void requestPermission(Activity context,String tip,int requestCode,String[] perms) {
        EasyPermissions.requestPermissions(context, tip,requestCode,perms);
    }



    public static boolean CanShowFloat = false;

    private static final int REQUEST_OVERLAY = 5004;

    /** 动态请求悬浮窗权限 */
    public static void RequestOverlayPermission(Activity Instatnce)
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (!Settings.canDrawOverlays(Instatnce))
            {
                String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
                Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + Instatnce.getPackageName()));

                Instatnce.startActivityForResult(intent, REQUEST_OVERLAY);
            }
            else
            {
                CanShowFloat = true;
            }
        }
    }

    /** 浮窗权限请求，Activity执行结果，回调函数 */
    public static void onActivityResult(int requestCode, int resultCode, Intent data, final Activity Instatnce)
    {
        // Toast.makeText(activity, "onActivityResult设置权限！", Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_OVERLAY)		// 从应用权限设置界面返回
        {
            if(resultCode == Activity.RESULT_OK)
            {
                CanShowFloat = true;		// 设置标识为可显示悬浮窗
            }
            else
            {
                CanShowFloat = false;

                if (!Settings.canDrawOverlays(Instatnce))	// 若当前未允许显示悬浮窗，则提示授权
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Instatnce);
                    builder.setCancelable(false);
                    builder.setTitle("悬浮窗权限未授权");
                    builder.setMessage("应用需要悬浮窗权限，以展示浮标");
                    builder.setPositiveButton("去添加 权限", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();

                            RequestOverlayPermission(Instatnce);
                        }
                    });

                    builder.setNegativeButton("拒绝则 退出", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();

                            // 若拒绝了所需的权限请求，则退出应用
                            Instatnce.finish();
                            System.exit(0);
                        }
                    });
                    builder.show();
                }
            }
        }
    }
}

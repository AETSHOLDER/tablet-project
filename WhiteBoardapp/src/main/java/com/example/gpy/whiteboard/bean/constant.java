package com.example.gpy.whiteboard.bean;

import android.Manifest;

/**
 * Created by 梅涛 on 2020/9/7.
 */

public class constant {

    /**
     * 写入权限的请求code,提示语，和权限码
     */
    public final static int WRITE_PERMISSION_CODE1 = 0x0244;
    public final static String WRITE_PERMISSION_TIP = "为了正常使用，请允许一下权限!";
    public final static String[] PERMS_WRITE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_WIFI_STATE
    };
    public final static String[] PERMS_WRITE1 = {Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
}

package com.example.paperlessmeeting_demo.tool;

import android.content.Context;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.MeetingAPP;

/**
 * Created by gpy on 2016/4/11.
 */
public class ToastUtils {

    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int intStr) {
        Toast.makeText(context, intStr, Toast.LENGTH_SHORT).show();
    }
    public static void showShort(int resId){
        Toast.makeText(MeetingAPP.getInstance(), MeetingAPP.getInstance().getResources().getString(resId),Toast.LENGTH_SHORT).show();
    }
    public static void showShort(String string){
        Toast.makeText(MeetingAPP.getInstance(),string,Toast.LENGTH_SHORT).show();
    }
}

package com.example.paperlessmeeting_demo.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.paperlessmeeting_demo.activity.LoginActivity;

/**
 * Created by 梅涛 on 2021/3/25.
 */

public class BootRestartReceiver extends BroadcastReceiver {

    private final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub

      /*  if (intent.getAction().equals(ACTION));
            {*/
            Intent intent2 = new Intent(context, LoginActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
            //Intent intentService = new Intent();
            //intentService.setClass(context, MyService.class);
            //context.startService(intentService);

    //    }

    }
}

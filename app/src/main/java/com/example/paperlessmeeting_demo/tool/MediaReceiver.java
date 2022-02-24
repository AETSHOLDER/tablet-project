package com.example.paperlessmeeting_demo.tool;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by 梅涛 on 2020/10/28.
 */

public class MediaReceiver extends BroadcastReceiver {
    private StorageManager mStorageManager;
    private sendfilePthInterface sendfilePthInterface;

    public sendfilePthInterface getSendfilePthInterface() {
        return sendfilePthInterface;
    }

    public void setSendfilePthInterface(MediaReceiver.sendfilePthInterface sendfilePthInterface) {
        this.sendfilePthInterface = sendfilePthInterface;
    }

    public interface sendfilePthInterface {
        public void sendfilePth(String path);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mStorageManager = (StorageManager) context.getSystemService(Activity.STORAGE_SERVICE);
        switch (intent.getAction()) {
            case Intent.ACTION_MEDIA_CHECKING:
                break;
            case Intent.ACTION_MEDIA_MOUNTED:
                // 获取挂载路径, 读取U盘文件
                Uri uri = intent.getData();
                if (uri != null) {
                    String filePath = uri.getPath();
                    Log.d("dSFDfaf111", filePath);
                    File rootFile = new File(filePath);
                 /*   for (File file : rootFile.listFiles()) {
                        Log.d("dSFDfaf22", (filePath + constant.USB_PATH) + "====" + file.toString());
                        if ((filePath + constant.USB_PATH).equals(file.toString())) {
                            sendfilePthInterface.sendfilePth(file.toString());
                            Log.d("dSFDfaf33", (filePath + constant.USB_PATH) + "====" + file.toString());
                        }
                    }*/
                }
                getUName();
                break;
            case Intent.ACTION_MEDIA_EJECT:
                break;
            case Intent.ACTION_MEDIA_UNMOUNTED:
                break;
        }
    }

    private void getUName() {
        Class<?> volumeInfoClazz = null;
        Method getDescriptionComparator = null;
        Method getBestVolumeDescription = null;
        Method getVolumes = null;
        Method isMountedReadable = null;
        Method getType = null;
        Method getPath = null;
        List<?> volumes = null;
        try {
            volumeInfoClazz = Class.forName("android.os.storage.VolumeInfo");
            getDescriptionComparator = volumeInfoClazz.getMethod("getDescriptionComparator");
            getBestVolumeDescription = StorageManager.class.getMethod("getBestVolumeDescription", volumeInfoClazz);
            getVolumes = StorageManager.class.getMethod("getVolumes");
            isMountedReadable = volumeInfoClazz.getMethod("isMountedReadable");
            getType = volumeInfoClazz.getMethod("getType");
            getPath = volumeInfoClazz.getMethod("getPath");
            volumes = (List<?>) getVolumes.invoke(mStorageManager);

            for (Object vol : volumes) {
                if (vol != null && (boolean) isMountedReadable.invoke(vol) && (int) getType.invoke(vol) == 0) {
                    File path2 = (File) getPath.invoke(vol);
                    String p1 = (String) getBestVolumeDescription.invoke(mStorageManager, vol);
                    String p2 = path2.getPath();
                    Log.d("dSFDfaf333", "-----------path1-----------------" + p1);               //打印U盘卷标名称
                    Log.d("dSFDfaf4444", "-----------path2 @@@@@-----------------" + p2);         //打印U盘路径
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


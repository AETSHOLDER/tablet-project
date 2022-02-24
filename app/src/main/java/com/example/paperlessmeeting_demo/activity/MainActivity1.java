package com.example.paperlessmeeting_demo.activity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.tool.FileUtils;
import com.example.paperlessmeeting_demo.tool.PermissionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity1 extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener, ServiceConnection {
    @BindView(R.id.image)
    ImageView image;
    private MediaPlayer mediaPlayer = new MediaPlayer();//实例化MediaPlayer类
    @BindView(R.id.aa)
    TextView aa;
    private String fileStr = Environment.getExternalStorageDirectory() + "/aa";
    private List<String> name = new ArrayList<>();
    private String[] ss = new String[]{};
    private List<Long> list = new ArrayList<>();
    private String TAG = "MainActivity11";
    private WhiteBoardServies myBinder;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aa:
                // moveTaskToBack(true);
                startFloatingButtonService();
                //   bindService(new Intent(MainActivity1.this, WhiteBoardServies.class), MainActivity1.this, BIND_AUTO_CREATE);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);
        checkWritePermission1();
        aa.setOnClickListener(this);
        File path = new File(fileStr);
        Log.d("sjshgha", "路过~~~~~");

        File[] files = path.listFiles();// 读取
        for (int i = 0; i < files.length; i++) {
            Log.d("sjshgha", files[i].toString() + "78687");
        }
        //getFileName(files);
    }

    private void initPlayer(String musicUrl) {
        Log.d(TAG, musicUrl + "=音乐路径");
        // Uri uri = Uri.fromFile(musicUrl);//网络中的音乐文件
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.parse(musicUrl));//音乐文件路径
            mediaPlayer.prepare();//资源文件准备
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();//播放
            Log.d(TAG, "音乐播放路过！！！~");
        }
    }

    public void startFloatingButtonService() {
        Log.d("gfjjfghjfg", "点击路过！！！~");
        if (WhiteBoardServies.isStarted) {
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            Log.d("gfjjfghjfg", "11111点击路过！！！~");
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {
            Log.d("gfjjfghjfg", "2222点击路过！！！~");
            bindService(new Intent(MainActivity1.this, WhiteBoardServies.class), MainActivity1.this, BIND_AUTO_CREATE);
            //startService(new Intent(MainActivity1.this, WhiteBoardServies.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            startFloatingButtonService();
        }
    }

    private void getFileName(File[] files) {

        Log.d(TAG, "路过~~~~~11");
        String content = "";
        if (files != null) {// 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                Log.d(TAG, "路过~~~~~11" + file.getPath());

              /*  try {
                    startActivity(FileUtils.getWordFileIntent(file.getPath(), MainActivity1.this));
                } catch (ActivityNotFoundException e) {
                    //检测到系统尚未安装OliveOffice的apk程序
                    Toast.makeText(MainActivity1.this, "未找到软件", Toast.LENGTH_LONG).show();
                    //请先到www.olivephone.com/e.apk下载并安装
                }*/
                // startActivity(FileUtils.getTextFileIntent(file.getPath(),false, MainActivity1.this));


                if (file.isDirectory()) {
                    Log.d(TAG, "若是文件目录。继续读1" + file.getName().toString()
                            + file.getPath().toString());

                    getFileName(file.listFiles());
                    Log.d(TAG, "若是文件目录。继续读2" + file.getName().toString()
                            + file.getPath().toString());
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(".docx")) {
                        //  initPlayer("file://" + file.getPath());
                        //initPlayer(file);
                        // startActivity(FileUtils.getVideoFileIntent(file.getPath(), MainActivity1.this));
                        //  startActivity(FileUtils.getAudioFileIntent(file.getPath(), MainActivity1.this));
                        //   startActivity(FileUtils.getPptFileIntent(file.getPath(), MainActivity1.this));
                        //   startActivity(FileUtils.getPdfFileIntent(file.getPath(), MainActivity1.this));
                        // startActivity(FileUtils.getTextFileIntent(file.getPath(), MainActivity1.this));
                        try {
                            startActivity(FileUtils.getWordFileIntent(file.getPath(), MainActivity1.this));
                        } catch (ActivityNotFoundException e) {
                            //检测到系统尚未安装OliveOffice的apk程序
                            Toast.makeText(MainActivity1.this, "未找到软件", Toast.LENGTH_LONG).show();
                            //请先到www.olivephone.com/e.apk下载并安装
                        }
                        //   startActivity(FileUtils.getImageFileIntent(file.getPath(), MainActivity1.this));

                        //  startActivity(FileUtils.getExcelFileIntent(file.getPath(), MainActivity1.this));
                        // ImageLoader.getInstance().displayImage("file://" + file.getPath(), image);
                        HashMap map = new HashMap();
                        String s = fileName.substring(0,
                                fileName.lastIndexOf(".")).toString();
                        Log.i(TAG, "文件名txt：：   " + s + "----" + file.getPath() + "~~~" + file.getAbsolutePath());
                        Log.d(TAG, "路过~~~~~22");
                      /*  map.put("Name", fileName.substring(0,
                                fileName.lastIndexOf(".")))*/
                        name.add(s);
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
                            } catch (FileNotFoundException e) {
                                Log.d(TAG, "The File doesn't not exist.");
                            } catch (IOException e) {
                                Log.d(TAG, e.getMessage());
                            }

                        }

                    }

                }
            }
//
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        myBinder = ((WhiteBoardServies.MyBinder) iBinder).getService();
        Log.d(TAG, "返回服务11");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(TAG, "返回服务222");
    }

    @Override
    public void onBindingDied(ComponentName name) {
        Log.d(TAG, "返回服务333");
    }

    /**
     * 检查读写权限权限
     */
    @AfterPermissionGranted(constant.WRITE_PERMISSION_CODE1)
    private void checkWritePermission1() {
        if (!PermissionManager.checkPermission(this, constant.PERMS_WRITE1)) {
            PermissionManager.requestPermission(this, constant.WRITE_PERMISSION_TIP, constant.WRITE_PERMISSION_CODE1, constant.PERMS_WRITE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 请求权限成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Toast.makeText(MainActivity1.this, "授权成功", Toast.LENGTH_LONG).show();


    }

    /**
     * 请求权限失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(MainActivity1.this, "用户授权失败", Toast.LENGTH_LONG).show();
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private boolean deleteLastFromFloder(String path) {
        boolean success = false;
        try {
            ArrayList<File> images = new ArrayList<File>();
            getFiles(images, path);
            File latestSavedImage = images.get(0);
            if (latestSavedImage.exists()) {
                for (int i = 1; i < images.size(); i++) {
                    File nextFile = images.get(i);
                    if (nextFile.lastModified() > latestSavedImage.lastModified()) {
                        latestSavedImage = nextFile;
                    }
                }

                Log.e("brady", "images = " + latestSavedImage.getAbsolutePath());
                success = latestSavedImage.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    private void getFiles(ArrayList<File> fileList, String path) {
        File[] allFiles = new File(path).listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            if (file.isFile()) {
                fileList.add(file);
            } else if (!file.getAbsolutePath().contains(".doc")) {
                getFiles(fileList, file.getAbsolutePath());
            }
        }
    }
}
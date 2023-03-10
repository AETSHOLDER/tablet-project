package com.example.gpy.whiteboard;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpy.whiteboard.bean.constant;
import com.example.gpy.whiteboard.utils.FileUtil;
import com.example.gpy.whiteboard.utils.JsonUtils;
import com.example.gpy.whiteboard.utils.PermissionManager;
import com.example.gpy.whiteboard.utils.StoreUtil;
import com.example.gpy.whiteboard.utils.ToastUtils;
import com.example.gpy.whiteboard.view.WhiteBoardActivity;
import com.example.gpy.whiteboard.view.base.BaseActivity;
import com.github.guanpy.wblib.bean.DrawPoint;
import com.github.guanpy.wblib.utils.OperationUtils;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.github.guanpy.wblib.utils.AppContextUtil.getContext;


public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.iv_wb_add)
    ImageView mIvAdd;
    @BindView(R.id.tv_list_loading)
    TextView tvListLoading;
    @BindView(R.id.lv_wb)
    ListView mLv;
    @BindView(R.id.fragmnet)
    FrameLayout fragmnet;
    private WbAdapter mWbAdapter;
    ArrayList<String> filenames;
    ArrayList<String> filepaths;
    private long mBackPressedTime;

    private ServerSocket mServerSocket;
    private Socket mSocket;
    private StringBuffer sb = new StringBuffer();
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                Bundle data = msg.getData();
                String jsonStr = data.getString("msg");
//                sb.append(jsonStr);
                StoreUtil.readJson(jsonStr);
                Bundle bundle = new Bundle();
                bundle.putString("type", "new");
                navi2Page(WhiteBoardActivity.class,bundle,true);


//                new AlertDialog.Builder(getContext())
//                        .setTitle(R.string.permission_dialog_title)
//                        .setMessage("???????????????????????????")
//                        .setPositiveButton(R.string.permission_dialog_ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                StoreUtil.readJson(jsonStr);
//                                navi2Page(WhiteBoardActivity.class);
//                            }
//                        })
//                        .setNegativeButton(R.string.permission_dialog_cancel, null)
//                        .create().show();


//                mIvAdd.setImageResource(R.drawable.bg_popup);
            }
        }
    };
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        checkWritePermission1();
        loadData();
        initView();

//        try {
//            mServerSocket = new ServerSocket(1989);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //??????????????????
//        MainActivity.SocketAcceptThread socketAcceptThread = new MainActivity.SocketAcceptThread();
//        socketAcceptThread.start();
    }


    private void loadData() {
        File folder = new File(StoreUtil.getWbPath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        final File[] files = folder.listFiles();
        if (files.length > 0) {
            filenames = new ArrayList<String>();
            filepaths = new ArrayList<String>();
            for (File f : files) {
                filenames.add(FileUtil.getFileName(f));
                filepaths.add(f.getAbsolutePath());
            }
        }
//        mWbAdapter.notifyDataSetChanged();

    }

    private void initView() {
        mWbAdapter = new WbAdapter();
        mLv.setAdapter(mWbAdapter);
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperationUtils.getInstance().initDrawPointList();
                navi2Page(WhiteBoardActivity.class);

            }
        });
    }


    class SocketAcceptThread extends Thread{
        @Override
        public void run() {
            try {
                //???????????????????????????Accept?????????????????????????????????
                //???????????????????????????????????????
                mSocket = mServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("info", "run: =============="+"accept error" );
                return;
            }
            Log.e("info", "accept success==================");
            //????????????????????????
            startReader(mSocket);
        }
    }

    /**
     * ????????????Socket????????????????????????
     */
    private void startReader(final Socket socket) {

        new Thread(){
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    // ???????????????
                    reader = new DataInputStream(socket.getInputStream());
                    while (true) {
                        System.out.println("*main?????????????????????*");
                        // ????????????
                        String msg = reader.readUTF();
                        System.out.println("main??????????????????????????????=" + msg);

                        //???????????????????????????
//                        DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
//                        writer.writeUTF("?????????" + msg); // ?????????UTF-8?????????

                        //???????????????UI
                        Message message = new Message();
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", msg);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if (mServerSocket != null){
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        final long mCurrentTime = System.currentTimeMillis();
        if (mCurrentTime - this.mBackPressedTime > 1000) {
            ToastUtils.showToast(this, R.string.app_logout);
            this.mBackPressedTime = mCurrentTime;
            return;
        }
        super.onBackPressed();
        System.exit(0);


        if (mServerSocket != null){
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

   /*     FragmentTransaction ts = getSupportFragmentManager().beginTransaction();

        WhiteBoardFragment whiteBoardFragment = WhiteBoardFragment.newInstance();
        ts.add(R.id.fragmnet, whiteBoardFragment, "wb").commit();*/
    }

    private class WbAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return filenames != null ? filenames.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            WbViewHolder holder = null;
            if (convertView != null) {
                holder = (WbViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.wb_item, null);
                if (convertView != null) {
                    convertView.setTag(
                            holder = new WbViewHolder(convertView)
                    );
                }
                if (holder != null) {
                    holder.nWbName.setText(filenames.get(position));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            StoreUtil.readWhiteBoardPoints(filepaths.get(position));
                            navi2Page(WhiteBoardActivity.class);
                        }
                    });
                }
            }
            return convertView;
        }
    }

    /**
     * ????????????????????????
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
        //?????????????????????EasyPermission?????????
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_LONG).show();


    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
        /**
         * ??????????????????????????????????????????'NEVER ASK AGAIN.'??????'????????????'?????????????????????
         * ?????????????????????????????????????????????????????????????????????
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private final class WbViewHolder {
        final TextView nWbName;

        public WbViewHolder(final View view) {
            this.nWbName = (TextView) view.findViewById(R.id.tv_wb_name);
        }
    }
}

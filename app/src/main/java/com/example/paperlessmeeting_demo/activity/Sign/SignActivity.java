package com.example.paperlessmeeting_demo.activity.Sign;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.sharefile.SocketShareFileManager;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.PCScreen.ScreenImageService;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.ScreenImageApi;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.ScreenVideoController;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.StreamController;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.TcpPacker;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.WSSender;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.SocketCmd;
import com.example.paperlessmeeting_demo.tool.StoreUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClient;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.VideoConfiguration;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.widgets.FloatingActionsMenu;
import com.example.paperlessmeeting_demo.widgets.FloatingImageButton;
import com.example.paperlessmeeting_demo.widgets.SignDrawPenView;
import com.github.guanpy.library.EventBus;
import com.github.guanpy.library.ann.ReceiveEvents;
import com.github.guanpy.wblib.bean.DrawPoint;
import com.github.guanpy.wblib.utils.Events;
import com.github.guanpy.wblib.utils.SignOperationUtils;
import com.github.guanpy.wblib.utils.WhiteBoardVariable;
import com.github.guanpy.wblib.widget.SignDrawTextLayout;
import com.github.guanpy.wblib.widget.SignDrawTextView;
import com.orhanobut.hawk.Hawk;
import com.tencent.smtt.sdk.TbsReaderView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import q.rorbin.verticaltablayout.util.DisplayUtil;
import static com.blankj.utilcode.util.ScreenUtils.getScreenHeight;
import static com.blankj.utilcode.util.ScreenUtils.getScreenWidth;

public class SignActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.sign_door)
    TextView sign_Door;
    @BindView(R.id.sign_commit)
    TextView sign_commit;
    @BindView(R.id.tools_bar)
    RelativeLayout toolsBar;
    @BindView(R.id.sign_title)
    TextView signTitle;
    @BindView(R.id.sign_screen)
    TextView sign_screen;
    @BindView(R.id.sign_bg)
    ImageView signBg;
    @BindView(R.id.sign_rl)
    RelativeLayout sign_rl;
    @BindView(R.id.screen_shoot_bg)
    ImageView screenShootBg;
    @BindView(R.id.rl_sign_tools)
    RelativeLayout rl_sign_tools;
    @BindView(R.id.sign_see)
    TextView sign_see;
    @BindView(R.id.fl_view)
    FrameLayout mFlView;
    @BindView(R.id.db_view)
    SignDrawPenView mDbView;
    @BindView(R.id.dt_view)
    SignDrawTextLayout mDtView;
    @BindView(R.id.fab_menu_size)
    FloatingActionsMenu mFabMenuSize;
    @BindView(R.id.bt_size_large)
    FloatingImageButton mBtSizeLarge;
    @BindView(R.id.bt_size_middle)
    FloatingImageButton mBtSizeMiddle;
    @BindView(R.id.bt_size_mini)
    FloatingImageButton mBtSizeMini;
    @BindView(R.id.fab_menu_color)
    FloatingActionsMenu mFabMenuColor;
    @BindView(R.id.bt_color_green)
    FloatingImageButton mBtColorGreen;
    @BindView(R.id.bt_color_purple)
    FloatingImageButton mBtColorPurple;
    @BindView(R.id.bt_color_pink)
    FloatingImageButton mBtColorPink;
    @BindView(R.id.bt_color_orange)
    FloatingImageButton mBtColorOrange;
    @BindView(R.id.bt_color_black)
    FloatingImageButton mBtColorBlack;
    @BindView(R.id.fab_menu_text)
    FloatingActionsMenu mFabMenuText;
    @BindView(R.id.bt_text_underline)
    FloatingImageButton mBtTextUnderline;
    @BindView(R.id.bt_text_italics)
    FloatingImageButton mBtTextItalics;
    @BindView(R.id.bt_text_bold)
    FloatingImageButton mBtTextBold;
    @BindView(R.id.fab_menu_eraser)
    FloatingActionsMenu mFabMenuEraser;
    @BindView(R.id.bt_eraser_large)
    FloatingImageButton mBtEraserLarge;
    @BindView(R.id.bt_eraser_middle)
    FloatingImageButton mBtEraserMiddle;
    @BindView(R.id.bt_eraser_mini)
    FloatingImageButton mBtEraserMini;
    @BindView(R.id.iv_white_board_undo)
    ImageView mIvWhiteBoardUndo;
    @BindView(R.id.iv_white_board_redo)
    ImageView mIvWhiteBoardRedo;
    @BindView(R.id.v_bottom_back)
    View mVBottomBack;
    @BindView(R.id.sv_content)
    ScrollView svContent;
    @BindView(R.id.iv_white_board_quit)
    ImageView mIvWhiteBoardQuit;
    @BindView(R.id.iv_white_board_confirm)
    ImageView mIvWhiteBoardConfirm;
    @BindView(R.id.tbs_notInit)
    TextView tbs_notInit;

    // ????????????
    private boolean isScreen = false;
    private static final int ACTIVITY_RESULT_CODE_SCREEN = 110;
    private StreamController mStreamController;
    private WSSender tcpSender;
    private VideoConfiguration mVideoConfiguration;

    private MyServiceConnect mConnect;
    private ScreenImageService screenImageService;

    private String url;
    TbsReaderView tbsReaderView;
    //??????????????????
    private boolean isOpenFile;
    private Drawable drawable_left_enter;
    private Drawable drawable_left_exit;
    private File appDir;
    private String fileName;
    private String TAG = "SignActivity";
    private SocketShareFileManager socketShareFileManager;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int port = (int)msg.obj;
                    Log.d("","port===="+port);
                    break;
                case 2:
                     String ss = (String)msg.obj;
                     Log.e("??????????????????",""+ss);
//                    Toast.makeText(SignActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 4:

                    break;
                case 5:
//                    Toast.makeText(SignActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
//                    Toast.makeText(SignActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    String df = msg.obj.toString();
                    Log.d("??????????????????++", df);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initView() {
        String title = getIntent().getStringExtra("fileName");
        signTitle.setText(title);
        int width = DisplayUtil.dp2px(SignActivity.this, 38);
        int height = DisplayUtil.dp2px(SignActivity.this, 36);
        Rect bonds = new Rect(0, 0, width, height);
        drawable_left_enter = SignActivity.this.getResources().getDrawable(R.mipmap.sign_enter);
        drawable_left_enter.setBounds(bonds);
        drawable_left_exit = SignActivity.this.getResources().getDrawable(R.mipmap.sign_exit);
        drawable_left_exit.setBounds(bonds);

        int width2 = DisplayUtil.dp2px(SignActivity.this, 53);

        Drawable drawable_commit = SignActivity.this.getResources().getDrawable(R.mipmap.sign_commit);
        drawable_commit.setBounds(bonds);
        Rect bonds_screen = new Rect(0, 0, width2, width2);
        Drawable drawable_screen = SignActivity.this.getResources().getDrawable(R.mipmap.sign_screen);
        drawable_screen.setBounds(bonds_screen);

        sign_Door.setCompoundDrawables(drawable_left_enter, null, null, null);
        sign_Door.setGravity(Gravity.CENTER);
        sign_commit.setCompoundDrawables(drawable_commit, null, null, null);
        sign_commit.setGravity(Gravity.CENTER);
        sign_screen.setCompoundDrawables(drawable_screen, null, null, null);
        sign_screen.setGravity(Gravity.CENTER);

        url = getIntent().getStringExtra("url");
        Log.d("SignActivity", url);
        isOpenFile = getIntent().getBooleanExtra("isOpenFile", false);
        tbsReaderView = new TbsReaderView(this, readerCallback);
        //????????????
        if (!TextUtils.isEmpty(url) && isOpenFile) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, R.id.tools_bar);
            params.setMargins(DisplayUtil.dp2px(SignActivity.this,120), 0, DisplayUtil.dp2px(SignActivity.this,120), 0);
            rlRoot.addView(tbsReaderView, params);
            openFile();
        }
        // ??????????????????????????????
        mVBottomBack.bringToFront();
        rl_sign_tools.bringToFront();

        // ????????????
        changePenBack();
        changeColorBack();
        changeEraserBack();
        ToolsOperation(WhiteBoardVariable.Operation.PEN_NORMAL);

        if(Hawk.contains("TBS")){
            tbs_notInit.setVisibility(Hawk.get("TBS")? View.GONE:View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        bindScreenService();
        socketShareFileManager = new SocketShareFileManager(mHandler);
        SignOperationUtils.getInstance().DISABLE = false;
        if (Hawk.contains("UserBehaviorBean")) {
            UserBehaviorBean userBehaviorBean = Hawk.get("UserBehaviorBean");
            UserBehaviorBean.DataBean dataBean = new UserBehaviorBean.DataBean();
            dataBean.setTittile(this.getClass().getName());
            dataBean.setTime(TimeUtils.getTime(System.currentTimeMillis()));
            List<UserBehaviorBean.DataBean> dataBeanList = userBehaviorBean.getData();
            dataBeanList.add(dataBean);
            Hawk.put("UserBehaviorBean", userBehaviorBean);
        }
        initEvent();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isScreen){
                    ToastUtils.showShort("??????????????????,??????????????????!");
                    return;
                }

                int size = SignOperationUtils.getInstance().getSavePoints().size();
                if(size>0) {
                    CVIPaperDialogUtils.showCustomDialog(SignActivity.this, "????????????????????????,?????????????", null, "??????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                clearData();
                                finish();
                            }
                        }
                    });
                }else {
                    finish();
                }
            }
        });
        sign_Door.setSelected(false);
        sign_Door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_Door.setSelected(!sign_Door.isSelected());
                if (sign_Door.isSelected()) {
                    if(isScreen || UserUtil.isShareScreen){
                        CVIPaperDialogUtils.showConfirmDialog(SignActivity.this,"??????????????????????????????!","??????",false,new CVIPaperDialogUtils.ConfirmDialogListener() {
                            @Override
                            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                if (clickConfirm) {
                                    // ??????????????????
                                    sign_Door.setSelected(!sign_Door.isSelected());
                                    return;
                                }
                            }
                        });
                        return;
                    }
                    if(url.contains("ppt") || url.contains("xls") || url.contains("txt")){
                        // ?????????office??????getBitmap?????????????????????????????????????????????
                        Log.e("111","url.contains(\"ppt\")");
                        takeScreenShot();
                    }else {
                        Bitmap bitmap  = getBitmap(tbsReaderView);
                        Bitmap bitmap22 = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                        changeSignUI();
                        mDbView.setVisibility(View.VISIBLE);
                        mDtView.setVisibility(View.VISIBLE);
                        tbsReaderView.setVisibility(View.GONE);
                        //??????
                        SignOperationUtils.getInstance().DISABLE = true;

//                        mDbView.setmBottomBitmap(bitmap22);
//                        onSaveBitmap(bitmap, "2");
                        // ??????????????????bitmap ?????????recycle

//                        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap22,IMAGE_WIDTH,IMAGE_HEIGHT ,true);
                        mDbView.setBackground(new BitmapDrawable(bitmap22));
                    }
                }else {
                    int size = SignOperationUtils.getInstance().getSavePoints().size();
                    if(size>0){
                        CVIPaperDialogUtils.showCustomDialog(SignActivity.this, "????????????????????????,???????????????????", null, "??????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                            @Override
                            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                if (clickConfirm){
                                    SignOperationUtils.getInstance().DISABLE = false;
                                    changeSignUI();
                                    mDbView.clearImage();
                                    mDtView.hideTextView();
                                    mDbView.setVisibility(View.GONE);
                                    mDtView.setVisibility(View.GONE);
                                    tbsReaderView.setVisibility(View.VISIBLE);
                                }else {
                                    // ??????????????????????????????
                                    sign_Door.setSelected(!sign_Door.isSelected());
                                }
                            }
                        });
                    }else {
                        SignOperationUtils.getInstance().DISABLE = false;
                        changeSignUI();
                        mDbView.clearImage();
                        mDtView.hideTextView();
                        mDbView.setVisibility(View.GONE);
                        mDtView.setVisibility(View.GONE);
                        tbsReaderView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        // ????????????
        sign_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ????????????????????????????????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                takeScreenShotCommit();
            }
        });
        sign_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignActivity.this, SignListActivity.class);
                startActivity(intent);
            }
        });

        // ??????
        if(UserUtil.ISCHAIRMAN){
            sign_screen.setVisibility(View.VISIBLE);
        }else {
            sign_screen.setVisibility(View.GONE);
        }
        sign_screen.setSelected(false);
        sign_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.MANUFACTURER.equalsIgnoreCase("XIAOMI")) {
                    //
                    ToastUtils.showShort("???????????????????????????!");
                    return;
                }
                if(sign_Door.isSelected()){
                    CVIPaperDialogUtils.showConfirmDialog(SignActivity.this, "??????????????????!", "??????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {

                        }
                    });
                    return;
                }

                sign_screen.setSelected(!sign_screen.isSelected());
                isScreen = sign_screen.isSelected();
                String text = sign_screen.isSelected() ? "????????????" : "??????";
                sign_screen.setText(text);

                if(sign_screen.isSelected()){
                    JWebSocketClientService.sendMsg(constant.START_SHARE_SCEEN);
                    try {
                        Thread.sleep(1000);//??????1????????????????????????????????????????????????????????????
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // ????????????????????????
                    if(UserUtil.isShareScreen){
//                        screenImageService.startWSSender();
                        screenImageService.startWSController();
                    }else {
                        mediaProjectionManager = (MediaProjectionManager) getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                        Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
                        startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE_SCREEN);
                    }

                }else {
                    if(UserUtil.isShareScreen){
//                        screenImageService.finishWSSender();
                        screenImageService.stopWSRecording();
                    }else {
                        mStreamController.stop();
                    }
                    JWebSocketClientService.sendMsg(constant.FINISH_SHARE_SCEEN);
                }
            }
        });
    }

    /**
     * ???????????????????????????
     */
    private void bindScreenService() {
        Intent intent = new Intent(this, ScreenImageService.class);
        mConnect = new MyServiceConnect();
        bindService(intent, mConnect, Context.BIND_AUTO_CREATE);
    }

    class MyServiceConnect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            screenImageService = ((ScreenImageService.ScreenImageBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    /**
     * ?????????????????????UI??????
     * */
    private void changeSignUI() {
        String text = sign_Door.isSelected() ? "????????????" : "????????????";
        int visable = sign_Door.isSelected() ? View.VISIBLE : View.GONE;
        int signTitlevisable = sign_Door.isSelected() ? View.GONE : View.VISIBLE;
        Drawable drawable = sign_Door.isSelected() ? drawable_left_exit : drawable_left_enter;

        sign_Door.setText(text);
        sign_Door.setCompoundDrawables(drawable, null, null, null);
        sign_Door.setGravity(Gravity.CENTER);
        sign_commit.setVisibility(visable);

        signTitle.setVisibility(signTitlevisable);
        rl_sign_tools.setVisibility(visable);
    }

    private void openFile() {
        File file = new File(url);
        if (!file.exists()) {
            Toast.makeText(this, "???????????????", Toast.LENGTH_LONG).show();
        }
        Bundle bundle = new Bundle();
        bundle.putString("filePath", url);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath() + "/TbsTempPath");
        Log.d("requestCodeUrnnn", Environment.getExternalStorageDirectory().getPath());
        boolean result = tbsReaderView.preOpen(parseFormat(parseName(url)), false);
        if (result) {
            Hawk.put("TBS",true);
            if (Hawk.contains(constant.isLaunch) && (Boolean)Hawk.get(constant.isLaunch)){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tbsReaderView.openFile(bundle);
                    }
                },1000);
                Hawk.put(constant.isLaunch,false);
            }else {
                tbsReaderView.openFile(bundle);
            }
        }else {
            Hawk.put("TBS",false);
            tbs_notInit.setVisibility(View.VISIBLE);
//            Intent intent = new Intent(SignActivity.this, ActivityBrowser.class);
//            Bundle bundle1 = new Bundle();
//            bundle1.putString("flag", "1");
//            intent.putExtras(bundle1);
//            startActivity(intent);
        }
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }

    TbsReaderView.ReaderCallback readerCallback = new TbsReaderView.ReaderCallback() {
        @Override
        public void onCallBackAction(Integer integer, Object o, Object o1) {

        }
    };
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1001:
                    break;
                case 1:
                    mDbView.post(new Runnable() {
                        @Override
                        public void run() {
                            showPoints();
                        }
                    });
                    tbsReaderView.setVisibility(View.GONE);
                    mDbView.setVisibility(View.VISIBLE);
                    mDtView.setVisibility(View.VISIBLE);
                    break;
            }
            return false;
        }
    });


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_exit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.registerAnnotatedReceiver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.unregisterAnnotatedReceiver(this);
    }

    @Override
    protected void onDestroy() {
        clearData();
        super.onDestroy();
    }
    public void clearData(){
        if(handler!= null){
            handler.removeMessages(1001);
            handler = null;
        }
        if(tbsReaderView!=null){
            tbsReaderView.onStop();
        }
        if(mDbView!=null){
            mDbView.clearImage();
        }
        if(mDtView!=null){
            mDtView.hideTextView();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    public static final int EVENT_SCREENSHOT = 22;//????????????
    public static final int EVENT_SCREENSHOTCOMMIT = 33;//????????????
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private Image image;
    public void takeScreenShot() {
        mediaProjectionManager = (MediaProjectionManager)
                getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), EVENT_SCREENSHOT);
    }
    public void takeScreenShotCommit() {
        mediaProjectionManager = (MediaProjectionManager)
                getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), EVENT_SCREENSHOTCOMMIT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult","resultCode == "+resultCode );
        if(resultCode != Activity.RESULT_OK){
            CVIPaperDialogUtils.showConfirmDialog(SignActivity.this, "???????????????????????????!", "?????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                @Override
                public void onClickButton(boolean clickConfirm, boolean clickCancel) {

                }
            });
            return;
        }
        if (requestCode == EVENT_SCREENSHOT) {
            super.onActivityResult(requestCode, resultCode, data);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            ImageReader mImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay("screen-mirror", width, height,
                    displayMetrics.densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        image = mImageReader.acquireLatestImage();
                        if (image != null) {
                            final Image.Plane[] planes = image.getPlanes();
                            final ByteBuffer buffer = planes[0].getBuffer();
                            int width = image.getWidth();
                            int height = image.getHeight();
//                            Log.e("whh0914", "image width=" + width + ", height=" + height);
                            int pixelStride = planes[0].getPixelStride();
                            int rowStride = planes[0].getRowStride();
                            int rowPadding = rowStride - pixelStride * width;
                            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                            bitmap.copyPixelsFromBuffer(buffer);
                            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                            if (bitmap != null) {
                                // ?????????????????????
                                int pix = DisplayUtil.dp2px(SignActivity.this, 120);
                                int piy = DisplayUtil.dp2px(SignActivity.this, 90);
                                int piW = getScreenWidth() - DisplayUtil.dp2px(SignActivity.this, 240);
                                int piH = getScreenHeight() - DisplayUtil.dp2px(SignActivity.this, 90);

                                Bitmap bitmap2 = FLUtil.cropBitmapCustom(bitmap,pix,piy,piW,piH,true);
                                Bitmap bitmap22 = bitmap2.copy(Bitmap.Config.ARGB_8888,true);

                                changeSignUI();
                                mDbView.setVisibility(View.VISIBLE);
                                mDtView.setVisibility(View.VISIBLE);
                                tbsReaderView.setVisibility(View.GONE);
                                //??????
                                SignOperationUtils.getInstance().DISABLE = true;
                                mDbView.setBackground(new BitmapDrawable(bitmap22));
                            }
                            bitmap.recycle();
                        }
                    } catch (Exception e) {
                        Log.e("whh0914", "?????????????????????" + e.toString());
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                        if (mImageReader != null) {
                            mImageReader.close();
                        }
                        if (virtualDisplay != null) {
                            virtualDisplay.release();
                        }
                        //???????????????????????????BufferQueueProducer: [ImageReader] dequeueBuffer: BufferQueue has been abandoned
                        mImageReader.setOnImageAvailableListener(null, null);
                        mediaProjection.stop();
                    }
                }
            }, 150);
        }else if(requestCode == EVENT_SCREENSHOTCOMMIT){
            super.onActivityResult(requestCode, resultCode, data);
            Log.e(TAG, "captureScreen...");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            Log.e(TAG, "displayMetrics width=" + width + ", height=" + height);
            ImageReader mImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay("screen-mirror", width, height,
                    displayMetrics.densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        image = mImageReader.acquireLatestImage();
                        if (image != null) {
                            final Image.Plane[] planes = image.getPlanes();
                            final ByteBuffer buffer = planes[0].getBuffer();
                            int width = image.getWidth();
                            int height = image.getHeight();
//                            Log.e("whh0914", "image width=" + width + ", height=" + height);
                            int pixelStride = planes[0].getPixelStride();
                            int rowStride = planes[0].getRowStride();
                            int rowPadding = rowStride - pixelStride * width;
                            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                            bitmap.copyPixelsFromBuffer(buffer);
                            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                            if (bitmap != null) {
                                // ?????????????????????
                                int pix = DisplayUtil.dp2px(SignActivity.this, 120);
                                int piy = DisplayUtil.dp2px(SignActivity.this, 90);
                                int piW = getScreenWidth() - DisplayUtil.dp2px(SignActivity.this, 240);
                                int piH = getScreenHeight() - DisplayUtil.dp2px(SignActivity.this, 90);

                                Bitmap bitmap2 = FLUtil.cropBitmapCustom(bitmap,pix,piy,piW,piH,true);

                                String filePath = saveImage22(bitmap2);

                                boolean ok = new File(filePath).exists();
                                Log.e("111","??????????????????==="+ok);

                                // ??????????????????????????????????????????
                                List<String> signFilePaths = null;
                                if (Hawk.contains(constant.SignFilePath) && ok) {
                                    signFilePaths = Hawk.get(constant.SignFilePath);
                                    if(signFilePaths!=null){
                                        signFilePaths.add(filePath);
                                        Hawk.put(constant.SignFilePath, signFilePaths);
                                    }
                                }

                                // ??????  ????????????????????????
                                if(!UserUtil.ISCHAIRMAN && filePath!=null && new File(filePath).exists()){
                                    String fileName = parseName(filePath);
                                    Thread sendThread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(500);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            //????????????????????????IP
                                            socketShareFileManager.SendFile(fileName, filePath, UserUtil.serverIP, constant.SHARE_PORT,"1");
                                        }
                                    });
                                    sendThread.start();
                                }
                            }
                            bitmap.recycle();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "?????????????????????" + e.toString());
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                        if (mImageReader != null) {
                            mImageReader.close();
                        }
                        if (virtualDisplay != null) {
                            virtualDisplay.release();
                        }
                        //???????????????????????????BufferQueueProducer: [ImageReader] dequeueBuffer: BufferQueue has been abandoned
                        mImageReader.setOnImageAvailableListener(null, null);
                        mediaProjection.stop();
                    }
                }
            }, 250);
        }
        //  ??????
        else if(requestCode == ACTIVITY_RESULT_CODE_SCREEN) {
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            startController(mediaProjection);
        }
    }

    /**
     * ??????????????????
     */
    public void startController(MediaProjection mediaProjection) {
//        ScreenVideoController screenVideoController = new ScreenVideoController(mMediaProjectionManage, resultCode, data);
        ScreenVideoController screenVideoController = new ScreenVideoController(mediaProjection,false);
        mStreamController = new StreamController(screenVideoController);
        TcpPacker packer = new TcpPacker();
        tcpSender = new WSSender();
//        tcpSender.setSenderListener(this);
        tcpSender.setMianCmd(SocketCmd.SocketCmd_ScreentData);
        tcpSender.setSendBody(constant.CVI_PAPER_SCREEN_DATA);
        mVideoConfiguration = new VideoConfiguration.Builder().setSize(1920, 1080).build();
        mStreamController.setVideoConfiguration(mVideoConfiguration);
        mStreamController.setPacker(packer);
        mStreamController.setSender(tcpSender);
        mStreamController.start();
        tcpSender.openConnect();
    }
    /**
     * @param view ?????????????????????view
     *             ?????????????????????????????????????????????????????????
     * @return ??????
     */
    private Bitmap getBitmap(View view) {
        View screenView = view == null ? getWindow().getDecorView() : view;
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();

        //????????????????????????
        Bitmap bitmap = screenView.getDrawingCache();
        return bitmap;
    }

    //???????????????????????????
    private void onSaveBitmap(Bitmap mBitmap, String flag) {
        //???Bitmap??????????????????????????????/sdcard/Boohee/??????????????????????????????????????????,???????????????????????????????????????????????????????????????
        File file = null;
        if (flag.equals("1")) {
            appDir = new File(Environment.getExternalStorageDirectory(), "???????????????");

            if (!appDir.exists()) {
                appDir.mkdir();
            }
            fileName = "qianpi.png";
            file = new File(appDir, fileName);
        } else {
            appDir = new File(Environment.getExternalStorageDirectory(), "comments");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            fileName = "screenshot.png";
            file = new File(appDir, fileName);
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Message ms = new Message();

            ms.obj = file.getPath();
            if (flag.equals("1")) {
                ms.what = 2;

            } else {
                ms.what = 1;
            }
            handler.sendMessage(ms);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));

    }


    private void initEvent() {
        //??????
        mVBottomBack.setOnClickListener(this);
        mIvWhiteBoardQuit.setOnClickListener(this);
        mIvWhiteBoardConfirm.setOnClickListener(this);
        //??????????????????
        mFabMenuSize.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.PEN_CLICK);
            }
        });
        mBtSizeLarge.setOnClickListener(this);
        mBtSizeMiddle.setOnClickListener(this);
        mBtSizeMini.setOnClickListener(this);
        //????????????????????????
        mFabMenuColor.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.COLOR_CLICK);
            }
        });
        mBtColorGreen.setOnClickListener(this);
        mBtColorPurple.setOnClickListener(this);
        mBtColorPink.setOnClickListener(this);
        mBtColorOrange.setOnClickListener(this);
        mBtColorBlack.setOnClickListener(this);
        //????????????
        mFabMenuText.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.TEXT_CLICK);
            }
        });
        mBtTextUnderline.setOnClickListener(this);
        mBtTextItalics.setOnClickListener(this);
        mBtTextBold.setOnClickListener(this);
        //?????????????????????
        mFabMenuEraser.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.ERASER_CLICK);
            }
        });
        mBtEraserLarge.setOnClickListener(this);
        mBtEraserMiddle.setOnClickListener(this);
        mBtEraserMini.setOnClickListener(this);

        mIvWhiteBoardUndo.setOnClickListener(this);
        mIvWhiteBoardRedo.setOnClickListener(this);
//        mIvWhiteBoardRedo.setVisibility(View.GONE);
//        mIvWhiteBoardUndo.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_white_board_quit://??????????????????
                afterEdit(false);
                break;
            case R.id.iv_white_board_confirm://??????????????????
                afterEdit(true);
                break;
            case R.id.iv_white_board_export://??????????????????????????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                StoreUtil.saveWhiteBoardPoints();
                break;
            case R.id.iv_white_board_save://?????????????????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                saveImage();
                break;
            case R.id.v_bottom_back://????????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                break;
            case R.id.bt_size_large://??????????????????-??????
                setPenSize(WhiteBoardVariable.PenSize.LARRGE);
                break;
            case R.id.bt_size_middle://??????????????????-??????
                setPenSize(WhiteBoardVariable.PenSize.MIDDLE);
                break;
            case R.id.bt_size_mini://??????????????????-??????
                setPenSize(WhiteBoardVariable.PenSize.MINI);
                break;

            case R.id.bt_color_green://????????????-??????
                setColor(WhiteBoardVariable.Color.GREEN);
                break;
            case R.id.bt_color_purple://????????????-??????
                setColor(WhiteBoardVariable.Color.PURPLE);
                break;
            case R.id.bt_color_pink://????????????-??????
                setColor(WhiteBoardVariable.Color.PINK);
                break;
            case R.id.bt_color_orange://????????????-??????
                setColor(WhiteBoardVariable.Color.ORANGE);
                break;
            case R.id.bt_color_black://????????????-??????
                setColor(WhiteBoardVariable.Color.BLACK);
                break;

            case R.id.bt_text_underline://??????????????????-?????????
                setTextStyle(WhiteBoardVariable.TextStyle.CHANGE_UNDERLINE);
                break;
            case R.id.bt_text_italics://??????????????????-??????
                setTextStyle(WhiteBoardVariable.TextStyle.CHANGE_ITALICS);
                break;
            case R.id.bt_text_bold://??????????????????-??????
                setTextStyle(WhiteBoardVariable.TextStyle.CHANGE_BOLD);
                break;

            case R.id.bt_eraser_large://?????????????????????-??????
                setEraserSize(WhiteBoardVariable.EraserSize.LARRGE);
                break;
            case R.id.bt_eraser_middle://?????????????????????-??????
                setEraserSize(WhiteBoardVariable.EraserSize.MIDDLE);
                break;
            case R.id.bt_eraser_mini://?????????????????????-??????
                setEraserSize(WhiteBoardVariable.EraserSize.MINI);
                break;

            case R.id.iv_white_board_undo://??????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (SignOperationUtils.getInstance().DISABLE) {
                    undo();
                }
                break;
            case R.id.iv_white_board_redo://??????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (SignOperationUtils.getInstance().DISABLE) {
                    redo();
                }
                break;
            case R.id.ll_white_board_pre:
            case R.id.iv_white_board_pre://?????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
//                prePage();
                break;
            case R.id.ll_white_board_next:
            case R.id.iv_white_board_next://?????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
//                nextPage();
                break;
            case R.id.iv_white_board_add://????????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
//                newPage();
                break;
            case R.id.iv_white_board_disable://??????/????????????
                break;
        }
    }

    /**
     * ??????????????????
     */
    private void setPenSize(int size) {
        SignOperationUtils.getInstance().mCurrentPenSize = size;
        changePenBack();
        mDbView.setPenSize();
    }

    /**
     * ?????????????????????????????????
     */
    private void changePenBack() {
        if (SignOperationUtils.getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.LARRGE) {
            mBtSizeLarge.drawCircleAndRing(WhiteBoardVariable.PenSize.LARRGE, SignOperationUtils.getInstance().mCurrentColor);
            mBtSizeMiddle.drawCircle(WhiteBoardVariable.PenSize.MIDDLE);
            mBtSizeMini.drawCircle(WhiteBoardVariable.PenSize.MINI);
        } else if (SignOperationUtils.getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.MIDDLE) {
            mBtSizeLarge.drawCircle(WhiteBoardVariable.PenSize.LARRGE);
            mBtSizeMiddle.drawCircleAndRing(WhiteBoardVariable.PenSize.MIDDLE, SignOperationUtils.getInstance().mCurrentColor);
            mBtSizeMini.drawCircle(WhiteBoardVariable.PenSize.MINI);
        } else if (SignOperationUtils.getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.MINI) {
            mBtSizeLarge.drawCircle(WhiteBoardVariable.PenSize.LARRGE);
            mBtSizeMiddle.drawCircle(WhiteBoardVariable.PenSize.MIDDLE);
            mBtSizeMini.drawCircleAndRing(WhiteBoardVariable.PenSize.MINI, SignOperationUtils.getInstance().mCurrentColor);

        }
    }

    /**
     * ????????????
     */
    private void setColor(int color) {
        SignOperationUtils.getInstance().mCurrentColor = color;
        changeColorBack();
        setPenSize(SignOperationUtils.getInstance().mCurrentPenSize);
        mDbView.setPenColor();
        mDtView.setTextColor();
    }

    /**
     * ??????????????????????????????
     */
    private void changeColorBack() {
        if (SignOperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.BLACK) {
            mFabMenuColor.setAddButtonBackground(R.drawable.sign_white_board_color_black_selector);
        } else if (SignOperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.ORANGE) {
            mFabMenuColor.setAddButtonBackground(R.drawable.sign_white_board_color_orange_selector);
        } else if (SignOperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.PINK) {
            mFabMenuColor.setAddButtonBackground(R.drawable.sign_white_board_color_pink_selector);
        } else if (SignOperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.PURPLE) {
            mFabMenuColor.setAddButtonBackground(R.drawable.sign_white_board_color_purple_selector);
        } else if (SignOperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.GREEN) {
            mFabMenuColor.setAddButtonBackground(R.drawable.sign_white_board_color_green_selector);
        }
    }

    /**
     * ??????????????????
     */
    private void setTextStyle(int textStyle) {
        mDtView.setTextStyle(textStyle);
        changeTextBack();
    }

    /**
     * ??????????????????????????????
     */
    private void changeTextBack() {
        int size = SignOperationUtils.getInstance().getSavePoints().size();
        if (size < 1) {
            return;
        }
        DrawPoint dp = SignOperationUtils.getInstance().getSavePoints().get(size - 1);
        if (dp.getType() != SignOperationUtils.DRAW_TEXT) {
            return;
        }
        if (dp.getDrawText().getIsUnderline()) {
            mBtTextUnderline.setBackgroundResource(R.drawable.white_board_text_underline_selected_selector);
        } else {
            mBtTextUnderline.setBackgroundResource(R.drawable.white_board_text_underline_selector);
        }

        if (dp.getDrawText().getIsItalics()) {
            mBtTextItalics.setBackgroundResource(R.drawable.white_board_text_italics_selected_selector);
        } else {
            mBtTextItalics.setBackgroundResource(R.drawable.white_board_text_italics_selector);
        }

        if (dp.getDrawText().getIsBold()) {
            mBtTextBold.setBackgroundResource(R.drawable.white_board_text_bold_selected_selector);
        } else {
            mBtTextBold.setBackgroundResource(R.drawable.white_board_text_bold_selector);
        }
    }

    /**
     * ?????????????????????
     */
    private void setEraserSize(int size) {
        SignOperationUtils.getInstance().mCurrentEraserSize = size;
        changeEraserBack();
        mDbView.setEraserSize();

    }

    /**
     * ?????????????????????????????????
     */
    private void changeEraserBack() {
        if (SignOperationUtils.getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.LARRGE) {
            mBtEraserLarge.drawCircleAndRing(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMiddle.drawCircle(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMini.drawCircle(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);
        } else if (SignOperationUtils.getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.MIDDLE) {
            mBtEraserLarge.drawCircle(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMiddle.drawCircleAndRing(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMini.drawCircle(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);
        } else if (SignOperationUtils.getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.MINI) {
            mBtEraserLarge.drawCircle(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMiddle.drawCircle(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMini.drawCircleAndRing(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);

        }
    }

    /**
     * ??????????????????
     */
    private void showPoints() {
//        Bitmap bitmap = null;
//        File file = new File(appDir, fileName);
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(file.getPath());
//            bitmap = BitmapFactory.decodeStream(fis);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        mDbView.setmBottomBitmap(bitmap);

        mDbView.showPoints();
        mDtView.showPoints();
        showUndoRedo();
    }

    /**
     * ??????
     */
    private void undo() {
        int size = SignOperationUtils.getInstance().getSavePoints().size();
        if (size == 0) {
            return;
        } else {
            SignOperationUtils.getInstance().getDeletePoints().add(SignOperationUtils.getInstance().getSavePoints().get(size - 1));
            SignOperationUtils.getInstance().getSavePoints().remove(size - 1);
            size = SignOperationUtils.getInstance().getDeletePoints().size();
            if (SignOperationUtils.getInstance().getDeletePoints().get(size - 1).getType() == SignOperationUtils.DRAW_PEN) {
                mDbView.undo();
            } else if (SignOperationUtils.getInstance().getDeletePoints().get(size - 1).getType() == SignOperationUtils.DRAW_TEXT) {
                mDtView.undo();
            }
            showUndoRedo();
        }

    }

    /**
     * ??????
     */
    private void redo() {
        int size = SignOperationUtils.getInstance().getDeletePoints().size();
        if (size == 0) {
            return;
        } else {
            SignOperationUtils.getInstance().getSavePoints().add(SignOperationUtils.getInstance().getDeletePoints().get(size - 1));
            SignOperationUtils.getInstance().getDeletePoints().remove(size - 1);
            size = SignOperationUtils.getInstance().getSavePoints().size();
            if (SignOperationUtils.getInstance().getSavePoints().get(size - 1).getType() == SignOperationUtils.DRAW_PEN) {
                mDbView.redo();
            } else if (SignOperationUtils.getInstance().getSavePoints().get(size - 1).getType() == SignOperationUtils.DRAW_TEXT) {
                mDtView.redo();
            }
            showUndoRedo();
        }

    }

    /**
     * ???????????????????????????
     * */
    private void sign_text_changeUI(boolean otherIsShow) {
        int vis = otherIsShow ? View.VISIBLE : View.GONE;
        rl_sign_tools.setVisibility(vis);
        ivBack.setVisibility(vis);
        sign_Door.setVisibility(vis);
        sign_commit.setVisibility(vis);
    }

    /**
     * ??????????????????
     */
    private void afterEdit(boolean isSave) {
//        mIvWhiteBoardBack.setVisibility(View.VISIBLE);
//        mIvWhiteBoardExport.setVisibility(View.VISIBLE);
//        mIvWhiteBoardSave.setVisibility(View.VISIBLE);
//        mRlBottom.setVisibility(View.VISIBLE);
//        mIvWhiteBoardDisable.setVisibility(View.VISIBLE);
////        mLayoutParams = (RelativeLayout.LayoutParams) mRlContent.getLayoutParams();
////        mLayoutParams.setMargins(OperationUtils.dip2px(24), 0, OperationUtils.dip2px(24), OperationUtils.dip2px(24));
////        mRlContent.setLayoutParams(mLayoutParams);
        sign_text_changeUI(true);
        mIvWhiteBoardQuit.setVisibility(View.GONE);
        mIvWhiteBoardConfirm.setVisibility(View.GONE);
        mDbView.showPoints();
        mDtView.afterEdit(isSave);
    }

    /**
     * ?????????????????????????????????
     */
    private void ToolsOperation(int currentOperation) {
        setPenOperation(currentOperation);
        setColorOperation(currentOperation);
        setTextOperation(currentOperation);
        setEraserOperation(currentOperation);
        showOutSideView();
    }

    /**
     * ????????????
     */
    private void showOutSideView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SignOperationUtils.getInstance().mCurrentOPerationPen == WhiteBoardVariable.Operation.PEN_EXPAND
                        || SignOperationUtils.getInstance().mCurrentOPerationColor == WhiteBoardVariable.Operation.COLOR_EXPAND
                        || SignOperationUtils.getInstance().mCurrentOPerationText == WhiteBoardVariable.Operation.TEXT_EXPAND
                        || SignOperationUtils.getInstance().mCurrentOPerationEraser == WhiteBoardVariable.Operation.ERASER_EXPAND) {
                    if(mVBottomBack!=null){
                        mVBottomBack.setVisibility(View.VISIBLE);
                    }
                } else {
                    if(mVBottomBack!=null){
                        mVBottomBack.setVisibility(View.GONE);
                    }
                }
            }
        }, 100);

    }

    /**
     * ?????????????????????????????????-??????
     */
    private void setPenOperation(int currentOperation) {
        switch (currentOperation) {
            case WhiteBoardVariable.Operation.PEN_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        SignOperationUtils.getInstance().mCurrentDrawType = SignOperationUtils.DRAW_PEN;
                        mDbView.setPaint(null);
                        mFabMenuSize.setAddButtonBackground(R.drawable.sign_white_board_pen_selected_selector);
                        SignOperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        mFabMenuSize.expand();
                        changePenBack();
                        SignOperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        mFabMenuSize.collapse();
                        SignOperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        mFabMenuSize.clearDraw();
                        mFabMenuSize.setAddButtonBackground(R.drawable.sign_white_board_pen_selected_selector);
                        SignOperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        mFabMenuSize.collapse();
                        mFabMenuSize.clearDraw();
                        mFabMenuSize.setAddButtonBackground(R.drawable.sign_white_board_pen_selected_selector);
                        SignOperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        mFabMenuSize.collapse();
                        SignOperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                }
                break;

        }

    }

    /**
     * ?????????????????????????????????-??????
     */
    private void setColorOperation(int currentOperation) {
        switch (currentOperation) {
            case WhiteBoardVariable.Operation.PEN_CLICK:
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationColor) {
                    case WhiteBoardVariable.Operation.COLOR_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.COLOR_EXPAND:
                        mFabMenuColor.collapse();
                        SignOperationUtils.getInstance().mCurrentOPerationColor = WhiteBoardVariable.Operation.COLOR_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationColor) {
                    case WhiteBoardVariable.Operation.COLOR_NORMAL:
                        mFabMenuColor.expand();
                        SignOperationUtils.getInstance().mCurrentOPerationColor = WhiteBoardVariable.Operation.COLOR_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.COLOR_EXPAND:
                        mFabMenuColor.collapse();
                        SignOperationUtils.getInstance().mCurrentOPerationColor = WhiteBoardVariable.Operation.COLOR_NORMAL;
                        break;
                }
                break;

        }

    }

    /**
     * ?????????????????????????????????-??????
     */
    private void setTextOperation(int currentOperation) {
        switch (currentOperation) {
            case WhiteBoardVariable.Operation.TEXT_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        SignOperationUtils.getInstance().mCurrentDrawType = SignOperationUtils.DRAW_TEXT;
                        mFabMenuText.setAddButtonBackground(R.drawable.sign_white_board_text_selected_selector);
                        SignOperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        int size = SignOperationUtils.getInstance().getSavePoints().size();
                        if (size > 0) {
                            DrawPoint dp = SignOperationUtils.getInstance().getSavePoints().get(size - 1);
                            if (dp.getType() == SignOperationUtils.DRAW_TEXT && dp.getDrawText().getStatus() == SignDrawTextView.TEXT_DETAIL) {
                                changeTextBack();
                                mFabMenuText.expand();
                                SignOperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_EXPAND;
                            }
                        }
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        mFabMenuText.collapse();
                        SignOperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.PEN_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        mFabMenuText.clearDraw();
                        mFabMenuText.setAddButtonBackground(R.drawable.sign_white_board_text_selector);
                        SignOperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        mFabMenuText.collapse();
                        mFabMenuText.clearDraw();
                        mFabMenuText.setAddButtonBackground(R.drawable.sign_white_board_text_selector);
                        SignOperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        mFabMenuText.collapse();
                        SignOperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                }
                break;

        }

    }

    /**
     * ?????????????????????????????????-?????????
     */
    private void setEraserOperation(int currentOperation) {
        switch (currentOperation) {
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        SignOperationUtils.getInstance().mCurrentDrawType = SignOperationUtils.DRAW_ERASER;
                        mDbView.changeEraser();
                        mFabMenuEraser.setAddButtonBackground(R.drawable.sign_white_board_eraser_selected_selector);
                        SignOperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        mFabMenuEraser.expand();
                        changeEraserBack();
                        SignOperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        mFabMenuEraser.collapse();
                        SignOperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.PEN_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        mFabMenuEraser.clearDraw();
                        mFabMenuEraser.setAddButtonBackground(R.drawable.sign_white_board_eraser_selector);
                        SignOperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        mFabMenuEraser.collapse();
                        mFabMenuEraser.clearDraw();
                        mFabMenuEraser.setAddButtonBackground(R.drawable.sign_white_board_eraser_selector);
                        SignOperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (SignOperationUtils.getInstance().mCurrentOPerationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        mFabMenuEraser.collapse();
                        SignOperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                }
                break;

        }

    }

    /**
     * ???????????????????????????
     */
    public String saveImage() {
        String fileName = StoreUtil.getPersonalSignPhotoSavePath();
        File file = new File(fileName);
        try {
            File directory = file.getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                showMessage(getString(R.string.white_board_export_fail));
                return null;
            }
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            mDbView.setDrawingCacheEnabled(true);
            mDbView.buildDrawingCache();
            Bitmap bitmap = mDbView.getDrawingCache();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            mDbView.destroyDrawingCache();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);//???????????????????????????????????????
            String title = getString(R.string.white_board_export_tip)+ StoreUtil.getPersonalSignPhotoPath();
            if(title.contains("/storage/emulated/0")){
                title = title.replace("/storage/emulated/0"," ");
            }
            CVIPaperDialogUtils.showConfirmDialog(SignActivity.this, title, "?????????", false, null);
            return fileName;
        } catch (Exception e) {
            showMessage(getString(R.string.white_board_export_fail));
            e.printStackTrace();
            return null;
        }
    }
    /**
     * ???????????????????????????
     */
    public String saveImage22(Bitmap bitmap) {
        String fileName = StoreUtil.getPersonalSignPhotoSavePath();
        File file = new File(fileName);
        try {
            File directory = file.getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                showMessage(getString(R.string.white_board_export_fail));
                return null;
            }
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();


            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);//???????????????????????????????????????
            String title = getString(R.string.white_board_export_tip)+ StoreUtil.getPersonalSignPhotoPath();
            if(title.contains("/storage/emulated/0")){
                title = title.replace("/storage/emulated/0"," ");
            }
            CVIPaperDialogUtils.showConfirmDialog(SignActivity.this, title, "?????????", false, null);
            return fileName;
        } catch (Exception e) {
            showMessage(getString(R.string.white_board_export_fail));
            e.printStackTrace();
            return null;
        }
    }

    @ReceiveEvents(name = Events.WHITE_BOARD_TEXT_EDIT)
    private void textEdit() {//????????????
        // ???????????????
        sign_text_changeUI(false);
        mIvWhiteBoardQuit.setVisibility(View.VISIBLE);
        mIvWhiteBoardConfirm.setVisibility(View.VISIBLE);
        Log.e("xxx","textEdit() {//????????????");
    }

    @ReceiveEvents(name = Events.WHITE_BOARD_UNDO_REDO)
    private void showUndoRedo() {//?????????????????????????????????
        if (SignOperationUtils.getInstance().getSavePoints().isEmpty()) {
            mIvWhiteBoardUndo.setVisibility(View.INVISIBLE);
        } else {
            mIvWhiteBoardUndo.setVisibility(View.VISIBLE);
        }
        if (SignOperationUtils.getInstance().getDeletePoints().isEmpty()) {
            mIvWhiteBoardRedo.setVisibility(View.INVISIBLE);
        } else {
            mIvWhiteBoardRedo.setVisibility(View.VISIBLE);
        }
    }
}
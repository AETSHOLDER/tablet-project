package com.example.paperlessmeeting_demo.activity.Sign;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.sharefile.SocketShareFileManager;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.StoreUtil;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
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
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.sign_door)
    TextView sign_Door;
    @BindView(R.id.sign_commit)
    TextView sign_commit;
    @BindView(R.id.tools_bar)
    RelativeLayout toolsBar;
    @BindView(R.id.sign_title)
    TextView signTitle;
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

    private String url;
    TbsReaderView tbsReaderView;
    //是否打开文件
    private boolean isOpenFile;
    private Drawable drawable_left_enter;
    private Drawable drawable_left_exit;
    private File appDir;
    private String fileName;
//    private Bitmap mBottomBitmap = null;
    private SocketShareFileManager socketShareFileManager;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int port = (int)msg.obj;
                    Log.e("","port===="+port);
                    break;
                case 2:
                     String ss = (String)msg.obj;
                     Log.e("正在接收文件",""+ss);
                    Toast.makeText(SignActivity.this, "正在接收文件", Toast.LENGTH_SHORT).show();
                    break;
                case 4:

                    break;
                case 5:
                    Toast.makeText(SignActivity.this, "文件分享成功", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(SignActivity.this, "文件分享失败", Toast.LENGTH_SHORT).show();
                    String df = msg.obj.toString();
                    Log.d("文件分享失败++", df);
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
        Rect bonds = new Rect(0, 0, 38, 36);
        drawable_left_enter = SignActivity.this.getResources().getDrawable(R.mipmap.sign_enter);
        drawable_left_enter.setBounds(bonds);
        drawable_left_exit = SignActivity.this.getResources().getDrawable(R.mipmap.sign_exit);
        drawable_left_exit.setBounds(bonds);

        Drawable drawable_commit = SignActivity.this.getResources().getDrawable(R.mipmap.sign_commit);
        drawable_commit.setBounds(bonds);

        sign_Door.setCompoundDrawables(drawable_left_enter, null, null, null);
        sign_Door.setGravity(Gravity.CENTER);
        sign_commit.setCompoundDrawables(drawable_commit, null, null, null);
        sign_commit.setGravity(Gravity.CENTER);
        url = getIntent().getStringExtra("url");
        Log.d("SignActivity", url);
        isOpenFile = getIntent().getBooleanExtra("isOpenFile", false);
        tbsReaderView = new TbsReaderView(this, readerCallback);
        //打开文件
        if (!TextUtils.isEmpty(url) && isOpenFile) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, R.id.tools_bar);
            params.setMargins(160, 0, 160, 0);
            rlRoot.addView(tbsReaderView, params);
            openFile();
        }
        // 下面两个顺序不能颠倒
        mVBottomBack.bringToFront();
        rl_sign_tools.bringToFront();

        // 白板模块
        changePenBack();
        changeColorBack();
        changeEraserBack();
        ToolsOperation(WhiteBoardVariable.Operation.PEN_NORMAL);
    }

    @Override
    protected void initData() {
        socketShareFileManager = new SocketShareFileManager(mHandler);
        SignOperationUtils.getInstance().DISABLE = true;
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
                int size = SignOperationUtils.getInstance().getSavePoints().size();
                if(size>0) {
                    CVIPaperDialogUtils.showCustomDialog(SignActivity.this, "当前存在签批内容,是否退出?", null, "退出", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
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
                    changeSignUI();
                    //签批
                    SignOperationUtils.getInstance().DISABLE = false;
                    Bitmap bitmap = getBitmap(tbsReaderView);
                    mDbView.setmBottomBitmap(bitmap);
                    onSaveBitmap(bitmap, "2");
                }else {
                    int size = SignOperationUtils.getInstance().getSavePoints().size();
                    if(size>0){
                        CVIPaperDialogUtils.showCustomDialog(SignActivity.this, "是否舍弃签批内容,退出签批模式?", null, "退出", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                            @Override
                            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                if (clickConfirm){
                                    SignOperationUtils.getInstance().DISABLE = true;
                                    changeSignUI();
                                    mDbView.clearImage();
                                    mDbView.setVisibility(View.GONE);
                                    mDtView.setVisibility(View.GONE);
                                    tbsReaderView.setVisibility(View.VISIBLE);
                                }else {
                                    // 点击取消，返回原状态
                                    sign_Door.setSelected(!sign_Door.isSelected());
                                }
                            }
                        });
                    }else {
                        SignOperationUtils.getInstance().DISABLE = true;
                        changeSignUI();
                        mDbView.clearImage();
                        mDbView.setVisibility(View.GONE);
                        mDtView.setVisibility(View.GONE);
                        tbsReaderView.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        // 提交签批
        sign_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 保存，普通用户将上传文档
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                String filePath = saveImage();
                // 上传  非主席发送给主席
                if(!UserUtil.ISCHAIRMAN && filePath!=null && new File(filePath).exists()){
                    String fileName = parseName(filePath);
                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //将文件发送到指定IP
                            socketShareFileManager.SendFile(fileName, filePath, UserUtil.serverIP, constant.SHARE_PORT,"2");
                        }
                    });
                    sendThread.start();
                }
            }
        });
        sign_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignActivity.this, SignListActivity.class);
                startActivity(intent);
            }
        });

    }
    /**
     * 签批按钮点击的UI更改
     * */
    private void changeSignUI() {
        String text = sign_Door.isSelected() ? "退出批注" : "开始批注";
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
            Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
        }
        Bundle bundle = new Bundle();
        bundle.putString("filePath", url);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath() + "/TbsTempPath");
        Log.d("requestCodeUrnnn", Environment.getExternalStorageDirectory().getPath());
        boolean result = tbsReaderView.preOpen(parseFormat(parseName(url)), false);
        if (result) {
            tbsReaderView.openFile(bundle);
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
        handler.removeMessages(1001);
        handler = null;
        tbsReaderView.onStop();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    /**
     * @param view 需要截取图片的view
     *             传入线性或相对布局就截取里面的所有内容
     * @return 截图
     */
    private Bitmap getBitmap(View view) {
        View screenView = view == null ? getWindow().getDecorView() : view;
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();

        //获取屏幕整张图片
        Bitmap bitmap = screenView.getDrawingCache();
        return bitmap;
    }

    //保存图片到系统图库
    private void onSaveBitmap(Bitmap mBitmap, String flag) {
        //将Bitmap保存图片到指定的路径/sdcard/Boohee/下，文件名以当前系统时间命名,但是这种方法保存的图片没有加入到系统图库中
        File file = null;
        if (flag.equals("1")) {
            appDir = new File(Environment.getExternalStorageDirectory(), "无纸化会议");

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
        //头部
        mVBottomBack.setOnClickListener(this);
        mIvWhiteBoardQuit.setOnClickListener(this);
        mIvWhiteBoardConfirm.setOnClickListener(this);
        //画笔尺寸大小
        mFabMenuSize.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.PEN_CLICK);
            }
        });
        mBtSizeLarge.setOnClickListener(this);
        mBtSizeMiddle.setOnClickListener(this);
        mBtSizeMini.setOnClickListener(this);
        //画笔或者文字颜色
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
        //文字样式
        mFabMenuText.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.TEXT_CLICK);
            }
        });
        mBtTextUnderline.setOnClickListener(this);
        mBtTextItalics.setOnClickListener(this);
        mBtTextBold.setOnClickListener(this);
        //橡皮擦尺寸大小
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
        mIvWhiteBoardRedo.setVisibility(View.GONE);
        mIvWhiteBoardUndo.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_white_board_quit://退出文字编辑
                afterEdit(false);
                break;
            case R.id.iv_white_board_confirm://保存文字编辑
                afterEdit(true);
                break;
            case R.id.iv_white_board_export://保存白板操作集到本地
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                StoreUtil.saveWhiteBoardPoints();
                break;
            case R.id.iv_white_board_save://保存白板为图片
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                saveImage();
                break;
            case R.id.v_bottom_back://点击挡板
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                break;
            case R.id.bt_size_large://设置画笔尺寸-大号
                setPenSize(WhiteBoardVariable.PenSize.LARRGE);
                break;
            case R.id.bt_size_middle://设置画笔尺寸-中号
                setPenSize(WhiteBoardVariable.PenSize.MIDDLE);
                break;
            case R.id.bt_size_mini://设置画笔尺寸-小号
                setPenSize(WhiteBoardVariable.PenSize.MINI);
                break;

            case R.id.bt_color_green://设置颜色-绿色
                setColor(WhiteBoardVariable.Color.GREEN);
                break;
            case R.id.bt_color_purple://设置颜色-紫色
                setColor(WhiteBoardVariable.Color.PURPLE);
                break;
            case R.id.bt_color_pink://设置颜色-粉色
                setColor(WhiteBoardVariable.Color.PINK);
                break;
            case R.id.bt_color_orange://设置颜色-橙色
                setColor(WhiteBoardVariable.Color.ORANGE);
                break;
            case R.id.bt_color_black://设置颜色-黑色
                setColor(WhiteBoardVariable.Color.BLACK);
                break;

            case R.id.bt_text_underline://设置文字样式-下划线
                setTextStyle(WhiteBoardVariable.TextStyle.CHANGE_UNDERLINE);
                break;
            case R.id.bt_text_italics://设置文字样式-斜体
                setTextStyle(WhiteBoardVariable.TextStyle.CHANGE_ITALICS);
                break;
            case R.id.bt_text_bold://设置文字样式-粗体
                setTextStyle(WhiteBoardVariable.TextStyle.CHANGE_BOLD);
                break;

            case R.id.bt_eraser_large://设置橡皮擦尺寸-大号
                setEraserSize(WhiteBoardVariable.EraserSize.LARRGE);
                break;
            case R.id.bt_eraser_middle://设置橡皮擦尺寸-中号
                setEraserSize(WhiteBoardVariable.EraserSize.MIDDLE);
                break;
            case R.id.bt_eraser_mini://设置橡皮擦尺寸-小号
                setEraserSize(WhiteBoardVariable.EraserSize.MINI);
                break;

            case R.id.iv_white_board_undo://撤销
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (SignOperationUtils.getInstance().DISABLE) {
                    undo();
                }
                break;
            case R.id.iv_white_board_redo://重做
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (SignOperationUtils.getInstance().DISABLE) {
                    redo();
                }
                break;
            case R.id.ll_white_board_pre:
            case R.id.iv_white_board_pre://上一页
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
//                prePage();
                break;
            case R.id.ll_white_board_next:
            case R.id.iv_white_board_next://下一页
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
//                nextPage();
                break;
            case R.id.iv_white_board_add://新建白板
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
//                newPage();
                break;
            case R.id.iv_white_board_disable://禁止/解禁按钮
                break;
        }
    }

    /**
     * 设置画笔尺寸
     */
    private void setPenSize(int size) {
        SignOperationUtils.getInstance().mCurrentPenSize = size;
        changePenBack();
        mDbView.setPenSize();
    }

    /**
     * 切换画笔尺寸按按钮背景
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
     * 设置颜色
     */
    private void setColor(int color) {
        SignOperationUtils.getInstance().mCurrentColor = color;
        changeColorBack();
        setPenSize(SignOperationUtils.getInstance().mCurrentPenSize);
        mDbView.setPenColor();
        mDtView.setTextColor();
    }

    /**
     * 切换颜色控制按钮背景
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
     * 设置文字风格
     */
    private void setTextStyle(int textStyle) {
        mDtView.setTextStyle(textStyle);
        changeTextBack();
    }

    /**
     * 切换文字相关按钮背景
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
     * 设置橡皮擦尺寸
     */
    private void setEraserSize(int size) {
        SignOperationUtils.getInstance().mCurrentEraserSize = size;
        changeEraserBack();
        mDbView.setEraserSize();

    }

    /**
     * 切换橡皮擦尺寸按钮背景
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
     * 重新显示白板
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
     * 撤销
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
     * 重做
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
     * 画笔工具栏显示隐藏
     * */
    private void sign_text_changeUI(boolean otherIsShow) {
        int vis = otherIsShow ? View.VISIBLE : View.GONE;
        rl_sign_tools.setVisibility(vis);
        ivBack.setVisibility(vis);
        sign_Door.setVisibility(vis);
        sign_commit.setVisibility(vis);
    }

    /**
     * 文字编辑之后
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
//        if(mBottomBitmap.isRecycled()){
//            File file = new File(appDir, fileName);
//            FileInputStream fis = null;
//            try {
//                fis = new FileInputStream(file.getPath());
//                mBottomBitmap = BitmapFactory.decodeStream(fis);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
        mDbView.showPoints();
        mDtView.afterEdit(isSave);
    }

    /**
     * 白板工具栏点击切换操作
     */
    private void ToolsOperation(int currentOperation) {
        setPenOperation(currentOperation);
        setColorOperation(currentOperation);
        setTextOperation(currentOperation);
        setEraserOperation(currentOperation);
        showOutSideView();
    }

    /**
     * 显示挡板
     */
    private void showOutSideView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SignOperationUtils.getInstance().mCurrentOPerationPen == WhiteBoardVariable.Operation.PEN_EXPAND
                        || SignOperationUtils.getInstance().mCurrentOPerationColor == WhiteBoardVariable.Operation.COLOR_EXPAND
                        || SignOperationUtils.getInstance().mCurrentOPerationText == WhiteBoardVariable.Operation.TEXT_EXPAND
                        || SignOperationUtils.getInstance().mCurrentOPerationEraser == WhiteBoardVariable.Operation.ERASER_EXPAND) {
                    mVBottomBack.setVisibility(View.VISIBLE);
                } else {
                    mVBottomBack.setVisibility(View.GONE);
                }
            }
        }, 100);

    }

    /**
     * 白板工具栏点击切换操作-画笔
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
     * 白板工具栏点击切换操作-颜色
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
     * 白板工具栏点击切换操作-文字
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
     * 白板工具栏点击切换操作-橡皮擦
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
     * 保存当前白板为图片
     */
    public String saveImage() {
        String fileName = StoreUtil.getPersonalSignPhotoSavePath();
        Log.e("saveImage", fileName);
        File file = new File(fileName);
        try {
            File directory = file.getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                showMessage(getString(R.string.white_board_export_fail));
                return null;
            }
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            mFlView.setDrawingCacheEnabled(true);
            mFlView.buildDrawingCache();
            Bitmap bitmap = mFlView.getDrawingCache();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            mFlView.destroyDrawingCache();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);//这个广播的目的就是更新图库

            showMessage(getString(R.string.white_board_export_tip) + StoreUtil.getPersonalSignPhotoPath());
            return fileName;
        } catch (Exception e) {
            showMessage(getString(R.string.white_board_export_fail));
            e.printStackTrace();
            return null;
        }
    }

    @ReceiveEvents(name = Events.WHITE_BOARD_TEXT_EDIT)
    private void textEdit() {//文字编辑
        // 隐藏工具栏
        sign_text_changeUI(false);
        mIvWhiteBoardQuit.setVisibility(View.VISIBLE);
        mIvWhiteBoardConfirm.setVisibility(View.VISIBLE);
        Log.e("xxx","textEdit() {//文字编辑");
    }

    // TODO 撤销 重做 有问题待解决
    @ReceiveEvents(name = Events.WHITE_BOARD_UNDO_REDO)
    private void showUndoRedo() {//是否显示撤销、重装按钮
//        if (SignOperationUtils.getInstance().getSavePoints().isEmpty()) {
//            mIvWhiteBoardUndo.setVisibility(View.INVISIBLE);
////            mIvWhiteBoardExport.setVisibility(View.INVISIBLE);
////            mIvWhiteBoardSave.setVisibility(View.INVISIBLE);
//        } else {
//            mIvWhiteBoardUndo.setVisibility(View.VISIBLE);
////            mIvWhiteBoardExport.setVisibility(View.VISIBLE);
////            mIvWhiteBoardSave.setVisibility(View.VISIBLE);
//        }
//        if (SignOperationUtils.getInstance().getDeletePoints().isEmpty()) {
//            mIvWhiteBoardRedo.setVisibility(View.INVISIBLE);
//        } else {
//            mIvWhiteBoardRedo.setVisibility(View.VISIBLE);
//        }
    }
}
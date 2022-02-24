package com.example.paperlessmeeting_demo.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.tool.FileUtil;
import com.example.paperlessmeeting_demo.tool.StoreUtil;
import com.example.paperlessmeeting_demo.widgets.FloatingActionsMenu;
import com.example.paperlessmeeting_demo.widgets.FloatingImageButton;
import com.github.guanpy.library.ann.ReceiveEvents;
import com.github.guanpy.wblib.bean.DrawPoint;
import com.github.guanpy.wblib.bean.WhiteBoardPoints;
import com.github.guanpy.wblib.utils.Events;
import com.github.guanpy.wblib.utils.OperationUtils;
import com.github.guanpy.wblib.utils.WhiteBoardVariable;
import com.github.guanpy.wblib.widget.DrawPenView;
import com.github.guanpy.wblib.widget.DrawTextLayout;
import com.github.guanpy.wblib.widget.DrawTextView;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import butterknife.BindView;

import static com.github.guanpy.wblib.utils.OperationUtils.getInstance;

/**
 * 白板工具
 * Created by gpy on 2015/6/2.
 */

public class WhiteBoardServies extends Service implements View.OnClickListener {
    /*  @BindView(R.id.iv_white_board_book)
      ImageView mIvWhiteBoardBook;*/
//    @BindView(R.id.iv_white_board_back)
//    ImageView mIvWhiteBoardBack;
    @BindView(R.id.iv_white_board_quit)
    ImageView mIvWhiteBoardQuit;
    /*   @BindView(R.id.tv_white_board_head)
       TextView mTvWhiteBoardHead;*/
    @BindView(R.id.iv_white_board_confirm)
    ImageView mIvWhiteBoardConfirm;
    // @BindView(R.id.iv_white_board_export)
    ImageView mIvWhiteBoardExport;
    //  @BindView(R.id.iv_white_board_save)
    ImageView mIvWhiteBoardSave;
    //  @BindView(R.id.rl_head)
    RelativeLayout mRlHead;
    //    @BindView(R.id.db_view)
    DrawPenView mDbView;
    // @BindView(R.id.dt_view)
    DrawTextLayout mDtView;
    //  @BindView(R.id.fl_view)
    FrameLayout mFlView;
    //  @BindView(R.id.sv_content)
    ScrollView svContent;
    //@BindView(R.id.v_bottom_back)
    View mVBottomBack;
    //  @BindView(R.id.bt_size_large)
    FloatingImageButton mBtSizeLarge;
    //  @BindView(R.id.bt_size_middle)
    FloatingImageButton mBtSizeMiddle;
    //@BindView(R.id.bt_size_mini)
    FloatingImageButton mBtSizeMini;
    // @BindView(R.id.fab_menu_size)
    FloatingActionsMenu mFabMenuSize;
    // @BindView(R.id.bt_color_green)
    FloatingImageButton mBtColorGreen;
    // @BindView(R.id.bt_color_purple)
    FloatingImageButton mBtColorPurple;
    // @BindView(R.id.bt_color_pink)
    FloatingImageButton mBtColorPink;
    // @BindView(R.id.bt_color_orange)
    FloatingImageButton mBtColorOrange;
    // @BindView(R.id.bt_color_black)
    FloatingImageButton mBtColorBlack;
    // @BindView(R.id.fab_menu_color)
    FloatingActionsMenu mFabMenuColor;
    //@BindView(R.id.bt_text_underline)
    FloatingImageButton mBtTextUnderline;
    //    @BindView(R.id.bt_text_italics)
    FloatingImageButton mBtTextItalics;
    //  @BindView(R.id.bt_text_bold)
    FloatingImageButton mBtTextBold;
    // @BindView(R.id.fab_menu_text)
    FloatingActionsMenu mFabMenuText;
    //  @BindView(R.id.bt_eraser_large)
    FloatingImageButton mBtEraserLarge;
    // @BindView(R.id.bt_eraser_middle)
    FloatingImageButton mBtEraserMiddle;
    //  @BindView(R.id.bt_eraser_mini)
    FloatingImageButton mBtEraserMini;
    //@BindView(R.id.fab_menu_eraser)
    FloatingActionsMenu mFabMenuEraser;
    //  @BindView(R.id.iv_white_board_undo)
    ImageView mIvWhiteBoardUndo;
    //    @BindView(R.id.iv_white_board_redo)
    ImageView mIvWhiteBoardRedo;
    //  @BindView(R.id.iv_white_board_pre)
    ImageView mIvWhiteBoardPre;
    //  @BindView(R.id.ll_white_board_pre)
    LinearLayout mLlWhiteBoardPre;
    // @BindView(R.id.tv_white_board_page)
    TextView mTvWhiteBoardPage;
    //  @BindView(R.id.iv_white_board_next)
    ImageView mIvWhiteBoardNext;
    //  @BindView(R.id.ll_white_board_next)
    LinearLayout mLlWhiteBoardNext;
    // @BindView(R.id.ll_white_board_page)
    LinearLayout mLlWhiteBoardPage;
    //@BindView(R.id.iv_white_board_add)
    ImageView mIvWhiteBoardAdd;
    // @BindView(R.id.rl_bottom)
    RelativeLayout mRlBottom;
    //  @BindView(R.id.iv_white_board_disable)
    ImageView mIvWhiteBoardDisable;
    // @BindView(R.id.rl_content)
    RelativeLayout mRlContent;
    /*    @BindView(R.id.min_image)
        ImageView minImage;
        @BindView(R.id.close_image)
        ImageView closeImage;
        @BindView(R.id.max_image)
        ImageView maxImage;
        @BindView(R.id.exit_image)
        ImageView exitImage;*/
    private MyReceiver myReceiver;
    private MyReceiver myRedoReceiver;
    ArrayList<String> filenames;
    ArrayList<String> filepaths;
    private ImageView board_share;
    AlertDialog.Builder inputDialog;
    AlertDialog alertInputDialog;
    View dialogView;
    public String targetip = "192.168.0.100";
    private ServerSocket mServerSocket;
    private Socket mSocket;
    private Socket mSocketConnect;

    private StringBuffer sb = new StringBuffer();
    private boolean isConnected = false;
    final String sendUedoMsg = "sendUedoMsg";
    final String sendRedoMsg = "sendRedoMsg";
    private boolean isfirstinit = true;
    private SocketConnectThread socketConnectThread;
    private OutputStream mOutStream;
    private InputStream mInStream;
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Bundle data = msg.getData();
                String jsonStr = data.getString("msg");
//                sb.append(jsonStr);
                //  收到消息，复现单个操作步骤
                if (!TextUtils.isEmpty(jsonStr)) {
                    DrawPoint whiteBoardPoint = new Gson().fromJson(jsonStr, DrawPoint.class);
                    //  将单个步骤进行存储，否则撤销操作无法进行
                    getInstance().getSavePoints().add(whiteBoardPoint);
//                    StoreUtil.readSingleDrawPoint(whiteBoardPoint);
                    showSinglePoint(whiteBoardPoint);
                }

                // 禁止操作

                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                OperationUtils.getInstance().DISABLE = false;
                //    mLlWhiteBoardSaveTools.setVisibility(View.INVISIBLE);
                mIvWhiteBoardDisable.setClickable(false);
                mIvWhiteBoardDisable.setImageResource(R.drawable.white_board_undisable_selector);
                mRlBottom.setVisibility(View.GONE);
                //   mIvWhiteBoardShare.setVisibility(View.INVISIBLE);
                board_share.setVisibility(View.INVISIBLE);
            } else if (msg.what == 2) {
                redo();
            } else if (msg.what == 3) {
                undo();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.tv_white_board_head:

                Intent intent = new Intent();
                //设置intent的动作为com.example.broadcast，可以任意定义
                intent.setAction(constant.HIDE_WHITEBOARD_BROADCAST);
                //发送无序广播
                sendBroadcast(intent);
                break;*/
         /*   case R.id.iv_white_board_back://返回键
                onBackPressed();
                break;*/
            case R.id.iv_white_board_quit://退出文字编辑
                afterEdit(false);
                break;
            case R.id.iv_white_board_confirm://保存文字编辑
                afterEdit(true);
                break;

            case R.id.iv_white_board_export://保存白板操作集到本地
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                StoreUtil.saveWhiteBoardPoints();//---------------
                break;
            case R.id.iv_white_board_save://保存白板为图片
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                saveImage();//----------
                break;
            case R.id.v_bottom_back://点击挡板
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//---------
                break;
            case R.id.bt_size_large://设置画笔尺寸-大号
                setPenSize(WhiteBoardVariable.PenSize.LARRGE);//--------
                break;
            case R.id.bt_size_middle://设置画笔尺寸-中号
                setPenSize(WhiteBoardVariable.PenSize.MIDDLE);//---------
                break;
            case R.id.bt_size_mini://设置画笔尺寸-小号
                setPenSize(WhiteBoardVariable.PenSize.MINI);//-----
                break;

            case R.id.bt_color_green://设置颜色-绿色
                setColor(WhiteBoardVariable.Color.GREEN);//------------
                break;
            case R.id.bt_color_purple://设置颜色-紫色
                setColor(WhiteBoardVariable.Color.PURPLE);//-----
                break;
            case R.id.bt_color_pink://设置颜色-粉色
                setColor(WhiteBoardVariable.Color.PINK);//-------------
                break;
            case R.id.bt_color_orange://设置颜色-橙色
                setColor(WhiteBoardVariable.Color.ORANGE);//--------
                break;
            case R.id.bt_color_black://设置颜色-黑色
                setColor(WhiteBoardVariable.Color.BLACK);//-----
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
                setEraserSize(WhiteBoardVariable.EraserSize.LARRGE);//----
                break;
            case R.id.bt_eraser_middle://设置橡皮擦尺寸-中号
                setEraserSize(WhiteBoardVariable.EraserSize.MIDDLE);//------
                break;
            case R.id.bt_eraser_mini://设置橡皮擦尺寸-小号
                setEraserSize(WhiteBoardVariable.EraserSize.MINI);//------
                break;

            case R.id.iv_white_board_undo://撤销
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//----
                if (OperationUtils.getInstance().DISABLE) {
                    undo();
                }
                break;
            case R.id.iv_white_board_redo://重做
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//-----
                if (OperationUtils.getInstance().DISABLE) {
                    redo();
                }
                break;
            case R.id.ll_white_board_pre:
            case R.id.iv_white_board_pre://上一页
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//----
                prePage();
                break;
            case R.id.ll_white_board_next:
            case R.id.iv_white_board_next://下一页
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//----
                nextPage();
                break;
            case R.id.iv_white_board_add://新建白板
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//----
                newPage();
                break;
            case R.id.iv_white_board_disable://禁止/解禁按钮
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (OperationUtils.getInstance().DISABLE) {
                    OperationUtils.getInstance().DISABLE = false;
                    mIvWhiteBoardDisable.setImageResource(R.drawable.white_board_undisable_selector);
                    mRlBottom.setVisibility(View.GONE);
                } else {
                    OperationUtils.getInstance().DISABLE = true;
                    mIvWhiteBoardDisable.setImageResource(R.drawable.white_board_disable_selector);
                    mRlBottom.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View displayView;

    private int[] images;
    private int imageIndex = 0;

    private Handler changeImageHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("gfjjfghjfg", "d fsdg路过！！！2222~");

        try {
            mServerSocket = new ServerSocket(1989);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //启动服务线程
        SocketAcceptThread socketAcceptThread = new SocketAcceptThread();
        socketAcceptThread.start();


        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.SHOW_WHITEBOARD_BROADCAST);
        registerReceiver(myReceiver, filter);

        myRedoReceiver = new MyReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(constant.SHOW_UI);
        registerReceiver(myRedoReceiver, filter1);

        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 930;
        layoutParams.height = 1000;
        layoutParams.x = 100;
        layoutParams.y = 300;
        changeImageHandler = new Handler(this.getMainLooper(), changeImageCallback);

    }

    @Override
    public void onDestroy() {
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        if (myRedoReceiver != null) {
            unregisterReceiver(myRedoReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        showFloatingWindow();
        return new MyBinder();
    }

    private void showFloatingWindow() {
        dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialo_inputadreass, null);
        Log.d("gfjjfghjfg", "d fsdg路过！！！~");
        if (Settings.canDrawOverlays(this)) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            displayView = layoutInflater.inflate(R.layout.activity_white_board, null);
            TextView mTvWhiteBoardHead = displayView.findViewById(R.id.tv_white_board_head);
            ImageView minIma = displayView.findViewById(R.id.min_image);
            ImageView closeIma = displayView.findViewById(R.id.close_image);
            final ImageView maxIma = displayView.findViewById(R.id.max_image);
            final ImageView exitIma = displayView.findViewById(R.id.exit_image);
            mBtSizeLarge = displayView.findViewById(R.id.bt_size_large);
            mBtSizeMiddle = displayView.findViewById(R.id.bt_size_middle);
            mBtSizeMini = displayView.findViewById(R.id.bt_size_mini);
            mIvWhiteBoardExport = displayView.findViewById(R.id.iv_white_board_export);
            mIvWhiteBoardSave = displayView.findViewById(R.id.iv_white_board_save);
            mRlHead = displayView.findViewById(R.id.rl_head);
            mDbView = displayView.findViewById(R.id.db_view);
            mDtView = displayView.findViewById(R.id.dt_view);
            mFlView = displayView.findViewById(R.id.fl_view);
            svContent = displayView.findViewById(R.id.sv_content);
            mVBottomBack = displayView.findViewById(R.id.v_bottom_back);
            mFabMenuSize = displayView.findViewById(R.id.fab_menu_size);
            mBtColorGreen = displayView.findViewById(R.id.bt_color_green);
            mBtColorPurple = displayView.findViewById(R.id.bt_color_purple);
            mBtColorPink = displayView.findViewById(R.id.bt_color_pink);
            mBtColorOrange = displayView.findViewById(R.id.bt_color_orange);
            mBtColorBlack = displayView.findViewById(R.id.bt_color_black);

            mFabMenuColor = displayView.findViewById(R.id.fab_menu_color);
            mBtTextUnderline = displayView.findViewById(R.id.bt_text_underline);
            mBtTextItalics = displayView.findViewById(R.id.bt_text_italics);
            mBtTextBold = displayView.findViewById(R.id.bt_text_bold);
            mFabMenuText = displayView.findViewById(R.id.fab_menu_text);
            mBtEraserLarge = displayView.findViewById(R.id.bt_eraser_large);
            mBtEraserMiddle = displayView.findViewById(R.id.bt_eraser_middle);
            mBtEraserMini = displayView.findViewById(R.id.bt_eraser_mini);

            mFabMenuEraser = displayView.findViewById(R.id.fab_menu_eraser);
            mIvWhiteBoardUndo = displayView.findViewById(R.id.iv_white_board_undo);

            mIvWhiteBoardRedo = displayView.findViewById(R.id.iv_white_board_redo);


            mIvWhiteBoardPre = displayView.findViewById(R.id.iv_white_board_pre);
            mLlWhiteBoardPre = displayView.findViewById(R.id.ll_white_board_pre);

            mTvWhiteBoardPage = displayView.findViewById(R.id.tv_white_board_page);
            mIvWhiteBoardNext = displayView.findViewById(R.id.iv_white_board_next);
            mLlWhiteBoardNext = displayView.findViewById(R.id.ll_white_board_next);

            mLlWhiteBoardPage = displayView.findViewById(R.id.ll_white_board_page);
            mIvWhiteBoardAdd = displayView.findViewById(R.id.iv_white_board_add);

            mRlBottom = displayView.findViewById(R.id.rl_bottom);
            mIvWhiteBoardDisable = displayView.findViewById(R.id.iv_white_board_disable);
            mRlContent = displayView.findViewById(R.id.rl_content);
            mIvWhiteBoardExport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                    StoreUtil.saveWhiteBoardPoints();
                }
            });
            mIvWhiteBoardSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                    saveImage();
                }
            });

            mVBottomBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                }
            });
            mBtSizeLarge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPenSize(WhiteBoardVariable.PenSize.LARRGE);
                }
            });
            mBtSizeMiddle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPenSize(WhiteBoardVariable.PenSize.MIDDLE);
                }
            });
            mBtSizeMini.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPenSize(WhiteBoardVariable.PenSize.MINI);
                }
            });
            mBtColorGreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setColor(WhiteBoardVariable.Color.GREEN);
                }
            });

            mBtColorPurple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setColor(WhiteBoardVariable.Color.PURPLE);
                }
            });

            mBtColorPink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setColor(WhiteBoardVariable.Color.PINK);//-------------
                }
            });
            mBtColorOrange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setColor(WhiteBoardVariable.Color.ORANGE);//--------
                }
            });
            mBtColorBlack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setColor(WhiteBoardVariable.Color.BLACK);//-----
                }
            });

            mBtEraserLarge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setEraserSize(WhiteBoardVariable.EraserSize.LARRGE);
                }
            });
            mBtEraserMiddle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setEraserSize(WhiteBoardVariable.EraserSize.MIDDLE);//------
                }
            });
            mBtEraserMini.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setEraserSize(WhiteBoardVariable.EraserSize.MINI);//------
                }
            });

            mIvWhiteBoardUndo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//----
                    if (OperationUtils.getInstance().DISABLE) {
                        undo();
                    }
                }
            });
            mIvWhiteBoardRedo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//-----
                    if (OperationUtils.getInstance().DISABLE) {
                        redo();
                    }

                }
            });
            mLlWhiteBoardPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//----
                    prePage();
                }
            });
            mIvWhiteBoardPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//----
                    prePage();
                }
            });

            mIvWhiteBoardNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//----
                    nextPage();
                }
            });
            mLlWhiteBoardNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//----
                    nextPage();
                }
            });
            mIvWhiteBoardAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);//
                    newPage();
                }
            });

            board_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //连接server端
                    buildDialog();
                }
            });
            minIma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayView.setVisibility(View.GONE);
                    Hawk.put("WhiteBoaord", true);
                }
            });
            displayView.setOnTouchListener(new FloatingOnTouchListener());
            changeImageHandler.sendEmptyMessageDelayed(0, 2000);
            ImageView imageView = displayView.findViewById(R.id.aa);
            windowManager.addView(displayView, layoutParams);

            closeIma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   displayView.setVisibility(View.GONE);
                    OperationUtils.getInstance().getSavePoints().clear();//新开清空白板笔迹
                    OperationUtils.getInstance().initDrawPointList();
                    OperationUtils.getInstance().getWhiteBoardPoints().getWhiteBoardPoints().clear();
                    windowManager.removeView(displayView);
                    Intent intent = new Intent();
                    //设置intent的动作为com.example.broadcast，可以任意定义
                    intent.setAction(constant.CLOSE_WHITEBOARD_BROADCAST);
                    //发送无序广播
                    sendBroadcast(intent);
                    Hawk.put("WhiteBoaord", false);

                }
            });
            maxIma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                    StoreUtil.saveWhiteBoardPoints();
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    windowManager.updateViewLayout(displayView, layoutParams);
                    exitIma.setVisibility(View.VISIBLE);
                    maxIma.setVisibility(View.GONE);
                    OperationUtils.getInstance().getSavePoints();
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
                    StoreUtil.readWhiteBoardPoints(filepaths.get(0));
                    Log.d("fsdgdgsdg2222", filenames.get(0) + "===" + filepaths.get(0));

                }
            });

            exitIma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                    StoreUtil.saveWhiteBoardPoints();
                    layoutParams.width = 930;
                    layoutParams.height = 1000;
                    windowManager.updateViewLayout(displayView, layoutParams);
                    OperationUtils.getInstance().getSavePoints();
                    maxIma.setVisibility(View.VISIBLE);
                    exitIma.setVisibility(View.GONE);
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
                    Log.d("fsdgdgsdg111", filenames.get(0) + "===" + filepaths.get(0));
                    StoreUtil.readWhiteBoardPoints(filepaths.get(0));


                }
            });

            initView();
            initEvent();
        }
    }

    private void buildDialog() {
        inputDialog = new AlertDialog.Builder(this);
        inputDialog.setTitle("请输入要连接的IP地址");
        inputDialog.setView(dialogView);
        inputDialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edit_text =
                        (EditText) dialogView.findViewById(R.id.editText_ip);
                targetip = edit_text.getText().toString();
                socketConnectThread.start();
                if (dialogView.getParent() != null) {
                    ViewGroup vg = (ViewGroup) dialogView.getParent();
                    vg.removeView(dialogView);
                }

            }
        });
        inputDialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (dialogView.getParent() != null) {
                    ViewGroup vg = (ViewGroup) dialogView.getParent();
                    vg.removeView(dialogView);
                }
            }
        });
        alertInputDialog = inputDialog.create();
        alertInputDialog.show();


    }

    private void initView() {
        changePenBack();
        changeColorBack();
        changeEraserBack();
        ToolsOperation(WhiteBoardVariable.Operation.PEN_NORMAL);
        mDbView.post(new Runnable() {
            @Override
            public void run() {
                showPoints();
            }
        });
    }

    private void initEvent() {
        //头部
     /*   mIvWhiteBoardBack.setOnClickListener(this);
        mIvWhiteBoardExport.setOnClickListener(this);
        mIvWhiteBoardSave.setOnClickListener(this);
        mIvWhiteBoardQuit.setOnClickListener(this);
        mIvWhiteBoardConfirm.setOnClickListener(this);
        mVBottomBack.setOnClickListener(this);*/
        //画笔尺寸大小
        mFabMenuSize.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.PEN_CLICK);
            }
        });
     /*   mBtSizeLarge.setOnClickListener(this);
        mBtSizeMiddle.setOnClickListener(this);
        mBtSizeMini.setOnClickListener(this);*/
        //画笔或者文字颜色
        mFabMenuColor.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.COLOR_CLICK);
            }
        });
    /*    mBtColorGreen.setOnClickListener(this);
        mBtColorPurple.setOnClickListener(this);

        mBtColorPink.setOnClickListener(this);
        mBtColorOrange.setOnClickListener(this);
        mBtColorBlack.setOnClickListener(this);*/
        //文字样式
        mFabMenuText.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.TEXT_CLICK);
            }
        });
     /*   mBtTextUnderline.setOnClickListener(this);
        mBtTextItalics.setOnClickListener(this);
        mBtTextBold.setOnClickListener(this);*/
        //橡皮擦尺寸大小
        mFabMenuEraser.setOnFloatingActionsMenuClickListener(new FloatingActionsMenu.OnFloatingActionsMenuClickListener() {
            @Override
            public void addButtonLister() {
                ToolsOperation(WhiteBoardVariable.Operation.ERASER_CLICK);
            }
        });
     /*   mBtEraserLarge.setOnClickListener(this);
        mBtEraserMiddle.setOnClickListener(this);
        mBtEraserMini.setOnClickListener(this);

        mIvWhiteBoardUndo.setOnClickListener(this);
        mIvWhiteBoardRedo.setOnClickListener(this);

        mLlWhiteBoardPre.setOnClickListener(this);
        mIvWhiteBoardPre.setOnClickListener(this);
        mLlWhiteBoardNext.setOnClickListener(this);
        mIvWhiteBoardNext.setOnClickListener(this);
        mIvWhiteBoardAdd.setOnClickListener(this);
        mIvWhiteBoardDisable.setOnClickListener(this);*/

    }

    /**
     * 设置画笔尺寸
     */
    private void setPenSize(int size) {
        OperationUtils.getInstance().mCurrentPenSize = size;
        changePenBack();
        mDbView.setPenSize();
    }

    /**
     * 切换画笔尺寸按按钮背景
     */
    private void changePenBack() {
        if (OperationUtils.getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.LARRGE) {
            mBtSizeLarge.drawCircleAndRing(WhiteBoardVariable.PenSize.LARRGE, OperationUtils.getInstance().mCurrentColor);
            mBtSizeMiddle.drawCircle(WhiteBoardVariable.PenSize.MIDDLE);
            mBtSizeMini.drawCircle(WhiteBoardVariable.PenSize.MINI);
        } else if (OperationUtils.getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.MIDDLE) {
            mBtSizeLarge.drawCircle(WhiteBoardVariable.PenSize.LARRGE);
            mBtSizeMiddle.drawCircleAndRing(WhiteBoardVariable.PenSize.MIDDLE, OperationUtils.getInstance().mCurrentColor);
            mBtSizeMini.drawCircle(WhiteBoardVariable.PenSize.MINI);
        } else if (OperationUtils.getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.MINI) {
            mBtSizeLarge.drawCircle(WhiteBoardVariable.PenSize.LARRGE);
            mBtSizeMiddle.drawCircle(WhiteBoardVariable.PenSize.MIDDLE);
            mBtSizeMini.drawCircleAndRing(WhiteBoardVariable.PenSize.MINI, OperationUtils.getInstance().mCurrentColor);

        }
    }

    /**
     * 设置颜色
     */
    private void setColor(int color) {
        OperationUtils.getInstance().mCurrentColor = color;
        changeColorBack();
        setPenSize(OperationUtils.getInstance().mCurrentPenSize);
        mDbView.setPenColor();
        mDtView.setTextColor();
    }

    /**
     * 切换颜色控制按钮背景
     */
    private void changeColorBack() {
        if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.BLACK) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_black_selector);
        } else if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.ORANGE) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_orange_selector);
        } else if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.PINK) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_pink_selector);
        } else if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.PURPLE) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_purple_selector);
        } else if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.GREEN) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_green_selector);
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
        int size = OperationUtils.getInstance().getSavePoints().size();
        if (size < 1) {
            return;
        }
        DrawPoint dp = OperationUtils.getInstance().getSavePoints().get(size - 1);
        if (dp.getType() != OperationUtils.DRAW_TEXT) {
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
        OperationUtils.getInstance().mCurrentEraserSize = size;
        changeEraserBack();
        mDbView.setEraserSize();

    }

    /**
     * 切换橡皮擦尺寸按钮背景
     */
    private void changeEraserBack() {
        if (OperationUtils.getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.LARRGE) {
            mBtEraserLarge.drawCircleAndRing(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMiddle.drawCircle(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMini.drawCircle(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);
        } else if (OperationUtils.getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.MIDDLE) {
            mBtEraserLarge.drawCircle(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMiddle.drawCircleAndRing(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMini.drawCircle(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);
        } else if (OperationUtils.getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.MINI) {
            mBtEraserLarge.drawCircle(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMiddle.drawCircle(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMini.drawCircleAndRing(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);

        }
    }

    /**
     * 新建白板
     */
    private void newPage() {
        OperationUtils.getInstance().newPage();
        showPoints();
    }

    /**
     * 上一页
     */
    private void prePage() {
        if (OperationUtils.getInstance().mCurrentIndex > 0) {
            OperationUtils.getInstance().mCurrentIndex--;
            showPoints();
        }
    }

    /**
     * 下一页
     */
    private void nextPage() {
        if (OperationUtils.getInstance().mCurrentIndex + 1 < OperationUtils.getInstance().getDrawPointSize()) {
            OperationUtils.getInstance().mCurrentIndex++;
            showPoints();
        }
    }

    /**
     * 重新显示白板
     */
    private void showPoints() {
        mDbView.showPoints();
        mDtView.showPoints();
        mTvWhiteBoardPage.setText("" + (OperationUtils.getInstance().mCurrentIndex + 1) + "/" + OperationUtils.getInstance().getDrawPointSize());
        showPage();
        showUndoRedo();
    }

    /**
     * 最大最小化重新显示白板
     */
    private void showRedisplayPoints() {
        mDbView.showPoints();
        mDtView.showPoints();
        mTvWhiteBoardPage.setText("" + (OperationUtils.getInstance().mCurrentIndex + 1) + "/" + OperationUtils.getInstance().getDrawPointSize());
        showPage();
        showUndoRedo();
    }

    /**
     * 显示上下页是否可点击
     */
    private void showPage() {
        if (OperationUtils.getInstance().mCurrentIndex + 1 == OperationUtils.getInstance().getDrawPointSize()) {
            mIvWhiteBoardNext.setImageResource(R.drawable.white_board_next_page_click);
        } else {
            mIvWhiteBoardNext.setImageResource(R.drawable.white_board_next_page_selector);
        }
        if (OperationUtils.getInstance().mCurrentIndex == 0) {
            mIvWhiteBoardPre.setImageResource(R.drawable.white_board_pre_page_click);
        } else {
            mIvWhiteBoardPre.setImageResource(R.drawable.white_board_pre_page_selector);
        }

    }

    /**
     * 撤销
     */
    private void undo() {
        int size = OperationUtils.getInstance().getSavePoints().size();
        if (size == 0) {
            return;
        } else {
            OperationUtils.getInstance().getDeletePoints().add(OperationUtils.getInstance().getSavePoints().get(size - 1));
            OperationUtils.getInstance().getSavePoints().remove(size - 1);
            size = OperationUtils.getInstance().getDeletePoints().size();
            if (OperationUtils.getInstance().getDeletePoints().get(size - 1).getType() == OperationUtils.DRAW_PEN) {
                mDbView.undo();
            } else if (OperationUtils.getInstance().getDeletePoints().get(size - 1).getType() == OperationUtils.DRAW_TEXT) {
                mDtView.undo();
            }
            showUndoRedo();
        }

    }

    /**
     * 重做
     */
    private void redo() {
        int size = OperationUtils.getInstance().getDeletePoints().size();
        if (size == 0) {
            return;
        } else {
            OperationUtils.getInstance().getSavePoints().add(OperationUtils.getInstance().getDeletePoints().get(size - 1));
            OperationUtils.getInstance().getDeletePoints().remove(size - 1);
            size = OperationUtils.getInstance().getSavePoints().size();
            if (OperationUtils.getInstance().getSavePoints().get(size - 1).getType() == OperationUtils.DRAW_PEN) {
                mDbView.redo();
            } else if (OperationUtils.getInstance().getSavePoints().get(size - 1).getType() == OperationUtils.DRAW_TEXT) {
                mDtView.redo();
            }
            Log.d("caffd", "退回");
            showUndoRedo();
        }

    }

    /**
     * 文字编辑之后
     */
    private void afterEdit(boolean isSave) {
//        mIvWhiteBoardBack.setVisibility(View.VISIBLE);
        mIvWhiteBoardExport.setVisibility(View.VISIBLE);
        mIvWhiteBoardSave.setVisibility(View.VISIBLE);
        mRlBottom.setVisibility(View.VISIBLE);
        mIvWhiteBoardDisable.setVisibility(View.VISIBLE);
//        mLayoutParams = (RelativeLayout.LayoutParams) mRlContent.getLayoutParams();
//        mLayoutParams.setMargins(OperationUtils.dip2px(24), 0, OperationUtils.dip2px(24), OperationUtils.dip2px(24));
//        mRlContent.setLayoutParams(mLayoutParams);
        mIvWhiteBoardQuit.setVisibility(View.GONE);
        mIvWhiteBoardConfirm.setVisibility(View.GONE);
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
                if (OperationUtils.getInstance().mCurrentOPerationPen == WhiteBoardVariable.Operation.PEN_EXPAND
                        || OperationUtils.getInstance().mCurrentOPerationColor == WhiteBoardVariable.Operation.COLOR_EXPAND
                        || OperationUtils.getInstance().mCurrentOPerationText == WhiteBoardVariable.Operation.TEXT_EXPAND
                        || OperationUtils.getInstance().mCurrentOPerationEraser == WhiteBoardVariable.Operation.ERASER_EXPAND) {
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
                switch (OperationUtils.getInstance().mCurrentOPerationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        OperationUtils.getInstance().mCurrentDrawType = OperationUtils.DRAW_PEN;
                        mDbView.setPaint(null);
                        mFabMenuSize.setAddButtonBackground(R.drawable.white_board_pen_selected_selector);
                        OperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        mFabMenuSize.expand();
                        changePenBack();
                        OperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        mFabMenuSize.collapse();
                        OperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                }
                //   mHandler.sendEmptyMessage(constant.MESSAGE);//-------------------
                break;
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (OperationUtils.getInstance().mCurrentOPerationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        mFabMenuSize.clearDraw();
                        mFabMenuSize.setAddButtonBackground(R.drawable.white_board_pen_selector);
                        OperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        mFabMenuSize.collapse();
                        mFabMenuSize.clearDraw();
                        mFabMenuSize.setAddButtonBackground(R.drawable.white_board_pen_selector);
                        OperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_NORMAL;

                        break;
                }
                // mHandler.sendEmptyMessage(constant.MESSAGE);//-------------------
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (OperationUtils.getInstance().mCurrentOPerationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        mFabMenuSize.collapse();
                        OperationUtils.getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                }
                //  mHandler.sendEmptyMessage(constant.MESSAGE);//-------------------
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
                switch (OperationUtils.getInstance().mCurrentOPerationColor) {
                    case WhiteBoardVariable.Operation.COLOR_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.COLOR_EXPAND:
                        mFabMenuColor.collapse();
                        OperationUtils.getInstance().mCurrentOPerationColor = WhiteBoardVariable.Operation.COLOR_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
                switch (OperationUtils.getInstance().mCurrentOPerationColor) {
                    case WhiteBoardVariable.Operation.COLOR_NORMAL:
                        mFabMenuColor.expand();
                        OperationUtils.getInstance().mCurrentOPerationColor = WhiteBoardVariable.Operation.COLOR_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.COLOR_EXPAND:
                        mFabMenuColor.collapse();
                        OperationUtils.getInstance().mCurrentOPerationColor = WhiteBoardVariable.Operation.COLOR_NORMAL;
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
                switch (OperationUtils.getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        OperationUtils.getInstance().mCurrentDrawType = OperationUtils.DRAW_TEXT;
                        mFabMenuText.setAddButtonBackground(R.drawable.white_board_text_selected_selector);
                        OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        Log.d("bbbbb", "44444");
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        Log.d("bbbbb", "555");
                        int size = OperationUtils.getInstance().getSavePoints().size();
                        if (size > 0) {
                            DrawPoint dp = OperationUtils.getInstance().getSavePoints().get(size - 1);
                            if (dp.getType() == OperationUtils.DRAW_TEXT && dp.getDrawText().getStatus() == DrawTextView.TEXT_DETAIL) {
                                changeTextBack();
                                mFabMenuText.expand();
                                OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_EXPAND;
                            }
                        }
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        Log.d("bbbbb", "6666");
                        mFabMenuText.collapse();
                        OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.PEN_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (OperationUtils.getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        Log.d("bbbbb", "777");
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        Log.d("bbbbb", "88888");
                        mFabMenuText.clearDraw();
                        mFabMenuText.setAddButtonBackground(R.drawable.white_board_text_selector);
                        OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        Log.d("bbbbb", "999");
                        mFabMenuText.collapse();
                        mFabMenuText.clearDraw();
                        mFabMenuText.setAddButtonBackground(R.drawable.white_board_text_selector);
                        OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (OperationUtils.getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        Log.d("bbbbb", "111111");
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        Log.d("bbbbb", "22222");

                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        Log.d("bbbbb", "333");
                        mFabMenuText.collapse();
                        OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
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
                switch (OperationUtils.getInstance().mCurrentOPerationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        OperationUtils.getInstance().mCurrentDrawType = OperationUtils.DRAW_ERASER;
                        mDbView.changeEraser();
                        mFabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selected_selector);
                        OperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        mFabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selected_selector);
                        mFabMenuEraser.expand();
                        changeEraserBack();
                        OperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        mFabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selected_selector);
                        mFabMenuEraser.collapse();
                        OperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.PEN_CLICK:
                switch (OperationUtils.getInstance().mCurrentOPerationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        mFabMenuEraser.clearDraw();
                        mFabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selector);
                        OperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        mFabMenuEraser.collapse();
                        mFabMenuEraser.clearDraw();
                        mFabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selector);
                        OperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (OperationUtils.getInstance().mCurrentOPerationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        mFabMenuEraser.collapse();
                        OperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                }
                break;

        }

    }

    /**
     * 从参数的Socket里获取最新的消息
     */
    private void startReader(final Socket socket) {

        new Thread() {
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    while (true) {
                        System.out.println("*WHITE_BOARD_等待客户端输入*");
                        // 读取数据
                        String msg = reader.readUTF();
                        Message message = new Message();
                        System.out.println("WHITE_BOARD获取到客户端的信息：=" + msg);

                        if (msg.equals(sendRedoMsg)) {
                            message.what = 2;
                            handler.sendMessage(message);
                        } else if (msg.equals(sendUedoMsg)) {
                            message.what = 3;
                            handler.sendMessage(message);
                        } else {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", msg);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 保存当前白板为图片
     */
    public void saveImage() {
        String fileName = StoreUtil.getPhotoSavePath();
        Log.e("gpy", fileName);
        File file = new File(fileName);
        try {
            File directory = file.getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                //showMessage(getString(R.string.white_board_export_fail));
                return;
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

            //   showMessage(getString(R.string.white_board_export_tip) + fileName);
        } catch (Exception e) {
            //    showMessage(getString(R.string.white_board_export_fail));
            e.printStackTrace();
        }
    }

    @ReceiveEvents(name = Events.WHITE_BOARD_TEXT_EDIT)
    private void textEdit() {//文字编辑
//        mIvWhiteBoardBack.setVisibility(View.GONE);
        mIvWhiteBoardExport.setVisibility(View.GONE);
        mIvWhiteBoardSave.setVisibility(View.GONE);
        mRlBottom.setVisibility(View.GONE);
        mIvWhiteBoardDisable.setVisibility(View.GONE);
//        mLayoutParams = (RelativeLayout.LayoutParams) mRlContent.getLayoutParams();
//        mLayoutParams.setMargins(OperationUtils.dip2px(24), 0, OperationUtils.dip2px(24), 0);
//        mRlContent.setLayoutParams(mLayoutParams);
        Log.d("bbbbbbb", "文字编辑状态");
        mIvWhiteBoardQuit.setVisibility(View.VISIBLE);
        mIvWhiteBoardConfirm.setVisibility(View.VISIBLE);
    }

    @ReceiveEvents(name = Events.WHITE_BOARD_UNDO_REDO)
    private void showUndoRedo() {//是否显示撤销、重装按钮
        if (OperationUtils.getInstance().getSavePoints().isEmpty()) {
            mIvWhiteBoardUndo.setVisibility(View.INVISIBLE);
            mIvWhiteBoardExport.setVisibility(View.INVISIBLE);
            mIvWhiteBoardSave.setVisibility(View.INVISIBLE);
        } else {
            mIvWhiteBoardUndo.setVisibility(View.VISIBLE);
            mIvWhiteBoardExport.setVisibility(View.VISIBLE);
            mIvWhiteBoardSave.setVisibility(View.VISIBLE);
        }
        if (OperationUtils.getInstance().getDeletePoints().isEmpty()) {
            mIvWhiteBoardRedo.setVisibility(View.INVISIBLE);
            Log.d("caffd", "退回-----");
        } else {
            mIvWhiteBoardRedo.setVisibility(View.VISIBLE);
            Log.d("caffd", "退回+++++");
        }
    }

    private Handler.Callback changeImageCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {

            }
            return false;
        }
    };

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    public void hideWhiteBoaord() {
        if (displayView != null) {
            displayView.setVisibility(View.GONE);
            Hawk.put("WhiteBoaord", true);
            Log.d("adfafaf", "白板隐藏");
        }
    }

    /**
     * 重新显示白板单个操作
     */
    private void showSinglePoint(DrawPoint drawpoint) {
        if (isfirstinit) {
            mDbView.initSinglePoint();
            isfirstinit = false;
        }
//        mDbView.showSinglePoint(drawpoint);
        mDtView.showPoints();
        mTvWhiteBoardPage.setText("" + (OperationUtils.getInstance().mCurrentIndex + 1) + "/" + OperationUtils.getInstance().getDrawPointSize());
        showPage();
        showUndoRedo();
    }

    public void showWhiteBoaord() {
        if (displayView != null) {
            // windowManager.updateViewLayout(displayView, layoutParams);
            displayView.setVisibility(View.VISIBLE);
            Hawk.put("WhiteBoaord", false);
            Log.d("adfafaf", "白板显示");
        }
    }

    public class MyBinder extends Binder {
        public WhiteBoardServies getService() {
            return WhiteBoardServies.this;
        }

       /* public void setData(String data) {
            FloatingVideoService.this.data = data;
        }*/
    }


    /*********************************************** 接受消息接受消息接受消息 *********************************************************/
    class SocketAcceptThread extends Thread {
        @Override
        public void run() {
            try {
                //等待客户端的连接，Accept会阻塞，直到建立连接，
                //所以需要放在子线程中运行。
                mSocketConnect = mServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("info", "run: ==============" + "accept error");
                return;
            }
            Log.e("info", "accept success==================");
            //启动消息接收线程
            startReader(mSocketConnect);
        }
    }
    class SocketConnectThread extends Thread{
        public void run(){
            Log.e("info", "run: ============线程启动" );
            try {
                //指定ip地址和端口号
                mSocket = new Socket(targetip, 1989);
                if(mSocket != null){
                    //获取输出流、输入流
                    mOutStream = mSocket.getOutputStream();
                    mInStream = mSocket.getInputStream();
                    isConnected = true;

                    //  分享文件的时候调用
                    //  连接成功，发送数据
//                    sendFileData();

                }else {
                    Log.e("info", "run: =========scoket==null");
                    isConnected = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isConnected = false;
                return;
            }
            Log.e("info","connect success========================================");
            startReader(mSocket);
        }

    }
    /**
     *  分享白板，即是分享白板的操作集
     */
    protected void sendFileData() {

        WhiteBoardPoints whiteBoardPoints = OperationUtils.getInstance().getWhiteBoardPoints();
        if (whiteBoardPoints == null || whiteBoardPoints.getWhiteBoardPoints() == null || whiteBoardPoints.getWhiteBoardPoints().isEmpty()) {
            return;
        }
        String strJson = new Gson().toJson(whiteBoardPoints);
        send(strJson);
    }

    /**
     *  分享白板单个操作
     */
    protected void sendsingleFileData() {
        if(!isConnected){return;}
        DrawPoint whiteBoardPoint = OperationUtils.getInstance().getMsingleWhiteBoardPoint();
        if (whiteBoardPoint == null) {
            return;
        }
        String strJson = new Gson().toJson(whiteBoardPoint);
        Log.d("TAG","strJson==" + strJson);

//        DrawPoint whiteBoardPoint2 = new Gson().fromJson(strJson, DrawPoint.class);
//        Log.d("TAG","whiteBoardPoint2==" + whiteBoardPoint2);
        send(strJson);
    }

    public void send(final String str) {
        if (str.length() == 0){
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    // socket.getInputStream()
                    DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
                    writer.writeUTF(str); // 写一个UTF-8的信息
                    System.out.println("发送消息");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("adfafaf11", "白板广播显示");
            if (constant.SHOW_WHITEBOARD_BROADCAST.equals(intent.getAction())) {
                showWhiteBoaord();
            } else if (constant.SHOW_UI.equals(intent.getAction())) {
                showUndoRedo();
            }

        }
    }
}


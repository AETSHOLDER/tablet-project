/*
package com.example.paperlessmeeting.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting.R;
import com.example.paperlessmeeting.base.BaseActivity;
import com.example.paperlessmeeting.base.BaseFragment;
import com.example.paperlessmeeting.tool.constant;
import com.example.paperlessmeeting.tool.StoreUtil;
import com.example.paperlessmeeting.widgets.FloatingActionsMenu;
import com.example.paperlessmeeting.widgets.FloatingImageButton;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.guanpy.wblib.utils.OperationUtils.getInstance;

*/
/**
 * ????????????
 * Created by gpy on 2015/6/2.
 *//*

@SuppressLint("ValidFragment")
public class ElectronicWhiteBoardFragment extends BaseFragment implements View.OnClickListener, DrawPenView.sendSingleFile {
    private String titles;
    private Context context;
    public ElectronicWhiteBoardFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }
    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initData() {

    }

    @BindView(R.id.iv_white_board_book)
    ImageView mIvWhiteBoardBook;
    @BindView(R.id.iv_white_board_back)
    ImageView mIvWhiteBoardBack;
    @BindView(R.id.iv_white_board_quit)
    ImageView mIvWhiteBoardQuit;
    @BindView(R.id.tv_white_board_head)
    TextView mTvWhiteBoardHead;
    @BindView(R.id.iv_white_board_confirm)
    ImageView mIvWhiteBoardConfirm;
    @BindView(R.id.iv_white_board_export)
    ImageView mIvWhiteBoardExport;
    @BindView(R.id.iv_white_board_save)
    ImageView mIvWhiteBoardSave;
    @BindView(R.id.rl_head)
    RelativeLayout mRlHead;
    @BindView(R.id.db_view)
    DrawPenView mDbView;
    @BindView(R.id.dt_view)
    DrawTextLayout mDtView;
    @BindView(R.id.fl_view)
    FrameLayout mFlView;
    @BindView(R.id.sv_content)
    ScrollView svContent;
    @BindView(R.id.v_bottom_back)
    View mVBottomBack;
    @BindView(R.id.bt_size_large)
    FloatingImageButton mBtSizeLarge;
    @BindView(R.id.bt_size_middle)
    FloatingImageButton mBtSizeMiddle;
    @BindView(R.id.bt_size_mini)
    FloatingImageButton mBtSizeMini;
    @BindView(R.id.fab_menu_size)
    FloatingActionsMenu mFabMenuSize;
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
    @BindView(R.id.fab_menu_color)
    FloatingActionsMenu mFabMenuColor;
    @BindView(R.id.bt_text_underline)
    FloatingImageButton mBtTextUnderline;
    @BindView(R.id.bt_text_italics)
    FloatingImageButton mBtTextItalics;
    @BindView(R.id.bt_text_bold)
    FloatingImageButton mBtTextBold;
    @BindView(R.id.fab_menu_text)
    FloatingActionsMenu mFabMenuText;
    @BindView(R.id.bt_eraser_large)
    FloatingImageButton mBtEraserLarge;
    @BindView(R.id.bt_eraser_middle)
    FloatingImageButton mBtEraserMiddle;
    @BindView(R.id.bt_eraser_mini)
    FloatingImageButton mBtEraserMini;
    @BindView(R.id.fab_menu_eraser)
    FloatingActionsMenu mFabMenuEraser;
    @BindView(R.id.iv_white_board_undo)
    ImageView mIvWhiteBoardUndo;
    @BindView(R.id.iv_white_board_redo)
    ImageView mIvWhiteBoardRedo;
    @BindView(R.id.iv_white_board_pre)
    ImageView mIvWhiteBoardPre;
    @BindView(R.id.ll_white_board_pre)
    LinearLayout mLlWhiteBoardPre;
    @BindView(R.id.tv_white_board_page)
    TextView mTvWhiteBoardPage;
    @BindView(R.id.iv_white_board_next)
    ImageView mIvWhiteBoardNext;
    @BindView(R.id.ll_white_board_next)
    LinearLayout mLlWhiteBoardNext;
    @BindView(R.id.ll_white_board_page)
    LinearLayout mLlWhiteBoardPage;
    @BindView(R.id.iv_white_board_add)
    ImageView mIvWhiteBoardAdd;
    @BindView(R.id.rl_bottom)
    RelativeLayout mRlBottom;
    @BindView(R.id.iv_white_board_disable)
    ImageView mIvWhiteBoardDisable;
    @BindView(R.id.rl_content)
    RelativeLayout mRlContent;
*/
/*
    @BindView(R.id.rl_savetools)
    LinearLayout mLlWhiteBoardSaveTools;
*//*



    @BindView(R.id.iv_white_board_share)
    ImageView mIvWhiteBoardShare;
    @BindView(R.id.iv_white_board_share2)
    ImageView mIvWhiteBoardShare2;

    AlertDialog.Builder inputDialog;
    AlertDialog alertInputDialog;
    View dialogView;
    public String targetip = "192.168.0.100";

    private ServerSocket mServerSocket;
    private boolean isfirstinit = true;
    private Socket mSocket;
    private Socket mSocketConnect;
    private OutputStream mOutStream;
    private InputStream mInStream;
    private ElectronicWhiteBoardFragment.SocketConnectThread socketConnectThread;
    private StringBuffer sb = new StringBuffer();
    private boolean isConnected = false;
    final String sendUedoMsg = "sendUedoMsg";
    final String sendRedoMsg = "sendRedoMsg";
    private MyReceiver myRedoReceiver;
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Bundle data = msg.getData();
                String jsonStr = data.getString("msg");
//                sb.append(jsonStr);
                //  ???????????????????????????????????????
                if (!TextUtils.isEmpty(jsonStr)) {
                    DrawPoint whiteBoardPoint = new Gson().fromJson(jsonStr, DrawPoint.class);
                    //  ????????????????????????????????????????????????????????????
                    getInstance().getSavePoints().add(whiteBoardPoint);
                    StoreUtil.readSingleDrawPoint(whiteBoardPoint);
                    showSinglePoint(whiteBoardPoint);
                }

                // ????????????

                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                OperationUtils.getInstance().DISABLE = false;
                //    mLlWhiteBoardSaveTools.setVisibility(View.INVISIBLE);
                mIvWhiteBoardDisable.setClickable(false);
                mIvWhiteBoardDisable.setImageResource(R.drawable.white_board_undisable_selector);
                mRlBottom.setVisibility(View.GONE);
                mIvWhiteBoardShare.setVisibility(View.INVISIBLE);
                mIvWhiteBoardShare2.setVisibility(View.INVISIBLE);
            } else if (msg.what == 2) {
                redo();
            } else if (msg.what == 3) {
                undo();
            }
        }
    };

    @Override
    protected void initView() {
        mLlWhiteBoardPage.setVisibility(View.VISIBLE);
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

        dialogView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialo_inputadreass, null);
    }

    private void initEvent() {
        //??????
        mIvWhiteBoardBack.setOnClickListener(this);
        mIvWhiteBoardExport.setOnClickListener(this);
        mIvWhiteBoardSave.setOnClickListener(this);
        mIvWhiteBoardQuit.setOnClickListener(this);
        mIvWhiteBoardConfirm.setOnClickListener(this);
        mIvWhiteBoardShare.setOnClickListener(this);
        mIvWhiteBoardShare2.setOnClickListener(this);
        mVBottomBack.setOnClickListener(this);
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

        mLlWhiteBoardPre.setOnClickListener(this);
        mIvWhiteBoardPre.setOnClickListener(this);
        mLlWhiteBoardNext.setOnClickListener(this);
        mIvWhiteBoardNext.setOnClickListener(this);
        mIvWhiteBoardAdd.setOnClickListener(this);
        mIvWhiteBoardDisable.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_white_board_back://?????????
             //   onBackPressed();
                break;
            case R.id.iv_white_board_quit://??????????????????
                afterEdit(false);
                break;
            case R.id.iv_white_board_confirm://??????????????????
                afterEdit(true);
                break;
            case R.id.iv_white_board_share: //  ??????
                if (isConnected) {
                    sendFileData();
                } else {
                    //??????server???
                    buildDialog();
                }
                break;
            case R.id.iv_white_board_share2:

                //??????server???
                if (Hawk.contains("ip")) {
                    Hawk.get("ip");
                    targetip = Hawk.get("ip");
                    socketConnectThread.start();
                    Toast.makeText(getActivity(), "???????????????????????????", Toast.LENGTH_LONG).show();
                } else {
                    buildDialog();
                }

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
                if (OperationUtils.getInstance().DISABLE) {
                    undo();
                }
                break;
            case R.id.iv_white_board_redo://??????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (OperationUtils.getInstance().DISABLE) {
                    redo();
                }
                break;
            case R.id.ll_white_board_pre:
            case R.id.iv_white_board_pre://?????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                prePage();
                break;
            case R.id.ll_white_board_next:
            case R.id.iv_white_board_next://?????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                nextPage();
                break;
            case R.id.iv_white_board_add://????????????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                newPage();
                break;
            case R.id.iv_white_board_disable://??????/????????????
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


    private void buildDialog() {
        inputDialog = new AlertDialog.Builder(getActivity());
        inputDialog.setTitle("?????????????????????IP??????");
        inputDialog.setView(dialogView);
        inputDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
        inputDialog.setNeutralButton("??????", new DialogInterface.OnClickListener() {
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

    */
/**
     * ??????????????????
     *//*

    private void setPenSize(int size) {
        OperationUtils.getInstance().mCurrentPenSize = size;
        changePenBack();
        mDbView.setPenSize();
    }

    */
/**
     * ?????????????????????????????????
     *//*

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

    */
/**
     * ????????????
     *//*

    private void setColor(int color) {
        OperationUtils.getInstance().mCurrentColor = color;
        changeColorBack();
        setPenSize(OperationUtils.getInstance().mCurrentPenSize);
        mDbView.setPenColor();
        mDtView.setTextColor();
    }

    */
/**
     * ??????????????????????????????
     *//*

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

    */
/**
     * ??????????????????
     *//*

    private void setTextStyle(int textStyle) {
        mDtView.setTextStyle(textStyle);
        changeTextBack();
    }

    */
/**
     * ??????????????????????????????
     *//*

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

    */
/**
     * ?????????????????????
     *//*

    private void setEraserSize(int size) {
        OperationUtils.getInstance().mCurrentEraserSize = size;
        changeEraserBack();
        mDbView.setEraserSize();
    }

    */
/**
     * ?????????????????????????????????
     *//*

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

    */
/**
     * ????????????
     *//*

    private void newPage() {
        OperationUtils.getInstance().newPage();
        showPoints();
    }

    */
/**
     * ?????????
     *//*

    private void prePage() {
        if (OperationUtils.getInstance().mCurrentIndex > 0) {
            OperationUtils.getInstance().mCurrentIndex--;
            showPoints();
        }
    }

    */
/**
     * ?????????
     *//*

    private void nextPage() {
        if (OperationUtils.getInstance().mCurrentIndex + 1 < OperationUtils.getInstance().getDrawPointSize()) {
            OperationUtils.getInstance().mCurrentIndex++;
            showPoints();
        }
    }

    */
/**
     * ??????????????????
     *//*

    private void showPoints() {
        mDtView.showPoints();
        mTvWhiteBoardPage.setText("" + (OperationUtils.getInstance().mCurrentIndex + 1) + "/" + OperationUtils.getInstance().getDrawPointSize());
        showPage();
        showUndoRedo();
    }

    */
/**
     * ??????????????????????????????
     *//*

    private void showSinglePoint(DrawPoint drawpoint) {
        if (isfirstinit) {
            mDbView.initSinglePoint();
            isfirstinit = false;
        }
        mDbView.showSinglePoint(drawpoint);
        mDtView.showPoints();
        mTvWhiteBoardPage.setText("" + (OperationUtils.getInstance().mCurrentIndex + 1) + "/" + OperationUtils.getInstance().getDrawPointSize());
        showPage();
        showUndoRedo();
    }

    */
/**
     * ??????????????????????????????
     *//*

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

    */
/**
     * ??????
     *//*

    private void undo() {
        sendUedoMsg();
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

    */
/**
     * ??????
     *//*

    private void redo() {
        sendRedoMsg();
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
            showUndoRedo();
        }

    }

    private void sendRedoMsg() {
        if (!isConnected) {
            return;
        }
        String strJson = sendRedoMsg;
        send(strJson);
    }

    private void sendUedoMsg() {
        if (!isConnected) {
            return;
        }
        String strJson = sendUedoMsg;
        send(strJson);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_white_board;
    }

    */
/**
     * ??????????????????
     *//*

    private void afterEdit(boolean isSave) {
        mIvWhiteBoardBack.setVisibility(View.VISIBLE);
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

    */
/**
     * ?????????????????????????????????
     *//*

    private void ToolsOperation(int currentOperation) {
        setPenOperation(currentOperation);
        setColorOperation(currentOperation);
        setTextOperation(currentOperation);
        setEraserOperation(currentOperation);
        showOutSideView();
    }

    */
/**
     * ????????????
     *//*

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

    */
/**
     * ?????????????????????????????????-??????
     *//*

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
                break;

        }

    }

    */
/**
     * ?????????????????????????????????-??????
     *//*

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

    */
/**
     * ?????????????????????????????????-??????
     *//*

    private void setTextOperation(int currentOperation) {
        switch (currentOperation) {
            case WhiteBoardVariable.Operation.TEXT_CLICK:
                switch (OperationUtils.getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        OperationUtils.getInstance().mCurrentDrawType = OperationUtils.DRAW_TEXT;
                        mFabMenuText.setAddButtonBackground(R.drawable.white_board_text_selected_selector);
                        OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
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
                        mFabMenuText.collapse();
                        OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.PEN_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (OperationUtils.getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        mFabMenuText.clearDraw();
                        mFabMenuText.setAddButtonBackground(R.drawable.white_board_text_selector);
                        OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
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
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        mFabMenuText.collapse();
                        OperationUtils.getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                }
                break;

        }

    }

    */
/**
     * ?????????????????????????????????-?????????
     *//*

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
                        mFabMenuEraser.expand();
                        changeEraserBack();
                        OperationUtils.getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
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

    */
/**
     * ???????????????????????????
     *//*

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
            getActivity().sendBroadcast(intent);//???????????????????????????????????????

            // getActivity().showMessage(getString(R.string.white_board_export_tip) + fileName);
        } catch (Exception e) {
            //showMessage(getString(R.string.white_board_export_fail));
            e.printStackTrace();
        }
    }


    class SocketConnectThread extends Thread {
        public void run() {
            Log.e("info", "run: ============????????????");
            try {
                //??????ip??????????????????
                mSocket = new Socket(targetip, 1989);
                if (mSocket != null) {
                    //???????????????????????????
                    mOutStream = mSocket.getOutputStream();
                    mInStream = mSocket.getInputStream();
                    isConnected = true;

                    //  ???????????????????????????
                    //  ???????????????????????????
//                    sendFileData();

                } else {
                    Log.e("info", "run: =========scoket==null");
                    isConnected = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isConnected = false;
                return;
            }
            Log.e("info", "connect success========================================");
            startReader(mSocket);
        }

    }

    */
/**
     * ?????????????????????????????????????????????
     *//*

    protected void sendFileData() {

        WhiteBoardPoints whiteBoardPoints = OperationUtils.getInstance().getWhiteBoardPoints();
        if (whiteBoardPoints == null || whiteBoardPoints.getWhiteBoardPoints() == null || whiteBoardPoints.getWhiteBoardPoints().isEmpty()) {
            return;
        }
        String strJson = new Gson().toJson(whiteBoardPoints);
        send(strJson);
    }


    */
/**
     * ????????????????????????
     *//*

    protected void sendsingleFileData() {
        if (!isConnected) {
            return;
        }
        DrawPoint whiteBoardPoint = OperationUtils.getInstance().getMsingleWhiteBoardPoint();
        if (whiteBoardPoint == null) {
            return;
        }
        String strJson = new Gson().toJson(whiteBoardPoint);
        Log.d("TAG", "strJson==" + strJson);

//        DrawPoint whiteBoardPoint2 = new Gson().fromJson(strJson, DrawPoint.class);
//        Log.d("TAG","whiteBoardPoint2==" + whiteBoardPoint2);
        send(strJson);
    }

    public void send(final String str) {
        if (str.length() == 0) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    // socket.getInputStream()
                    DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
                    writer.writeUTF(str); // ?????????UTF-8?????????
                    System.out.println("????????????");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    */
/**
     * ????????????Socket????????????????????????
     *//*

    private void startReader(final Socket socket) {

        new Thread() {
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    // ???????????????
                    reader = new DataInputStream(socket.getInputStream());
                    while (true) {
                        System.out.println("*WHITE_BOARD_?????????????????????*");
                        // ????????????
                        String msg = reader.readUTF();
                        Message message = new Message();
                        System.out.println("WHITE_BOARD??????????????????????????????=" + msg);

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

    */
/*********************************************** ???????????????????????????????????? *********************************************************//*

    class SocketAcceptThread extends Thread {
        @Override
        public void run() {
            try {
                //???????????????????????????Accept?????????????????????????????????
                //???????????????????????????????????????
                mSocketConnect = mServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("info", "run: ==============" + "accept error");
                return;
            }
            Log.e("info", "accept success==================");
            //????????????????????????
            startReader(mSocketConnect);
        }
    }


   */
/* @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*//*


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (myRedoReceiver != null) {
            getActivity().unregisterReceiver(myRedoReceiver);
        }
    }

    @Override
    public void sendsinglefile() {
        sendsingleFileData();
    }


    @ReceiveEvents(name = Events.WHITE_BOARD_TEXT_EDIT)
    private void textEdit() {//????????????
        mIvWhiteBoardBack.setVisibility(View.GONE);
        mIvWhiteBoardExport.setVisibility(View.GONE);
        mIvWhiteBoardSave.setVisibility(View.GONE);
        mRlBottom.setVisibility(View.GONE);
        mIvWhiteBoardDisable.setVisibility(View.GONE);
//        mLayoutParams = (RelativeLayout.LayoutParams) mRlContent.getLayoutParams();
//        mLayoutParams.setMargins(OperationUtils.dip2px(24), 0, OperationUtils.dip2px(24), 0);
//        mRlContent.setLayoutParams(mLayoutParams);

        mIvWhiteBoardQuit.setVisibility(View.VISIBLE);
        mIvWhiteBoardConfirm.setVisibility(View.VISIBLE);
    }

    @ReceiveEvents(name = Events.WHITE_BOARD_UNDO_REDO)
    private void showUndoRedo() {//?????????????????????????????????
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
        } else {
            mIvWhiteBoardRedo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        myRedoReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.SHOW_UI);
        getActivity().registerReceiver(myRedoReceiver, filter);
        socketConnectThread = new SocketConnectThread();
        initView();
        initEvent();
        mDbView.sendfile = (DrawPenView.sendSingleFile) this;
        try {
            mServerSocket = new ServerSocket(1989);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //??????????????????
        ElectronicWhiteBoardFragment.SocketAcceptThread socketAcceptThread = new ElectronicWhiteBoardFragment.SocketAcceptThread();
        socketAcceptThread.start();
        // copy();
        return rootView;
    }
    */
/*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

    }*//*


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (constant.SHOW_UI.equals(intent.getAction())) {
                showUndoRedo();
            }

        }
    }
}
*/

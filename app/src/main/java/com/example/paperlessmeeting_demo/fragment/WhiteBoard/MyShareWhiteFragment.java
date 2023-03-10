package com.example.paperlessmeeting_demo.fragment.WhiteBoard;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.WhiteBoardActivity2;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.WSWhiteBoardBean;
import com.example.paperlessmeeting_demo.enums.ToFragmentIsConnectedType;
import com.example.paperlessmeeting_demo.enums.ToFragmentType;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.StoreUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.widgets.FloatingActionsMenu;
import com.example.paperlessmeeting_demo.widgets.FloatingImageButton;
import com.github.guanpy.library.ann.ReceiveEvents;
import com.github.guanpy.wblib.bean.DrawPenStr;
import com.github.guanpy.wblib.bean.DrawPoint;
import com.github.guanpy.wblib.utils.Events;
import com.github.guanpy.wblib.utils.OperationUtils;
import com.github.guanpy.wblib.utils.WhiteBoardVariable;
import com.github.guanpy.wblib.widget.DrawPenView;
import com.github.guanpy.wblib.widget.DrawTextLayout;
import com.github.guanpy.wblib.widget.DrawTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.github.guanpy.wblib.utils.OperationUtils.getInstance;
/**
 *  ????????????
 * */
public class MyShareWhiteFragment extends BaseFragment implements View.OnClickListener, DrawPenView.sendSingleFile, WhiteBoardActivity2.ToFragmentListener{
    @BindView(R.id.iv_white_board_book)
    ImageView mIvWhiteBoardBook;
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
    @BindView(R.id.iv_white_board_swap)
    ImageView mIvWhiteBoardSwap;
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
    /*
        @BindView(R.id.rl_savetools)
        LinearLayout mLlWhiteBoardSaveTools;
    */

    final String sendMsg = "sendMsg";
    final String sendUedoMsg = "sendUedoMsg";
    final String sendRedoMsg = "sendRedoMsg";
    final String sendSwapMsg = "sendSwapMsg";
    private MyShareWhiteFragment.MyReceiver myRedoReceiver;
    private boolean isShareConnected = false;
    private  boolean isfirstinit = true;
    private final String TAG = "MyShareWhiteFragment";
    private Type type = new TypeToken<TempWSBean<WSWhiteBoardBean>>() {}.getType();
    private Gson gson = new Gson();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_white;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initView() {
        mLlWhiteBoardPage.setVisibility(View.INVISIBLE);
        mIvWhiteBoardAdd.setVisibility(View.INVISIBLE);
        changePenBack();
        changeColorBack();
        changeEraserBack();
        ToolsOperation(WhiteBoardVariable.Operation.PEN_NORMAL);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        //  ??????????????????????????????,??????????????????
        OperationUtils.getInstance().DISABLE = false;
        if(Hawk.get(constant.team_share) != null && Hawk.get(constant.team_share).equals("share")){
            OperationUtils.getInstance().DISABLE = true;
            mDbView.DISABLE = false;
            ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
            isShareConnected = true;
            mRlBottom.setVisibility(View.GONE);
        }else {
            mDbView.DISABLE = true;
        }

        mDbView.post(new Runnable() {
            @Override
            public void run() {
                getInstance().mCurrentIndex = 0;
                if(isfirstinit){ // ???????????????
                    OperationUtils.getInstance().setmWhiteBoardPoints(null);
                    isfirstinit = false;
                }else {
                    OperationUtils.getInstance().setmWhiteBoardPoints(OperationUtils.getInstance().getmWhiteBoardPointList().get(0));
                }
                showPoints();
            }
        });
    }

    @Override
    protected void initData() {}
    public MyShareWhiteFragment() {
        // Required empty public constructor
    }

    private void initEvent() {
        //??????
        mIvWhiteBoardExport.setOnClickListener(this);
        mIvWhiteBoardSave.setOnClickListener(this);
        mIvWhiteBoardQuit.setOnClickListener(this);
        mIvWhiteBoardConfirm.setOnClickListener(this);
//        mIvWhiteBoardShare2.setOnClickListener(this);
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
        mIvWhiteBoardSwap.setOnClickListener(this);
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
            case R.id.iv_white_board_swap://????????????
                CVIPaperDialogUtils.showCustomDialog(getActivity(), "???????????????????", null, "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                    @Override
                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                        if(clickConfirm){
                            mDbView.clearImage();
                            sendSwapMsg();
                        }
                    }
                });
                break;
            case R.id.iv_white_board_undo://??????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (!getInstance().DISABLE) {
                    undo();
                }
                break;
            case R.id.iv_white_board_redo://??????
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (!getInstance().DISABLE) {
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
                if (getInstance().DISABLE) {
                    getInstance().DISABLE = false;
                    mIvWhiteBoardDisable.setImageResource(R.drawable.white_board_disable_selector);
                    mRlBottom.setVisibility(View.VISIBLE);
                } else {
                    getInstance().DISABLE = true;
                    mIvWhiteBoardDisable.setImageResource(R.drawable.white_board_undisable_selector);
                    mRlBottom.setVisibility(View.GONE);
                }
        }
    }


    /**
     * ??????????????????
     */
    private void setPenSize(int size) {
        ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
        getInstance().mCurrentPenSize = size;
        changePenBack();
        mDbView.setPenSize();
    }

    /**
     * ?????????????????????????????????
     */
    private void changePenBack() {
        if (getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.LARRGE) {
            mBtSizeLarge.drawCircleAndRing(WhiteBoardVariable.PenSize.LARRGE, getInstance().mCurrentColor);
            mBtSizeMiddle.drawCircle(WhiteBoardVariable.PenSize.MIDDLE);
            mBtSizeMini.drawCircle(WhiteBoardVariable.PenSize.MINI);
        } else if (getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.MIDDLE) {
            mBtSizeLarge.drawCircle(WhiteBoardVariable.PenSize.LARRGE);
            mBtSizeMiddle.drawCircleAndRing(WhiteBoardVariable.PenSize.MIDDLE, getInstance().mCurrentColor);
            mBtSizeMini.drawCircle(WhiteBoardVariable.PenSize.MINI);
        } else if (getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.MINI) {
            mBtSizeLarge.drawCircle(WhiteBoardVariable.PenSize.LARRGE);
            mBtSizeMiddle.drawCircle(WhiteBoardVariable.PenSize.MIDDLE);
            mBtSizeMini.drawCircleAndRing(WhiteBoardVariable.PenSize.MINI, getInstance().mCurrentColor);

        }
    }

    /**
     * ????????????
     */
    private void setColor(int color) {
        ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
        getInstance().mCurrentColor = color;
        changeColorBack();
        setPenSize(getInstance().mCurrentPenSize);
        mDbView.setPenColor();
        mDtView.setTextColor();
    }

    /**
     * ??????????????????????????????
     */
    private void changeColorBack() {
        if (getInstance().mCurrentColor == WhiteBoardVariable.Color.BLACK) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_black_selector);
        } else if (getInstance().mCurrentColor == WhiteBoardVariable.Color.ORANGE) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_orange_selector);
        } else if (getInstance().mCurrentColor == WhiteBoardVariable.Color.PINK) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_pink_selector);
        } else if (getInstance().mCurrentColor == WhiteBoardVariable.Color.PURPLE) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_purple_selector);
        } else if (getInstance().mCurrentColor == WhiteBoardVariable.Color.GREEN) {
            mFabMenuColor.setAddButtonBackground(R.drawable.white_board_color_green_selector);
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
        int size = getInstance().getSavePoints().size();
        if (size < 1) {
            return;
        }
        DrawPoint dp = getInstance().getSavePoints().get(size - 1);
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
     * ?????????????????????
     */
    private void setEraserSize(int size) {
        ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
        getInstance().mCurrentEraserSize = size;
        changeEraserBack();
        mDbView.setEraserSize();
    }

    /**
     * ?????????????????????????????????
     */
    private void changeEraserBack() {
        if (getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.LARRGE) {
            mBtEraserLarge.drawCircleAndRing(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMiddle.drawCircle(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMini.drawCircle(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);
        } else if (getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.MIDDLE) {
            mBtEraserLarge.drawCircle(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMiddle.drawCircleAndRing(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMini.drawCircle(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);
        } else if (getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.MINI) {
            mBtEraserLarge.drawCircle(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMiddle.drawCircle(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            mBtEraserMini.drawCircleAndRing(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);

        }
    }

    /**
     * ????????????
     */
    private void newPage() {
        OperationUtils.getInstance().newPage();
        showPoints();
    }

    /**
     * ?????????
     */
    private void prePage() {
        if (getInstance().mCurrentIndex > 0) {
            getInstance().mCurrentIndex--;
            showPoints();
        }
    }

    /**
     * ?????????
     */
    private void nextPage() {
        if (getInstance().mCurrentIndex + 1 < getInstance().getDrawPointSize()) {
            getInstance().mCurrentIndex++;
            showPoints();
        }
    }

    /**
     * ??????????????????
     */
    private void showPoints() {
        mDbView.showPoints();
//        mDtView.showPoints();
        mTvWhiteBoardPage.setText("" + (getInstance().mCurrentIndex + 1) + "/" + getInstance().getDrawPointSize());
        showPageVisiable();
        showUndoRedo();
    }

    /**
     * ??????????????????????????????
     */
    private void showSinglePoint(Path mpath, Paint mpaint) {
        if (isfirstinit) {
            mDbView.initSinglePoint();
            isfirstinit = false;
        }
        mDbView.showSinglePoint(mpath,mpaint);
//        mDtView.showPoints();
//        mTvWhiteBoardPage.setText("" + (getInstance().mCurrentIndex + 1) + "/" + getInstance().getDrawPointSize());
//        showPageVisiable();
        showUndoRedo();
    }

    /**
     * ??????????????????????????????
     */
    private void showPageVisiable() {
        if (getInstance().mCurrentIndex + 1 == getInstance().getDrawPointSize()) {
            mIvWhiteBoardNext.setImageResource(R.drawable.white_board_next_page_click);
        } else {
            mIvWhiteBoardNext.setImageResource(R.drawable.white_board_next_page_selector);
        }
        if (getInstance().mCurrentIndex == 0) {
            mIvWhiteBoardPre.setImageResource(R.drawable.white_board_pre_page_click);
        } else {
            mIvWhiteBoardPre.setImageResource(R.drawable.white_board_pre_page_selector);
        }

    }

    /**
     * ??????
     */
    private void undo() {
        // ?????????????????????????????????ws????????????????????????????????????
        if(UserUtil.isTempMeeting && UserUtil.ISCHAIRMAN){
            sendUedoMsg();
        }
        int size = getInstance().getSavePoints().size();
        if (size == 0) {
            return;
        } else {
            getInstance().getDeletePoints().add(getInstance().getSavePoints().get(size - 1));
            getInstance().getSavePoints().remove(size - 1);
            size = getInstance().getDeletePoints().size();
            if (getInstance().getDeletePoints().get(size - 1).getType() == OperationUtils.DRAW_PEN) {
                mDbView.undo();
            } else if (getInstance().getDeletePoints().get(size - 1).getType() == OperationUtils.DRAW_TEXT) {
                mDtView.undo();
            }
            showUndoRedo();
        }

    }

    /**
     * ??????
     */
    private void redo() {
        if(UserUtil.isTempMeeting && UserUtil.ISCHAIRMAN){
            sendRedoMsg();
        }

        int size = getInstance().getDeletePoints().size();
        if (size == 0) {
            return;
        } else {
            getInstance().getSavePoints().add(getInstance().getDeletePoints().get(size - 1));
            getInstance().getDeletePoints().remove(size - 1);
            size = getInstance().getSavePoints().size();
            if (getInstance().getSavePoints().get(size - 1).getType() == OperationUtils.DRAW_PEN) {
                mDbView.redo();
            } else if (getInstance().getSavePoints().get(size - 1).getType() == OperationUtils.DRAW_TEXT) {
                mDtView.redo();
            }
            showUndoRedo();
        }

    }

    /**
     *  ??????????????????
     * */
    private void sendSwapMsg() {
        if (!isShareConnected || !JWebSocketClientService.client.isOpen()) {
            return;
        }

        new Thread() {
            @Override
            public void run() {
                WSWhiteBoardBean whiteBean = new  WSWhiteBoardBean();
                whiteBean.setUser_name(UserUtil.user_name);
                whiteBean.setType(sendSwapMsg);
                whiteBean.setDrawPenStr(new DrawPenStr());

                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPSHARE);
                bean.setBody(whiteBean);

                String strJson = gson.toJson(bean,type);
                send(strJson);

            }
        }.start();
    }

    private void sendRedoMsg() {
        if (!isShareConnected || !JWebSocketClientService.client.isOpen()) {
            return;
        }

        new Thread() {
            @Override
            public void run() {
                WSWhiteBoardBean whiteBean = new  WSWhiteBoardBean();
                whiteBean.setUser_name(UserUtil.user_name);
                whiteBean.setType(sendRedoMsg);
                whiteBean.setDrawPenStr(new DrawPenStr());

                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPSHARE);
                bean.setBody(whiteBean);

                String strJson = gson.toJson(bean,type);
                send(strJson);

            }
        }.start();
    }

    private void sendUedoMsg() {
        if (!isShareConnected || !JWebSocketClientService.client.isOpen()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                WSWhiteBoardBean whiteBean = new  WSWhiteBoardBean();
                whiteBean.setUser_name(UserUtil.user_name);
                whiteBean.setType(sendUedoMsg);
                whiteBean.setDrawPenStr(new DrawPenStr());

                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPSHARE);
                bean.setBody(whiteBean);

                String strJson = gson.toJson(bean,type);
                send(strJson);

            }
        }.start();
    }
    /**
     * ??????????????????
     */
    private void afterEdit(boolean isSave) {
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
                if (getInstance().mCurrentOPerationPen == WhiteBoardVariable.Operation.PEN_EXPAND
                        || getInstance().mCurrentOPerationColor == WhiteBoardVariable.Operation.COLOR_EXPAND
                        || getInstance().mCurrentOPerationText == WhiteBoardVariable.Operation.TEXT_EXPAND
                        || getInstance().mCurrentOPerationEraser == WhiteBoardVariable.Operation.ERASER_EXPAND) {
                    mVBottomBack.setVisibility(View.VISIBLE);
                } else {
                    mVBottomBack.setVisibility(View.GONE);
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
                mFabMenuColor.setVisibility(View.VISIBLE);
                switch (getInstance().mCurrentOPerationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        getInstance().mCurrentDrawType = OperationUtils.DRAW_PEN;
                        mDbView.setPaint(null);
                        mFabMenuSize.setAddButtonBackground(R.drawable.white_board_pen_selected_selector);
                        getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        mFabMenuSize.expand();
                        changePenBack();
                        getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        mFabMenuSize.collapse();
                        getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (getInstance().mCurrentOPerationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        mFabMenuSize.clearDraw();
                        mFabMenuSize.setAddButtonBackground(R.drawable.white_board_pen_selector);
                        getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        mFabMenuSize.collapse();
                        mFabMenuSize.clearDraw();
                        mFabMenuSize.setAddButtonBackground(R.drawable.white_board_pen_selector);
                        getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (getInstance().mCurrentOPerationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        mFabMenuSize.collapse();
                        getInstance().mCurrentOPerationPen = WhiteBoardVariable.Operation.PEN_CLICK;
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
                switch (getInstance().mCurrentOPerationColor) {
                    case WhiteBoardVariable.Operation.COLOR_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.COLOR_EXPAND:
                        mFabMenuColor.collapse();
                        getInstance().mCurrentOPerationColor = WhiteBoardVariable.Operation.COLOR_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
                switch (getInstance().mCurrentOPerationColor) {
                    case WhiteBoardVariable.Operation.COLOR_NORMAL:
                        mFabMenuColor.expand();
                        getInstance().mCurrentOPerationColor = WhiteBoardVariable.Operation.COLOR_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.COLOR_EXPAND:
                        mFabMenuColor.collapse();
                        getInstance().mCurrentOPerationColor = WhiteBoardVariable.Operation.COLOR_NORMAL;
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
                switch (getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        getInstance().mCurrentDrawType = OperationUtils.DRAW_TEXT;
                        mFabMenuText.setAddButtonBackground(R.drawable.white_board_text_selected_selector);
                        getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        int size = getInstance().getSavePoints().size();
                        if (size > 0) {
                            DrawPoint dp = getInstance().getSavePoints().get(size - 1);
                            if (dp.getType() == OperationUtils.DRAW_TEXT && dp.getDrawText().getStatus() == DrawTextView.TEXT_DETAIL) {
                                changeTextBack();
                                mFabMenuText.expand();
                                getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_EXPAND;
                            }
                        }
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        mFabMenuText.collapse();
                        getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.PEN_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        mFabMenuText.clearDraw();
                        mFabMenuText.setAddButtonBackground(R.drawable.white_board_text_selector);
                        getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        mFabMenuText.collapse();
                        mFabMenuText.clearDraw();
                        mFabMenuText.setAddButtonBackground(R.drawable.white_board_text_selector);
                        getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (getInstance().mCurrentOPerationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        mFabMenuText.collapse();
                        getInstance().mCurrentOPerationText = WhiteBoardVariable.Operation.TEXT_CLICK;
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

                mFabMenuColor.setVisibility(View.INVISIBLE);

                switch (getInstance().mCurrentOPerationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        getInstance().mCurrentDrawType = OperationUtils.DRAW_ERASER;
                        mDbView.changeEraser();
                        mFabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selected_selector);
                        getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        mFabMenuEraser.expand();
                        changeEraserBack();
                        getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        mFabMenuEraser.collapse();
                        getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.PEN_CLICK:
                switch (getInstance().mCurrentOPerationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        mFabMenuEraser.clearDraw();
                        mFabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selector);
                        getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        mFabMenuEraser.collapse();
                        mFabMenuEraser.clearDraw();
                        mFabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selector);
                        getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (getInstance().mCurrentOPerationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        mFabMenuEraser.collapse();
                        getInstance().mCurrentOPerationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                }
                break;
        }
    }

    /**
     * ???????????????????????????
     */
    public void saveImage() {
        String fileName = StoreUtil.getPhotoSavePath();
        Log.e(TAG, fileName);
        File file = new File(fileName);
        String content = null;
        String title   = null;
        try {
            File directory = file.getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                ToastUtil.makeText(getActivity(),getString(R.string.white_board_export_fail));
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
            if(fileName.contains("/storage/emulated/0")){
                title = "??????????????????!???????????????:"+ fileName.replace("/storage/emulated/0","???????????????");
            }
        } catch (Exception e) {
            title = getString(R.string.white_board_export_fail);
            e.printStackTrace();
        }


        CVIPaperDialogUtils.showCountDownConfirmDialog(getActivity(),title,"?????????",true,new CVIPaperDialogUtils.ConfirmDialogListener() {
            @Override
            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                if(clickConfirm){

                }
            }
        });
    }


    /**
     * ?????????????????????????????????????????????
     */
//    protected void sendFileData() {
//
//        WhiteBoardPoints whiteBoardPoints = OperationUtils.getInstance().getWhiteBoardPoints();
//        if (whiteBoardPoints == null || whiteBoardPoints.getWhiteBoardPoints() == null || whiteBoardPoints.getWhiteBoardPoints().isEmpty()) {
//            return;
//        }
//        String strJson = new Gson().toJson(whiteBoardPoints);
//        send(strJson);
//    }


    /**
     * ????????????????????????
     */
    protected void sendsingleFileData() {
        if (!isShareConnected || !JWebSocketClientService.client.isOpen()) {
            return;
        }
        DrawPoint whiteBoardPoint = getInstance().getMsingleWhiteBoardPoint();
        if (whiteBoardPoint == null) {
            return;
        }

        new Thread() {
            @Override
            public void run() {
                WSWhiteBoardBean whiteBean = new  WSWhiteBoardBean();
                whiteBean.setUser_name(UserUtil.user_name);
                whiteBean.setType(sendMsg);
                whiteBean.setDrawPenStr(whiteBoardPoint.getDrawPenStr());
                ArrayList<String> arr = new ArrayList<>();
                whiteBean.setUser_list(arr);

                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPSHARE);
                bean.setBody(whiteBean);

                String strJson = gson.toJson(bean,type);
                send(strJson);
            }
        }.start();
    }

    /**
     * ????????????????????????
     */
    private void sendDisconnectMsg() {
        if (!JWebSocketClientService.client.isOpen()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPSHAREDIS);
                bean.setBody("");
                String strJson = new Gson().toJson(bean);
                send(strJson);
            }
        }.start();
    }
    /**
     * ????????????????????????,??????
     */
    private void sendShareReqMsg() {
        if (!JWebSocketClientService.client.isOpen()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(constant.TEMPSHAREREQ);
                bean.setBody("");
                String strJson = new Gson().toJson(bean);
                send(strJson);

            }
        }.start();
        isShareConnected = true;
        // ??????
        mDbView.clearImage();
        ToastUtil.makeText(getActivity(),"????????????????????????!");
    }


    private void send(final String str) {
        if (str.length() == 0) {
            return;
        }
        JWebSocketClientService.sendMsg(str);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        getActivity().unregisterReceiver(myRedoReceiver);

        if (Hawk.contains(constant.team_share)) {
            Hawk.delete(constant.team_share);
        }
    }

    @Override
    public void sendsinglefile() {
        sendsingleFileData();
    }


    @ReceiveEvents(name = Events.WHITE_BOARD_TEXT_EDIT)
    private void textEdit() {//????????????
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
        if (getInstance().getSavePoints().isEmpty()) {
            mIvWhiteBoardUndo.setVisibility(View.INVISIBLE);
            mIvWhiteBoardExport.setVisibility(View.INVISIBLE);
            mIvWhiteBoardSave.setVisibility(View.INVISIBLE);
        } else {
            mIvWhiteBoardUndo.setVisibility(View.VISIBLE);
            mIvWhiteBoardExport.setVisibility(View.VISIBLE);
            mIvWhiteBoardSave.setVisibility(View.VISIBLE);
        }
        if (getInstance().getDeletePoints().isEmpty()) {
            mIvWhiteBoardRedo.setVisibility(View.INVISIBLE);
        } else {
            mIvWhiteBoardRedo.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        myRedoReceiver = new MyShareWhiteFragment.MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.SHOW_UI);
        getActivity().registerReceiver(myRedoReceiver, filter);
        initView();
        initEvent();
        //  ??????websocket????????????
        EventBus.getDefault().register(this);
        mDbView.sendfile = (DrawPenView.sendSingleFile) this;
        return rootView;
    }
    /**
     *  ??????websocket ??????
     * */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onReceiveMsg(EventMessage message) {
//        if(UserUtil.ISCHAIRMAN || !message.getMessage().contains(constant.TEMPSHARE) || StringUtils.isEmpty(message.getMessage()) ) {
//            return;
//        }
        if(!message.getMessage().contains(constant.TEMPSHARE) || StringUtils.isEmpty(message.getMessage()) ) {
            return;
        }
//        Log.e("????????????", "onReceiveMsg: " + message.toString());
        try {

            TempWSBean<WSWhiteBoardBean> wsebean = gson.fromJson(message.getMessage(), type);
            if(wsebean==null || !wsebean.getPackType().equals(constant.TEMPSHARE) || wsebean.getUserMac_id().equals(FLUtil.getMacAddress())){
                return;
            }
            WSWhiteBoardBean wsWhiteBoardBean = wsebean.getBody();
            DrawPenStr drawPenStr = wsWhiteBoardBean.getDrawPenStr();

            if(wsWhiteBoardBean.getType().equals(sendMsg)){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        DrawPoint drawPoint = StoreUtil.convertSingleWhiteBoardPoint(drawPenStr);
                        showSinglePoint(drawPoint.getDrawPen().getPath(),drawPoint.getDrawPen().getPaint());
                    }
                });
            }else if(wsWhiteBoardBean.getType().equals(sendUedoMsg)){
                undo();
            }else if(wsWhiteBoardBean.getType().equals(sendRedoMsg)){
                redo();
            }else if(wsWhiteBoardBean.getType().equals(sendSwapMsg)){
                // ?????????????????????
                if(wsebean.getUserMac_id().equals(FLUtil.getMacAddress())){
                    return;
                }
                mDbView.clearImage();
            }
        }catch (Exception e){
            Log.d(" Gson().fromJson",""+ e.getLocalizedMessage());
        }
    }

    @Override
    public void onTypeClick(ToFragmentType toFragmentType, ToFragmentIsConnectedType isConnected) {
        Log.e(TAG,"onTypeClick");
        switch (toFragmentType) {
            case SaveData:
                OperationUtils.getInstance().setmWhiteBoardPointList(getInstance().getWhiteBoardPoints(),0);
                break;
            case Close:
                mDbView.clearImage();
                OperationUtils.getInstance().setmWhiteBoardPointList(null,0);
                break;
            case IsConnected:
                isShareConnected = isConnected.equals(ToFragmentIsConnectedType.isConnected);
                break;

        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (constant.SHOW_UI.equals(intent.getAction())) {
                showUndoRedo();
            }
        }
    }

}
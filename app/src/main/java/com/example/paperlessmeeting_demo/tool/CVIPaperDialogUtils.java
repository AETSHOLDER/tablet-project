package com.example.paperlessmeeting_demo.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;

public class CVIPaperDialogUtils {

    private static String mtTpStr = "提示";
    private static String mErrorStr = "错误";
    private static String mConfirmStr = "知道了";
    private static String mCancelStr = "取消";
    private static Dialog confirmDialog;
    private static CountDownTimer countDownTimer;
    /**
     *  弹出确认倒计时弹框 ，只有一个确定按钮
     * */
    public static void showCountDownConfirmDialog(Activity context, String title, String confirmStr, boolean canceledOnTouchOutside,final ConfirmDialogListener confirmDialogListener) {
        showAlertDialog(context, title,null, confirmStr,3,3000, canceledOnTouchOutside,confirmDialogListener);
    }
    /**
     *  弹出确认倒计时弹框 ，两个按钮
     * */
    public static void showCountDownCustomDialog(Activity context, String title, String confirmStr, boolean canceledOnTouchOutside,final ConfirmDialogListener confirmDialogListener) {
        showAlertDialog(context, title,null, confirmStr,4, 9000, canceledOnTouchOutside,confirmDialogListener);
    }
    /**
     *  弹出倒计时弹框 ，默认倒计时9秒
     * */
    public static void showCountDownDialog(Activity context, String title, String confirmStr,long countDown, boolean canceledOnTouchOutside,final ConfirmDialogListener confirmDialogListener) {
        showAlertDialog(context, title,null, confirmStr,4,countDown, canceledOnTouchOutside,confirmDialogListener);
    }

    /**
     *  弹出确认弹框 ，只有一个确定按钮
     * */
    public static void showConfirmDialog(Activity context, String title, String confirmStr, boolean canceledOnTouchOutside,final ConfirmDialogListener confirmDialogListener) {
        showAlertDialog(context, title,null, confirmStr,1,0, canceledOnTouchOutside,confirmDialogListener);
    }

    /**
     *  弹出普通弹框 ，底部两个按钮(确认，取消)
     * */
    public static void showCustomDialog(Activity context, String title,String content, String confirmStr, boolean canceledOnTouchOutside,final ConfirmDialogListener confirmDialogListener) {
        showAlertDialog(context, title, content,confirmStr,2,0, canceledOnTouchOutside,confirmDialogListener);
    }
    private static void showAlertDialog(Activity context, String title, String content, String confirmStr, int type,long countDown, boolean canceledOnTouchOutside, final ConfirmDialogListener confirmDialogListener) {
        if (context == null || context.isFinishing()) {
            return;
        }
        closeKeyboardHidden(context);
        initDialog();
         confirmDialog = new Dialog(context, R.style.dialogTransparent);
       // AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.dialogTransparent);

        View view;
        switch (type) {
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
                break;
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
                break;
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
                break;
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
                break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
                break;
        }
        confirmDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        confirmDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        //将布局设置给Dialog
        confirmDialog.setContentView(view);

        TextView confirm = view.findViewById(R.id.confir_btn);
        TextView contentTxt = (TextView) view.findViewById(R.id.content_tv);
        confirmDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        confirmDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        confirmDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                confirmDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        confirmDialog.show();

        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = confirmDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.45); //设置宽度
        lp.height = (int) (display.getHeight() * 0.23); //设置宽度
        confirmDialog.getWindow().setAttributes(lp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmDialogListener != null) {
                    confirmDialogListener.onClickButton(true, false);
                }
                if (confirmDialog != null && confirmDialog.isShowing()) {
                    confirmDialog.dismiss();
                }
                if(countDownTimer != null){
                    countDownTimer.cancel();
                    countDownTimer = null;
                    Log.e("","countDownTimer已取消");
                }
            }

        });
        if(type == 2 || type == 4){
            TextView cancel = view.findViewById(R.id.cancel_btn);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (confirmDialogListener != null) {
                        confirmDialogListener.onClickButton(false, true);
                    }
                    if (confirmDialog != null && confirmDialog.isShowing()) {
                        confirmDialog.dismiss();
                    }
                    if(type == 4 && countDownTimer != null){
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                }
            });
        }

        String currentStr = null;
        if(TextUtils.isEmpty(confirmStr)){
            confirm.setText(mConfirmStr);
            currentStr = mConfirmStr;
        }else {
            confirm.setText(confirmStr);
            currentStr = confirmStr;
        }

        if(type == 3 || type == 4){
            String finalCurrentStr = currentStr;
            countDownTimer = new CountDownTimer(countDown, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String value = " (" + String.valueOf((int) (millisUntilFinished / 1000)) + "s)";
                    confirm.setText(finalCurrentStr + value);
                }

                @Override
                public void onFinish() {
                    Log.e("CVIPaperDialogUtils","倒计时结束");
                    if(type == 4){
                        confirm.setText("正在进入");
                    }
                    confirm.performClick();

                }
            }.start();
        }

        if(contentTxt != null ){
            if(TextUtils.isEmpty(content)){
                contentTxt.setVisibility(View.GONE);
            }else {
                contentTxt.setVisibility(View.VISIBLE);
                contentTxt.setText(content);
            }
        }

        TextView title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText(title);
    }

    /**
     * 隐藏软键盘
     **/
    private static void closeKeyboardHidden(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 新的對話框彈出之前，先把舊的清除掉
     */
    private static void initDialog() {
        if (confirmDialog != null && confirmDialog.isShowing()) {
            confirmDialog.dismiss();
            confirmDialog = null;
        }
        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public interface ConfirmDialogListener {
        void onClickButton(boolean clickConfirm, boolean clickCancel);
    }

}
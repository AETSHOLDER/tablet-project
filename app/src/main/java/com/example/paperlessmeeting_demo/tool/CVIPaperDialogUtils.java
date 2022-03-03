package com.example.paperlessmeeting_demo.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
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
    private static AlertDialog confirmDialog;

    /**
     *  弹出确认倒计时弹框 ，只有一个确定按钮
     * */
    public static void showCountDownConfirmDialog(Activity context, String title, String confirmStr, boolean canceledOnTouchOutside,final ConfirmDialogListener confirmDialogListener) {
        showAlertDialog(context, title,null, confirmStr,3, canceledOnTouchOutside,confirmDialogListener);
    }
    /**
     *  弹出确认倒计时弹框 ，两个按钮
     * */
    public static void showCountDownCustomDialog(Activity context, String title, String confirmStr, boolean canceledOnTouchOutside,final ConfirmDialogListener confirmDialogListener) {
        showAlertDialog(context, title,null, confirmStr,4, canceledOnTouchOutside,confirmDialogListener);
    }
    /**
     *  弹出确认弹框 ，只有一个确定按钮
     * */
    public static void showConfirmDialog(Activity context, String title, String confirmStr, boolean canceledOnTouchOutside,final ConfirmDialogListener confirmDialogListener) {
        showAlertDialog(context, title,null, confirmStr,1, canceledOnTouchOutside,confirmDialogListener);
    }
    /**
     *  弹出普通弹框 ，底部两个按钮(确认，取消)
     * */
    public static void showCustomDialog(Activity context, String title,String content, String confirmStr, boolean canceledOnTouchOutside,final ConfirmDialogListener confirmDialogListener) {
        showAlertDialog(context, title, content,confirmStr,2, canceledOnTouchOutside,confirmDialogListener);
    }
    private static void showAlertDialog(Activity context, String title, String content, String confirmStr, int type, boolean canceledOnTouchOutside, final ConfirmDialogListener confirmDialogListener) {
        if (context == null || context.isFinishing()) {
            return;
        }
        closeKeyboardHidden(context);
        initDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        builder.setView(view);
        builder.setCancelable(canceledOnTouchOutside);
        Button confirm = view.findViewById(R.id.confir_btn);
        TextView contentTxt = (TextView) view.findViewById(R.id.content_tv);
        confirmDialog = builder.create();
        confirmDialog.show();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = confirmDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.33); //设置宽度

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
            }

        });
        if(type == 2 || type == 4){
            Button cancel = view.findViewById(R.id.cancel_btn);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (confirmDialogListener != null) {
                        confirmDialogListener.onClickButton(false, true);
                    }
                    if (confirmDialog != null && confirmDialog.isShowing()) {
                        confirmDialog.dismiss();
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
            CountDownTimer countDownTimer = new CountDownTimer(9000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String value = " (" + String.valueOf((int) (millisUntilFinished / 1000)) + "s)";
                    confirm.setText(finalCurrentStr + value);
                }

                @Override
                public void onFinish() {
                    if(type == 3){
                        confirm.setText("正在进入");
                        confirm.performClick();
                    }
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
    }

    public interface ConfirmDialogListener {
        void onClickButton(boolean clickConfirm, boolean clickCancel);
    }

}
package com.example.paperlessmeeting_demo.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.paperlessmeeting_demo.R;

public class MyDialog   extends Dialog {

    public onTouchOutsideInterFace onTouchOutside;

    public onTouchOutsideInterFace getOnTouchOutside() {
        return onTouchOutside;
    }

    public void setOnTouchOutside(onTouchOutsideInterFace onTouchOutside) {
        this.onTouchOutside = onTouchOutside;
    }

    public MyDialog(Context context) {

        super(context, R.style.dialogTransparent);

    }

    public MyDialog(Context context, int themeResId) {

        super(context, R.style.dialogTransparent);

    }

    public  interface onTouchOutsideInterFace{

        void outSide();
    };

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        /* 触摸外部弹窗 */

        if (isOutOfBounds(getContext(), event)) {

            onTouchOutside.outSide();

        }

        return super.onTouchEvent(event);

    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {

        final int x = (int) event.getX();

        final int y = (int) event.getY();

        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();

        final View decorView = getWindow().getDecorView();

        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))

                || (y > (decorView.getHeight() + slop));

    }
}

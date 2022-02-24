package com.example.paperlessmeeting_demo.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 梅涛 on 2021/3/29.
 */

public class VerticalSeekBar extends AppCompatSeekBar {
    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //将SeekBar旋转90度
        canvas.rotate(-90);
        //将SeekBar移动到原来的位置
        canvas.translate(-canvas.getHeight(), 0);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            //用户开始触摸
            case MotionEvent.ACTION_DOWN:
                if (onChangeListener != null){
                    onChangeListener.onStartTrackingTouch(this);
                }
                break;
                //用户在移动
            case MotionEvent.ACTION_MOVE:
                int i;
                if (onChangeListener != null) {
                    i = getMax() - (int) (getMax() * event.getY() / getHeight());
                    onChangeListener.onProgressChanged(this, i, true);
                }
                break;

                //用户离开屏幕
            case MotionEvent.ACTION_UP:
                //获取滑动的距离
                i = getMax() - (int) (getMax() * event.getY() / getHeight());
                //设置进度
                setProgress(i);
                //每次拖动SeekBar都会调用
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                if (onChangeListener != null){
                    onChangeListener.onStopTrackingTouch(this);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private OnSeekBarChangeListener onChangeListener;
    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onChangeListener){
        this.onChangeListener = onChangeListener;
    }
}

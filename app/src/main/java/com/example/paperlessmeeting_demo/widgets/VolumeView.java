package com.example.paperlessmeeting_demo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.paperlessmeeting_demo.R;

/**
 * Created by 梅涛 on 2021/3/29.
 */

public class VolumeView extends View {
    private Paint paint;
    // 控件宽度
    private int width = 430;
    // 控件高度
    private int height = 100;
    // 两个音量矩形最左侧之间的间隔
    private int rectMargin = 10;
    // 音量矩形高
    private int rectH = 30;
    // 音量矩形宽
    private int rectW = 15;
    // 未选中音量颜色
    private int unChoiceVolumeColor;
    // 选中音量颜色
    private int choiceVolumeColor;
    // 当前音量
    private int currentVolume;
    // 最大音量
    private int maxVolume;
    // 音量减-左坐标
    private int minusLeft;
    // 音量减-右坐标
    private int minusRight;
    // 音量加-左坐标
    private int plusLeft;
    // 音量加-右坐标
    private int plusRight;
    //音量图标
    private Bitmap volumeIcon;
    //音量关闭图标
    private Bitmap volumeCloseIcon;
    //减音量图标
    private Bitmap minusIcon;
    //加音量图标
    private Bitmap plusIcon;

    private OnVolumeChangedListener onVolumeChangedListener;

    public VolumeView(Context context) {
        this(context, null);
    }

    public VolumeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VolumeView);
        choiceVolumeColor = typedArray.getColor(R.styleable.VolumeView_volumeColor, Color.BLACK);
        unChoiceVolumeColor = typedArray.getColor(R.styleable.VolumeView_defaultVolumenColor, Color.WHITE);
        currentVolume = typedArray.getInteger(R.styleable.VolumeView_volume, 0);
        maxVolume = typedArray.getInteger(R.styleable.VolumeView_max, 0);
        typedArray.recycle();

        paint = new Paint();

        volumeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_volume);
        volumeCloseIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_close_volume);
        minusIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_add_hot);
        plusIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_add_usb);
    }

    public void setOnVolumeChangedListener(OnVolumeChangedListener onVolumeChangedListener) {
        this.onVolumeChangedListener = onVolumeChangedListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (onVolumeChangedListener != null) {
            onVolumeChangedListener.onVolumenChanged(currentVolume);
        }

        if (currentVolume == 0) {
            canvas.drawBitmap(volumeCloseIcon, 0, 0, paint);
        } else {
            canvas.drawBitmap(volumeIcon, 0, 0, paint);
        }

        int iconWidth = volumeIcon.getWidth();
        int iconHeight = volumeIcon.getHeight();

        int offsetTop = (iconHeight - minusIcon.getHeight()) / 2;
        int offsetLeft = iconWidth + rectMargin * 2;
        minusLeft = iconWidth;
        canvas.drawBitmap(minusIcon, offsetLeft, offsetTop, paint);

        int offsetVolumeLeft = offsetLeft + minusIcon.getWidth() + rectMargin * 2;
        minusRight = offsetVolumeLeft;
        int offsetVolumeTop = (iconHeight - rectH) / 2;
        paint.setColor(choiceVolumeColor);
        for (int i = 0; i < currentVolume; i++) {
            int left = offsetVolumeLeft + i * rectW + i * rectMargin;
            canvas.drawRect(left, offsetVolumeTop, left + rectW, offsetVolumeTop + rectH, paint);
        }

        paint.setColor(unChoiceVolumeColor);
        for (int i = currentVolume; i < maxVolume; i++) {
            int left = offsetVolumeLeft + i * rectW + i * rectMargin;
            canvas.drawRect(left, offsetVolumeTop, left + rectW, offsetVolumeTop + rectH, paint);
        }

        int offsetPlusTop = (iconHeight - plusIcon.getHeight()) / 2;
        int offsetPlusLeft = offsetVolumeLeft + maxVolume * rectW + maxVolume * rectMargin + rectMargin;
        plusLeft = offsetVolumeLeft + maxVolume * rectW + (maxVolume - 1) * rectMargin;
        plusRight = offsetPlusLeft + plusIcon.getWidth() + rectMargin;
        canvas.drawBitmap(plusIcon, offsetPlusLeft, offsetPlusTop, paint);
    }

    public void addVolume() {
        if (currentVolume >= maxVolume) {
            return;
        }
        currentVolume++;
        invalidate();
    }

    public void minusVolume() {
        if (currentVolume <= 0) {
            return;
        }
        currentVolume--;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float eventX = event.getX();
            if (eventX >= minusLeft && eventX <= minusRight) {//minusVolume
                minusVolume();
                return true;
            } else if (eventX >= plusLeft && eventX <= plusRight) {//addVolume
                addVolume();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public interface OnVolumeChangedListener {
        void onVolumenChanged(int volume);
    }


}

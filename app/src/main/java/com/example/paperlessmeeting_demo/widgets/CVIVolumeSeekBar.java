package com.example.paperlessmeeting_demo.widgets;

import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import com.example.paperlessmeeting_demo.R;


public class CVIVolumeSeekBar extends View {
    /**
     * 刻度线画笔
     */
    private Paint mRulerPaint;

    /**
     * 刻度线的个数,等分数等于刻度线的个数加1
     */
    private int mRulerCount = 10;

    /**
     * 每条刻度线的宽度
     */
    private int mRulerWidth = 2;

    /**
     * 刻度线的颜色
     */
    private int mRulerColor = Color.BLACK;
    private int mRulerColor2 = Color.WHITE;

    private float maxCount = 100; //进度条最大值
    private float currentCount; //进度条当前值
    // private Paint mPaint ;
    private int mWidth,mHeight;
    private Context mContext;
    private float currentX;
    // 滑动监听
    private CVIVolumeSeekBar.onVolumeChangeListener onVolumeChangeListener;
    public CVIVolumeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    public CVIVolumeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public CVIVolumeSeekBar(Context context) {
        super(context);
        initView(context);
    }
    /**
     *  获取当前值
     * */

    public int getCurrentProgress() {
        float integrity = currentX/mWidth *maxCount;
        return Math.round(integrity);
    }

    private void initView(Context context) {
        mContext=context;
        //创建绘制刻度线的画笔
        mRulerPaint = new Paint();
        mRulerPaint.setColor(mRulerColor);
        mRulerPaint.setAntiAlias(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //极限条件校验
        if (getWidth() <= 0 || mRulerCount <= 0) {
            return;
        }
        currentX = currentCount * mWidth /maxCount;
        //获取每一份的长度
        int length = (getWidth() - getPaddingLeft() - getPaddingRight() - mRulerCount * mRulerWidth) / (mRulerCount + 1);

        //计算刻度线的顶部坐标和底部坐标
        int rulerTop = getHeight() / 2 - (getMinimumHeight() / 2 ) + 20;
        int rulerBottom = getMinimumHeight() -20;

        //获取滑块的位置信息
        Rect thumbRect =  new Rect(0, 0, mWidth, mHeight);

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int round = mHeight/3; //半径
        mPaint.setColor(getResources().getColor(R.color.color_volume_bg)); //设置边框背景颜色
        RectF rectBg = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectBg, round, round, mPaint);//绘制 最外面的大 圆角矩形，背景为白色
        float section = currentCount/maxCount; //进度条的比例
        RectF rectProgressBg = new RectF(0, 0, mWidth*section, mHeight);
        Log.e("VolumeSeekBar", currentCount+"");
        Log.e("VolumeSeekBar", section+"");
        //Paint设置setColor(白色无透明)和setShader，只让setShader生效；不然前面setColor设置了透明度，透明度会生效，和setShader效果叠加
        mPaint.setColor(getResources().getColor(R.color.white));
        mPaint.setShader(getLinearGradient());
        canvas.drawRoundRect(rectProgressBg, round, round, mPaint); //最左边的圆角矩形


        //绘制刻度线
        for (int i = 1; i <= mRulerCount; i++) {
            //计算刻度线的左边坐标和右边坐标
            int rulerLeft = i * length + getPaddingLeft();
            int rulerRight = rulerLeft + mRulerWidth;
            //判断是否需要绘制刻度线
//            if ( thumbRect != null && rulerLeft - getPaddingLeft() > thumbRect.left && rulerRight - getPaddingLeft() < thumbRect.right) {
                //进行绘制
                Rect mrect = new Rect(rulerLeft, rulerTop, rulerRight, rulerBottom);

                if(rulerRight < currentX){
                    mRulerPaint.setColor(mRulerColor2);
                }else {
                    mRulerPaint.setColor(mRulerColor);
                }

                canvas.drawRect(mrect,mRulerPaint);
                continue;
//            }
        }

        if (currentCount < maxCount-2 && currentCount>2 ){ //如果不是100%，绘制第三段矩形
            RectF rectProgressBg2 = new RectF(mWidth*section-round, 0, mWidth*section, mHeight);

            mPaint.setShader(getLinearGradient());
            canvas.drawRect(rectProgressBg2, mPaint);

            Paint newPaint = new Paint();
            newPaint.setAntiAlias(true);
            int round2 = 5; //半径
            newPaint.setColor(Color.WHITE); //设置边框背景颜色
            int rulerTop1 = getHeight() / 2 - (getMinimumHeight() / 2 ) + 15;
            int rulerBottom2 = getMinimumHeight() -15;
            RectF rectBg2 = new RectF(mWidth*section-18, rulerTop1, mWidth*section-10, rulerBottom2);
            canvas.drawRoundRect(rectBg2, round2, round2, newPaint); //最左边的圆角矩形
        }
    }
    private LinearGradient linearGradient;
    private LinearGradient getLinearGradient(){
        if(linearGradient==null){
            linearGradient = new LinearGradient(0, 0, getWidth(), mHeight, new int[]{mContext.getResources().getColor(R.color.progress_color_1),
                    mContext.getResources().getColor(R.color.progress_color_2),mContext.getResources().getColor(R.color.progress_color_1)}, null, Shader.TileMode.CLAMP); //根据R文件中的id获取到color
        }
        return linearGradient;
    }
    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip  == 0 ? 1 : -1));
    }
    /***
     * 设置最大的进度值
     * @param maxCount 最大的进度值
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }
    /***
     * 设置当前的进度值
     * @param currentCount 当前进度值
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }
    public float getMaxCount() {
        return maxCount;
    }
    public float getCurrentCount() {
        return currentCount;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(18);
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float integrity;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(event.getX()<0 || event.getX() >mWidth){
                    return false;
                }
                currentX = event.getX();
                integrity = currentX/mWidth *maxCount;
                setCurrentCount((int) integrity);
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getX()<0 ||  event.getX() >mWidth){
                    return false;
                }
                currentX = event.getX();
                integrity = currentX/mWidth *maxCount;
                setCurrentCount((int) integrity);
                if(onVolumeChangeListener != null){
                onVolumeChangeListener.volumechange(Math.round(integrity));
                break;
            }

            case MotionEvent.ACTION_UP:
                if(event.getX()<0 || event.getX() >mWidth){
                    return false;
                }
                integrity = currentX/mWidth *maxCount;
                if(onVolumeChangeListener != null){
                    onVolumeChangeListener.volumeStopChange(Math.round(integrity));
                }
                break;
        }
        return true;
    }

    public interface onVolumeChangeListener{
        void volumechange(int volume);

        void volumeStopChange(int volume);
    }

    public void setOnVolumeChangeListener(CVIVolumeSeekBar.onVolumeChangeListener onVolumeChangeListener) {
        this.onVolumeChangeListener = onVolumeChangeListener;
    }
}

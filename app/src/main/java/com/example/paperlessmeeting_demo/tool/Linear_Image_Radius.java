package com.example.paperlessmeeting_demo.tool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Linear设置圆角背景图片
 */
public class Linear_Image_Radius {
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        //创建和原始照片一样大小的矩形
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);//抗锯齿
        canvas.drawARGB(0, 0, 0, 0);

        //paint.setAlpha(1); //这个透明度好像不起作用，可以在主类里再设置
        paint.setColor(color);
        //画一个基于前面创建的矩形大小的圆角矩形
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        //设置相交模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //图片画到矩形
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}


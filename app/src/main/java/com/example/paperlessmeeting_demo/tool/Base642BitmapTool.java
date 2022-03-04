package com.example.paperlessmeeting_demo.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Base642BitmapTool {
    public final static Bitmap base642Bitmap(String base64) {
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; //图片宽高都为原来的二分之一，即图片为原来的四分一
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //inJustDecodeBounds参数设为true并加载图片。
        //这样做的原因是inJustDecodeBounds为true时
        //BitmapFactory只会解析图片的原始宽高信息，并不会真正的加载图片。
        options.inJustDecodeBounds = true;
        Bitmap mBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length,options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0){

            sampleSize = 1;

        } else if ( sampleSize > 6 ){

            sampleSize = 6;

        }

        options.inSampleSize = sampleSize;

        mBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length,options);



        return mBitmap;
    }



}

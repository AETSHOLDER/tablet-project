package com.example.paperlessmeeting_demo.activity.Sign.CallBack;

import com.example.paperlessmeeting_demo.activity.Sign.Bean.SignThumbBean;

public interface PictureLoaderCallback {
    /**
     * 更新ui状态
     * @param bitmap
     */
    public void bitmapCallback(SignThumbBean bitmap);

}

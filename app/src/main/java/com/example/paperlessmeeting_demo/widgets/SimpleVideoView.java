package com.example.paperlessmeeting_demo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 自定义VideoView
 */
public class SimpleVideoView extends VideoView {

	private int videoWidth;
	private int videoHeight;

	public SimpleVideoView(Context context) {
		super(context);
	}

	public SimpleVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SimpleVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(videoWidth, widthMeasureSpec);
		int height = getDefaultSize(videoHeight, heightMeasureSpec);
		if (videoWidth > 0 && videoHeight > 0) {
			//等比缩放宽高
			if (videoWidth * height > width * videoHeight) {
				height = videoHeight * width / videoWidth;//计算出新高
			} else if (videoWidth * height < width * videoHeight) {
				width = videoWidth * height / videoHeight;//计算出新宽
			}
		}
		setMeasuredDimension(width, height);
	}

	public int getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	public int getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}
}

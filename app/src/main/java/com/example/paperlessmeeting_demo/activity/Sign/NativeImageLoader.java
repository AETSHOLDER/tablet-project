package com.example.paperlessmeeting_demo.activity.Sign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import com.example.paperlessmeeting_demo.activity.Sign.Bean.SignThumbBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NativeImageLoader {
    private LruCache<String, SignThumbBean> mMemoryCache;
    private static NativeImageLoader mInstance = new NativeImageLoader();
    private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);

    private NativeImageLoader(){
        //获取应用程序的最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        //用最大内存的1/4来存储图片
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, SignThumbBean>(cacheSize) {

            //获取每张图片的大小
            @Override
            protected int sizeOf(String key, SignThumbBean signThumbBean) {
                if(signThumbBean.getThumb() != null){
                    return signThumbBean.getThumb().getRowBytes() * signThumbBean.getThumb().getHeight() / 1024;
                }else {
                    return 0;
                }

            }
        };
    }

    /**
     * 通过此方法来获取NativeImageLoader的实例
     * @return
     */
    public static NativeImageLoader getInstance(){
        return mInstance;
    }

    /**
     * 加载本地图片，对图片不进行裁剪
     * @param path
     * @param mCallBack
     * @return
     */
    public void loadNativeImage(final String path, final NativeImageCallBack mCallBack){
         this.loadNativeImage(path, null, mCallBack);
    }

    /**
     * 此方法来加载本地图片，这里的mPoint是用来封装ImageView的宽和高，我们会根据ImageView控件的大小来裁剪Bitmap
     * 如果你不想裁剪图片，调用loadNativeImage(final String path, final NativeImageCallBack mCallBack)来加载
     * @param path
     * @param mPoint
     * @param mCallBack
     * @return
     */
    public void loadNativeImage(final String path, final Point mPoint, final NativeImageCallBack mCallBack){
        //先获取内存中的Bitmap
        SignThumbBean signThumbBean = getBitmapFromMemCache(path);

        final Handler mHander = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mCallBack.onImageLoader((SignThumbBean)msg.obj, path);
            }

        };

        //若该Bitmap不在内存缓存中，则启用线程去加载本地的图片，并将Bitmap加入到mMemoryCache中
        if(signThumbBean == null){
            mImageThreadPool.execute(new Runnable() {

                @Override
                public void run() {
                    SignThumbBean vo = new SignThumbBean();

                    //先获取图片的缩略图
                    Bitmap mBitmap = decodeThumbBitmapForFile(path, mPoint == null ? 0: mPoint.x, mPoint == null ? 0: mPoint.y);
                    vo.setThumb(mBitmap);
                    vo.setPath(path);
                    vo.setCreatTime(getCreatTime(path));
                    Message msg = mHander.obtainMessage();
                    msg.obj = vo;
                    mHander.sendMessage(msg);
                    //将图片加入到内存缓存
                    addBitmapToMemoryCache(path, vo);
                }
            });
        }else {
            Message msg = mHander.obtainMessage();
            msg.obj = signThumbBean;
            mHander.sendMessage(msg);
        }
//        return signThumbBean;

    }
    /**
     * 往内存缓存中添加Bitmap
     *
     * @param key
     * @param signThumbBean
     */
    private void addBitmapToMemoryCache(String key, SignThumbBean signThumbBean) {
        if (getBitmapFromMemCache(key) == null && signThumbBean != null) {
            mMemoryCache.put(key, signThumbBean);
        }
    }

    /**
     * 根据key来获取内存中的图片
     * @param key
     * @return
     */
    private SignThumbBean getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    /**
     * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
     * @param path
     * @param viewWidth
     * @param viewHeight
     * @return
     */
    private Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //设置缩放比例
        options.inSampleSize = computeScale(options, viewWidth, viewHeight);

        //设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


    /**
     * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
     * @param options
     */
    private int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight){
        int inSampleSize = 1;
        if(viewWidth == 0 || viewWidth == 0){
            return inSampleSize;
        }
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;

        //假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){
            int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
            int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);

            //为了保证图片不缩放变形，我们取宽高比例最小的那个
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }
        return inSampleSize;
    }

    public static String getCreatTime(String path) {
        int start=path.lastIndexOf("/");
        int end=path.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return path.substring(start+1,end);
        }else{
            return null;
        }
    }

    /**
     * 加载本地图片的回调接口
     *
     * @author xiaanming
     *
     */
    public interface NativeImageCallBack{
        /**
         * 当子线程加载完了本地的图片，将Bitmap和图片路径回调在此方法中
         * @param signThumbBean
         * @param path
         */
        public void onImageLoader(SignThumbBean signThumbBean, String path);
    }
}

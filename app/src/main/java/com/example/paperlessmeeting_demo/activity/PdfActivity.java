package com.example.paperlessmeeting_demo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import android.graphics.Canvas;
import android.graphics.Matrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;

public class PdfActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnDrawListener  {


    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.pizhu)
    ImageView pizhu;
    private File appDir;
    private String pdfPath;
    private String fileName;
    private Handler  handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    
                    break;

            }


        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pdf;
    }

    @Override
    protected void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        onSaveBitmap(getBitmap()) ;
                    }
                }.start();

            }
        });
    }
    private void displayFromFile( String path ) {
        File  file=new File(path);
        pdfView.fromFile(file)   //设置pdf文件地址
                .defaultPage(6)         //设置默认显示第1页
                .onPageChange(this)     //设置翻页监听
                .onLoad(this)           //设置加载监听
                .onDraw(this)            //绘图监听
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical( false )  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻
                // .pages( 2 ，5  )  //把2  5 过滤掉
                .load();
    }
    @Override
    protected void initData() {
        pdfPath=  getIntent().getStringExtra("pdfPath");
        displayFromFile(pdfPath);
    }
    @Override
    public void onLayerDrawn(Canvas canvas, float v, float v1, int i) {

    }

    @Override
    public void loadComplete(int i) {

    }

    @Override
    public void onPageChanged(int i, int i1) {

    }

    /**
     * @param view 需要截取图片的view
     *             传入线性或相对布局就截取里面的所有内容
     * @return 截图
     */
    private Bitmap getBitmap() {

        View screenView = getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();

        //获取屏幕整张图片
        Bitmap bitmap = screenView.getDrawingCache();

        if (bitmap != null) {

            //需要截取的长和宽
            int outWidth = screenView.getWidth();
            int outHeight = screenView.getHeight();

            //获取需要截图部分的在屏幕上的坐标(view的左上角坐标）
            int[] viewLocationArray = new int[2];
            screenView.getLocationOnScreen(viewLocationArray);

            //从屏幕整张图片中截取指定区域
            bitmap = Bitmap.createBitmap(bitmap, viewLocationArray[0], viewLocationArray[1], outWidth, outHeight);
            screenView.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        }

        return bitmap;
    }

    //保存图片到系统图库
    private void onSaveBitmap(Bitmap mBitmap) {
        //将Bitmap保存图片到指定的路径/sdcard/Boohee/下，文件名以当前系统时间命名,但是这种方法保存的图片没有加入到系统图库中
        appDir = new File(Environment.getExternalStorageDirectory(), "comments");

        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Log.d("fgdddh", "路过~~~~");
        fileName ="screenshot.png";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
           Message ms = new Message();
            ms.what = 1;
            handler.sendMessage(ms);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  Toast.makeText(context, "保存图片成功", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));


    }
    //两张图合成一张
    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, 0, 0, null);
        return bitmap;
    }

}
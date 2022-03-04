package com.example.paperlessmeeting_demo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.widgets.ZoomImageView;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Canvas;
import android.graphics.Matrix;
import java.io.File;
import java.io.FileInputStream;
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
    @BindView(R.id.root_rl)
    RelativeLayout root_rl;


    private AlertDialog imaDialog;
    private File appDir;
    private String pdfPath;
    private String fileName;
    private String  originalPath;
    private File qianpiPath;
    private String qianpiName;
    private Handler  handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    originalPath=(String) msg.obj;
                    onSaveBitmap(merge(originalPath),"1") ;
                    break;
                case 2:
                    originalPath=(String) msg.obj;
                    showIma(originalPath);
                    break;
            }


        }
    };


    private void showIma(String originalPath) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PdfActivity.this);
        View view = LayoutInflater.from(PdfActivity.this).inflate(R.layout.dialog_qianpi_ima, null);
        ZoomImageView imageView = view.findViewById(R.id.see_ima);
        TextView commitTv=view.findViewById(R.id.commit);
        builder.setView(view);
        builder.setCancelable(true);
        imaDialog = builder.create();
        imaDialog.show();

        Window window = imaDialog.getWindow();//获取dialog屏幕对象
        window.setGravity(Gravity.CENTER);//设置展示位置
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width);//设置宽
        p.height = (int) (height);//设置高
        window.setAttributes(p);
        commitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PdfActivity.this,"批注提交成功，请到qianpi文件夹查看",Toast.LENGTH_SHORT).show();
                imaDialog.dismiss();

            }
        });
      ImageLoader.getInstance().displayImage("file://" + originalPath, imageView);
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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                 onSaveBitmap(getBitmap(),"2") ;
                    }
                }).start();

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
    private void onSaveBitmap(Bitmap mBitmap,String flag) {
        //将Bitmap保存图片到指定的路径/sdcard/Boohee/下，文件名以当前系统时间命名,但是这种方法保存的图片没有加入到系统图库中
        File file =null;
        if (flag.equals("1")){
            appDir = new File(Environment.getExternalStorageDirectory(), "qianpi");

            if (!appDir.exists()) {
                appDir.mkdir();
            }

            fileName ="qianpi.png";
             file = new File(appDir, fileName);
            Log.d("fgdddh签批合成路径", "路过~~~~"+" file.getPath()=  "+file.getPath());
        }else {
            appDir = new File(Environment.getExternalStorageDirectory(), "comments");

            if (!appDir.exists()) {
                appDir.mkdir();
            }

            fileName ="screenshot.png";
             file = new File(appDir, fileName);
            Log.d("fgdddh文件截图", "路过~~~~"+" file.getPath()=  "+file.getPath());
        }


        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
           Message ms = new Message();
            ms.obj=file.getPath();
            if (flag.equals("1")){
                ms.what = 2;

            }else {
                ms.what = 1;

            }

            handler.sendMessage(ms);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  Toast.makeText(context, "保存图片成功", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));


    }
    private Bitmap merge(String path2) {
          qianpiPath= new File(Environment.getExternalStorageDirectory(), "comments");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
          qianpiName="bg_qianpi.png";
        File file2 = new File(qianpiPath, qianpiName);
              Bitmap bitmap1=null ;
              Bitmap bitmap2=null ;
              try {
                  File originalFile = new File(path2);
                  File awaitFile=new File(file2.getPath());
                  Log.d("fgdddh签批截图",path2);
                  Log.d("fgdddh签批等待图",file2.getPath());
                  bitmap1 = BitmapFactory.decodeStream(new FileInputStream(originalFile));
                  bitmap2= BitmapFactory.decodeStream(new FileInputStream(awaitFile));
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              }

        int bitmap1Width = bitmap1.getWidth();

        int bitmap1Height = bitmap1.getHeight();

        int bitmap2Width = bitmap2.getWidth();

        int bitmap2Height = bitmap2.getHeight();

        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);

        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);

        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.getConfig());

        Canvas canvas = new Canvas(overlayBitmap);

        canvas.drawBitmap(bitmap1, new Matrix(), null);

        canvas.drawBitmap(bitmap2, marginLeft, marginTop, null);
          /*    int bgWidth = bitmap1.getWidth();
              int bgHeight = bitmap1.getHeight();
              //create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
        Bitmap  newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
              Canvas cv = new Canvas(newbmp);
              //draw bg into
              cv.drawBitmap(bitmap1, 0, 0, null);//在 0，0坐标开始画入bg
              //draw fg into
              cv.drawBitmap(bitmap2, 0, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入

              cv.save();//保存
              cv.restore();//存储*/
        return overlayBitmap;
          }



  /*  public  Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
        if( background == null ) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        //create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);//在 0，0坐标开始画入bg
        //draw fg into
        cv.drawBitmap(foreground, 0, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入

        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        cv.restore();//存储
        return newbmp;
    }*/

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
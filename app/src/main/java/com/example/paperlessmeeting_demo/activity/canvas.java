package com.example.paperlessmeeting_demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 梅涛 on 2021/6/28.
 */

public class canvas extends BaseActivity {
    Paint paint;
    Canvas canvas;
    Bitmap bitmap, newbitmap;
    int startX, startY, endX, endY;
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.rb_red)
    RadioButton rbRed;
    @BindView(R.id.rb_green)
    RadioButton rbGreen;
    @BindView(R.id.rb_blue)
    RadioButton rbBlue;
    @BindView(R.id.rb_yellow)
    RadioButton rbYellow;
    @BindView(R.id.rb_purple)
    RadioButton rbPurple;
    @BindView(R.id.rg_color)
    RadioGroup rg_color;
    @BindView(R.id.btn_clear)
    Button btn_clear;
    @BindView(R.id.btn_eraser)
    Button btn_eraser;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.sb_stroke)
    SeekBar sbStroke;
    @BindView(R.id.tv_stroke)
    TextView tv_stroke;
    @BindView(R.id.ll_layout)
    LinearLayout ll_layout;
    private String imagePath;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_canvas;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

                /*
* 统计用户行为日志
* */
        if (Hawk.contains("UserBehaviorBean")) {
            UserBehaviorBean userBehaviorBean = Hawk.get("UserBehaviorBean");
            UserBehaviorBean.DataBean dataBean = new UserBehaviorBean.DataBean();
            dataBean.setTittile(this.getClass().getName());
            dataBean.setTime(TimeUtils.getTime(System.currentTimeMillis()));
            List<UserBehaviorBean.DataBean> dataBeanList = userBehaviorBean.getData();
            dataBeanList.add(dataBean);
            Hawk.put("UserBehaviorBean", userBehaviorBean);
        }
        // 原始Bitmap
        //  originalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.j).copy(Bitmap.Config.ARGB_8888,true);
        // 循环遍历单选按钮
        for (int i = 0; i < rg_color.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rg_color.getChildAt(i);
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  // 为单选按钮注册选中响应事件
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        paint.setColor(buttonView.getTextColors().getDefaultColor());  // 获取单选按钮颜色并将颜色设置
                    }
                }
            });

            Log.i("MyPaintToolsActivity", imageview.getWidth() + " " + imageview.getHeight());
            Point point = new Point();
            getWindowManager().getDefaultDisplay().getSize(point);
            Log.i("MyPaintToolsActivity", point.x + " " + point.y);

            bitmap = Bitmap.createBitmap(800, 1000, Bitmap.Config.ARGB_8888);  // 创建一张空白图片
            canvas = new Canvas(bitmap);    // 创建一张画布,并图片放在画布上面
            canvas.drawColor(Color.argb(100, 250, 250, 250));   // 设置画布背景颜色为白色
            paint = new Paint();
            paint.setStrokeWidth(5);
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);
            canvas.drawBitmap(bitmap, new Matrix(), paint);  //把灰色背景画在画布上
            imageview.setImageBitmap(bitmap);         // 把图片加载到ImageView上

            // 注册触摸监听事件
            imageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.i("MyPaintToolsActivity", "ACTION_DOWN");

                            // 获取鼠标按下时的坐标
                            startX = (int) (event.getX() / 1.4);
                            startY = (int) (event.getY() / 1.4);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.i("MyPaintToolsActivity", "ACTION_MOVE");

                            // 获取鼠标移动后的坐标
                            endX = (int) (event.getX() / 1.4);
                            endY = (int) (event.getY() / 1.4);

                            //在开始和结束之间画一条直线
                            canvas.drawLine(startX, startY, endX, endY, paint);
                            // 实时更新开始坐标
                            startX = (int) (event.getX() / 1.4);
                            startY = (int) (event.getY() / 1.4);
                            // 更新ImageView上的画布图片
                            imageview.setImageBitmap(bitmap);
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.i("MyPaintToolsActivity", "ACTION_UP");
                            break;
                    }
                    imageview.invalidate();
                    return true;
                }
            });


            // 清除
            btn_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 清空画布的方法：
                    // 方法一：
                    // canvas.drawColor(0,PorterDuff.Mode.CLEAR);
                    // 方法二：
                    // canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
                    // 方法三：
                    Paint paint = new Paint();
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    canvas.drawPaint(paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                    imageview.invalidate(); //显示到布局文件中的ImageView控件上
                }
            });


            // 保存
            btn_save.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    try {
                        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                        OutputStream stream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        stream.close();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
                        intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
                        sendBroadcast(intent);
                        Toast.makeText(getApplicationContext(), "保存图片成功", 0).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "保存图片失败", 0).show();
                        e.printStackTrace();
                    }
                }
            });


            // 擦除

            btn_eraser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paint.setColor(Color.rgb(250, 250, 250));
                    paint.setStrokeWidth(30);
                }
            });

            // Progress进度条 ，调节画笔粗细
            SeekBar sb_stroke = findViewById(R.id.sb_stroke);
            sb_stroke.setProgress(5);  //进度条初始大小值为5
            sb_stroke.setMax(30);
            sb_stroke.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    paint.setStrokeWidth(progress);
                    tv_stroke.setText("画笔粗度为:" + progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
/**
 * 获取和保存当前屏幕的截图
 */
        getandsavecurrentimage();
        screenshot();

    }
    //截取屏幕的方法
    private void screenshot() {
        // 获取屏幕
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null)
        {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                imagePath = sdCardPath + File.separator + "screenshot.png";
                Log.d("fgfhh",imagePath);
                File file = new File(imagePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
            }
        }
    }
    public Bitmap takeScreenShot(Activity act) {
        if (act == null || act.isFinishing()) {

            return null;
        }

        // 获取当前视图的view
        View scrView = act.getWindow().getDecorView();
        scrView.setDrawingCacheEnabled(true);
        scrView.buildDrawingCache(true);

        // 获取状态栏高度
        Rect statuBarRect = new Rect();
        scrView.getWindowVisibleDisplayFrame(statuBarRect);
        int statusBarHeight = statuBarRect.top;
        int width = act.getWindowManager().getDefaultDisplay().getWidth();
        int height = act.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap scrBmp = null;
        try {
            // 去掉标题栏的截图
            scrBmp = bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //     scrBmp = Bitmap.createBitmap(scrView.getDrawingCache(),statusBarHeight,width,height - statusBarHeight);
        } catch (IllegalArgumentException e) {
            Log.d("", "#### 旋转屏幕导致去掉状态栏失败");
        }
        scrView.setDrawingCacheEnabled(false);
        scrView.destroyDrawingCache();
        return scrBmp;
    }

    /**
     * 获取和保存当前屏幕的截图
     */
    private void getandsavecurrentimage() {
        //1.构建getDefaultDisplay
        WindowManager windowManager = getWindowManager();

        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

        Bitmap bmp = bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //2.获取屏幕getdrawingcache
        View decorview = this.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        bmp = decorview.getDrawingCache();

        String savepath = getsdcardpath() + "/andydemo/screenimage";
        Log.d("dfgsg", savepath);

        //3.保存bitmap
        try {
            File path = new File(savepath);
            //文件
            String filepath = savepath + "/screen_1.png";
            File file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = null;
            try {
                fos = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (null != fos) {
                bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                try {
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取sdcard的目录路径功能
     *
     * @return
     */
    private String getsdcardpath() {
        File sdcarddir = null;
        //getexternalstoragedirectory
        boolean sdcardexist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardexist) {
            sdcarddir = Environment.getExternalStorageDirectory();
        }
        return sdcarddir.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

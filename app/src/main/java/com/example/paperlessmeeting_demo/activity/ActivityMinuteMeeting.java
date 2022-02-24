package com.example.paperlessmeeting_demo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.GridLaunchAdapter;
import com.example.paperlessmeeting_demo.adapter.MinuiteMeetingAdapter;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanRequest;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanResponse;
import com.example.paperlessmeeting_demo.bean.HandWritingBean;
import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;
import com.example.paperlessmeeting_demo.bean.MinuiteMeetBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.UploadBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.bean.WSBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.ScreenShotUtils;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.widgets.MyGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.FileNameMap;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by 梅涛 on 2021/6/23.
 * 秘书会议纪要
 */

public class ActivityMinuteMeeting extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.initiate_endorsement)
    TextView initiateEndorsement;
    @BindView(R.id.temporary_summary)
    TextView temporarySummary;
    @BindView(R.id.topics)
    TextView topics;
    @BindView(R.id.meeting_time)
    TextView meetingTime;
    @BindView(R.id.meeting_place)
    TextView meetingPlace;
    @BindView(R.id.meeting_attendence)
    TextView meetingAttendence;
    @BindView(R.id.tittle1)
    TextView tittle1;
    @BindView(R.id.content1)
    EditText content1;
    @BindView(R.id.tittle2)
    TextView tittle2;
    @BindView(R.id.content2)
    EditText content2;
    @BindView(R.id.tittle3)
    TextView tittle3;
    @BindView(R.id.content3)
    EditText content3;
    @BindView(R.id.tittle4)
    TextView tittle4;
    @BindView(R.id.content4)
    EditText content4;
    @BindView(R.id.launch_state)
    TextView launchState;
    @BindView(R.id.launch_num)
    TextView launchNum;
    @BindView(R.id.gridview)
    MyGridView gridview;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.root_ll)
    LinearLayout rootLl;
    @BindView(R.id.vv)
    ImageView vv;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.load_tv)
    TextView loadTv;
    /*  @BindView(R.id.recyclerview)
        RecyclerView recyclerview;*/
    private MinuiteMeetingAdapter minuiteMeetingAdapter;
    private List<MinuiteMeetBean.MeetingConten> list = new ArrayList<>();
    private GridLaunchAdapter gridLaunchAdapter;
    private List<Bitmap> bitmapList = new ArrayList<>();
    private String path = Environment.getExternalStorageDirectory() + "/pppp/" + "kk" + ".png";
    private String upLoadFileType;
    private String endStrAll;
    private UploadBean uploadBean;
    private CreateFileBeanRequest createFileBeanRequest;
    private String strContent;
    private List<AttendeBean> attendeBeanList = new ArrayList<>();
    private String secretaryId = "";//秘书ID
    private String otherParticipantsId;//其他参会人员
    private MeetingInfoBean meetingInfoBean;//会议实体类
    private HandWritingBean handWritingBean;
    private MinuiteMeetBean meetBean;
    private boolean isStartEndorsement = false;//是否已经发起过签批；
    private String selfIp = "";
    private int num = 0;//签名个数
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (!isStartEndorsement) {
                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DatagramSocket socket = new DatagramSocket();
                                String str = constant.INIATE_ENDORSEMENT;
                                byte[] sendStr = str.getBytes();
                                InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                                DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                                socket.send(packet);
                                socket.close();
                            } catch (SocketException e) {
                                e.printStackTrace();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    sendThread.start();
                    try {
                        Thread.sleep(2000);//休眠2秒，保证其他设备有足够的时间打开签名界面
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                launchState.setText("参会人员签字中...");
                /*
                * 重新签批-start
                * */
                num = 0;
                launchNum.setText("已签人数：" + num + "/" + attendeBeanList.size());
                //  cacheData();//缓存写入写数据
                if ("重新签批".equals(initiateEndorsement.getText().toString())) {
                    bitmapList.clear();
                    if (Hawk.contains("handWritingBean")) {
                        Hawk.delete("handWritingBean");
                    }
                    Hawk.put("handWritingBean", bitmapList);
                    launchState.setText("参会人员待签字");
                }
                initiateEndorsement.setText("重新签批");
                gridLaunchAdapter.setGridViewBeanList(bitmapList);
                gridLaunchAdapter.notifyDataSetChanged();
                 /*
                * 重新签批-end
                * */
                loadTv.setVisibility(View.VISIBLE);
                WSBean bean = new WSBean();
                HandWritingBean handWritingBean = new HandWritingBean();
                //   handWritingBean.setSendUserId("5f677602bd0c1f2d906dad63");//秘书
                handWritingBean.setMeetingRecordId(UserUtil.meeting_record_id);
                try {
                    handWritingBean.setBaseData(strContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bean.setReqType(0);
                bean.setPackType("meetingSummaryShare");
                bean.setDevice("app");
                bean.setUser_id(secretaryId);//秘书
                bean.setBody(handWritingBean);
                String strJson = new Gson().toJson(bean);
                JWebSocketClientService.sendMsg(strJson);
                isStartEndorsement = true;
                Log.d("ghff", "图片" + bean.getPackType());
            } else if (msg.what == 2) {
                if (Hawk.contains("handWritingBean")) {
                    bitmapList = Hawk.get("handWritingBean");
                }
                launchState.setText("参会人员签字中...");
                gridLaunchAdapter.setGridViewBeanList(bitmapList);
                gridLaunchAdapter.notifyDataSetChanged();
                ++num;
                launchNum.setText("已签人数：" + num + "/" + attendeBeanList.size());
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch_endorsement;
    }

    @Override
    protected void initView() {
        /*
        * 获取参会人员列表
        * */
        getAttenData();

        if (Hawk.contains("handWritingBean")) {
            bitmapList = Hawk.get("handWritingBean");
            num = bitmapList.size();
            launchNum.setText("已签人数：" + num + "/" + attendeBeanList.size());

        }


        /*MinuiteMeetBean minuiteMeetBean = new MinuiteMeetBean();
        minuiteMeetBean.setTittle("fdgs");
        minuiteMeetBean.setContent("fsgfg");
        MinuiteMeetBean minuiteMeetBean1 = new MinuiteMeetBean();
        minuiteMeetBean1.setTittle("士大夫南");
        minuiteMeetBean1.setContent("拿到平均分是拼命吗");
        list.add(minuiteMeetBean);
        list.add(minuiteMeetBean1);
        //  纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        minuiteMeetingAdapter = new MinuiteMeetingAdapter(ActivityMinuteMeeting.this, list, new MinuiteMeetingAdapter.ChoseClickListener() {
            @Override
            public void clickListener(int position) {
                if (list.size() > 0) {
                    // add_chose.setVisibility(View.INVISIBLE);
                } else {

                    //add_chose.setVisibility(View.VISIBLE);
                }

            }
        });
        recyclerview.setAdapter(minuiteMeetingAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        minuiteMeetingAdapter.notifyDataSetChanged();*/
    }

    /**
     * 收到websocket 信息接收其他会员签名图片
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        try {
            WSBean wsebean = new Gson().fromJson(message.getMessage(), WSBean.class);
            //  收到vote的websocket的信息pushSign
            Log.d("qianpi2222", "qianpi222: " + wsebean.toString() + "==" + wsebean.getPackType());
            if (wsebean != null) {
                if (constant.MINUTE_MEETING.equals(wsebean.getPackType())) {
                    handWritingBean = new HandWritingBean();
                    TempWSBean<HandWritingBean> wb = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<HandWritingBean>>() {
                    }.getType());
                    if (wb != null) {
                        handWritingBean = wb.getBody();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    base642Bitmap(handWritingBean.getBaseData());
                                    //  compressImage(base642Bitmap(handWritingBean.getBaseData()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {

                                }
                                // bitmapList.add(compressImage(base642Bitmap(handWritingBean.getBaseData())));
                                // bitmapList.add(/*compressImage(*/base642Bitmap(handWritingBean.getBaseData())/*)*/);
                                //  Hawk.put("handWritingBean", bitmapList);
                            }
                        }).start();
                        /*if (Hawk.contains("handWritingBean")) {
                            bitmapList = Hawk.get("handWritingBean");
                        }
                        Log.d("qianpi222", bitmapList.size() + "大小");
                        gridLaunchAdapter.setGridViewBeanList(bitmapList);
                        gridLaunchAdapter.notifyDataSetChanged();*/
                    }
                }
            }
        } catch (Exception e) {
            Log.d("qianpi222", e.getMessage().toString());
            e.printStackTrace();
        }
    }

    private Bitmap base642Bitmap(String base64) {
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //inJustDecodeBounds参数设为true并加载图片。
        //这样做的原因是inJustDecodeBounds为true时
        //BitmapFactory只会解析图片的原始宽高信息，并不会真正的加载图片。
        options.inJustDecodeBounds = true;
        Bitmap mBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
     /*   try {
            Log.d("log11111", "bitmap原始图片内存大小：" + mBitmap.getAllocationByteCount());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("log11111222", "原始图片的宽width：" + options.outWidth + "原始图片的高height：" + options.outHeight);
        options.inSampleSize = 2;
        //得到采样率后，将BitmapFacpry.Options的inSampleSize参数设为false并重新加载图片。
        //此时是真正的加载图片。
        options.inJustDecodeBounds = false;
        Bitmap newbitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length, options);
        Log.d("log111113333", "压缩后图片的宽width：" + options.outWidth + "压缩后图片的高height：" + options.outHeight);
        try {
            Log.d("log1111444", "bitmap压缩后图片内存大小：" + newbitmap.getByteCount());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
     /*   if (Hawk.contains("handWritingBean")) {
            bitmapList = Hawk.get("handWritingBean");
        }
        Message ms = new Message();
        ms.what = 2;
        handler.sendMessage(ms);//截图屏幕上传服务器*/
        bitmapList.add(mBitmap);
        Hawk.put("handWritingBean", bitmapList);
        Message ms = new Message();
        ms.what = 2;
        handler.sendMessage(ms);//更新主线程
        return mBitmap;
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 5, baos);
        int options = 80;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 5) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 80;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        bitmapList.add(bitmap);
        Hawk.put("handWritingBean", bitmapList);
        Message ms = new Message();
        ms.what = 2;
        handler.sendMessage(ms);//更新主线程
        return bitmap;
    }

/*
    //在这个方法里我们对图片做处理
    private Bitmap decodeBitmapFromResource() {

        Log.d("log", "bitmap原始图片内存大小：" + BitmapFactory.decodeByteArray(getResources(), R.drawable.tupian).getAllocationByteCount());
        //获取  BitmapFactory.Options的实例
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //inJustDecodeBounds参数设为true并加载图片。
        //这样做的原因是inJustDecodeBounds为true时
        //BitmapFactory只会解析图片的原始宽高信息，并不会真正的加载图片。
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.tupian, options);
        Log.d("log", "原始图片的宽width：" + options.outWidth + "原始图片的高height：" + options.outHeight);
//        Log.d("log","bitmap原始图片内存大小："+ bitmap.getAllocationByteCount());
        //原始图片的宽高已经存放在options中。options.outWidth和options.outHeight
        //我们可以用它去做一些判断，得到我们想要的采样率
        //options.inSampleSize = calculateSampleSize(options,300,300);
        options.inSampleSize = 2;
        //得到采样率后，将BitmapFacpry.Options的inSampleSize参数设为false并重新加载图片。
        //此时是真正的加载图片。
        options.inJustDecodeBounds = false;
        Bitmap newbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tupian, options);
        Log.d("log", "压缩后图片的宽width：" + options.outWidth + "压缩后图片的高height：" + options.outHeight);
        Log.d("log", "bitmap压缩后图片内存大小：" + newbitmap.getByteCount());
        return BitmapFactory.decodeResource(getResources(), R.drawable.tupian, options);
    }*/


    private boolean fileIsExist(String fileName) {
        //传入指定的路径，然后判断路径是否存在
        File file = new File(fileName);
        if (file.exists())
            return true;
        else {
            //file.mkdirs() 创建文件夹的意思
            return file.mkdirs();
        }
    }

    private void setBitmap(Bitmap bitmap) {

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bitmap, 0, 0, null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            try {
                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanMediaFile(file);
        }
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

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

        if (Hawk.contains("meetingInfoBean")) {
            meetingInfoBean = Hawk.get("meetingInfoBean");
            meetingTime.setText("时间：" + meetingInfoBean.getSche_start_time());

            List<MeetingInfoBean.MeetingRoomIdBean> meetingRoomIdBeanList = meetingInfoBean.getMeeting_room_id();
            if (meetingRoomIdBeanList != null) {

                topics.setText(meetingInfoBean.getName());
                meetingPlace.setText("地点：" + meetingRoomIdBeanList.get(0).getName());
            }

        }
        if (Hawk.contains("meeting_time")) {
            String time = Hawk.get("meeting_time");
            meetingTime.setText("时间：" + time);
        }

        if (Hawk.contains("cacheData")) {
            meetBean = Hawk.get("cacheData");
            list.clear();
            bitmapList.clear();
            list = meetBean.getMeetingContens();
            Log.d("qianpi", "路过会议内容！！！！" + list.size() + "");
            tittle1.setText(list.get(0).getTittle());
            content1.setText(list.get(0).getContent());

            tittle2.setText(list.get(1).getTittle());
            content2.setText(list.get(1).getContent());

            tittle3.setText(list.get(2).getTittle());
            content3.setText(list.get(2).getContent());

            tittle4.setText(list.get(3).getTittle());
            content4.setText(list.get(3).getContent());
            if (meetBean.getBitmapList() != null) {
                bitmapList = meetBean.getBitmapList();
                num = +bitmapList.size();
                launchNum.setText("已签人数：" + num + "/" + attendeBeanList.size());
                if (bitmapList.size() > 0) {
                    if (gridLaunchAdapter != null) {
                        gridLaunchAdapter.setGridViewBeanList(bitmapList);
                        gridLaunchAdapter.notifyDataSetChanged();
                    }

                }

            }

        }
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        //   WindowManager windowManager = context.getWindowManager();
        //获取屏幕宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        View view = getWindow().getDecorView();     // 获取DecorView
        Bitmap bitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap2);
        view.draw(canvas);

        temporarySummary.setOnClickListener(this);
        initiateEndorsement.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        loadTv.setOnClickListener(this);
        /*
        * 上传签批
        * */
        loadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenShotUtils.savePic(ScreenShotUtils.getBitmapByView(scrollView, ActivityMinuteMeeting.this));
                Log.d("sdfsdg", ScreenShotUtils.savePic(ScreenShotUtils.getBitmapByView(scrollView, ActivityMinuteMeeting.this)));
                sendFileInfo(ScreenShotUtils.savePic(ScreenShotUtils.getBitmapByView(scrollView, ActivityMinuteMeeting.this)), "3", "0");
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    /*    screenshot();
        GetandSaveCurrentImage();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        JWebSocketClientService.initSocketClient();
        gridLaunchAdapter = new GridLaunchAdapter(ActivityMinuteMeeting.this, bitmapList);
        gridview.setAdapter(gridLaunchAdapter);
        gridLaunchAdapter.notifyDataSetChanged();
        selfIp = getNetworkType();
        Hawk.put("SelfIpAddress", selfIp);//自身Ip
        Log.d("fgdgg222", selfIp);
    }

    @Override
    protected void onDestroy() {
        cacheData();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.initiate_endorsement:
                getContent();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message ms = new Message();
                        ms.what = 1;
                        handler.sendMessage(ms);//截图屏幕上传服务器
                        //ScreenShotUtils.savePic(bitmap2);
                    }
                }).start();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.temporary_summary:
                cacheData();//暂存数据
                Toast.makeText(ActivityMinuteMeeting.this, "保留成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.load_tv:
                cacheData();//暂存数据
                break;
        }

    }

    private void getContent() {
        list.clear();
        MinuiteMeetBean minuiteMeetBean = new MinuiteMeetBean();
        MinuiteMeetBean.MeetingConten minuiteMeetBean1 = new MinuiteMeetBean.MeetingConten();
        minuiteMeetBean1.setTittle(tittle1.getText().toString().trim());
        minuiteMeetBean1.setContent(content1.getText().toString().trim());

        MinuiteMeetBean.MeetingConten minuiteMeetBean2 = new MinuiteMeetBean.MeetingConten();
        minuiteMeetBean2.setTittle(tittle2.getText().toString().trim());
        minuiteMeetBean2.setContent(content2.getText().toString().trim());

        MinuiteMeetBean.MeetingConten minuiteMeetBean3 = new MinuiteMeetBean.MeetingConten();
        minuiteMeetBean3.setTittle(tittle3.getText().toString().trim());
        minuiteMeetBean3.setContent(content3.getText().toString().trim());

        MinuiteMeetBean.MeetingConten minuiteMeetBean4 = new MinuiteMeetBean.MeetingConten();
        minuiteMeetBean4.setTittle(tittle4.getText().toString().trim());
        minuiteMeetBean4.setContent(content4.getText().toString().trim());
        list.add(minuiteMeetBean1);
        list.add(minuiteMeetBean2);
        list.add(minuiteMeetBean3);
        list.add(minuiteMeetBean4);
        minuiteMeetBean.setMeetingContens(list);
        Gson gson = new Gson();
        strContent = gson.toJson(minuiteMeetBean);
    }

    private void cacheData() {
        list.clear();
        MinuiteMeetBean minuiteMeetBean = new MinuiteMeetBean();
        MinuiteMeetBean.MeetingConten minuiteMeetBean1 = new MinuiteMeetBean.MeetingConten();
        minuiteMeetBean1.setTittle(tittle1.getText().toString().trim());
        minuiteMeetBean1.setContent(content1.getText().toString().trim());

        MinuiteMeetBean.MeetingConten minuiteMeetBean2 = new MinuiteMeetBean.MeetingConten();
        minuiteMeetBean2.setTittle(tittle2.getText().toString().trim());
        minuiteMeetBean2.setContent(content2.getText().toString().trim());

        MinuiteMeetBean.MeetingConten minuiteMeetBean3 = new MinuiteMeetBean.MeetingConten();
        minuiteMeetBean3.setTittle(tittle3.getText().toString().trim());
        minuiteMeetBean3.setContent(content3.getText().toString().trim());

        MinuiteMeetBean.MeetingConten minuiteMeetBean4 = new MinuiteMeetBean.MeetingConten();
        minuiteMeetBean4.setTittle(tittle4.getText().toString().trim());
        minuiteMeetBean4.setContent(content4.getText().toString().trim());
        list.add(minuiteMeetBean1);
        list.add(minuiteMeetBean2);
        list.add(minuiteMeetBean3);
        list.add(minuiteMeetBean4);
        minuiteMeetBean.setMeetingContens(list);
        minuiteMeetBean.setBitmapList(bitmapList);
        Log.d("fdfs", bitmapList.size() + "");
      /*  if (Hawk.contains("handWritingBean")) {
            bitmapList = Hawk.get("handWritingBean");
            minuiteMeetBean.setBitmapList(bitmapList);

        }*/
        Hawk.put("cacheData", minuiteMeetBean);

    }

    private void getAttenData() {
        String _id = UserUtil.meeting_record_id;
        if (attendeBeanList != null) {
            attendeBeanList.clear();
        }
        NetWorkManager.getInstance().getNetWorkApiService().getMeetingUserList(_id).compose(this.<BasicResponse<List<AttendeBean>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<AttendeBean>>>() {
                    @Override
                    protected void onSuccess(BasicResponse<List<AttendeBean>> response) {
                        if (response.getData() != null) {
                            attendeBeanList = (List<AttendeBean>) response.getData();
                            launchNum.setText("已签人数：" + num + "/" + attendeBeanList.size());
                            setAgendaUi(attendeBeanList);
                        }

                    }
                });

    }

    /*
              * 临时会议时获取设备是有线网还是无线网
              * */
    private String getNetworkType() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                return getIpAddressString();
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return getIpAddress();
            }

        }
        return "0.0.0.0";
    }

    /*
  * 临时会议时获取设备有线网IP地址
  * */
    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    /*
    * 临时会议时获取设备无线网IP地址
    * */
    public String getIpAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int i = wifiInfo.getIpAddress();
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
    }

    private void setAgendaUi(List<AttendeBean> attendeBeans) {
        for (int i = 0; i < attendeBeans.size(); i++) {
            if ("1".equals(attendeBeans.get(i).getRole())) {
                secretaryId = attendeBeans.get(i).getUser_id();
            }
        }
        StringBuffer sb2 = new StringBuffer();
        if (attendeBeans.size() > 0) {
            for (int i = 0; i < attendeBeans.size(); i++) {
                sb2.append(attendeBeans.get(i).getName() + "、");

            }
            meetingAttendence.setText("参会人员:" + sb2.subSequence(0, sb2.length() - 1));
        }
    }

    /**
     * 获取和保存当前屏幕的截图
     */
    private void GetandSaveCurrentImage() {
        //1.构建Bitmap
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

        Bitmap Bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //2.获取屏幕
        View decorview = this.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();

        String SavePath = getSDCardPath() + "/AndyDemo/ScreenImage";

        //3.保存Bitmap
        try {
            File path = new File(SavePath);
            //文件
            String filepath = SavePath + "/Screen_1.png";
            File file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();

                Toast.makeText(mContext, "截屏文件已保存至SDCard/AndyDemo/ScreenImage/下", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SDCard的目录路径功能
     *
     * @return
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }

    public void sendFileInfo(String path, String type, String flag) {
        //把文件封装在RequestBody里
        Log.d("dfgsgsf", path);
        File file = new File(path);
        if (file == null) {
            return;
        }
        upLoadFileType = getMimeType(file.getName());
        endStrAll = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse(upLoadFileType), file);

        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        NetWorkManager.getInstance().getNetWorkApiService().upLoadFile(creatDirectory(type), "", "", part).compose(this.<BasicResponse<UploadBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<UploadBean>>() {
                    @Override
                    protected void onSuccess(BasicResponse<UploadBean> response) {
                        if (response != null) {
                            String c_id = "";
                            String user_id = "";
                            String _id = "";
                            if (Hawk.contains("_id")) {
                                _id = Hawk.get("_id");
                            }
                            if (Hawk.contains("c_id")) {
                                c_id = Hawk.get("c_id");
                            }
                            if (Hawk.contains("user_id")) {
                                user_id = Hawk.get("user_id");
                            }
                            uploadBean = (UploadBean) response.getData();
                            //创建文件请求体类
                            createFileBeanRequest = new CreateFileBeanRequest();
                            createFileBeanRequest.setC_id(c_id);
                            createFileBeanRequest.setUser_id(user_id);
                            // createFileBeanRequest.setType(endStr);
                            createFileBeanRequest.setMeeting_record_id(_id);
                            List<CreateFileBeanRequest.FileListBean> fileListBeans = new ArrayList<>();
                            CreateFileBeanRequest.FileListBean fileListBean = new CreateFileBeanRequest.FileListBean();
                            fileListBean.setName(file.getName());
                            fileListBean.setPath(uploadBean.getUrl());
                            fileListBean.setSize("100");
                            fileListBean.setSuffix(endStrAll);
                            fileListBean.setUnclassified(flag);
                            fileListBean.setType(getFileType(endStrAll));//约定的文件四种类型：文档、图片、视频、其他。
                            fileListBeans.add(fileListBean);
                            createFileBeanRequest.setFile_list(fileListBeans);
                            creatFile(createFileBeanRequest);


                        }

                    }
                });


    }

    private void creatFile(CreateFileBeanRequest createFileBeanRequest) {
        NetWorkManager.getInstance().getNetWorkApiService().createMeetingFile(createFileBeanRequest).compose(this.<BasicResponse<CreateFileBeanResponse>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<CreateFileBeanResponse>>() {
                    @Override
                    protected void onFail(BasicResponse<CreateFileBeanResponse> response) {
                        super.onFail(response);
                       /* progressBarLl.setVisibility(View.GONE);*/
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                     /*   progressBarLl.setVisibility(View.GONE);*/
                    }

                    @Override
                    protected void onSuccess(BasicResponse<CreateFileBeanResponse> response) {
                        if (response != null) {
                            Toast.makeText(ActivityMinuteMeeting.this, "上传成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("fileType", constant.SUMMARY);
                            intent.putExtras(bundle);
                            intent.setAction(constant.FRESH_FILE);
                            sendBroadcast(intent);
                        }

                    }
                });
    }

    /**
     * 获取文件MimeType
     *
     * @param filename 文件名
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "multipart/form-data"; //* exe,所有的可执行程序
        }
        return contentType;
    }

    //传给后台创建文件目录
    private String creatDirectory(String end) {
        if ("1".equals(end)) {
            return "music";
        } else if ("2".equals(end)) {
            return "video";
        } else if ("3".equals(end)) {
            return "image";
        } else if ("4".equals(end)) {
            return "directory";
        } else if ("4".equals(end)) {
            return "directory";
        } else if ("4".equals(end)) {
            return "directory";
        } else if ("4".equals(end)) {
            return "directory";
        } else {
            return "directory";
        }

    }

    private String getFileType(String end) {
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return constant.OTHER;
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return constant.VIDEO;
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return constant.SUMMARY;
        } else if ((end.equals("ppt") || end.equals("pptx"))) {
            return constant.DOCUMENT;
        } else if (end.equals("xls")) {
            return constant.DOCUMENT;
        } else if (end.equals("doc") || end.equals("docx")) {
            return constant.DOCUMENT;
        } else if (end.equals("pdf")) {
            return constant.DOCUMENT;
        } else {
            return constant.OTHER;
        }

    }
}

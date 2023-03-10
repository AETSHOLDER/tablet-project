package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.RecycleAdapter;
import com.example.paperlessmeeting_demo.adapter.VoteAdapter;
import com.example.paperlessmeeting_demo.adapter.VoteChairmanAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.ChoseBean;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.bean.ItemBean;
import com.example.paperlessmeeting_demo.bean.PieEntry;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.dialog.CheckBoxDialog;
import com.example.paperlessmeeting_demo.dialog.PieChartDialog;
import com.example.paperlessmeeting_demo.dialog.RadioDialog;
import com.example.paperlessmeeting_demo.dialog.VoteInfoDialog;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.PermissionManager;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.widgets.DrawableTextView;
import com.example.paperlessmeeting_demo.widgets.ZoomImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrgao.luckly_popupwindow.LucklyPopopWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.hawk.Hawk;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import com.example.paperlessmeeting_demo.bean.VoteListBean.VoteBean;

@SuppressLint("ValidFragment")
public class TempVoteListFragment extends BaseFragment implements VoteAdapter.voteClickListener, VoteChairmanAdapter.voteClickListener, EasyPermissions.PermissionCallbacks, RecycleAdapter.ChoseOptionItemImaListener, RecycleAdapter.SeePhotosItemImaListener {


    @Override
    protected int getLayoutId() {
        if (UserUtil.ISCHAIRMAN) {
            return R.layout.activity_vote_list_chairman;
        } else {
            return R.layout.activity_vote_list;
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        /**
         * ????????????????????????
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
        loadData();
    }

    @BindView(R.id.vote_refreshLayout)
    protected RefreshLayout home_refreshLayout;

    @BindView(R.id.recycle_view)
    protected RecyclerView rv;

    ArrayList<VoteListBean.VoteBean> voteList = new ArrayList<>();
    private LucklyPopopWindow mLucklyPopopWindow;
    private RecyclerView.LayoutManager mLayoutManager;
    //  ??????1
    VoteAdapter voteAdapter1;
    VoteChairmanAdapter voteAdapter2;

    private String[] popTitleArr;
    private String TAG = "TempVoteListFragment";
    //  ??????????????????
    private ImageView currentSelDanxuan;
    //  ??????????????????
    private ImageView currentSelNiming;
    private RecycleAdapter adapter;
    private List<ItemBean> list = new ArrayList<ItemBean>();
    LinearLayout layoutContent;
    RecyclerView mRecyclerView;
    ImageView add_chose;
    ImageView close_icon;
    Button btn_pos;
    Button btn_neg;
    ImageView ivCheckState1;
    ImageView ivCheckState2;
    ImageView ivCheckState3;
    ImageView ivCheckState4;

    EditText edit_content;

    EditText edit_text;
    EditText edit_text2;
    EditText edit_text3;

    private AlertDialog results;
    private AlertDialog optionFormDialog;
    private AlertDialog imaDialog;
    private FragmentManager fragmentManager;
    private String titles;
    private Context context;
    private String currentEndDate;
    private int positionIma = -1;

    public TempVoteListFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public TempVoteListFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public TempVoteListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        voteAdapter1 = new VoteAdapter(getActivity());
        voteAdapter2 = new VoteChairmanAdapter(getActivity());
        DrawableTextView new_vote = null;
        if (UserUtil.ISCHAIRMAN) {
            new_vote = rootView.findViewById(R.id.new_vote);

            voteAdapter2.setOnVoteListener(this);
            new_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptionForm();

                }
            });
        } else {

            voteAdapter1.setOnVoteListener(this);
        }

        checkWritePermission1();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager = layoutManager;
        rv.setLayoutManager(layoutManager);
        home_refreshLayout.setEnableLoadMore(false);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        rv.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(dividerItemDecoration);
        rv.setHasFixedSize(true);


        if (UserUtil.ISCHAIRMAN) {
            rv.setAdapter(voteAdapter2);
        } else {
            rv.setAdapter(voteAdapter1);
        }
        // ??????socket??????
        EventBus.getDefault().register(this);
        return rootView;
    }

    /**
     * ??????websocket ??????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        //  client??????????????????
        if (message.getType().equals(MessageReceiveType.MessageClient)) {
            // ??????????????????
            if (message.getMessage().contains(constant.QUERYVOTE)) {
                Log.e(TAG, "onReceiveMsg11111: " + message.toString());
                try {
                    TempWSBean<ArrayList> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<ArrayList<VoteBean>>>() {
                    }.getType());
                    //  ??????vote???websocket?????????
                    if (wsebean != null) {
                        voteList = wsebean.getBody();
                        String flag = wsebean.getFlag();//???????????????????????????????????????
                        Log.d("gdgsdgsdgdgf444555",flag+"");
                        for (VoteListBean.VoteBean bean : voteList) {
                            bean.setStatus(bean.getStatus());
                        }
                        refreshUI(voteList, flag);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (message.getMessage().contains(constant.NEWVOTE)) {
                Log.e(TAG, "onReceiveMsg22222: " + message.toString());
                try {
                    TempWSBean<ArrayList> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<ArrayList<VoteBean>>>() {
                    }.getType());
                    //  ??????vote???websocket?????????
                    if (wsebean != null) {
                        voteList = wsebean.getBody();
                        String flag = wsebean.getFlag();//???????????????????????????????????????
                        Log.d("gdgsdgsdgdgf444888",flag+"");
                        for (VoteListBean.VoteBean bean : voteList) {
                            bean.setStatus(bean.getStatus());
                        }
                        refreshUI(voteList, flag);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (message.getMessage().contains(constant.SURENAME)) {
                loadData();
            }
        } else {  //  ???????????????

        }

    }

    //????????????
    private void showOptionForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_option_form, null);
        RadioGroup rg = view.findViewById(R.id.rg_option_form);
        RadioButton tv = view.findViewById(R.id.rb_tv);
        RadioButton ima = view.findViewById(R.id.rb_ima);
        builder.setView(view);
        builder.setCancelable(true);
        optionFormDialog = builder.create();
        optionFormDialog.show();

        Window window = optionFormDialog.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.4);//?????????
        p.height = (int) (height * 0.3);//?????????
        window.setAttributes(p);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // ??????????????????????????????
                switch (checkedId) {
                    case R.id.rb_tv:
                        // ??????
                        showNewVoteItem("1");
                        optionFormDialog.dismiss();
                        break;
                    case R.id.rb_ima:
                        // ??????
                        showNewVoteItem("2");
                        optionFormDialog.dismiss();
                        break;
                }
            }
        });

    }

    //????????????
    private void showIma(int imaPath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_see_ima, null);
        ZoomImageView imageView = view.findViewById(R.id.see_ima);
        builder.setView(view);
        builder.setCancelable(true);
        imaDialog = builder.create();
        imaDialog.show();
        Window window = imaDialog.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.8);//?????????
        p.height = (int) (height * 0.8);//?????????
        window.setAttributes(p);
        ImageLoader.getInstance().displayImage("file://" + list.get(imaPath).getText(), imageView);
    }

    //flag  1:????????????  2???????????????
    private void showNewVoteItem(String flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = null;
        if (flag.equals("1")) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_newvote, null);
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_newvote_ima, null);
        }

        layoutContent = view.findViewById(R.id.layout_newvote);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        add_chose = view.findViewById(R.id.add_chose);//???????????????
        close_icon = view.findViewById(R.id.close_icon);
        btn_pos = view.findViewById(R.id.btn_pos);//??????
        btn_neg = view.findViewById(R.id.btn_neg);//??????
        ivCheckState1 = view.findViewById(R.id.iv_check_state1);
        ivCheckState2 = view.findViewById(R.id.iv_check_state2);
        ivCheckState3 = view.findViewById(R.id.iv_check_state3);
        ivCheckState4 = view.findViewById(R.id.iv_check_state4);
        edit_content = view.findViewById(R.id.edit_content);
        edit_text = view.findViewById(R.id.edit_text);
        edit_text2 = view.findViewById(R.id.edit_text2);
        edit_text3 = view.findViewById(R.id.edit_text3);

        edit_text3.setFocusable(false);
        edit_text3.setKeyListener(null);
        currentSelDanxuan = ivCheckState1;
        currentSelNiming = ivCheckState4;
        builder.setView(view);
        builder.setCancelable(false);
        results = builder.create();
        results.show();

        Window window = results.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.8);//?????????
        p.height = (int) (height * 0.8);//?????????
        window.setAttributes(p);

        initRecycle(flag);
        edit_text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickDialog dialog = new DatePickDialog(getActivity());
                //????????????????????????
                dialog.setYearLimt(3);
                //????????????
                dialog.setTitle("????????????");
                //????????????
                dialog.setType(DateType.TYPE_ALL);
                //?????????????????????????????????????????????
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");
                //??????????????????
                dialog.setOnChangeLisener(null);
                //??????????????????????????????
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = format.format(date);
                        Log.d("DatePickDialog", str);
                        edit_text3.setText(str);
                        currentEndDate = str;
                    }
                });
                dialog.show();
            }
        });
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                results.dismiss();
            }
        });
        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                results.dismiss();
            }
        });
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_content.getText())) {
                    Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edit_content.getText().length() < 5) {
                    Toast.makeText(getActivity(), "??????????????????5?????????", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(currentEndDate)) {
                    Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<String> options_list = new ArrayList();
                //?????????????????????
                if (flag.equals("1")) {
                    if (TextUtils.isEmpty(edit_text.getText())) {
                        Toast.makeText(getActivity(), "???????????????1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(edit_text2.getText())) {
                        Toast.makeText(getActivity(), "???????????????2", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    options_list.add(edit_text.getText().toString());
                    options_list.add(edit_text2.getText().toString());
                    //  ?????????????????????
                    for (int i = 0; i < list.size(); i++) {
                        ItemBean bean = list.get(i);
                        if (!TextUtils.isEmpty(bean.getText())) {
                            bean.setFlag("1");
                            options_list.add(bean.getText().toString());
                        }
                    }

                } else {
                    //?????????????????????

                    //  ?????????????????????
                    for (int i = 0; i < list.size(); i++) {
                        ItemBean bean = list.get(i);
                        if (!TextUtils.isEmpty(bean.getText())) {
                            try {
                                bean.setFlag("2");
                                options_list.add(byte2Base64(readStream(bean.getText())));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }


                VoteListBean.VoteBean voteBean = new VoteListBean.VoteBean();
                VoteBean.FromBean fromBean = new VoteBean.FromBean();
                fromBean.setName(Hawk.get(constant.myNumber));
                fromBean.set_id(FLUtil.getMacAddress());


                voteBean.setTopic(edit_content.getText().toString());
                voteBean.setOptions(options_list);
                voteBean.setEnd_time(currentEndDate);
                voteBean.setType(currentSelDanxuan == ivCheckState1 ? "0" : "1");
                voteBean.setAnonymity(currentSelNiming == ivCheckState3 ? "1" : "0");
                voteBean.setFrom(fromBean);
                // ??????????????????????????????
                voteBean.setUser_list(new ArrayList<>());
                voteBean.setStatus("ENABLE");
//                Map<String,Object> map = new HashMap<>();
//                map.put("topic",edit_content.getText().toString());
////                map.put("c_id",UserUtil.c_id);
//                map.put("status","ENABLE");
//                map.put("options",options_list);
//                map.put("end_time",currentEndDate);
//                map.put("type",currentSelDanxuan == ivCheckState1 ? "0" : "1");
//                map.put("anonymity",currentSelNiming == ivCheckState3 ? "1" : "0");
//                map.put("from", FLUtil.getMacAddress());
////                map.put("meeting_record_id",UserUtil.meeting_record_id);
                //?????????????????????
                if (flag.equals("1")) {
                    creatVote(voteBean, "1");
                } else {
                    //???????????????
                    creatVote(voteBean, "2");
                }

                results.dismiss();
            }
        });
        add_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addData(list.size());
                add_chose.setVisibility(View.INVISIBLE);
            }
        });

        // ??????
        ivCheckState1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDanxuan(1);
            }
        });
        ivCheckState2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDanxuan(2);
            }
        });

        //  ??????
        ivCheckState3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNiming(1);
            }
        });
        ivCheckState4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNiming(2);
            }
        });

    }

    //  ??????????????????
    private void checkDanxuan(Integer tag) {

        if (currentSelDanxuan.equals(ivCheckState1)) {
            if (1 == tag) {
                return;
            }
            ivCheckState1.setImageResource(R.drawable.radio_unselected);
            ivCheckState2.setImageResource(R.drawable.radio_selected);
            currentSelDanxuan = ivCheckState2;
        } else {
            if (2 == tag) {
                return;
            }
            ivCheckState1.setImageResource(R.drawable.radio_selected);
            ivCheckState2.setImageResource(R.drawable.radio_unselected);
            currentSelDanxuan = ivCheckState1;
        }
    }

    //  ??????????????????
    private void checkNiming(Integer tag) {

        if (currentSelNiming.equals(ivCheckState3)) {
            if (1 == tag) {
                return;
            }
            ivCheckState3.setImageResource(R.drawable.radio_unselected);
            ivCheckState4.setImageResource(R.drawable.radio_selected);
            currentSelNiming = ivCheckState4;
        } else {
            if (2 == tag) {
                return;
            }
            ivCheckState3.setImageResource(R.drawable.radio_selected);
            ivCheckState4.setImageResource(R.drawable.radio_unselected);
            currentSelNiming = ivCheckState3;
        }
    }

    //  ???????????????
    private void initRecycle(String flag) {
        if (list != null) {
            list.clear();
        }

        //  ????????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //      ??????????????????????????????????????????????????????
//        list = initData();
        adapter = new RecycleAdapter(getActivity(), list, new RecycleAdapter.ChoseClickListener() {
            @Override
            public void clickListener(int position, String flag) {
                if (list.size() > 0) {
                    add_chose.setVisibility(View.INVISIBLE);
                } else {
                    add_chose.setVisibility(View.VISIBLE);
                }

            }
        }, flag);
        adapter.setChoseOptionItemImaListener(this);
        adapter.setSeePhotosItemImaListener(this);
        mRecyclerView.setAdapter(adapter);
        //      ????????????
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    protected ArrayList<ItemBean> initDatas() {
        ArrayList<ItemBean> mDatas = new ArrayList<ItemBean>();
        for (int i = 0; i < 1; i++) {
            mDatas.add(new ItemBean());
        }
        return mDatas;
    }

    // ??????????????????????????????????????????
    protected void refreshUI(ArrayList<VoteListBean.VoteBean> data, String flag) {
//        if (tempArr.size() == 0) {
//            emptyView.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.INVISIBLE);
//        }else{
//            emptyView.setVisibility(View.INVISIBLE);
//            recyclerView.setVisibility(View.VISIBLE);
//        }
        Log.d("gdgsdgsdgdgf444",flag+"");
        if (UserUtil.ISCHAIRMAN) {
//            voteAdapter2.addFinalVotestate(data);
            voteAdapter2.setList(data);
            voteAdapter2.setFlag(flag);
            voteAdapter2.notifyDataSetChanged();
        } else {
//            voteAdapter1.addFinalVotestate(data);
            voteAdapter1.setList(data);
            voteAdapter1.setFlag(flag);
            voteAdapter1.notifyDataSetChanged();
        }

        home_refreshLayout.finishRefresh();
    }

    //????????????????????????????????????
    public void loadData() {
        voteList.clear();
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(constant.QUERYVOTE);
        bean.setBody("");
        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);

    }

    //????????????
    @Override
    public void seeImaistener(int position) {

        showIma(position);

    }

    //????????????-??????
    @Override
    public void choseImaistener(int position) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//???????????????
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
        positionIma = position;
    }

    /**
     * ?????????byte?????????
     *
     * @param imagepath ?????????byte???????????????
     * @return ???????????????byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[3*1024*1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }

    private String byte2Base64(byte[] imageByte) {
        if (null == imageByte) return null;
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    private Bitmap base642Bitmap(String base64) {
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        Bitmap mBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        // bitmapList.add(mBitmap);
        Message ms = new Message();
        ms.what = 2;
        // handler.sendMessage(ms);//???????????????
        return mBitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                try {
                    File file = new File(getFilePath(getActivity(), uri));
                    Log.d("requestCodeUr00000", Environment.getExternalStorageDirectory().getAbsolutePath() + "===" + Environment.getExternalStorageDirectory().toString() + "====" + Environment.getStorageState(file));
                    Log.d("requestCodeUrl111", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName() + "++++++++++" + getFilePath(getActivity(), uri) + "===" + uri.getAuthority());
                    String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    if ("jpg".equals(endStr) || "gif".equals(endStr) || "png".equals(endStr) || "jpeg".equals(endStr) || "bmp".equals(endStr)) {
                        list.get(positionIma).setText(file.getPath());
                        adapter.notifyDataSetChanged();
                    } else {

                        Toast.makeText(getActivity(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("requestCodeUr7788", "????????????=" + endStr + "????????????" + file.getName() + getFilePath(getActivity(), uri));

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else {
                //???????????????????????????
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    List<String> pathList = new ArrayList<>();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        try {
                            File file = new File(getFilePath(getActivity(), uri));
                            ToastUtil.makeText(getActivity(), "uri.getPath()=====" + uri.getPath());
                            Log.d("requestCodeUr2222", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName() + "++++++++++" + getFilePath(getActivity(), uri) + "===" + uri.getAuthority());
                            String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                            if ("jpg".equals(endStr) || "gif".equals(endStr) || "png".equals(endStr) || "jpeg".equals(endStr) || "bmp".equals(endStr)) {
                                list.get(positionIma).setText(file.getPath());
                                adapter.notifyDataSetChanged();
                            } else {

                                Toast.makeText(getActivity(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                            }
                            Log.d(TAG, "????????????=" + endStr + "????????????" + file.getName());
                            // fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                            //???????????????????????????
                       /*     new Thread() {
                                @Override
                                public void run() {
                                    copyFile(file.getPath(), fileStrPath + file.getName());
                                }
                            }.start();*/
                           /* fileBean.setResImage(getIamge(endStr));
                            fileBean.setFile_type(getType(endStr));
                            fileBeans.add(fileBean);*/
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

      /*    fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();*/
        }

    }

    @SuppressLint("NewApi")
    public String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                for (int i = 0; i < split.length; i++) {
                    Log.d("requestCodeUr4444", split[i] + "i==" + i);
                }
                Log.d("requestCodeUr55555", Environment.getExternalStorageDirectory() + "/" + split[1]);
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    return "/storage/" + split[0] + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                for (int i = 0; i < split.length; i++) {
                    Log.d("requestCodeUr6666", split[i]);
                }
                Log.d("requestCodeUr7777", split[0]);
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {

            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            String[] projection = {
                    MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //   ????????????????????????????????????
    @Override
    public void clickListener(int position, String flag) {
        Log.d("gdgsdgsdgdgf444????????????","flag==="+flag);
        if (position >= 0 && voteList.size() > position) {
            VoteListBean.VoteBean model = voteList.get(position);
            int status = model.getMvoteStatus();

            // ????????????
            if (status == Constants.VoteStatusEnum.hasFinshed) {
                boolean isNoUser = true;        //  ??????????????????user??????
                boolean isAllNoChoose = true;   //  ???????????????????????????????????????
                if (model.getUser_list().size() > 0) {
                    isNoUser = false;
                    for (VoteListBean.VoteBean.UserListBean bean : model.getUser_list()) {
                        if (bean.getChoose().size() > 0) {
                            isAllNoChoose = false;
                        }
                    }
                }

                if (isNoUser || isAllNoChoose) {
                    ToastUtils.showShort("???????????????!");
                    return;
                }
                if(flag.equals("2")){
                    ToastUtils.showShort("????????????????????????!");
                    return;
                }

                PieChartDialog piechartDialog = new PieChartDialog(getActivity(), R.style.AlertDialogStyle, createData(model), voteList.get(position));
                piechartDialog.show();

                Window window = piechartDialog.getWindow();//??????dialog????????????
                window.setGravity(Gravity.CENTER);//??????????????????
                Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
                WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
                Point size = new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;
                p.width = (int) (width * 0.9);//?????????
                p.height = (int) (height * 0.9);//?????????
                window.setAttributes(p);
            }

            //  ??????
            if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                if (model.getType().equals("0")) {
                    showRadioDialog(model, position, flag);
                } else {
                    showCheckBoxDialog(model, position, flag);
                }
            }
        }
    }

    //   ?????????????????????
    @Override
    public void chairmanClickListener(int position, String flag) {
        Log.d("gdgsdgsdgdgf444??????","flag==="+flag);
        if (position >= 0 && voteList.size() > position) {
            VoteListBean.VoteBean model = voteList.get(position);
            int status = model.getMvoteStatus();

            // ????????????
            if (status == Constants.VoteStatusEnum.hasFinshed) {
                boolean isNoUser = true;        //  ??????????????????user??????
                boolean isAllNoChoose = true;   //  ???????????????????????????????????????
                if (model.getUser_list().size() > 0) {
                    isNoUser = false;
                    for (VoteListBean.VoteBean.UserListBean bean : model.getUser_list()) {
                        if (bean.getChoose().size() > 0) {
                            isAllNoChoose = false;
                        }
                    }
                }

                if (isNoUser || isAllNoChoose) {
                    ToastUtils.showShort("???????????????!");
                    return;
                }
                if(flag.equals("2")){
                    ToastUtils.showShort("????????????????????????!");
                    return;
                }

                PieChartDialog piechartDialog = new PieChartDialog(getActivity(), R.style.AlertDialogStyle, createData(model), voteList.get(position));
                piechartDialog.show();

                Window window = piechartDialog.getWindow();//??????dialog????????????
                window.setGravity(Gravity.CENTER);//??????????????????
                Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
                WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
                Point size = new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;
                p.width = (int) (width * 0.8);//?????????
                p.height = (int) (height * 0.85);//?????????
                window.setAttributes(p);
            }
            Log.d("fgsdfgsdg445555", flag);
            //  ??????
            if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                if (model.getType().equals("0")) {
                    showRadioDialog(model, position, flag);
                } else {
                    showCheckBoxDialog(model, position, flag);
                }
            }
        }
    }

    //  ????????????
    @Override
    public void operationclickListener(int position, String flag) {
        Log.d("operation","flag==="+flag);
        int status = voteList.get(position).getMvoteStatus();
        if (status == Constants.VoteStatusEnum.hasStartUnVote) {
            popTitleArr = new String[]{"??????", "??????"};
        } else if (status == Constants.VoteStatusEnum.hasFinshed) {
            return;
        } else {
            popTitleArr = new String[]{"??????"};
        }

        mLucklyPopopWindow = new LucklyPopopWindow(getActivity());
        //???popupWindow????????????
//        mLucklyPopopWindow.setData(new String[]{"??????","??????"}, new int[]{ R.mipmap.operation_end, R.mipmap.operation_delet});
        mLucklyPopopWindow.setData(popTitleArr);

        //??????????????????
        mLucklyPopopWindow.setWidth(160);
        mLucklyPopopWindow.setRadius(0);
        //????????????
        mLucklyPopopWindow.setOnItemClickListener(new LucklyPopopWindow.OnItemClickListener() {
            @Override
            public void onItemClick(int popPosition) {
                VoteListBean.VoteBean model = voteList.get(position);

                if (popTitleArr[popPosition].equals("??????")) {
                    int status = model.getMvoteStatus();

                    //  ?????????????????????
                    if (status == Constants.VoteStatusEnum.hasStartUnVote) {
                        if (model.getType().equals("0")) {
                            showRadioDialog(model, position, flag);
                        } else {
                            showCheckBoxDialog(model, position, flag);
                        }
                    }
                } else if (popTitleArr[popPosition].equals("??????")) {
                    model.setStatus("FINISH");


                    finishVote(model, flag);

                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", model.get_id());
                    deleatVote(map);
                }
                mLucklyPopopWindow.dismiss();
            }
        });

        //???????????????(??????)
        mLucklyPopopWindow.addItemDecoration(LucklyPopopWindow.VERTICAL, Color.GRAY, 1);
        //??????image?????????(??????)
        mLucklyPopopWindow.setImageDisable(true);
        //??????image?????????(??????)
        mLucklyPopopWindow.setTextSize(20);
        mLucklyPopopWindow.setAnimationStyle(0);
//        mLucklyPopopWindow.setImageSize(15,15);
        //??????popopWindow
        mLucklyPopopWindow.setTrianleHeight(0);
        //  ???????????????view
        View view = mLayoutManager.findViewByPosition(position);
        //mLayoutManager???recyclre??????????????????
        RelativeLayout layout = (RelativeLayout) view;        //?????????????????????????????????
        ImageView operation = (ImageView) layout.findViewById(R.id.operation);
        mLucklyPopopWindow.showAtLocation(getActivity().getWindow().getDecorView(), operation);

    }

    //  ????????????????????????
    private ArrayList<PieEntry> createData(VoteListBean.VoteBean model) {
        ArrayList<PieEntry> pieLists = new ArrayList<>();

        String entryname = "";
        ArrayList<String> optionAll = new ArrayList<>();
        Map<String, Integer> map = new HashMap();
        //  ??????????????????????????????????????????
        for (int i = 0; i < model.getUser_list().size(); i++) {
            VoteListBean.VoteBean.UserListBean userbean = model.getUser_list().get(i);
            for (int j = 0; j < userbean.getChoose().size(); j++) {

                optionAll.add(userbean.getChoose().get(j));
            }
        }

        // ???????????????????????????????????????
        float totalNum = optionAll.size();
        if (optionAll.size() > 0) {
            if (optionAll.size() > 1) {
                int count = 1;// ????????????????????????1
                for (int i = 0; i < optionAll.size(); i++) {
                    count = 1;
                    for (int j = i + 1; j < optionAll.size(); j++) {
                        if (optionAll.get(i).equals(optionAll.get(j))) {
                            count++;// ??????+1
                            optionAll.remove(j);
                            j--;// ??????????????????????????????remove???????????????????????????????????????????????????????????????????????????//????????????
                        }
                    }
                    System.out.println(optionAll.get(i) + "??????????????????" + count);
                    map.put(optionAll.get(i), count);

                }
            } else {
                // ??????1?????????
                totalNum = 1;
                map.put(optionAll.get(0), 1);
            }
        } else {
            //   ?????????,???????????????????????????????????????
        }

        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            List<String> chooseUserList = new ArrayList();
            for (int i = 0; i < model.getUser_list().size(); i++) {
                VoteListBean.VoteBean.UserListBean userbean = model.getUser_list().get(i);
                if (userbean.getChoose().contains(entry.getKey())) {
                    chooseUserList.add(userbean.getUser_name());
                }
            }

            float percent = (entry.getValue() / totalNum) * 100;
            PieEntry pieentry = new PieEntry(percent, entry.getKey());
            pieentry.setChooseUserList(chooseUserList);
            pieLists.add(pieentry);
        }
        return pieLists;
    }


    /*????????????*/
    private void voteSucessDialog() {
        VoteInfoDialog infoDialog = new VoteInfoDialog.Builder(getActivity())
                .setTitle("????????????")
                .setMessage("??????????????????????????????")
                .setButton(view -> {
                        }
                ).create();
        infoDialog.show();
    }

    //  ?????????
    private void showRadioDialog(VoteListBean.VoteBean model, int index, String flag) {
        final List<ChoseBean> list = new ArrayList<>();
        for (int i = 0; i < model.getOptions().size(); i++) {
            ChoseBean object = new ChoseBean();
            object.setContent(model.getOptions().get(i));
            object.setChecked(false);
            list.add(object);
        }

        RadioDialog dialog = new RadioDialog(getActivity(), R.style.AlertDialogStyle, list, flag);


        dialog.show();

       Window window = dialog.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.8);//?????????
        p.height = (int) (height * 0.6);//?????????
        window.setAttributes(p);

        dialog.setTitle(model.getTopic());
        dialog.setEndTime("??????????????????:" + model.getEnd_time());
        dialog.setCreator("????????????" + model.getFrom().getName());
        dialog.setNegBtn("??????", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (model.getAnonymity().equals("1")) {
            dialog.setNiming("??????");
        } else {
            dialog.setNiming("??????");
        }
        dialog.setPosBtn("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> chose = new ArrayList<>();
                ArrayList<VoteListBean.VoteBean.UserListBean> userbeanList = new ArrayList<>(model.getUser_list());

                for (int i = 0; i < list.size(); i++) {
                    ChoseBean object = list.get(i);
                    if (object.isChecked()) {
                        chose.add(object.getContent());
                    }
                }
                VoteBean.UserListBean userListBean = new VoteBean.UserListBean();
                userListBean.setStatus("FINISH");
                userListBean.setUser_name(Hawk.get(constant.myNumber));
                userListBean.setUser_id(FLUtil.getMacAddress());
                userListBean.setMeeting_vote_id(model.get_id());
                userListBean.setChoose(chose);
                requestToVote(userListBean, flag);
            }
        });
        dialog.setOnItemClickEvent(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * websocket??????????????????
     */
    private void wsUpdataVote(Object obj, String packType, String flag) {
        Log.d("gdgsdgsdgdgf444666",flag+"");
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setFlag(flag);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(packType);
        bean.setBody(obj);

        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);
    }


    /**
     * ??????????????????
     */
    private void creatVote(VoteBean bean, String flag) {
        if (UserUtil.isTempMeeting) {
            wsUpdataVote(bean, constant.NEWVOTE, flag);

            VoteInfoDialog infoDialog = new VoteInfoDialog.Builder(getActivity())
                    .setTitle("??????????????????")
                    .create();
            infoDialog.show();
        }
    }

    /**
     * ??????????????????(??????????????????)??????
     */
    private void requestToVote(VoteBean.UserListBean bean, String flag) {
        wsUpdataVote(bean, constant.UPDATEVOTE, flag);

    }

    /**
     * ??????????????????
     */
    private void deleatVote(Map<String, Object> map) {
        NetWorkManager.getInstance().getNetWorkApiService().removeMeetingVote(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onSuccess(BasicResponse response) {
                        loadData();
                    }
                });
    }

    /**
     * ??????????????????
     */
    private void updateMeetingVote(Map<String, Object> map) {
        NetWorkManager.getInstance().getNetWorkApiService().updateMeetingVote(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onSuccess(BasicResponse response) {
                        loadData();
                    }
                });
    }

    /**
     * ??????????????????
     */
    private void finishVote(VoteBean voteBean, String flag) {

        wsUpdataVote(voteBean, constant.FINISHVOTE, flag);
    }


    //  ?????????
    private void showCheckBoxDialog(VoteListBean.VoteBean model, int index, String flag) {
        final List<ChoseBean> list = new ArrayList<>();
        for (int i = 0; i < model.getOptions().size(); i++) {
            ChoseBean object = new ChoseBean();
            object.setContent(model.getOptions().get(i));
            object.setChecked(false);
            list.add(object);
        }

        CheckBoxDialog dialog = new CheckBoxDialog(getActivity(), R.style.AlertDialogStyle, list, flag);
        dialog.show();
        Window window = dialog.getWindow();//??????dialog????????????
        window.setGravity(Gravity.CENTER);//??????????????????
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        WindowManager.LayoutParams p = window.getAttributes(); // ?????????????????????????????????
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.width = (int) (width * 0.8);//?????????
        p.height = (int) (height * 0.6);//?????????
        window.setAttributes(p);

        dialog.setTitle(model.getTopic());
        dialog.setEndTime("??????????????????:" + model.getEnd_time());
        dialog.setCreator("????????????" + model.getFrom().getName());
        dialog.setNegBtn("??????", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (model.getAnonymity().equals("1")) {
            dialog.setNiming("??????");
        } else {
            dialog.setNiming("??????");
        }
        dialog.setPosBtn("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> chose = new ArrayList<>();
                ArrayList<VoteListBean.VoteBean.UserListBean> userbeanList = new ArrayList<>(model.getUser_list());

                for (int i = 0; i < list.size(); i++) {
                    ChoseBean object = list.get(i);
                    if (object.isChecked()) {
                        chose.add(object.getContent());
                    }
                }

                VoteBean.UserListBean userListBean = new VoteBean.UserListBean();
                userListBean.setUser_name(Hawk.get(constant.myNumber));
                userListBean.setStatus("FINISH");
                userListBean.setUser_id(FLUtil.getMacAddress());
                userListBean.setMeeting_vote_id(model.get_id());
                userListBean.setChoose(chose);
                requestToVote(userListBean, flag);
            }
        });
        dialog.setOnItemClickEvent(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    /**
     * ????????????????????????
     */
    @AfterPermissionGranted(constant.WRITE_PERMISSION_CODE1)
    private void checkWritePermission1() {
        if (!PermissionManager.checkPermission(getActivity(), constant.PERMS_WRITE1)) {
            PermissionManager.requestPermission(getActivity(), constant.WRITE_PERMISSION_TIP, constant.WRITE_PERMISSION_CODE1, constant.PERMS_WRITE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //?????????????????????EasyPermission?????????
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Toast.makeText(getActivity(), "????????????", Toast.LENGTH_LONG).show();

    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(getActivity(), "??????????????????", Toast.LENGTH_LONG).show();
        /**
         * ??????????????????????????????????????????'NEVER ASK AGAIN.'??????'????????????'?????????????????????
         * ?????????????????????????????????????????????????????????????????????
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}


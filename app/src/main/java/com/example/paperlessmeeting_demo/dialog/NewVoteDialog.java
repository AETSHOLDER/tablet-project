package com.example.paperlessmeeting_demo.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.RecycleAdapter;
import com.example.paperlessmeeting_demo.bean.ItemBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewVoteDialog extends AlertDialog implements RecycleAdapter.ChoseClickListener {

    public interface creatVoteClickListener {
        public void creatvoteclickListener(VoteListBean.VoteBean model);
    }

    private NewVoteDialog.creatVoteClickListener mcreatvoterClickListener;
    private Context context;

    @BindView(R.id.layout_newvote)
    LinearLayout layoutContent;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.add_chose)
    ImageView add_chose;

    @BindView(R.id.close_icon)
    ImageView close_icon;

    @BindView(R.id.btn_pos)
    Button btn_pos;

    @BindView(R.id.btn_neg)
    Button btn_neg;

    @BindView(R.id.iv_check_state1)
    ImageView ivCheckState1;

    @BindView(R.id.iv_check_state2)
    ImageView ivCheckState2;

    @BindView(R.id.iv_check_state3)
    ImageView ivCheckState3;

    @BindView(R.id.iv_check_state4)
    ImageView ivCheckState4;

    //  输入框
    @BindView(R.id.edit_content)
    EditText edit_content;

    @BindView(R.id.edit_text)
    EditText edit_text;

    @BindView(R.id.edit_text2)
    EditText edit_text2;


    //  当前选中单选
    private ImageView currentSelDanxuan;
    //  当前选中匿名
    private ImageView currentSelNiming;
    private RecycleAdapter adapter;
    private List<ItemBean> list = new ArrayList<ItemBean>();

    public NewVoteDialog(Context context, @StyleRes int themeResId, creatVoteClickListener mcreatvoterClickListener) {
        super(context, themeResId);
        this.context = context;
        this.mcreatvoterClickListener = mcreatvoterClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_newvote);

        ButterKnife.bind(this);
        currentSelDanxuan = ivCheckState1;
        currentSelNiming = ivCheckState4;
        initRecycle();
        initAvtions();

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        layoutContent.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.33), (int) (display
                .getHeight() * 0.55)));
    }


    //  初始化方法
    private void initAvtions() {
        add_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //              添加自带默认动画
                adapter.addData(list.size());
                add_chose.setVisibility(View.INVISIBLE);
            }
        });

        //  关闭
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //  确定创建投票
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_content.getText())) {
                    Toast.makeText(context, "请输入投票内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edit_content.getText().length() < 5) {
                    Toast.makeText(context, "投票内容至少5个字符", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edit_text.getText())) {
                    Toast.makeText(context, "请输入选项1", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edit_text2.getText())) {
                    Toast.makeText(context, "请输入选项2", Toast.LENGTH_SHORT).show();
                    return;
                }

                VoteListBean.VoteBean model = new VoteListBean.VoteBean();
//                model.title = edit_content.getText().toString();
//                Random random = new Random();
//                int num = random.nextInt(500) % 401 + 100;
//                model.setId(num);
//                model.state = Constants.VoteStatusEnum.hasStartUnVote;
//
//                ArrayList<String> options_list = new ArrayList();
//                options_list.add(edit_text.getText().toString());
//                options_list.add(edit_text2.getText().toString());
//                //  拼接选项字符串
//                for (int i = 0; i < list.size(); i++) {
//                    ItemBean bean = list.get(i);
//                    if (!TextUtils.isEmpty(bean.getText())) {
//                        options_list.add(bean.getText().toString());
//                    }
//                }
//                model.options_list = options_list;
//
//                //  单选，匿名
//                model.isduoxuan = currentSelDanxuan == ivCheckState1 ? 0 : 1;
//
//                model.isniming = currentSelNiming == ivCheckState3 ? 1 : 0;

                mcreatvoterClickListener.creatvoteclickListener(model);
                dismiss();

            }
        });

        //取消
        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 单选
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

        //  匿名
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

    //  单选按钮选择
    private void checkDanxuan(Integer tag) {

        if (currentSelDanxuan.equals(ivCheckState1)) {
            if (tag == 1) {
                return;
            }
            ivCheckState1.setImageResource(R.drawable.radio_unselected);
            ivCheckState2.setImageResource(R.drawable.radio_selected);
            currentSelDanxuan = ivCheckState2;
        } else {
            if (tag == 2) {
                return;
            }
            ivCheckState1.setImageResource(R.drawable.radio_selected);
            ivCheckState2.setImageResource(R.drawable.radio_unselected);
            currentSelDanxuan = ivCheckState1;
        }
    }

    //  单选按钮选择
    private void checkNiming(Integer tag) {

        if (currentSelNiming.equals(ivCheckState3)) {
            if (tag == 1) {
                return;
            }
            ivCheckState3.setImageResource(R.drawable.radio_unselected);
            ivCheckState4.setImageResource(R.drawable.radio_selected);
            currentSelNiming = ivCheckState4;
        } else {
            if (tag == 2) {
                return;
            }
            ivCheckState3.setImageResource(R.drawable.radio_selected);
            ivCheckState4.setImageResource(R.drawable.radio_unselected);
            currentSelNiming = ivCheckState3;
        }
    }

    //  初始化列表
    private void initRecycle() {
        //  纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //      获取数据，向适配器传数据，绑定适配器
//        list = initData();
        adapter = new RecycleAdapter(context, list, this);
        mRecyclerView.setAdapter(adapter);
        //      添加动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    protected ArrayList<ItemBean> initData() {
        ArrayList<ItemBean> mDatas = new ArrayList<ItemBean>();
        for (int i = 0; i < 1; i++) {
            mDatas.add(new ItemBean());
        }
        return mDatas;
    }

    //  新增选项
    @Override
    public void clickListener(int position) {
        if (list.size() > 0) {
            add_chose.setVisibility(View.INVISIBLE);
        } else {
            add_chose.setVisibility(View.VISIBLE);
        }
    }

}

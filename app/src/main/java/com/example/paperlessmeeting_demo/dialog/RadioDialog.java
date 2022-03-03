package com.example.paperlessmeeting_demo.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.RadioAdapter;
import com.example.paperlessmeeting_demo.bean.ChoseBean;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RadioDialog extends AlertDialog {
    private RelativeLayout layoutContent;
    private Context context;
    private TextView endTime;
    private TextView creator;
    private CircleImageView avater_img;
    private TextView title;
    private Button btnNeg;
    private Button btnPos;
    private  String flag;
    private Button btnDanxuan;

    private Button btnNiming;
    private MyListView listView;
    private List<ChoseBean> dataList = new ArrayList<>();
    private RadioAdapter adapter;

    public RadioDialog(Context context, @StyleRes int themeResId, List<ChoseBean> dataList, String flag) {
        super(context, themeResId);
        this.context = context;
        this.dataList = dataList;
        this.flag = flag;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_checkbox);

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();

        layoutContent = (RelativeLayout) findViewById(R.id.layout_content);
        layoutContent.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() ), (int) (display.getHeight())));

        endTime = (TextView) this.findViewById(R.id.endtimeClock);
        title = (TextView) this.findViewById(R.id.title);
        avater_img = (CircleImageView) this.findViewById(R.id.avater_img);
        creator = (TextView) this.findViewById(R.id.creator);
        btnNeg = (Button) this.findViewById(R.id.btn_neg);
        btnPos = (Button) this.findViewById(R.id.btn_pos);
        btnDanxuan = (Button) this.findViewById(R.id.danxaun);
        btnNiming = (Button) this.findViewById(R.id.niming);
        btnDanxuan.setText("单选");
        listView = (MyListView) this.findViewById(R.id.listview);
        adapter = new RadioAdapter(context, dataList,flag);
        listView.setAdapter(adapter);
    }

    public void setOnItemClickEvent(final AdapterView.OnItemClickListener listener) {
        if (listView == null) {
            return;
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(parent, view, position, id);
//                dismiss();
            }
        });
    }

    public void setNiming(String text) {
        if (btnNiming == null) {
            return;
        }
        btnNiming.setText(text + "");
    }

    public void setTitle(String text) {
        if (title == null) {
            return;
        }
        title.setText(text + "");
    }

    public void setEndTime(String text) {
        if (endTime == null) {
            return;
        }
        endTime.setText(text + "");
    }

    public void setCreator(String text) {
        if (creator == null) {
            return;
        }
        creator.setText(text + "");
    }

    public void setAvater_img(String avaterStr){
        Drawable drawable =  MeetingAPP.getContext().getResources().getDrawable(R.mipmap.avater_default);
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(drawable).showImageForEmptyUri(drawable).showImageOnFail(drawable).resetViewBeforeLoading(false).delayBeforeLoading(1000).
                cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).handler(new Handler()).build();
        String avate = UrlConstant.baseUrl + "/" + avaterStr;
        ImageLoader.getInstance().displayImage(avate, avater_img,options);
    }


    public void setNegBtn(String text, final View.OnClickListener listener) {
        if (btnNeg == null) {
            return;
        }
        btnNeg.setText(text);
        btnNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dismiss();
            }
        });
    }

    public void setPosBtn(String text, final View.OnClickListener listener) {
        if (btnPos == null) {
            return;
        }
        btnPos.setText(text);
        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasVote = false;
                for (int i = 0; i < dataList.size(); i++) {
                    ChoseBean object = dataList.get(i);
                    if (object.isChecked()) {
                        hasVote = true;
                    }
                }

                if (!hasVote) {
                    Toast.makeText(context, "请先选择选项!", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onClick(v);
                dismiss();
            }
        });
    }

}
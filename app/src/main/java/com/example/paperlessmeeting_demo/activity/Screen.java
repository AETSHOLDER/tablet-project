package com.example.paperlessmeeting_demo.activity;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.util.NetworkUtil;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.tool.VideoEncoderUtil;


public class Screen extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_CODE = 110;
    private MediaProjectionManager projectionManager;
    private Context context;
    private TextView main_demo_click_txt;
    private VideoEncoderUtil videoEncoder;
    private EditText main_demo_edit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*    Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);*/
        setContentView(R.layout.send_screen_main);
        context = this;
        initView();
    }


    private void initView() {



        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        main_demo_edit = (EditText) findViewById(R.id.main_demo_edit);
        main_demo_edit.setText("192.168.31.234");
        main_demo_click_txt = (TextView) findViewById(R.id.main_demo_click_txt);

        main_demo_click_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_demo_click_txt.getText().equals(context.getString(R.string.open_share))) {
                    if (!NetworkUtil.ipCheck(main_demo_edit.getText().toString())) {
                        ToastUtil.makeText(context, "大兄弟IP不对");
                        return;
                    }
                    Intent captureIntent = projectionManager.createScreenCaptureIntent();
                    startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE);
                } else {
                    videoEncoder.stop();
                    main_demo_click_txt.setText(context.getString(R.string.open_share));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            MediaProjection mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            videoEncoder = new VideoEncoderUtil(mediaProjection, main_demo_edit.getText().toString());
            videoEncoder.start();
            main_demo_click_txt.setText(context.getString(R.string.close_share));
        }
    }

}

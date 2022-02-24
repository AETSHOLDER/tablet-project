package com.example.paperlessmeeting_demo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatCheckBox;

import com.example.paperlessmeeting_demo.R;

public class ThreeStateCheckbox extends AppCompatCheckBox {
    static public final int NORMAL = -1;        //  正常状态
    static public final int SPEACH = 0;         //  发言状态
    static public final int MUTE = 1;           //  禁言状态
    private int state;

    public ThreeStateCheckbox(Context context) {
        super(context);
        init();
    }

    public ThreeStateCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThreeStateCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        state = NORMAL;
        updateBtn();

        //  三态 不需要通过此控制
//        setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            // checkbox status is changed from uncheck to checked.
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                switch (state) {
//                    default:
//                    case NORMAL:
//                        state = SPEACH;
//                        break;
//                    case SPEACH:
//                        state = MUTE;
//                        break;
//                    case MUTE:
//                        state = NORMAL;
//                        break;
//                }
//                updateBtn();
//            }
//        });
    }

    private void updateBtn() {
        int btnDrawable = R.drawable.volume_idle;
        switch (state) {
            default:
            case NORMAL:
                btnDrawable = R.drawable.volume_idle;
                break;
            case MUTE:
                btnDrawable = R.drawable.volume_unchecked;
                break;
            case SPEACH:
                btnDrawable = R.drawable.volume_checked;
                break;
        }

        setButtonDrawable(btnDrawable);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        updateBtn();
    }
}

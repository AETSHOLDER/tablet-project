package com.example.paperlessmeeting_demo.widgets;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.paperlessmeeting_demo.R;
import java.util.ArrayList;
import java.util.List;

public class MyWhiteTabLayout extends TabLayout {
    private List<String> titles;

    public MyWhiteTabLayout(Context context) {
        super(context);
        init();
    }

    public MyWhiteTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWhiteTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        titles = new ArrayList<>();
        this.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(Tab tab) {
                /**
                 * 设置当前选中的Tab为特殊高亮样式。
                 */
                if (tab != null && tab.getCustomView() != null) {
                    TextView tab_text = tab.getCustomView().findViewById(R.id.tab_text);
//                    TextPaint paint = tab_text.getPaint();
//                    paint.setFakeBoldText(true);
                    tab_text.setTextSize(26);
                    tab_text.setTextColor(Color.WHITE);
                    tab_text.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    ImageView tab_layout_indicator = tab.getCustomView().findViewById(R.id.tab_indicator);
                    tab_text.bringToFront();
                    tab_layout_indicator.setBackgroundResource(R.drawable.tablayout_item_indicator);
                }
            }

            @Override
            public void onTabUnselected(Tab tab) {
                /**
                 * 重置所有未选中的Tab颜色、字体、背景恢复常态(未选中状态)。
                 */
                if (tab != null && tab.getCustomView() != null) {
                    TextView tab_text = tab.getCustomView().findViewById(R.id.tab_text);
                    tab_text.setTextSize(16);
                    tab_text.setTextColor(Color.WHITE);
//                    TextPaint paint = tab_text.getPaint();
//                    paint.setFakeBoldText(false);

                    ImageView tab_indicator = tab.getCustomView().findViewById(R.id.tab_indicator);
                    tab_indicator.setBackgroundResource(0);
                }
            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }

    public void setTitle(List<String> titles) {
        this.titles = titles;

        /**
         * 开始添加切换的Tab。
         */
        for (String title : this.titles) {
            Tab tab = newTab();
            tab.setCustomView(R.layout.tablayout_white_item);

            if (tab.getCustomView() != null) {
                TextView tab_text = tab.getCustomView().findViewById(R.id.tab_text);
                tab_text.setText(title);
            }

            this.addTab(tab);
        }
    }

    /**
     * 数组长度可以发生变化，采用这种
     * */
    public void refreshTitles(List<String> titles) {
        this.removeAllTabs();
        setTitle(titles);
    }
}

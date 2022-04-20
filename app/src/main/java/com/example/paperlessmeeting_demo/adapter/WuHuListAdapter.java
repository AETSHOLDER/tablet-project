package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 梅涛 on 2020/7/23.
 */

public class WuHuListAdapter extends BaseAdapter {
    private Context context;
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList;
    private saveSeparatelyInterface saveSeparatelyInterface;
    private deletSeparatelyInterface deletSeparatelyInterface;
  private addSeparatelyInterface  addSeparatelyInterface;

    public WuHuListAdapter.addSeparatelyInterface getAddSeparatelyInterface() {
        return addSeparatelyInterface;
    }

    public void setAddSeparatelyInterface(WuHuListAdapter.addSeparatelyInterface addSeparatelyInterface) {
        this.addSeparatelyInterface = addSeparatelyInterface;
    }

    public WuHuListAdapter.deletSeparatelyInterface getDeletSeparatelyInterface() {
        return deletSeparatelyInterface;
    }

    public void setDeletSeparatelyInterface(WuHuListAdapter.deletSeparatelyInterface deletSeparatelyInterface) {
        this.deletSeparatelyInterface = deletSeparatelyInterface;
    }

    public WuHuListAdapter.saveSeparatelyInterface getSaveSeparatelyInterface() {
        return saveSeparatelyInterface;
    }

    public void setSaveSeparatelyInterface(WuHuListAdapter.saveSeparatelyInterface saveSeparatelyInterface) {
        this.saveSeparatelyInterface = saveSeparatelyInterface;
    }

    public List<WuHuEditBean.EditListBean> getWuHuEditBeanList() {
        return wuHuEditBeanList;
    }

    public void setWuHuEditBeanList(List<WuHuEditBean.EditListBean> wuHuEditBeanList) {
        this.wuHuEditBeanList = wuHuEditBeanList;
    }

    public WuHuListAdapter(Context context, List<WuHuEditBean.EditListBean> wuHuEditBeanList) {
        this.context = context;
        this.wuHuEditBeanList = wuHuEditBeanList;
    }

    @Override
    public int getCount() {
        return wuHuEditBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return wuHuEditBeanList.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder viHolder;
        WuHuEditBean.EditListBean wuHuEditBean = wuHuEditBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_dialog_wuhu_edit_list, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }

        if (wuHuEditBeanList.size() > 1) {

            if (i == wuHuEditBeanList.size() - 1) {
                viHolder.add.setVisibility(View.VISIBLE);
            } else {
                viHolder.add.setVisibility(View.INVISIBLE);
            }
        }

        if (viHolder.tittle2.getTag() instanceof TextWatcher) {
            viHolder.tittle2.removeTextChangedListener((TextWatcher) viHolder.tittle2.getTag());
        }
        if (viHolder.tittle3.getTag() instanceof TextWatcher) {
            viHolder.tittle3.removeTextChangedListener((TextWatcher) viHolder.tittle3.getTag());
        }
       if (i==0){
            viHolder.delete.setVisibility(View.GONE);

        }else {
            viHolder.delete.setVisibility(View.VISIBLE);
        }
       if (wuHuEditBeanList.size()==1){
           viHolder.add.setVisibility(View.VISIBLE);
       }
        TextWatcher tittle2Watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    wuHuEditBean.setTemporarySubTopics("");
                    wuHuEditBean.setSubTopics("");
                } else {
                    wuHuEditBean.setSubTopics(s.toString());
                    wuHuEditBean.setTemporarySubTopics(s.toString());
                }
            }
        };
        viHolder.tittle_pos.setText("议题"+(i+1));
        viHolder.tittle2.addTextChangedListener(tittle2Watcher);
        viHolder.tittle2.setTag(tittle2Watcher);
        TextWatcher tittle3Watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    wuHuEditBean.setTemporaryAttendeBean("");
                    wuHuEditBean.setAttendeBean("");
                } else {
                    wuHuEditBean.setAttendeBean(s.toString());
                    wuHuEditBean.setTemporaryAttendeBean(s.toString());
                }
            }
        };
        viHolder.tittle3.addTextChangedListener(tittle3Watcher);
        viHolder.tittle3.setTag(tittle3Watcher);

        viHolder.tittle2.setText(wuHuEditBean.getSubTopics());
        viHolder.tittle3.setText(wuHuEditBean.getAttendeBean());
        viHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletSeparatelyInterface.deletData(i);
            }
        });
        //单独保存
        viHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wuHuEditBean.setSubTopics(viHolder.tittle2.getText().toString());
                wuHuEditBean.setAttendeBean(viHolder.tittle3.getText().toString());
                 saveSeparatelyInterface.saveData(i);
             /*   Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("refreshType","8");
                intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                context.sendBroadcast(intent);*/

            }
        });
        viHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSeparatelyInterface.addData(i);
            }
        });
        return view;
    }
  public interface saveSeparatelyInterface{

    public void saveData(int position);
}
    public interface deletSeparatelyInterface{

        public void deletData(int position);
    }
    public interface addSeparatelyInterface{

        public void addData(int position);
    }

    static class ViewHolder {
        @BindView(R.id.tittle_pos)
        TextView tittle_pos;
        @BindView(R.id.tittle2)
        EditText tittle2;
        @BindView(R.id.tittle3)
        EditText tittle3;
        @BindView(R.id.save)
        TextView save;
        @BindView(R.id.delete)
        TextView delete;
        @BindView(R.id.add)
        TextView add;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

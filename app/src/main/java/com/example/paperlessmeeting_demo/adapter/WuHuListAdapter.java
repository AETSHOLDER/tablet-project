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
import com.orhanobut.hawk.Hawk;

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

        if (viHolder.tittle2.getTag() instanceof TextWatcher) {
            viHolder.tittle2.removeTextChangedListener((TextWatcher) viHolder.tittle2.getTag());
        }
        if (viHolder.tittle3.getTag() instanceof TextWatcher) {
            viHolder.tittle3.removeTextChangedListener((TextWatcher) viHolder.tittle3.getTag());
        }
       if (i==0){
            viHolder.delete.setVisibility(View.GONE);
           viHolder.tittle_pos.setVisibility(View.GONE);
           viHolder.tittle2.setVisibility(View.GONE);
           viHolder.tittle3.setVisibility(View.GONE);
           viHolder.save.setVisibility(View.GONE);
           viHolder.add.setVisibility(View.GONE);
           viHolder.aa.setVisibility(View.GONE);
           viHolder.bb.setVisibility(View.GONE);
           viHolder.add.setVisibility(View.GONE);
           viHolder.edit_rl.setVisibility(View.GONE);

        }else if (i==1){
           viHolder.edit_rl.setVisibility(View.VISIBLE);
           viHolder.delete.setVisibility(View.GONE);
           viHolder.tittle_pos.setVisibility(View.VISIBLE);
           viHolder.tittle2.setVisibility(View.VISIBLE);
           viHolder.tittle3.setVisibility(View.VISIBLE);
           viHolder.save.setVisibility(View.VISIBLE);
           viHolder.aa.setVisibility(View.VISIBLE);
           viHolder.bb.setVisibility(View.VISIBLE);
           if (wuHuEditBeanList.size() ==2) {
               viHolder.add.setVisibility(View.VISIBLE);
           }else {
             viHolder.add.setVisibility(View.GONE);
           }
       }else {
            viHolder.delete.setVisibility(View.VISIBLE);
           viHolder.tittle_pos.setVisibility(View.VISIBLE);
           viHolder.tittle2.setVisibility(View.VISIBLE);
           viHolder.tittle3.setVisibility(View.VISIBLE);
           viHolder.save.setVisibility(View.VISIBLE);
           viHolder.add.setVisibility(View.VISIBLE);
           viHolder.aa.setVisibility(View.VISIBLE);
           viHolder.bb.setVisibility(View.VISIBLE);
           viHolder.edit_rl.setVisibility(View.VISIBLE);
           if (wuHuEditBeanList.size() > 2) {
               if (i == wuHuEditBeanList.size() - 1) {
                   viHolder.add.setVisibility(View.VISIBLE);
               } else {
                   viHolder.add.setVisibility(View.INVISIBLE);
               }
           }
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
                    if (Hawk.contains("WuHuFragmentData")) {
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData",wuHuEditBean);
                    }
                }
            }
        };
       switch (i){
           case 0:
               viHolder.tittle_pos.setText("议题");
               break;
           case 1:
               viHolder.tittle_pos.setText("议题一");
               break;
           case 2:
               viHolder.tittle_pos.setText("议题二");
               break;
           case 3:
               viHolder.tittle_pos.setText("议题三");
               break;
           case 4:
               viHolder.tittle_pos.setText("议题四");
               break;
           case 5:
               viHolder.tittle_pos.setText("议题五");
               break;
           case 6:
               viHolder.tittle_pos.setText("议题六");
               break;
           case 7:
               viHolder.tittle_pos.setText("议题七");
               break;
           case 8:
               viHolder.tittle_pos.setText("议题八");
               break;
           case 9:
               viHolder.tittle_pos.setText("议题九");
               break;
           default:
               viHolder.tittle_pos.setText("议题"+(i));
               break;
       }

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
                    if (Hawk.contains("WuHuFragmentData")) {
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData",wuHuEditBean);
                    }
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
        @BindView(R.id.aa)
        TextView aa;
        @BindView(R.id.bb)
        TextView bb;
        @BindView(R.id.edit_rl)
        LinearLayout edit_rl;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

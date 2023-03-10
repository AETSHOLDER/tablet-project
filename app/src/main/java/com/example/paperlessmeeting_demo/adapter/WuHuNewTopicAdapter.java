package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.ChoseBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.widgets.MyGridView;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WuHuNewTopicAdapter extends RecyclerView.Adapter<WuHuNewTopicAdapter.MyViewHolder> {

    private Context context;
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList;
    private saveSeparatelyInterface saveSeparatelyInterface;
    private deletSeparatelyInterface deletSeparatelyInterface;
    private addSeparatelyInterface addSeparatelyInterface;

    public addSeparatelyInterface getAddSeparatelyInterface() {
        return addSeparatelyInterface;
    }

    public void setAddSeparatelyInterface(addSeparatelyInterface addSeparatelyInterface) {
        this.addSeparatelyInterface = addSeparatelyInterface;
    }

    public deletSeparatelyInterface getDeletSeparatelyInterface() {
        return deletSeparatelyInterface;
    }

    public void setDeletSeparatelyInterface(deletSeparatelyInterface deletSeparatelyInterface) {
        this.deletSeparatelyInterface = deletSeparatelyInterface;
    }

    public saveSeparatelyInterface getSaveSeparatelyInterface() {
        return saveSeparatelyInterface;
    }

    public void setSaveSeparatelyInterface(saveSeparatelyInterface saveSeparatelyInterface) {
        this.saveSeparatelyInterface = saveSeparatelyInterface;
    }

    public List<WuHuEditBean.EditListBean> getWuHuEditBeanList() {
        return wuHuEditBeanList;
    }

    public void setWuHuEditBeanList(List<WuHuEditBean.EditListBean> wuHuEditBeanList) {
        this.wuHuEditBeanList = wuHuEditBeanList;
        notifyDataSetChanged();
    }

    public WuHuNewTopicAdapter(Context context, List<WuHuEditBean.EditListBean> wuHuEditBeanList) {
        this.context = context;
        this.wuHuEditBeanList = wuHuEditBeanList;
        notifyDataSetChanged();
    }
    public WuHuNewTopicAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_dialog_wuhu_edit_list, parent,
                false));

   /*     View v = LayoutInflater.from(context).inflate(R.layout.item_wuhu_vote_list, null, false);
        RecyclerView.ViewHolder holder = null;
        holder = new WuHuVoteChairmanViewHolder(v);*/
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viHolder, int i) {

        WuHuEditBean.EditListBean wuHuEditBean = wuHuEditBeanList.get(i);

        if (viHolder.tittle2.getTag() instanceof TextWatcher) {
            viHolder.tittle2.removeTextChangedListener((TextWatcher) viHolder.tittle2.getTag());
        }
        if (viHolder.tittle3.getTag() instanceof TextWatcher) {
            viHolder.tittle3.removeTextChangedListener((TextWatcher) viHolder.tittle3.getTag());
        }
        if (viHolder.tittle4.getTag() instanceof TextWatcher) {
            viHolder.tittle4.removeTextChangedListener((TextWatcher) viHolder.tittle4.getTag());
        }
        if (i==0){
            viHolder.delete.setVisibility(View.GONE);
            viHolder.tittle_pos.setVisibility(View.GONE);
            viHolder.tittle2.setVisibility(View.GONE);
            viHolder.tittle3.setVisibility(View.GONE);
            viHolder.tittle4.setVisibility(View.GONE);
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
            viHolder.tittle4.setVisibility(View.VISIBLE);
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
            viHolder.tittle4.setVisibility(View.VISIBLE);
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
                }
            }
        };
        switch (i){
            case 0:
                viHolder.tittle_pos.setText("??????");
                break;
            case 1:
                viHolder.tittle_pos.setText("?????????");
                break;
            case 2:
                viHolder.tittle_pos.setText("?????????");
                break;
            case 3:
                viHolder.tittle_pos.setText("?????????");
                break;
            case 4:
                viHolder.tittle_pos.setText("?????????");
                break;
            case 5:
                viHolder.tittle_pos.setText("?????????");
                break;
            case 6:
                viHolder.tittle_pos.setText("?????????");
                break;
            case 7:
                viHolder.tittle_pos.setText("?????????");
                break;
            case 8:
                viHolder.tittle_pos.setText("?????????");
                break;
            case 9:
                viHolder.tittle_pos.setText("?????????");
                break;
            default:
                viHolder.tittle_pos.setText("??????"+(i));
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
                    wuHuEditBean.setReportingUnit("");
                } else {
                    wuHuEditBean.setReportingUnit(s.toString());//????????????
                    wuHuEditBean.setTemporaryAttendeBean(s.toString());

                }
            }
        };
        TextWatcher tittle4Watcher = new TextWatcher() {
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
                    wuHuEditBean.setParticipantUnits("");
                } else {
                    wuHuEditBean.setParticipantUnits(s.toString());//????????????
                    wuHuEditBean.setTemporaryAttendeBean2(s.toString());
                }
            }
        };

        viHolder.tittle3.addTextChangedListener(tittle3Watcher);
        viHolder.tittle3.setTag(tittle3Watcher);

        viHolder.tittle4.addTextChangedListener(tittle4Watcher);
        viHolder.tittle4.setTag(tittle4Watcher);

        viHolder.tittle2.setText(wuHuEditBean.getSubTopics());
        viHolder.tittle3.setText(wuHuEditBean.getReportingUnit());
        viHolder.tittle4.setText(wuHuEditBean.getParticipantUnits());
        viHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletSeparatelyInterface.deletData(i);
            }
        });
        //????????????
        viHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wuHuEditBean.setSubTopics(viHolder.tittle2.getText().toString());
                wuHuEditBean.setReportingUnit(viHolder.tittle3.getText().toString());
                wuHuEditBean.setParticipantUnits(viHolder.tittle4.getText().toString());
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
    @Override
    public int getItemCount() {
        return wuHuEditBeanList.size();
    }

    /**
     * ViewHolder???????????????????????????
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tittle_pos)
        TextView tittle_pos;
        @BindView(R.id.tittle2)
        EditText tittle2;
        @BindView(R.id.tittle3)
        EditText tittle3;
        @BindView(R.id.tittle4)
        EditText tittle4;
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
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);

        }
    }
}

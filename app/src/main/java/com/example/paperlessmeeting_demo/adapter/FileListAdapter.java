package com.example.paperlessmeeting_demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.NewFileBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/7/23.
 * 公开文件适配器
 */

public class FileListAdapter extends BaseAdapter {
    private Context context;
    private List<NewFileBean.MeetingFileListBean> gridViewBeanList;
    private PlayerClickInterface onPlayerClickInterface;
    private PublicToPrivateInterface toPrivateInterface;
    private DeleteInterface deleteInterface;

    public DeleteInterface getDeleteInterface() {
        return deleteInterface;
    }

    public void setDeleteInterface(DeleteInterface deleteInterface) {
        this.deleteInterface = deleteInterface;
    }

    public PublicToPrivateInterface getToPrivateInterface() {
        return toPrivateInterface;
    }

    public void setToPrivateInterface(PublicToPrivateInterface toPrivateInterface) {
        this.toPrivateInterface = toPrivateInterface;
    }

    private ImageView preBtn;
    private NewFileBean.MeetingFileListBean preBean;
    private boolean isCompleted;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public PlayerClickInterface getOnPlayerClickInterface() {
        return onPlayerClickInterface;
    }

    public void setOnPlayerClickInterface(PlayerClickInterface onPlayerClickInterface) {
        this.onPlayerClickInterface = onPlayerClickInterface;
    }

    public List<NewFileBean.MeetingFileListBean> getGridViewBeanList() {
        return gridViewBeanList;
    }

    public void setGridViewBeanList(List<NewFileBean.MeetingFileListBean> gridViewBeanList) {
        this.gridViewBeanList = gridViewBeanList;
    }

    public FileListAdapter(Context context, List<NewFileBean.MeetingFileListBean> gridViewBeanList) {
        this.context = context;
        this.gridViewBeanList = gridViewBeanList;
    }

    @Override
    public int getCount() {
        return gridViewBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return gridViewBeanList.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viHolder = null;
        NewFileBean.MeetingFileListBean gridViewBean = gridViewBeanList.get(i);
        NewFileBean.MeetingFileListBean.UserIdBean userIdBean = gridViewBean.getUser_id();

        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_file_list, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }
        viHolder.typeIma.setImageResource(gridViewBean.getResImage());
        viHolder.fielName.setText(gridViewBean.getName());
        viHolder.person.setText(gridViewBean.getAuthor());
        viHolder.time.setText(gridViewBean.getTime());
        if ("1".equals(gridViewBean.getFile_type())) {
            viHolder.musicReset.setVisibility(View.VISIBLE);
            viHolder.musicStrat.setVisibility(View.VISIBLE);
            viHolder.open.setVisibility(View.GONE);
        } else {
            viHolder.musicReset.setVisibility(View.GONE);
            viHolder.musicStrat.setVisibility(View.GONE);
            viHolder.open.setVisibility(View.VISIBLE);
        }
        if (isCompleted) {
            viHolder.musicStrat.setImageResource(R.mipmap.ic_music_strat);
        }
        ImageView tempIma = viHolder.musicStrat;
        viHolder.musicReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempIma.setImageResource(R.mipmap.ic_music_pasue);
                gridViewBean.setPlaying(true);
                onPlayerClickInterface.onResetMusic(gridViewBean.getPath());
            }
        });
        viHolder.private_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toPrivateInterface.onTransfor(gridViewBean.get_id(), userIdBean.get_id());

            }
        });
        viHolder.delete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dfafaf222", gridViewBean.getC_id()+"");
                deleteInterface.onDelte(gridViewBean.get_id(),gridViewBean.getC_id());

            }
        });
        viHolder.musicStrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dfafaf", "musicStrat" + gridViewBean.getPath());
                ImageView button = (ImageView) view;
                if (gridViewBean.isPlaying()) { //正在播放
                    button.setImageResource(R.mipmap.ic_music_strat);
                    onPlayerClickInterface.onPauseClick(gridViewBean.getPath());
                    gridViewBean.setPlaying(false);
                } else {  //未在播放
                    if (preBean != null) {//有前一个播放的音频，先暂停
                        preBean.setPlaying(false);
                        onPlayerClickInterface.onPauseClick(gridViewBean.getPath());
                    }
                    if (preBtn != null) {
                        preBtn.setImageResource(R.mipmap.ic_music_strat);
                    }
                    Log.d("dfafaf11", "musicStrat" + gridViewBean.getPath());
                    onPlayerClickInterface.onPlayerClick(gridViewBean.getPath());
                    button.setImageResource(R.mipmap.ic_music_pasue);
                    gridViewBean.setPlaying(true);
                    // flagBean.setCategoriesId(categoriesId);
                    //   flagBean.setMusicId(dataBean.getId());
                    //  Hawk.put("flagBean", flagBean);
                    preBtn = button;
                    preBean = gridViewBean;
                }
            }
        });
        return view;
    }

    public interface PlayerClickInterface {
        //播放监听
        public void onPlayerClick(String url);

        //暂停监听
        public void onPauseClick(String url);

        public void onResetMusic(String url);
    }

    public interface PublicToPrivateInterface {
        public void onTransfor(String id, String userId);
    }

    public interface DeleteInterface {
        public void onDelte(String id,String c_id);
    }

    static class ViewHolder {
        @BindView(R.id.type_ima)
        ImageView typeIma;
        @BindView(R.id.fiel_name)
        TextView fielName;
        @BindView(R.id.person)
        TextView person;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.open)
        TextView open;
        @BindView(R.id.private_tv)
        TextView private_tv;
        @BindView(R.id.delete_tv)
        TextView delete_tv;
        @BindView(R.id.music_strat)
        ImageView musicStrat;
        @BindView(R.id.music_reset)
        ImageView musicReset;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

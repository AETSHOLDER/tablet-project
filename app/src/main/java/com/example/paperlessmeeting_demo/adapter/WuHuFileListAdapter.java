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
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.tool.UserUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/7/23.
 */

public class WuHuFileListAdapter extends BaseAdapter {
    private Context context;
    private List<WuHuEditBean.EditListBean.FileListBean> gridViewBeanList;
    private upLoadFileInterface upLoadFileInterface;
    private ImageView preBtn;
    private FileListBean preBean;
    private boolean isCompleted;
    private PlayerClickInterface onPlayerClickInterface;
    private ShareFileInterface shareFileInterface;
    private PushFileInterface pushFileInterface;

    public PushFileInterface getPushFileInterface() {
        return pushFileInterface;
    }

    public void setPushFileInterface(PushFileInterface pushFileInterface) {
        this.pushFileInterface = pushFileInterface;
    }

    public ShareFileInterface getShareFileInterface() {
        return shareFileInterface;
    }

    public void setShareFileInterface(ShareFileInterface shareFileInterface) {
        this.shareFileInterface = shareFileInterface;
    }

    public PlayerClickInterface getOnPlayerClickInterface() {
        return onPlayerClickInterface;
    }

    public void setOnPlayerClickInterface(PlayerClickInterface onPlayerClickInterface) {
        this.onPlayerClickInterface = onPlayerClickInterface;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public WuHuFileListAdapter.upLoadFileInterface getUpLoadFileInterface() {
        return upLoadFileInterface;
    }

    public void setUpLoadFileInterface(WuHuFileListAdapter.upLoadFileInterface upLoadFileInterface) {
        this.upLoadFileInterface = upLoadFileInterface;
    }

    public List<WuHuEditBean.EditListBean.FileListBean> getGridViewBeanList() {
        return gridViewBeanList;
    }

    public void setGridViewBeanList(List<WuHuEditBean.EditListBean.FileListBean> gridViewBeanList) {
        this.gridViewBeanList = gridViewBeanList;
    }

    public WuHuFileListAdapter(Context context, List<WuHuEditBean.EditListBean.FileListBean> gridViewBeanList) {
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
        WuHuEditBean.EditListBean.FileListBean
                gridViewBean = gridViewBeanList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_wuhu_file_list, null);
            viHolder = new ViewHolder(view);
            view.setTag(viHolder);
        } else {
            viHolder = (ViewHolder) view.getTag();
        }
       if (gridViewBean.isNet()){


           viHolder.open.setVisibility(View.GONE);
           viHolder.proprietary.setVisibility(View.GONE);
       }
        viHolder.typeIma.setImageResource(gridViewBean.getResImage());
        viHolder.fielName.setText(gridViewBean.getName());
        viHolder.person.setText(gridViewBean.getAuthor());
        viHolder.time.setText(gridViewBean.getTime());
        if(UserUtil.ISCHAIRMAN){
            viHolder.open.setVisibility(View.VISIBLE);
        }else {
            viHolder.open.setVisibility(View.GONE);
        }
        if (UserUtil.isTempMeeting) {
            viHolder.open.setText("推送");
            viHolder.proprietary.setText("分享");
            //文件分享
            viHolder.proprietary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("asdfasfsf", gridViewBean.getPath() + "=====" + gridViewBean.getFile_type());
                    shareFileInterface.shareFileInfo(gridViewBean.getPath(), gridViewBean.getFile_type(), "1", gridViewBean.getName(), gridViewBean.getAuthor(), gridViewBean.getTime());
                }
            });
            //文件推送
            viHolder.open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("asdfasfsf222", gridViewBean.getPath() + "=====" + gridViewBean.getFile_type());
                    pushFileInterface.pushFileInfo(gridViewBean.getPath(), gridViewBean.getFile_type(), "1", gridViewBean.getName(), gridViewBean.getAuthor(), gridViewBean.getTime());
                }
            });

        } else {

            //文件公开
            viHolder.open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("asdfasfsf", gridViewBean.getPath() + "=====" + gridViewBean.getFile_type());
                    upLoadFileInterface.sendFileInfo(gridViewBean.getPath(), gridViewBean.getFile_type(), "0");
                }
            });
            //文件私有
            viHolder.proprietary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("asdfasfsf", gridViewBean.getPath() + "=====" + gridViewBean.getFile_type());
                    upLoadFileInterface.sendFileInfo(gridViewBean.getPath(), gridViewBean.getFile_type(), "1");
                }
            });
        }


        if ("1".equals(gridViewBean.getFile_type())) {
            viHolder.musicReset.setVisibility(View.VISIBLE);
            viHolder.musicStrat.setVisibility(View.VISIBLE);

        } else {
            viHolder.musicReset.setVisibility(View.GONE);
            viHolder.musicStrat.setVisibility(View.GONE);

        }
        return view;
    }

    public interface upLoadFileInterface

    {
        public void sendFileInfo(String path, String type, String flag);
    }

    /*
    * 临时会议分享文件
    * String actionType:标识事件是分享还是推送  1：分享  2：推送
    * */
    public interface ShareFileInterface {

        public void shareFileInfo(String path, String type, String flag, String name, String author, String time);
    }
    /*
     * 临时会议推送文件
     * */
    public interface PushFileInterface {

        public void pushFileInfo(String path, String type, String flag, String name, String author, String time);
    }
    public interface PlayerClickInterface {
        //播放监听
        public void onPlayerClick(String url);

        //暂停监听
        public void onPauseClick(String url);

        public void onResetMusic(String url);
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
        @BindView(R.id.proprietary)
        TextView proprietary;
        @BindView(R.id.music_strat)
        ImageView musicStrat;
        @BindView(R.id.music_reset)
        ImageView musicReset;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

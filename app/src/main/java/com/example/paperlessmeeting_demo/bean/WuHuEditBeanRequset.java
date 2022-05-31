package com.example.paperlessmeeting_demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**贵金属--规格请求类：SPECLIST，ITEMLIST两个并列类作为请求参数传给服务端
 * Created by js002 on 2016/8/29.
 *
 */
public class WuHuEditBeanRequset implements Serializable {
 private WuHuEditBean content;

    public WuHuEditBean getContent() {
        return content;
    }

    public void setContent(WuHuEditBean content) {
        this.content = content;
    }
}



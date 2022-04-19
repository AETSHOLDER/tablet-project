package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

public class ChoseBean extends Object implements Serializable {
    private String content;
    private boolean isChecked;
    private String  orderNumb;
     private String votePath;

    public String getVotePath() {
        return votePath;
    }

    public void setVotePath(String votePath) {
        this.votePath = votePath;
    }

    public String getOrderNumb() {
        return orderNumb;
    }

    public void setOrderNumb(String orderNumb) {
        this.orderNumb = orderNumb;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public boolean  isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

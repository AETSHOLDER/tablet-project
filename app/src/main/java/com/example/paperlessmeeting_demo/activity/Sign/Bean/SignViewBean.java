package com.example.paperlessmeeting_demo.activity.Sign.Bean;

import java.util.ArrayList;
import java.util.List;

public class SignViewBean {
    private String signAuthor;  // 签批作者
    private List<SignThumbBean> listDatas = new ArrayList<SignThumbBean>();

    public SignViewBean() {
    }

    public SignViewBean(String signAuthor, List<SignThumbBean> listDatas) {
        this.signAuthor = signAuthor;
        this.listDatas = listDatas;
    }

    public String getSignAuthor() {
        return signAuthor;
    }

    public void setSignAuthor(String signAuthor) {
        this.signAuthor = signAuthor;
    }

    public List<SignThumbBean> getListDatas() {
        return listDatas;
    }

    public void setListDatas(List<SignThumbBean> listDatas) {
        this.listDatas = listDatas;
    }
}

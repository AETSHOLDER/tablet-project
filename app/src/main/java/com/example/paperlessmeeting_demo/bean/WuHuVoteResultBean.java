package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

public class WuHuVoteResultBean implements Serializable {

    private String content;//选项内容
    private String  choosenNumb;//选项编号
    private String voteNumb;//投票数
    private int prossBarNumb;//投票所占总进度比
    private String VotePath;

    public String getVotePath() {
        return VotePath;
    }

    public void setVotePath(String votePath) {
        VotePath = votePath;
    }

    public int getProssBarNumb() {
        return prossBarNumb;
    }

    public void setProssBarNumb(int prossBarNumb) {
        this.prossBarNumb = prossBarNumb;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WuHuVoteResultBean() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChoosenNumb() {
        return choosenNumb;
    }

    public void setChoosenNumb(String choosenNumb) {
        this.choosenNumb = choosenNumb;
    }

    public String getVoteNumb() {
        return voteNumb;
    }

    public void setVoteNumb(String voteNumb) {
        this.voteNumb = voteNumb;
    }


}

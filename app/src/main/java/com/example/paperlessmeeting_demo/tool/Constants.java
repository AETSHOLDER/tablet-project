package com.example.paperlessmeeting_demo.tool;

/**
 * Created by wt on 2018/5/28.
 */
public interface Constants {
    interface VoteStatusEnum {
        int hasStartUnVote           = 0; //已开始自己未投票
        int hasVotedNoResult         = 1; //已投票无结果
        int hasFinshed               = 2; //投票流程结束

    }

}

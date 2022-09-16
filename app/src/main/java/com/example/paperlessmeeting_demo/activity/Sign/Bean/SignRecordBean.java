package com.example.paperlessmeeting_demo.activity.Sign.Bean;

import java.util.List;
/**
 * @author cyj
 * @createDate 9/15/22
 * @ClassName: SignRecordBean
 * @Description: 签批文件区分不同会议
 * @Version: 1.0
 */
public class SignRecordBean {
    public SignRecordBean(String signRecord, List<String> signPath) {
        this.signRecord = signRecord;
        this.signPath = signPath;
    }

    /**
     * signRecord : 1111111111
     * signPath : ["111","222"]
     */

    private String signRecord;     //会议id记录
    private List<String> signPath; //签批文件路径

    public String getSignRecord() {
        return signRecord;
    }

    public void setSignRecord(String signRecord) {
        this.signRecord = signRecord;
    }

    public List<String> getSignPath() {
        return signPath;
    }

    public void setSignPath(List<String> signPath) {
        this.signPath = signPath;
    }
}

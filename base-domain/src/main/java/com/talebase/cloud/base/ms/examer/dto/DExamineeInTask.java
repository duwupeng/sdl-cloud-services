package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by kanghong.zhao on 2016-12-20.
 */
public class DExamineeInTask {

    private Integer serialNo;
    private Integer userId;
    private Integer subScore;

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSubScore() {
        return subScore;
    }

    public void setSubScore(Integer subScore) {
        this.subScore = subScore;
    }

//    public Boolean getDoneFlag(){
//        return this.subScore != null & this.subScore >= 0;
//    }
}

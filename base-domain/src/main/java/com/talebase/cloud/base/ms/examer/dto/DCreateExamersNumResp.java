package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
public class DCreateExamersNumResp {

    private Integer succAccsNum = 0;
    private Integer failAccsNum = 0;

    public Integer getFailAccsNum() {
        return failAccsNum;
    }

    public void setFailAccsNum(Integer failAccsNum) {
        this.failAccsNum = failAccsNum;
    }

    public Integer getSuccAccsNum() {
        return succAccsNum;
    }

    public void setSuccAccsNum(Integer succAccsNum) {
        this.succAccsNum = succAccsNum;
    }

    public DCreateExamersNumResp(){}

    public DCreateExamersNumResp(DCreateExamersResp resp){
        this.succAccsNum = resp.getSuccAccs().size();
        this.failAccsNum = resp.getFailAccs().size();
    }
}

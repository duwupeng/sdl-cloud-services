package com.talebase.cloud.base.ms.examer.dto;


/**
 * Created by kanghong.zhao on 2017-1-18.
 */
public class DNextQzResp {

    private Integer userId;
    private Integer taskId;
    private Integer paperId;
    private Integer userExamId;
    private Integer serialNo;
    private String scoreCreater;
    private Integer done;
    private Integer total;
    private String score;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getUserExamId() {
        return userExamId;
    }

    public void setUserExamId(Integer userExamId) {
        this.userExamId = userExamId;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getScoreCreater() {
        return scoreCreater;
    }

    public void setScoreCreater(String scoreCreater) {
        this.scoreCreater = scoreCreater;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

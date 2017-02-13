package com.talebase.cloud.base.ms.examer.dto;

/**
 * 获取打分列表请求参数
 * Created by daorong.li on 2016-12-20.
 */
public class DScoreReq {
    public static Integer SHOW_PART =1;//显示部分
    public static Integer SHOW_ALL = 2;//显示所有
    //private Integer MarkType;//评卷类型,1:按考生评卷，2:按试题评卷
    private Integer showType;//显示类型,1:仅显示未评试题，2：显示全部
    private Integer number;//试卷序号
    private Integer seqNo;//考生序号
    private Integer taskId;//任务id
    private Integer paperId;//试卷id
    private Integer userId;//用户id
    private String buttonType;//up 上一步，next 下一步

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

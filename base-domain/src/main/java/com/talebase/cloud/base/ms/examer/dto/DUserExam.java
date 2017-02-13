package com.talebase.cloud.base.ms.examer.dto;

import java.sql.Timestamp;

/**
 * Created by daorong.li on 2016-12-20.
 */
public class DUserExam {
    /**
     * 测试id
     */
    Integer id;

    /**
     * 测试类型,1:考试(exam);2:360测试(360test);3:在线心理测评(psychological_test);4:调研(research)
     */
    Integer type;

    /**
     * 用户id
     */
    Integer userId;

    /**
     * 项目id
     */
    Integer projectId;

    /**
     * 任务id
     */
    Integer taskId;

    /**
     * 测试开始时间
     */
    java.sql.Timestamp examStartTime;

    /**
     * 测试完成时间
     */
    java.sql.Timestamp examFinishedTime;

    /**
     * 测试使用时间
     */
    Integer useTime;

    /**
     * 测试状态,-1:删除(delete);0:禁用(disabled);1:启用(enabled)
     */
    Integer status;

    /**
     *
     */
    java.sql.Timestamp createTime;

    /**
     * 创建人
     */
    String creater;

    /**
     * 公司id
     */
    Integer companyId;

    /**
     * 发送邮件状态,1:发送中(email_sending);2:发送失败(email_failure);3:发送成功(email_success)
     */
    Integer sendEmailStatus;

    /**
     * 短信发送状态,1:发送中(sms_sending);2:发送失败(sms_failure);3:发送成功(sms_success)
     */
    Integer sendSmsStatus;
    /**
     * 得分
     */
    String score;

    /**
     * 已评填空数
     */
    Integer done;
    /**
     * 总共填空数
     */
    Integer total;
    /**
     * 打分用户
     */
    String scoreCreater;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Timestamp getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(Timestamp examStartTime) {
        this.examStartTime = examStartTime;
    }

    public Timestamp getExamFinishedTime() {
        return examFinishedTime;
    }

    public void setExamFinishedTime(Timestamp examFinishedTime) {
        this.examFinishedTime = examFinishedTime;
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getSendEmailStatus() {
        return sendEmailStatus;
    }

    public void setSendEmailStatus(Integer sendEmailStatus) {
        this.sendEmailStatus = sendEmailStatus;
    }

    public Integer getSendSmsStatus() {
        return sendSmsStatus;
    }

    public void setSendSmsStatus(Integer sendSmsStatus) {
        this.sendSmsStatus = sendSmsStatus;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public String getScoreCreater() {
        return scoreCreater;
    }

    public void setScoreCreater(String scoreCreater) {
        this.scoreCreater = scoreCreater;
    }
}

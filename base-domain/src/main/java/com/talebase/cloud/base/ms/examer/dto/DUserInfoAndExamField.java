package com.talebase.cloud.base.ms.examer.dto;

import java.sql.Timestamp;

/**
 * Created by daorong.li on 2016-12-12.
 */
public class DUserInfoAndExamField {
    /**
     * 测试id
     */
    Integer id;

    /**
     * 测试类型,1:考试(exam);2:360测试(360test);3:在线心理测评(psychological_test);4:调用(research)
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
     *
     */
    java.sql.Timestamp createTime;


    /**
     * 发送邮件状态,1:发送中(email_sending);2:发送失败(email_failure);3:发送成功(email_success)
     */
    Integer sendEmailStatus;

    /**
     * 短信发送状态,1:发送中(sms_sending);2:发送失败(sms_failure);3:发送成功(sms_success)
     */
    Integer sendSmsStatus;

    /**
     * 用户账号
     */
    String account;

    /**
     * 用户姓名
     */
    String name;

    /**
     *
     */
    String email;

    /**
     * 手机号码
     */
    String mobile;

    /**
     * 性别,0:女(female);1:男(male);2:其他(other)
     */
    String gender;

    /**
     * 密码
     */
    String password;

    /**
     * 最高学历
     */
    String highestEducation;

    /**
     * 身份证号码
     */
    String identityNum;

    /**
     * 出生年月
     */
    java.sql.Timestamp birthday;

    /**
     * 工作年限
     */
    Integer workYears;

    /**
     * 行业
     */
    String industryName;

    /**
     * 政治面貌
     */
    String politicalStatus;

    /**
     * 部门id
     */
    Integer deptId;

    /**
     * 部门名称
     */
    String deptName;

    /**
     * 职位
     */
    String position;

    /**
     * 毕业院校
     */
    String school;

    /**
     * 专业
     */
    String profession;

    /**
     * 测试状态,0:禁用(disabled);1:启用(enabled)
     */
    Integer status;

    /**
     * 所属公司id
     */
    Integer companyId;

    /**
     * 创建时间
     */
    java.sql.Timestamp createDate;

    /**
     * 创建人
     */
    String creater;

    /**
     * 扩展字段，保存json内容
     */
    String extensionField;

    /**
     * 用户状态 0:禁用(disabled);1:启用(enabled)
     */
    Integer userStatus;

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public Integer getWorkYears() {
        return workYears;
    }

    public void setWorkYears(Integer workYears) {
        this.workYears = workYears;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getExtensionField() {
        return extensionField;
    }

    public void setExtensionField(String extensionField) {
        this.extensionField = extensionField;
    }
}

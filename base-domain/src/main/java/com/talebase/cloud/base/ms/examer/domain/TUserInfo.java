package com.talebase.cloud.base.ms.examer.domain;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */public class TUserInfo {
    /**
     * 测试用户id
     */
    Integer id;

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
    String workYears;

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
     * 用户状态,0:禁用(disabled);1:启用(enabled)
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getWorkYears() {
        return workYears;
    }

    public void setWorkYears(String workYears) {
        this.workYears = workYears;
    }
}

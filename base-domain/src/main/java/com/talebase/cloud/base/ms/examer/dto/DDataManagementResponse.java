package com.talebase.cloud.base.ms.examer.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author auto-tool
 */
public class DDataManagementResponse {
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
     * 提交时间
     */
    Timestamp endTime;

    /**
     * 考试用时
     */
    String usedTime;

    /**
     * 得分
     */
    BigDecimal totalScore = new BigDecimal(0);

    /**
     * 评卷是否完成
     */
    boolean whetherMarkDone;

    private List<Map<String,Object>> userInfos = new ArrayList<>();

    public List<Map<String, Object>> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<Map<String, Object>> userInfos) {
        this.userInfos = userInfos;
    }

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

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public boolean isWhetherMarkDone() {
        return whetherMarkDone;
    }

    public void setWhetherMarkDone(boolean whetherMarkDone) {
        this.whetherMarkDone = whetherMarkDone;
    }
}

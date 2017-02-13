package com.talebase.cloud.base.ms.consume.domain;

import java.util.Date;

/**
 * Created by suntree.xu on 2016-12-7.
 */
public class TAccount {
    /**
     * 公司id
     */
    Integer companyId;

    /**
     * 点数余额
     */
    Integer pointBalance;

    /**
     * 短信余额
     */
    Integer smsBalance;

    /**
     * 哈希验证码；若验证码与当前记录不符则会锁号，不允许操作
     */
    String vailCode;

    /**
     * 最近修改时间
     */
    Date modifiedDate;

    /**
     * 最近修改者
     */
    String modifier;

    /**
     * 公司有效期
     */
    Date companyValidate;

    /**
     * T币数伐值
     */
    Integer pointValid;

    /**
     * 短信数阀值
     */
    Integer smsValid;

    /**
     * 开通一个账号扣除的T币数
     */
    Integer peraccountValid;

    public Integer getPeraccountValid() {
        return peraccountValid;
    }

    public void setPeraccountValid(Integer peraccountValid) {
        this.peraccountValid = peraccountValid;
    }

    public Date getCompanyValidate() {
        return companyValidate;
    }

    public void setCompanyValidate(Date companyValidate) {
        this.companyValidate = companyValidate;
    }

    public Integer getPointValid() {
        return pointValid;
    }

    public void setPointValid(Integer pointValid) {
        this.pointValid = pointValid;
    }

    public Integer getSmsValid() {
        return smsValid;
    }

    public void setSmsValid(Integer smsValid) {
        this.smsValid = smsValid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(Integer pointBalance) {
        this.pointBalance = pointBalance;
    }

    public Integer getSmsBalance() {
        return smsBalance;
    }

    public void setSmsBalance(Integer smsBalance) {
        this.smsBalance = smsBalance;
    }

    public String getVailCode() {
        return vailCode;
    }

    public void setVailCode(String vailCode) {
        this.vailCode = vailCode;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}

package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.examer.domain.TExercise;

/**
 * 定时交卷使用类
 * Created by daorong.li on 2017-2-7.
 */
public class DTimerExercise extends TExercise {
    /**
     * 公司id
     */
    Integer companyId;
    /**
     * 用户账号
     */
    String account;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

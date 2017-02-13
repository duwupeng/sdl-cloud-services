package com.talebase.cloud.os.login.bind;

import java.util.Date;

/**
 * Created by suntree.xu on 2016-12-16.
 */
public class CodeRule {

    private String verifyCode;

    private Date createTime;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

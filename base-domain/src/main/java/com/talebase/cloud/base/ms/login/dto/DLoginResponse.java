package com.talebase.cloud.base.ms.login.dto;

import com.talebase.cloud.base.ms.examer.domain.TUserInfo;

/**
 * Created by zhangchunlin on 2016-12-21.
 */
public class DLoginResponse {
    /**
     *
     *
     */
    public String token; // optional

   /**
    * 公司ID
    */
    TUserInfo tUserInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TUserInfo gettUserInfo() {
        return tUserInfo;
    }

    public void settUserInfo(TUserInfo tUserInfo) {
        this.tUserInfo = tUserInfo;
    }
}

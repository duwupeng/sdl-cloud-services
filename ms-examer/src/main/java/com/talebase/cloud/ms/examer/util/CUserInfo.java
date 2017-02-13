package com.talebase.cloud.ms.examer.util;

import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.common.exception.BizEnums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-29.
 */
public class CUserInfo {

    private TUserInfo userInfo;

    private List<FieldErrTip> errTips = new ArrayList<>();

    private boolean ingoreErr = true;

    public TUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(TUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<FieldErrTip> getErrTips() {
        return errTips;
    }

    public void setErrTips(List<FieldErrTip> errTips) {
        this.errTips = errTips;
    }

//    public void addBizEnums(BizEnums biz, TUserShowField field){
//        addBizEnums(biz, null);
//    }

    public void addErrTip(BizEnums biz, TUserShowField field){
        getErrTips().add(new FieldErrTip(field, biz));
    }

    public void addErrTip(BizEnums biz, String key, String name){
        getErrTips().add(new FieldErrTip(key, name, biz));
    }

    public boolean isIngoreErr() {
        return ingoreErr;
    }

    public void setIngoreErr(boolean ingoreErr) {
        this.ingoreErr = ingoreErr;
    }

    public String getErrMsg(){
        StringBuffer sb = new StringBuffer();
        for(FieldErrTip tip : errTips){
            sb.append(tip.getName() + ":" + tip.getBizErr().getMessage() + ";");
        }
        return sb.toString();
    }

}

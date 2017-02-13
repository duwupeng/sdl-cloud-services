package com.talebase.cloud.ms.examer.util;

import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.common.exception.BizEnums;

/**
 * Created by kanghong.zhao on 2016-12-30.
 */
public class FieldErrTip {

    private String key;
    private String name;
    private BizEnums bizErr;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BizEnums getBizErr() {
        return bizErr;
    }

    public void setBizErr(BizEnums bizErr) {
        this.bizErr = bizErr;
    }

    public FieldErrTip(String key, String name, BizEnums bizErr) {
        this.key = key;
        this.name = name;
        this.bizErr = bizErr;
    }

    public FieldErrTip(TUserShowField field, BizEnums bizErr) {
        this.key = field.getFieldKey();
        this.name = field.getFieldName();
        this.bizErr = bizErr;
    }

    public FieldErrTip(){}
}

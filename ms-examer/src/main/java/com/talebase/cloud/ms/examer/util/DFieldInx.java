package com.talebase.cloud.ms.examer.util;

import com.talebase.cloud.base.ms.examer.domain.TUserShowField;

/**
 * Created by kanghong.zhao on 2016-12-9.
 */
public class DFieldInx{
    private TUserShowField field;
    private Integer idx;

    public DFieldInx(Integer idx, TUserShowField field){
        this.idx = idx;
        this.field = field;
    }

    public TUserShowField getField() {
        return field;
    }

    public void setField(TUserShowField field) {
        this.field = field;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }
}


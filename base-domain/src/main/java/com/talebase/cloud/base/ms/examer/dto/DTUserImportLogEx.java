package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.examer.domain.TUserImportLog;
import com.talebase.cloud.base.ms.examer.domain.TUserImportRecord;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
public class DTUserImportLogEx extends TUserImportLog{

    private List<TUserImportRecord> failRecords;

    public List<TUserImportRecord> getFailRecords() {
        return failRecords;
    }

    public void setFailRecords(List<TUserImportRecord> failRecords) {
        this.failRecords = failRecords;
    }
}

package com.talebase.cloud.base.ms.notify.dto;

import java.util.List;

/**
 * Created by bin.yang on 2016-12-14.
 */
public class DNotifyData {
    List<DNotifyRecord> dNotifyRecords;

    Integer emailCount;

    Integer smsCount;

    public List<DNotifyRecord> getdNotifyRecords() {
        return dNotifyRecords;
    }

    public void setdNotifyRecords(List<DNotifyRecord> dNotifyRecords) {
        this.dNotifyRecords = dNotifyRecords;
    }

    public Integer getEmailCount() {
        return emailCount;
    }

    public void setEmailCount(Integer emailCount) {
        this.emailCount = emailCount;
    }

    public Integer getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
    }
}

package com.talebase.cloud.base.ms.notify.dto;

/**
 * Created by bin.yang on 2016-12-5.
 */
public class DReceiverStatistics {
    Integer totalCount;
    Integer hasSmsCount;
    Integer hasEmailCount;
    String emailReceiver;
    String smsReceiver;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getHasSmsCount() {
        return hasSmsCount;
    }

    public void setHasSmsCount(Integer hasSmsCount) {
        this.hasSmsCount = hasSmsCount;
    }

    public Integer getHasEmailCount() {
        return hasEmailCount;
    }

    public void setHasEmailCount(Integer hasEmailCount) {
        this.hasEmailCount = hasEmailCount;
    }

    public String getEmailReceiver() {
        return emailReceiver;
    }

    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    public String getSmsReceiver() {
        return smsReceiver;
    }

    public void setSmsReceiver(String smsReceiver) {
        this.smsReceiver = smsReceiver;
    }
}

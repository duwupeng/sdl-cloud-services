package com.talebase.cloud.base.ms.notify.dto;

import java.util.List;

/**
 * Created by bin.yang on 2016-12-2.
 */
public class DToSendNotifyResponse {
    List emailList;
    List smsList;
    Integer totalCount;
    Integer smsCount;
    Integer emailCount;
    List<DReceiverInfo> dReceiverInfos;

    public List getEmailList() {
        return emailList;
    }

    public void setEmailList(List emailList) {
        this.emailList = emailList;
    }

    public List getSmsList() {
        return smsList;
    }

    public void setSmsList(List smsList) {
        this.smsList = smsList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
    }

    public Integer getEmailCount() {
        return emailCount;
    }

    public void setEmailCount(Integer emailCount) {
        this.emailCount = emailCount;
    }

    public List<DReceiverInfo> getdReceiverInfos() {
        return dReceiverInfos;
    }

    public void setdReceiverInfos(List<DReceiverInfo> dReceiverInfos) {
        this.dReceiverInfos = dReceiverInfos;
    }
}

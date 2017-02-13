package com.talebase.cloud.base.ms.notify.dto;

import java.sql.Timestamp;

/**
 * Created by bin.yang on 2016-12-6.
 */
public class DNotifyRecordStatus {
    /**
     * 发送日志-自增长id
     */
    Integer id;
    /**
     * 状态
     */
    Integer status;
    /**
     * 发送时间
     */
    Timestamp sendTime;
    /**
     * 类型0:邮件；1:短信
     */
    Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

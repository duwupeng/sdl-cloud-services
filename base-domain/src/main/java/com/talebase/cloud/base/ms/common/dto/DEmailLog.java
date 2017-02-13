package com.talebase.cloud.base.ms.common.dto;

import java.sql.Timestamp;

/**
 * Created by daorong.li on 2016-12-2.
 */
public class DEmailLog {
   /**
    *
    */
    Integer id;

    /**
     * 表明
     */
    String tableName;

    /**
     * 表id
     */
    Integer tableId;

    /**
     * 发送内容
     */
    String sendContent;

    /**
     * 发送时间
     */
    java.sql.Timestamp sendTime;

    /**
     * 发送人
     */
    String sender;

    /**
     * 发送次数,预留字段
     */
    Integer sendTimes;

    /**
     * 发送状态,0:失败(failure);1:成功(success)
     */
    Integer status;

    String subject;

    String email;
    /**
     * 多个表名
     */
    String tableNames;

    /**
     * 多个表ids
     */
    String tableIds;
    /**
     * 查询状态
     */
    String statuss;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Integer getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(Integer sendTimes) {
        this.sendTimes = sendTimes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTableNames() {
        return tableNames;
    }

    public void setTableNames(String tableNames) {
        this.tableNames = tableNames;
    }

    public String getTableIds() {
        return tableIds;
    }

    public void setTableIds(String tableIds) {
        this.tableIds = tableIds;
    }

    public String getStatuss() {
        return statuss;
    }

    public void setStatuss(String statuss) {
        this.statuss = statuss;
    }
}

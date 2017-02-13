package com.talebase.cloud.base.ms.examer.dto;

import java.util.Date;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
public class DUserImportRecord {

    private Integer id;
    private String batchNo;
    private String examinee;
    private Integer status;
    private String remark;

    private String creater;
    private Long createdDateL;

    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getExaminee() {
        return examinee;
    }

    public void setExaminee(String examinee) {
        this.examinee = examinee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Long getCreatedDateL() {
        return createdDateL;
    }

    public void setCreatedDateL(Long createdDateL) {
        this.createdDateL = createdDateL;
    }
}

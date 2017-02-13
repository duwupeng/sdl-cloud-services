package com.talebase.cloud.base.ms.project.dto;

import com.talebase.cloud.base.ms.project.domain.TProjectAdmin;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class DProjectEditReq {

    private String name;
    private String description;
    private String startDateStr;
    private String endDateStr;
    private String accountsStr;

    private List<TProjectAdmin> projectAdmins;//不作为os参数，但是作为ms参数

    private Boolean scanEnable;
    private String scanAccountPre;
    private Integer scanMax;
    private String scanStartDateStr;
    private String scanEndDateStr;
    private String scanImage;

    public String getScanImage() {
        return scanImage;
    }

    public void setScanImage(String scanImage) {
        this.scanImage = scanImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountsStr() {
        return accountsStr;
    }

    public void setAccountsStr(String accountsStr) {
        this.accountsStr = accountsStr;
    }

    public Boolean getScanEnable() {
        return scanEnable;
    }

    public void setScanEnable(Boolean scanEnable) {
        this.scanEnable = scanEnable;
    }

    public String getScanAccountPre() {
        return scanAccountPre;
    }

    public void setScanAccountPre(String scanAccountPre) {
        this.scanAccountPre = scanAccountPre;
    }

    public Integer getScanMax() {
        return scanMax;
    }

    public void setScanMax(Integer scanMax) {
        this.scanMax = scanMax;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getScanStartDateStr() {
        return scanStartDateStr;
    }

    public void setScanStartDateStr(String scanStartDateStr) {
        this.scanStartDateStr = scanStartDateStr;
    }

    public String getScanEndDateStr() {
        return scanEndDateStr;
    }

    public void setScanEndDateStr(String scanEndDateStr) {
        this.scanEndDateStr = scanEndDateStr;
    }

    public List<TProjectAdmin> getProjectAdmins() {
        return projectAdmins;
    }

    public void setProjectAdmins(List<TProjectAdmin> projectAdmins) {
        this.projectAdmins = projectAdmins;
    }
}

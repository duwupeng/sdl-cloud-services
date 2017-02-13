package com.talebase.cloud.base.ms.project.dto;

import com.talebase.cloud.base.ms.project.domain.TProjectAdmin;

import java.util.Date;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class DProjectInEdit {

    private Integer id;
    private String name;
    private String description;
//    private Date startDate;
//    private Date endDate;
    private Long startDateL;
    private Long endDateL;
    private List<TProjectAdmin> admins;
    private Boolean scanEnable;
    private Integer scanMax;
    private String scanAccountPre;
    private Integer scanNow;
//    private Date scanStartDate;
//    private Date scanEndDate;
    private Long scanStartDateL;
    private Long scanEndDateL;
    private String scanImage;
    private String imagePath;

    private String adminAccount;
    private String adminName;


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getScanImage() {
        return scanImage;
    }

    public void setScanImage(String scanImage) {
        this.scanImage = scanImage;
    }

    public Long getStartDateL() {
        return startDateL;
    }

    public void setStartDateL(Long startDateL) {
        this.startDateL = startDateL;
    }

    public Long getEndDateL() {
        return endDateL;
    }

    public void setEndDateL(Long endDateL) {
        this.endDateL = endDateL;
    }

    public Long getScanStartDateL() {
        return scanStartDateL;
    }

    public void setScanStartDateL(Long scanStartDateL) {
        this.scanStartDateL = scanStartDateL;
    }

    public Long getScanEndDateL() {
        return scanEndDateL;
    }

    public void setScanEndDateL(Long scanEndDateL) {
        this.scanEndDateL = scanEndDateL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<TProjectAdmin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<TProjectAdmin> admins) {
        this.admins = admins;
    }

    public Boolean getScanEnable() {
        return scanEnable;
    }

    public void setScanEnable(Boolean scanEnable) {
        this.scanEnable = scanEnable;
    }

    public Integer getScanMax() {
        return scanMax;
    }

    public void setScanMax(Integer scanMax) {
        this.scanMax = scanMax;
    }

    public String getScanAccountPre() {
        return scanAccountPre;
    }

    public void setScanAccountPre(String scanAccountPre) {
        this.scanAccountPre = scanAccountPre;
    }

    public Integer getScanNow() {
        return scanNow;
    }

    public void setScanNow(Integer scanNow) {
        this.scanNow = scanNow;
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public void setAdminAccount(String adminAccount) {
        this.adminAccount = adminAccount;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}

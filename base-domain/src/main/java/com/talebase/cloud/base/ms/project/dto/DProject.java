package com.talebase.cloud.base.ms.project.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class DProject {

    private Integer id;
    private String name;
    private Integer status;

//    private Date startDate;
//    private Date endDate;

    private Long startDateL;
    private Long endDateL;

    private Integer scanEnable;


    private Integer errNum = 0;

    private List<DTask> tasks = new ArrayList<>();

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getErrNum() {
        return errNum;
    }

    public void setErrNum(Integer errNum) {
        this.errNum = errNum;
    }

    public Integer getScanEnable() {
        return scanEnable;
    }

    public void setScanEnable(Integer scanEnable) {
        this.scanEnable = scanEnable;
    }
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }

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


//    public Date getEndDate() {
//        return endDate;
//
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }

    public List<DTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<DTask> tasks) {
        this.tasks = tasks;
    }

}

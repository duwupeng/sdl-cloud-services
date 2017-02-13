package com.talebase.cloud.base.ms.notify.dto;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by bin.yang on 2016-11-25.
 */
public class DNotifyTemplateStatus {

    private Integer id;
    private Boolean status;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

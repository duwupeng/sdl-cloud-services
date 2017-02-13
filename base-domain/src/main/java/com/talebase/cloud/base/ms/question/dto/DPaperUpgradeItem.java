package com.talebase.cloud.base.ms.question.dto;

/**
 * Created by kanghong.zhao on 2016-12-5.
 */
public class DPaperUpgradeItem {

    private Integer paperId;
    private Boolean hasNewVersion;

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Boolean getHasNewVersion() {
        return hasNewVersion;
    }

    public void setHasNewVersion(Boolean hasNewVersion) {
        this.hasNewVersion = hasNewVersion;
    }
}

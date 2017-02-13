package com.talebase.cloud.base.ms.examer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
public class DCreateExamersResp {

    private List<String> succAccs = new ArrayList<>();

    private List<String> failAccs = new ArrayList<>();

    public List<String> getSuccAccs() {
        return succAccs;
    }

    public void setSuccAccs(List<String> succAccs) {
        this.succAccs = succAccs;
    }

    public List<String> getFailAccs() {
        return failAccs;
    }

    public void setFailAccs(List<String> failAccs) {
        this.failAccs = failAccs;
    }
}

package com.talebase.cloud.base.ms.examer.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by daorong.li on 2016-12-8.
 */
public class DUserShowFieldResponseList {
    private List<Map<String,Object>> optionalList;
    private List<Map<String,Object>> requiredList;
    private List<Map<String,Object>> uniqueList;

    public List<Map<String, Object>> getOptionalList() {
        return optionalList;
    }

    public void setOptionalList(List<Map<String, Object>> optionalList) {
        this.optionalList = optionalList;
    }

    public List<Map<String, Object>> getRequiredList() {
        return requiredList;
    }

    public void setRequiredList(List<Map<String, Object>> requiredList) {
        this.requiredList = requiredList;
    }

    public List<Map<String, Object>> getUniqueList() {
        return uniqueList;
    }

    public void setUniqueList(List<Map<String, Object>> uniqueList) {
        this.uniqueList = uniqueList;
    }
}

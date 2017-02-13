package com.talebase.cloud.base.ms.paper.dto;

import com.talebase.cloud.common.util.StringUtil;

import java.math.BigDecimal;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DPaperQuery {
    String key1Begin;
    String searchType1;
    String key1End;
    String key2;
    String searchType2;
    Integer status;

    private static String TOTAL_SCORE = "totalScore";
    private static String DURATION = "duration";
    private static String USAGE = "usage";
    private static String CREATOR = "creator";
    private static String NAME = "name";
    private static String TOTALNUM = "totalNum";
    private static String CREATED_DATE = "createdDate";


    public boolean isCreatedDateBegin() {
        if (CREATED_DATE.equals(searchType1) && !StringUtil.isEmpty(key1Begin)) {
            return true;
        }
        return false;
    }

    public boolean isCreatedDateEnd() {
        if (CREATED_DATE.equals(searchType1) && !StringUtil.isEmpty(key1End)) {
            return true;
        }
        return false;
    }

    public boolean isTotalScore() {
        if (TOTAL_SCORE.equals(searchType2) && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public boolean isDuration() {
        if (DURATION.equals(searchType2) && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public boolean isUsage() {
        if (USAGE.equals(searchType2) && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public boolean isCreator() {
        if (CREATOR.equals(searchType2) && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public boolean isTotalNum() {
        if (TOTALNUM.equals(searchType2) && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public boolean isName() {
        if (NAME.equals(searchType2) && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public String getKey1Begin() {
        return key1Begin;
    }

    public void setKey1Begin(String key1Begin) {
        this.key1Begin = key1Begin;
    }

    public String getSearchType1() {
        return searchType1;
    }

    public void setSearchType1(String searchType1) {
        this.searchType1 = searchType1;
    }

    public String getKey1End() {
        return key1End;
    }

    public void setKey1End(String key1End) {
        this.key1End = key1End;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getSearchType2() {
        return searchType2;
    }

    public void setSearchType2(String searchType2) {
        this.searchType2 = searchType2;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

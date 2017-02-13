package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by kanghong.zhao on 2017-1-18.
 */
public class DNextExamUserReq {

    public static String INGORE_YES = "YES";
    public static String INGORE_NO = "NO";

    private Integer userId = 0;
    private Integer taskId;
    private String preOrNext;
//    private Boolean ingoreMarke = false;
    private String ingoreMarke ;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getPreOrNext() {
        return preOrNext;
    }

    public void setPreOrNext(String preOrNext) {
        this.preOrNext = preOrNext;
    }

    public String getIngoreMarke() {
        return ingoreMarke;
    }

    public void setIngoreMarke(String ingoreMarke) {
        this.ingoreMarke = ingoreMarke;
    }

    //    public Boolean getIngoreMarke() {
//        return ingoreMarke;
//    }
//
//    public void setIngoreMarke(Boolean ingoreMarke) {
//        this.ingoreMarke = ingoreMarke;
//    }
}

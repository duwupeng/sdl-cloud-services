package com.talebase.cloud.base.ms.examer.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by daorong.li on 2016-12-19.
 */
public class DScoreJsonValue {
    private String number;//题目序号
    private Integer userId;
    private Integer seq;//考生号
    private String[]  score;
    private String[]  fullScore;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

//    public String getScore() {
//        return score;
//    }
//
//    public void setScore(String score) {
//        this.score = score;
//    }


    public String[] getScore() {
        return score;
    }

    public void setScore(String[] score) {
        this.score = score;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

//    public String getFullScore() {
//        return fullScore;
//    }
//
//    public void setFullScore(String fullScore) {
//        this.fullScore = fullScore;
//    }

    public String[] getFullScore() {
        return fullScore;
    }

    public void setFullScore(String[] fullScore) {
        this.fullScore = fullScore;
    }
}

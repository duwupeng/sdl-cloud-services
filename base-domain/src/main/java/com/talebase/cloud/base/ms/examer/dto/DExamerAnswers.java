package com.talebase.cloud.base.ms.examer.dto;

import org.springframework.boot.autoconfigure.mail.MailProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by daorong.li on 2016-12-21.
 */
public class DExamerAnswers {

    /**
     * 考生序号
     */
    Integer seqNo;
    /**
     * 考生ID
     */
    //Integer examerId;
    Integer userId;

    /**
     * 本题附件地址
     */
    //List<String> filePath;

    /**
     * 考生答案
     */
    //String[] answers;//考生答案
    List<Map<String,Object>> answers;//考生答案

    List<String> fullScore;//题满分

    List<String> score;//得分

    boolean canScore;//能不能打分



    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
//    public Integer getExamerId() {
//        return examerId;
//    }
//
//    public void setExamerId(Integer examerId) {
//        this.examerId = examerId;
//    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

/*    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }*/

    public List<Map<String, Object>> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Map<String, Object>> answers) {
        this.answers = answers;
    }

    public List<String> getFullScore() {
        return fullScore;
    }

    public void setFullScore(List<String> fullScore) {
        this.fullScore = fullScore;
    }

    public List<String> getScore() {
        return score;
    }

    public void setScore(List<String> score) {
        this.score = score;
    }

    public boolean isCanScore() {
        return canScore;
    }

    public void setCanScore(boolean canScore) {
        this.canScore = canScore;
    }
}

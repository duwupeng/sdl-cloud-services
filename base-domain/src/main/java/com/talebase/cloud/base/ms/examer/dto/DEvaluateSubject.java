package com.talebase.cloud.base.ms.examer.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by eric.du on 2016-12-19.
 */
public class DEvaluateSubject{
    /**
     * 试题详情
     */
    DEvaluateItemDetail dItemDetail;
    /**
     * 考生答题详细
     */
    List<ExamerAnswers> examerAnswers;

    public DEvaluateItemDetail getdItemDetail() {
        return dItemDetail;
    }

    public void setdItemDetail(DEvaluateItemDetail dItemDetail) {
        this.dItemDetail = dItemDetail;
    }

    public List<ExamerAnswers> getExamerAnswers() {
        return examerAnswers;
    }

    public void setExamerAnswers(List<ExamerAnswers> examerAnswers) {
        this.examerAnswers = examerAnswers;
    }

    public class ExamerAnswers{

        /**
         * 考生序号
         */
        Integer seq;
        /**
         * 考生ID
         */
        Integer examerId;

        /**
         * 本题附件地址
         */
        List<String> filePath;
        /**
         * 考生答案
         */

        //String[] answers;
        List<Map<String,Object>> answers;
        /**
         * 打分空格数
         */
        Integer spaceNum;
        /**
         * 打分明细
         */
        DNextQzResp markScore;

        public List<String> getFilePath() {
            return filePath;
        }

        public void setFilePath(List<String> filePath) {
            this.filePath = filePath;
        }

        public List<Map<String, Object>> getAnswers() {
            return answers;
        }

        public void setAnswers(List<Map<String, Object>> answers) {
            this.answers = answers;
        }

        public Integer getSeq() {
            return seq;
        }

        public void setSeq(Integer seq) {
            this.seq = seq;
        }

        public Integer getExamerId() {
            return examerId;
        }

        public void setExamerId(Integer examerId) {
            this.examerId = examerId;
        }

        public Integer getSpaceNum() {
            return spaceNum;
        }

        public void setSpaceNum(Integer spaceNum) {
            this.spaceNum = spaceNum;
        }

        public DNextQzResp getMarkScore() {
            return markScore;
        }

        public void setMarkScore(DNextQzResp markScore) {
            this.markScore = markScore;
        }
    }
}

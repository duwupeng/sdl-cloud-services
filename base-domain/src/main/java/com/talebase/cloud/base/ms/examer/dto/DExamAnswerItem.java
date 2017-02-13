package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by eric.du on 2016-12-12.
 */
public class DExamAnswerItem {

    /**
     *4. 单选
     *5. 多选
     *6. 填空
     *7. 附件
     */
    Integer type;
    /**
     * 题目的id
     */
    Integer id;
    /**
     * 题目编号
     */
    int  seqNo;

    /**
     *所选答案
     * seqNo. 当前题目的Id
     * 1. 选择题 ：[{"seqNo":1,"answers":["A"],"type":4}]
     * 2. 多选题： [{"seqNo":1,"answers":["A","B"],"type":5}]
     * 3. 填空题：[{"seqNo":1,"answers":["填空答案1","填空答案2"],"type":6}]
     * 4. 上传题目[{"seqNo":1,"answers":["附件path1","附件path2"],"type":7}]
     * @return
     */
    String[] answers;
    double[] scores;
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double[] getScores() {
        return scores;
    }

    public void setScores(double[] scores) {
        this.scores = scores;
    }
}

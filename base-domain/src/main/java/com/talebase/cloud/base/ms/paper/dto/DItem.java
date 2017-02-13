package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by eric.du on 2016-12-12.
 */
public class DItem {

    /**
     * 自增长ID
     */
    Integer id;

    /**
     * 编号
     */
    String unicode;


    /**
     * 1. 试卷信息
     * 2. 页码
     * 3. 说明
     * 4. 单择题
     * 5. 多择题
     * 6. 填空题
     * 7. 上传题目
     * 8. 结束语
     */
    Integer type;

    /**
     * 创建人
     */
    String creator;
    /**
     * 更新人
     */
    String modifier;

    /**
     * 版本类型.1:大版本,0 小版本
     */
    int versionType;

    /**
     * 分数
     */
    BigDecimal score;

    /**
     * 分数
     */
    List<String> scores;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public List<String> getScores() {
        return scores;
    }

    public void setScores(List<String> scores) {
        this.scores = scores;
    }

    public int getVersionType() {
        return versionType;
    }

    public void setVersionType(int versionType) {
        this.versionType = versionType;
    }
}

package com.talebase.cloud.base.ms.paper.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TPaperRemark {
    /**
     *
     */
    Integer id;

    /**
     * 起始分数
     */
    BigDecimal startScore;

    /**
     * 结束分数
     */
    BigDecimal endScore;


    /**
     * 结束语
     */
    String description;

    /**
     *
     */
    String unicode;

    /**
     *
     */
    int version;

    /**
     * 创建人
     */
    String creator;

    /**
     * 创建日期
     */
    java.sql.Timestamp createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getStartScore() {
        return startScore;
    }

    public void setStartScore(BigDecimal startScore) {
        this.startScore = startScore;
    }

    public BigDecimal getEndScore() {
        return endScore;
    }

    public void setEndScore(BigDecimal endScore) {
        this.endScore = endScore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TPaperRemark that = (TPaperRemark) o;

        if (!(startScore.compareTo(that.startScore)==0)) return false;
        if (!(endScore.compareTo(that.endScore)==0)) return false;
        if (!description.equals(that.description)) return false;
        return unicode.equals(that.unicode);
    }

    @Override
    public int hashCode() {
        int result = startScore.hashCode();
        result = 31 * result + endScore.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + unicode.hashCode();
        return result;
    }
}
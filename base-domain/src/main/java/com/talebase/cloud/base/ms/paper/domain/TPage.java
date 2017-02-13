package com.talebase.cloud.base.ms.paper.domain;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TPage {
    /**
     * 唯一标示
     */
    Integer id;

    /**
     * 题目乱序,0:否(no);1:是(yes)
     */
    int subjectOrder;

    /**
     * 选项乱序,0:否(no);1:是(yes)
     */

    int optionOrder;

    /**
     * 创建日期
     */

    java.sql.Timestamp createdDate;

    /**
     * 创建人
     */
    String creator;

    /**
     * 页码编号
     */
    String unicode;

    /**
     * 页码版本
     */
    int version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public void setSubjectOrder(int subjectOrder) {
        this.subjectOrder = subjectOrder;
    }

    public void setOptionOrder(int optionOrder) {
        this.optionOrder = optionOrder;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getSubjectOrder() {
        return subjectOrder;
    }

    public int getOptionOrder() {
        return optionOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TPage tPage = (TPage) o;

        if (subjectOrder != tPage.subjectOrder) return false;
        if (optionOrder != tPage.optionOrder) return false;
        return unicode.equals(tPage.unicode);
    }

    @Override
    public int hashCode() {
        int result = subjectOrder;
        result = 31 * result + optionOrder;
        result = 31 * result + unicode.hashCode();
        return result;
    }
}

package com.talebase.cloud.base.ms.paper.domain;

/**
 * @author auto-tool
 */
public class TCompany {
    /**
     *
     */
    Integer id;

    /**
     * 考卷页码编号(外键)
     */
    Integer paperId;

    /**
     * 题型,0:选择题(option);1:填空题(blank);2:上传题(atachment)
     */
    Integer subjectType;

    /**
     * 题目标识
     */
    Integer subjectId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }



}

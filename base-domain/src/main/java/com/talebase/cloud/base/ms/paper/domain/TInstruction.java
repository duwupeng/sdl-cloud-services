package com.talebase.cloud.base.ms.paper.domain;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TInstruction {
    /**
     *
     */
    Integer id;

    /**
     *
     */
    String comment;

    /**
     *
     */
    String filePath;

    /**
     *
     */
    java.sql.Timestamp createdDate;

    /**
     *
     */
    String creator;

    /**
     *
     */
    String unicode;

    /**
     *
     */
    int version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

        TInstruction that = (TInstruction) o;

        if (!comment.equals(that.comment)) return false;
        if (!filePath.equals(that.filePath)) return false;
        return unicode.equals(that.unicode);
    }

    @Override
    public int hashCode() {
        int result = comment.hashCode();
        result = 31 * result + filePath.hashCode();
        result = 31 * result + unicode.hashCode();
        return result;
    }
}

package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DInstruction extends DItem {
    /**
     *
     */
    String comment;

    /**
     *
     */
    String filePath;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DInstruction dInstruction = (DInstruction) o;
        return comment == dInstruction.comment;
    }

    @Override
    public int hashCode() {
        int result = comment.hashCode();
        return result;
    }
}

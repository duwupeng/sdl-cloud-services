package com.talebase.cloud.os.paper.vo;

import java.math.BigDecimal;

/**
 * Created by bin.yang on 2016-12-21.
 */
public class Attachment {
    String type;
    String title;
    BigDecimal score;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}

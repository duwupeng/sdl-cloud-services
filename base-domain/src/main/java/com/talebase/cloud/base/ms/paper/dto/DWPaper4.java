package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 题库新建与修改中的返回
 * Created by eric.du on 2016-12-6.
 */
public class DWPaper4 {
    /**
     * 试卷名称
     */
    String name;

    /**
     * 题目
     */
    List items = new ArrayList();
    /**
     * 新建中，修改中，完成
     */
    Integer mode;

    /**
     * 试卷总分
     */
    BigDecimal score;

    public List getItems() {
        return items;
    }

    public void setItem(Object items) {
        this.items.add(items);
    }

    public String getName() {
        return name;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setItems(List items) {
        this.items = items;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}

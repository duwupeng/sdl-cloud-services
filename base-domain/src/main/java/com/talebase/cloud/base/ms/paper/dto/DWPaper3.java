package com.talebase.cloud.base.ms.paper.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 题库新建与修改中的返回
 * Created by eric.du on 2016-12-6.
 */
public class DWPaper3 extends DItem {
    /**
     * 试卷名称
     */
    String name;

    /**
     * 题目
     */
    List items = new ArrayList();

    /**
     * 数量
     */
    Integer number;

    /**
     * 总页数
     */
    Integer totalPage;

    /**
     * 每页有多少题[{"pageIndex":1,"itemSize":5},{"pageIndex":2,"itemSize":3}]
     *
     * @return
     */
    List<Map> pageList;

    public List getItems() {
        return items;
    }

    public void setItem(Object items) {
        this.items.add(items);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<Map> getPageList() {
        return pageList;
    }

    public void setPageList(List<Map> pageList) {
        this.pageList = pageList;
    }
}

package com.talebase.cloud.common.protocal;

/**
 * 分页请求
 * Created by eric on 16/11/14.
 */
public class PageRequest {
    /**
     * 查询的开始序号（序号从零开始）
     *
     */
    private Integer start = 0; // required
    /**
     * 返回记录数
     *
     */
    private Integer limit = 10; // required

    /**
     * 当前页码
     */
    private Integer pageIndex = 1;

    /**
     * 排序的字段<br/>
     * <ul>
     * <li>field1 asc, field2 desc</li>
     * </ul>
     *
     */
    public String sortFields; // optional

    public Integer getStart() {
        if(start == 0 && pageIndex > 1){
            setStart((pageIndex - 1) * limit);
        }
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        this.sortFields = sortFields;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
//        this.start = (pageIndex - 1) * limit;
    }
}

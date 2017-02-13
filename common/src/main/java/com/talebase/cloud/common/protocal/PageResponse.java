package com.talebase.cloud.common.protocal;

/**
 * 分页返回
 * Created by eric on 16/11/15.
 */
import java.util.ArrayList;
import java.util.List;
/**
 * 分页返回
 * Created by eric on 16/11/15.
 */
public class PageResponse<T> {
    /**
     * 查询的开始序号（序号从零开始）
     **/
    private Integer start;
    /**
     * 最大返回的记录数
     **/
    private Integer limit;

    private Integer pageIndex;

    /**
     * 总记录数
     */
    private Integer total = 0;
    private List<T>  results = new ArrayList<T>();

//    private L req;

    public PageResponse(){}

    public PageResponse(PageRequest pageReq){
        this.pageIndex = pageReq.getPageIndex();
        this.start = pageReq.getStart() != null ? pageReq.getStart() : 0;
        this.limit = pageReq.getLimit();
    }

    public PageResponse(PageRequest pageReq, List<T> results, Integer total){
        this.pageIndex = pageReq.getPageIndex();
        this.start = pageReq.getStart() != null ? pageReq.getStart() : 0;
        this.limit = pageReq.getLimit();
        this.results = results;
        this.total = total;
    }

//    public PageResponse(L req, PageRequest pageReq){
//        this.setReq(req);
//        this.pageIndex = pageReq.getPageIndex();
//        this.start = pageReq.getStart() != null ? pageReq.getStart() : 0;
//        this.limit = pageReq.getLimit();
//    }
//
//    public L getReq() {
//        return req;
//    }
//
//    public void setReq(L req) {
//        this.req = req;
//    }

    public Integer getStart() {
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
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
    public List<T> getResults() {
        return results;
    }
    public void setResults(List<T> results) {
        this.results = results;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
}
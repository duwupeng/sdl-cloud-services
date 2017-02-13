package com.talebase.cloud.common.protocal;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class PageResponseWithParam<T, L> extends PageResponse<T>{

    private L param;

    public L getParam() {
        return param;
    }

    public void setParam(L param) {
        this.param = param;
    }

    public PageResponseWithParam(PageRequest pageRequest, L param){
        super(pageRequest);
        this.param = param;
    }

    public PageResponseWithParam(PageRequest pageRequest, L param, List<T> results){
        super(pageRequest);
        this.param = param;
        setResults(results);
    }

    public PageResponseWithParam(PageRequest pageRequest, L param, List<T> results, Integer total){
        super(pageRequest, results, total);
        this.param = param;
    }

    public PageResponseWithParam(){}
}

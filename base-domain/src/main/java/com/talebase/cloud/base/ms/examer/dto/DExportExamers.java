package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.examer.domain.TUserShowField;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-13.
 */
public class DExportExamers {

    private List<String> headers;
    private List<List<String>> data;

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}

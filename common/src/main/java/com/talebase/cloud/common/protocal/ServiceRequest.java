package com.talebase.cloud.common.protocal;


/**
 * Created by kanghong.zhao on 2016-11-16.
 */
public class ServiceRequest<T> {
    private T request;
    private String accessToken;
    private ServiceHeader requestHeader;
    private PageRequest pageReq;
    public T getRequest() {
        return request;
    }
    public void setRequest(T request) {
        this.request = request;
    }
    public ServiceHeader getRequestHeader() {
        return requestHeader;
    }
    public void setRequestHeader(ServiceHeader requestHeader) {
        this.requestHeader = requestHeader;
    }
    public PageRequest getPageReq() {
        return pageReq;
    }
    public void setPageReq(PageRequest pageReq) {
        this.pageReq = pageReq;
    }

    public ServiceRequest(String accessToken, T request){
        this.accessToken = accessToken;
        this.request = request;
    }

    public ServiceRequest(String accessToken){
        this.accessToken = accessToken;
    }

    public ServiceRequest(ServiceHeader requestHeader, T request){
        this.requestHeader = requestHeader;
        this.request = request;
    }

    public ServiceRequest(ServiceHeader requestHeader){
        this.requestHeader = requestHeader;
    }

    public ServiceRequest(ServiceHeader requestHeader, PageRequest pageReq){
        this.requestHeader = requestHeader;
        this.pageReq = pageReq;
    }

    public ServiceRequest(ServiceHeader requestHeader, T request, PageRequest pageReq){
        this.requestHeader = requestHeader;
        this.request = request;
        this.pageReq = pageReq;
    }

    public ServiceRequest(){}
}
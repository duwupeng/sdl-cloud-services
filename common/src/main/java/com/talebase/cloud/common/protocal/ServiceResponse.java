package com.talebase.cloud.common.protocal;

import com.talebase.cloud.common.exception.ExceptionEnums;
import com.talebase.cloud.common.exception.WrappedException;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by eric on 16/11/11.
 */
public class ServiceResponse<T> {
    //返回范型
    T response;

    //返回码
    int code=0;

    //返回消息
    String message="SUCESS";

    //是否为业务异常
    boolean isBizError=false;

    //拥有的权限
    Map<String,Boolean> permission = new HashMap<>();

    public ServiceResponse() {}

    public ServiceResponse(T response) {
        this.response = response;
    }

    public ServiceResponse(T response,ExceptionEnums exEnum) {
        this.response = response;
        this.code = exEnum.getCode();
        this.message = exEnum.getCode() == 500 ? "网络错误" : exEnum.getMessage();
    }

    public ServiceResponse(ExceptionEnums exEnum) {
        this.code = exEnum.getCode();
        this.message = exEnum.getCode() == 500 ? "网络错误" : exEnum.getMessage();
    }

    public ServiceResponse(WrappedException ex) {
        this.code = ex.getErrCode();
        this.message = ex.getErrMsg();
    }

    public ServiceResponse(ExceptionEnums exEnum,boolean isBizError) {
        this.code = exEnum.getCode();
        this.message = exEnum.getCode() == 500 ? "网络错误" : exEnum.getMessage();
        this.isBizError = isBizError;
    }

    public ServiceResponse(T response,ExceptionEnums exEnum,Map<String,Boolean> permission) {
        this.response = response;
        this.code = exEnum.getCode();
        this.message = exEnum.getCode() == 500 ? "网络错误" : exEnum.getMessage();
        this.permission = permission;
    }

    public ServiceResponse(Map<String,Boolean> permission) {
        this.permission = permission;
    }

    public ServiceResponse(T response,Map<String,Boolean> permission) {
        this.response = response;
        this.permission = permission;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBizError() {
        return isBizError;
    }

    public void setBizError(boolean bizError) {
        isBizError = bizError;
    }

    public Map<String, Boolean> getPermission() {
        return permission;
    }

    public void setPermission(Map<String, Boolean> permission) {
        this.permission = permission;
    }

//    public Long getSystemTimeL(){
//        return System.currentTimeMillis();
//    }
}

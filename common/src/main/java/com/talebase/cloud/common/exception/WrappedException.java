package com.talebase.cloud.common.exception;

/**
 * 异常包装类
 * Created by eric on 16/11/10.
 */
public class WrappedException extends RuntimeException{
    private ExceptionEnums errEnum;
    private String errMsg;
    private Integer errCode;

    public WrappedException(ExceptionEnums openServiceErrEnum){
        this.errEnum = openServiceErrEnum;
        this.errMsg = openServiceErrEnum.getMessage();
        this.errCode = openServiceErrEnum.getCode();
    }

    public WrappedException(ExceptionEnums openServiceErrEnum, String errMsg){
        this.errEnum = openServiceErrEnum;
        this.errMsg = errMsg;
        this.errCode = openServiceErrEnum.getCode();
    }

    public WrappedException(Integer errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public ExceptionEnums getOpenServiceErrEnum(){
        return errEnum;
    }

    public void setOpenServiceErrEnum(ExceptionEnums openServiceErrEnum) {
        this.errEnum = openServiceErrEnum;
    }

    public ExceptionEnums getErrEnum() {
        return errEnum;
    }

    public void setErrEnum(ExceptionEnums errEnum) {
        this.errEnum = errEnum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }
}
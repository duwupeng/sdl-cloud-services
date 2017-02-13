package com.talebase.cloud.common.exception;

/**
 * Created by eric on 16/11/10.
 */
public enum SystemErrEnums implements ExceptionEnums{

    BAD_SQL(10001, "SQL错误");

    public int code;
    public String message;

	private SystemErrEnums(int code, String message){
        this.code = code;
        this.message = message;
    }


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

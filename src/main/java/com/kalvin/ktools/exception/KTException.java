package com.kalvin.ktools.exception;

/**
 * 自定义异常类
 */
public class KTException extends RuntimeException {

    private Integer errorCode;
    private String msg;

    public KTException(String msg) {
        this.msg = msg;
    }

    public KTException(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

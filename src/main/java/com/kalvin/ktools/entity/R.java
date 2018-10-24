package com.kalvin.ktools.entity;

import com.kalvin.ktools.comm.constant.Constant;

import java.io.Serializable;

/**
 * 前端数据返回类
 */
public class R implements Serializable {

    private long serialVersionUID = 1L;

    private int errorCode;
    private String msg;
    private Object data;

    private R(int errorCode, String msg, Object data) {
        this.errorCode = errorCode;
        this.msg = msg;
        this.data = data;
    }

    public static R ok() {
        return new R(Constant.OK_CODE, Constant.OK_MSG, null);
    }

    public static R ok(String msg) {
        return new R(Constant.OK_CODE, msg, null);
    }

    public static R ok(Object data) {
        return new R(Constant.OK_CODE, Constant.OK_MSG, data);
    }

    public static R fail(String msg) {
        return new R(Constant.FAIL_CODE, msg, null);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}

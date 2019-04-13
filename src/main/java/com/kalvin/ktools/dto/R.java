package com.kalvin.ktools.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.ktools.comm.constant.Constant;

import java.io.Serializable;

/**
 * 前端数据返回类
 */
public class R implements Serializable {

    private final static long serialVersionUID = 1L;

    private Integer errorCode;
    private String msg;
    private Object data;
    private Long total; // 分页信息：总条数

    private R(int errorCode, String msg, Object data) {
        this.errorCode = errorCode;
        this.msg = msg;
        this.data = data;
        if (data instanceof Page<?>) {
            Page<?> page = (Page<?>) data;
            this.total = page.getTotal();
        }
    }

    public static R ok() {
        return new R(Constant.OK_CODE, Constant.OK_MSG, null);
    }

//    public static R ok(String msg) {
//        return new R(Constant.OK_CODE, msg, null);
//    }

    public static R ok(Object data) {
        return new R(Constant.OK_CODE, Constant.OK_MSG, data);
    }

    public static R ok(String msg, Object data) {
        return new R(Constant.OK_CODE, msg, data);
    }

    public static R fail(String msg) {
        return new R(Constant.FAIL_CODE, msg, null);
    }

    public static R fail(int errorCode, String msg) {
        return new R(errorCode, msg, null);
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

    public Long getTotal() {
        return total;
    }
}

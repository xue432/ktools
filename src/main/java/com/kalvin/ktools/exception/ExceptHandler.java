package com.kalvin.ktools.exception;

import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.entity.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class ExceptHandler {

    @ExceptionHandler(KTException.class)
    public R handleKtException(KTException e) {
        if (e.getErrorCode() == null) {
            return R.fail(e.getMsg());
        }
        return R.fail(e.getErrorCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        return R.fail(Constant.OTHER_FAIL_CODE, e.getMessage());
    }
}

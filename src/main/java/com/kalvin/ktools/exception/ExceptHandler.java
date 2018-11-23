package com.kalvin.ktools.exception;

import com.kalvin.ktools.comm.constant.Constant;
import com.kalvin.ktools.entity.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
public class ExceptHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptHandler.class);

    @ExceptionHandler(KTException.class)
    public R handleKtException(KTException e) {
        LOGGER.error("KTools异常：{}", e);
        if (e.getErrorCode() == null) {
            return R.fail(e.getMsg());
        }
        return R.fail(e.getErrorCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        LOGGER.error("KTools异常：{}", e);
        return R.fail(Constant.OTHER_FAIL_CODE, e.getMessage());
    }
}

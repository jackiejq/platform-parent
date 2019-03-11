package com.slljr.finance.admin.heandler;

import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 1、统一处理异常
 *
 * @author XueYi
 * 2018年12月5日 上午9:25:22
 */
@RestControllerAdvice
public class BaseHeandler {

    private static final Logger log = LogManager.getLogger(BaseHeandler.class);

    /**
     * 统一异常返回
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelMap handleException(Exception e) {
        log.error("系统异常:", e);
        return WriteJson.errorWebMsg(MsgEnum.SERVER_EXCEPTION);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelMap handleException(IllegalArgumentException e) {
        log.error("非法参数异常:" + e);
        return WriteJson.errorWebMsg(e.getMessage());
    }
}
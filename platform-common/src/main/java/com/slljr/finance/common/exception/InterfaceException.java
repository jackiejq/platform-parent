package com.slljr.finance.common.exception;

import com.slljr.finance.common.utils.MsgEnum;

/**
 * 自定义异常
 * @Auth Created by guoqun.yang
 * @Date Created in 16:06 2018/1/19
 * @Version 1.0
 */
public class InterfaceException extends Exception {

    private Integer code;

    private String message;

    public InterfaceException(String message) {
        this.code = MsgEnum.SERVER_EXCEPTION.getCode();
        this.message = message;
    }

    public InterfaceException(MsgEnum msgEnum) {
        this.code = msgEnum.getCode();
        this.message = msgEnum.getMsg();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

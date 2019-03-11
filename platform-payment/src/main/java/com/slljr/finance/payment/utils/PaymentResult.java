package com.slljr.finance.payment.utils;

import com.slljr.finance.common.enums.PaymentResultCodeEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

public class PaymentResult {
    /**
     * 业务办理是否成功
     */
    private boolean success = false;
    private Integer code = PaymentResultCodeEnum.FAIL.getCode();
    private String msg = PaymentResultCodeEnum.FAIL.getMsg();
    private Map<String, String> map = new HashMap<>();

    public void setSuccess() {
        setSuccess(PaymentResultCodeEnum.SUCCESS);
    }
    public void setSuccess(PaymentResultCodeEnum resultCodeEnum) {
        this.success = true;
        if (resultCodeEnum != null){
            this.code = resultCodeEnum.getCode();
            this.msg = resultCodeEnum.getMsg();
        }
    }

    public void setError(PaymentResultCodeEnum resultCodeEnum) {
        this.success = false;
        if (resultCodeEnum != null){
            this.code = resultCodeEnum.getCode();
            this.msg = resultCodeEnum.getMsg();
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String get(String key) {
        return map.get(key);
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void setError(String msg){
        this.success = false;
        this.code = -999;
        this.msg = msg;
    }



    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

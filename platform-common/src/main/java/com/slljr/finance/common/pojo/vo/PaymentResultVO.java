package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.enums.PaymentResultCodeEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PaymentResultVO {
    private Boolean success = false;
    private Integer code = PaymentResultCodeEnum.FAIL.getCode();
    private String msg = PaymentResultCodeEnum.FAIL.getMsg();
    private String businessId;
    private Integer tradeOrderId;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(Integer tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

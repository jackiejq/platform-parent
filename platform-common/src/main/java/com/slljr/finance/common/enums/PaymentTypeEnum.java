package com.slljr.finance.common.enums;

/**
 * @description: 数据状态
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 19:28.
 */
public enum PaymentTypeEnum {
    PAYMENT(1, "支付/扣款"), REPAYMENT(2, "提现/还款");

    int key;
    String msg;

    PaymentTypeEnum(int key, String msg) {
        this.key = key;
        this.msg = msg;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

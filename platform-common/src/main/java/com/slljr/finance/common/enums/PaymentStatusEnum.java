package com.slljr.finance.common.enums;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2018/12/13.
 * @time: 17:45.
 */
public enum PaymentStatusEnum {

    CANCEL(-2, "取消"), FAIL(-1, "支付失败"), WAITING_PAY(0, "待支付"), PAY_WAITING_CONFIRM(1, "已支付待确认"), PAY_SUCCESS(2, "支付成功");

    int key;
    String msg;

    PaymentStatusEnum(int key, String msg) {
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

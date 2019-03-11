package com.slljr.finance.common.enums;

/**
 * @description: 数据状态
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 19:28.
 */
public enum GoodsOrderStatusEnum {

    CANCEL(-1, "取消"),PACKED(0, "待发货"), SHIPPED(-1, "已发货");

    int key;
    String msg;

    GoodsOrderStatusEnum(int key, String msg) {
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

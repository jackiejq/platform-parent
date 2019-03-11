package com.slljr.finance.common.enums;

public enum TradeOrderStatusEnum {
    RESET(-4, "已重置"),CANCEL(-3, "取消"), FAIL_ALL(-2, "全部失败"), FAIL_SOME(-1, "部分失败"), WAITING_CONFIRM(0, "订单待确认"), ING(1, "进行中"), SUCCESS(2, "成功");

    int key;
    String msg;
    TradeOrderStatusEnum(int key, String msg){
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

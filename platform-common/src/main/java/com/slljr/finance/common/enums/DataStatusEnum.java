package com.slljr.finance.common.enums;

/**
 * @description: 数据状态
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 19:28.
 */
public enum DataStatusEnum {

    NORMAL(0, "正常"), INVALID(-1, "删除");

    int key;
    String msg;

    DataStatusEnum(int key, String msg) {
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

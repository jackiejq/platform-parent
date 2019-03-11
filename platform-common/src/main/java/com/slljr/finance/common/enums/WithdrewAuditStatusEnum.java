package com.slljr.finance.common.enums;

public enum WithdrewAuditStatusEnum {
    FAIL(-1, "审核不通过"), WAITING(0, "待审核"), REVIEW(1, "审核中"), PASS(2, "审核通过"),;

    int key;
    String msg;

    WithdrewAuditStatusEnum(int key, String msg) {
        this.key = key;
        this.msg = msg;
    }

    public static WithdrewAuditStatusEnum val(Integer operate) {
        //values()方法返回enum实例的数组
        for (WithdrewAuditStatusEnum s : values()) {
            if (operate.equals(s.getKey())) {
                return s;
            }
        }
        return null;
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

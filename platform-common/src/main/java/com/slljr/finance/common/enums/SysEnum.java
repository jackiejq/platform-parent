package com.slljr.finance.common.enums;

public enum SysEnum {
    LOGIN_SESSION_KEY("LOGIN_SESSION_KEY", "登录sessionKey");

    String key;
    String value;
    SysEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

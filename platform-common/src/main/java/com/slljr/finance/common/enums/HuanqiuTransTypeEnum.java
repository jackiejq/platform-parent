package com.slljr.finance.common.enums;

/**
 * @description: 环球汇聚落地还款交易类型
 * @author: uncle.quentin.
 * @date: 2018/12/21.
 * @time: 19:19.
 */
public enum HuanqiuTransTypeEnum {

    TRANSACTION("55", "交易"),
    WITHDRAW("56", "提现"),
    OPEN_CARD("57", "开卡"),
    BINDCARD("58", "绑卡"),
    ENTERING("59", "入驻");
    public String key;
    public String value;

    HuanqiuTransTypeEnum(String key, String value) {
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

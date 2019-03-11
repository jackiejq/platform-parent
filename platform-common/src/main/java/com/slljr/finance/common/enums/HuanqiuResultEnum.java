package com.slljr.finance.common.enums;

/**
 * @description: 环球汇聚落地还款返回信息
 * @author: uncle.quentin.
 * @date: 2018/12/21.
 * @time: 19:19.
 */
public enum HuanqiuResultEnum {

    HUANQIU_RESUL_SUCCESS("0000", "交易成功"),
    HUANQIU_RESUL_FAIL("0001", "交易失败"),
    HUANQIU_RESUL_ERROR("0002", "交易异常"),
    HUANQIU_RESUL_PROCESSING("0003", "交易处理中"),
    HUANQIU_RESUL_UNREGISTERED("0004", "商户未注册"),
    HUANQIU_RESUL_DISABLE("0005", "商户被禁用"),
    HUANQIU_RESUL_REQUEST_ERROR("0006", "报文验签失败");
    public String key;
    public String value;

    HuanqiuResultEnum(String key, String value) {
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

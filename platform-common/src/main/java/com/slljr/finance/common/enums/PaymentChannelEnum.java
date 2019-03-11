package com.slljr.finance.common.enums;

/**
 * @description: 通道类型枚举
 * @author: uncle.quentin.
 * @date: 2018/12/17.
 * @time: 13:34.
 */
public enum PaymentChannelEnum {
    //腾付通-代还
    TENGFUTONG(1, "TENGFUTONG"),
    //通联-代还/收款
    TONGLIAN(2, "TONGLIAN"),
    //佳付通-收款
    JIAFUTONG(3, "JIAFUTONG"),
    //环球汇聚-代还/收款
    HUANQIUHUIJU(4, "HUANQIUHUIJU");

    int key;
    String code;

    PaymentChannelEnum(int key, String msg) {
        this.key = key;
        this.code = msg;
    }

    public static PaymentChannelEnum get(String code){
        for(PaymentChannelEnum channel : PaymentChannelEnum.values()){
            if (channel.getCode().equals(code)){
                return channel;
            }
        }
        return null;
    }
    public static PaymentChannelEnum get(int key){
        for(PaymentChannelEnum channel : PaymentChannelEnum.values()){
            if (channel.getKey() == key){
                return channel;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

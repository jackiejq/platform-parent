package com.slljr.finance.common.enums;

public enum PaymentChannelTypeEnum {
    DAIHUAN(1, "代还/小额免密"), SHOUKUAN(2, "收款/快捷支付");

    int key;
    String code;
    PaymentChannelTypeEnum (int key, String msg) {
        this.key = key;
        this.code = msg;
    }

    public static PaymentChannelTypeEnum get(int key){
        for(PaymentChannelTypeEnum type : PaymentChannelTypeEnum.values()){
            if (type.getKey() == key){
                return type;
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

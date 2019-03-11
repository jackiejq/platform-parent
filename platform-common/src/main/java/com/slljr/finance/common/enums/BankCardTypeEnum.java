package com.slljr.finance.common.enums;

public enum BankCardTypeEnum {
    CREADIT_CARD(1, "信用卡"), DEPOSIT_CARD(2, "储蓄卡");
    public int key;
    public String value;

    BankCardTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static BankCardTypeEnum getType(int key) {
        for (BankCardTypeEnum typeEnum : BankCardTypeEnum.values()) {
            if (typeEnum.key == key) {
                return typeEnum;
            }
        }
        return BankCardTypeEnum.CREADIT_CARD;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package com.slljr.finance.common.enums;

public enum PaymentResultCodeEnum {
    SUCCESS(1001, "业务办理成功"), FAIL(1002, "业务办理失败"),

    PAY_SUCCESS(1101, "支付成功"), PAY_FAIL(1102, "支付失败"), PAY_ING(1103, "支付中"), PAY_WAIT(1104, "待支付"),

    WITHDRAW_SUCCESS(1201, "提现成功"), WITHDRAW_FAIL(1202, "提现失败"), WITHDRAW_AMOUNT_LOW(1203, "余额不足"),WITHDRAW_ING(1204, "提现中"),

    BIND_CARD_SUCCESS(1301, "绑卡成功"), BIND_CARD_FAIL(1302, "绑卡失败"), BIND_CARD_EXIST(1303, "该卡已存在"), BIND_CARD_FAIL_BANK_INFO_ERROR(1304, "绑卡失败,银行卡信息错误"),

    SMS_AUTO_SEND(1401, "需要验证码,自动发送"), SMS_MANUAL_SEND(1402, "需要验证码,手动发送"),

    SMS_SEND_SUCCESS(1501, "短信发送成功"), SMS_SEND_FAIL(1502, "短信发送失败"),

    SMS_VERIFY_SUCCESS(1601, "验证码正确"), SMS_VERIFY_FAIL(1602, "验证码错误"), SMS_VERIFY_EXPIRE(1603, "验证码过期"),

    UPDATE_BIND_CARD_SUCCESS(1701, "修改用户绑定卡片成功"), UPDATE_BIND_CARD_FAIL(1702, "修改用户绑定卡片失败"),


    UN_BIND_CARD_FAIL(1901, "解绑卡片失败"),
    ORDER_EXISTS(1902, "支付订单已存在"),
    PHONE_EXISTS(1903, "用户手机号码已做过绑定"),
    QUERY_SUCESS(1904, "支付订单查询成功"),
    QUERY_FAIL(1905, "支付订单查询失败"),
    REGISTER_SUCCESS(1906, "用户注册成功"),REGISTER_FAIL(1907, "用户注册失败"),
    GET_ORDER_STATU_FAIL(1908, "获取订单状态失败"),

    PROCESSING(1909, "处理中半个小时后再次发起对账"),

    ORDER_NOT_EXIST(1910, "订单号不存在"),

    OPEN_CARD_SUCCESS(1911, "绑定提现卡成功"),OPEN_CARD_FAIL(1912, "绑定提现卡失败"),

    IO_EXCEPTION(-1901, "网络异常"),
    RESULT_FORMAT_CHANGE_EXCEPTION(-1902, "结果格式变化异常"),
    UNKNOW_EXCEPTION(-1903, "未知异常"),;


    int code;
    String msg;

    PaymentResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

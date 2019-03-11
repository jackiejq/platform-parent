package com.slljr.finance.common.pojo.dto;

import com.slljr.finance.common.pojo.model.UserBankCard;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2018/12/24.
 * @time: 14:48.
 */
public class UserBankCardDTO extends UserBankCard {

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 短信验证码（修改预留手机号码）
     */
    private String smsCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}

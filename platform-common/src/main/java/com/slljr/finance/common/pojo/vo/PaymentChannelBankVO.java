package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.PaymentChannelBank;

public class PaymentChannelBankVO extends PaymentChannelBank {
    private String channelName;
    private String channelCode;
    private String bankName;
    private String bankCode;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}

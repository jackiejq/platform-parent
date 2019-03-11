package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.UserBankCard;

public class UserBankCardVo extends UserBankCard {
    private String bankCode;
    private String bankName;

    /**
     * 银行图标(小)
     */
    private String smallLogoUrl;
    private String bigLogoUrl;

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

    public String getSmallLogoUrl() {
        return smallLogoUrl;
    }

    public void setSmallLogoUrl(String smallLogoUrl) {
        this.smallLogoUrl = smallLogoUrl;
    }

    public String getBigLogoUrl() {
        return bigLogoUrl;
    }

    public void setBigLogoUrl(String bigLogoUrl) {
        this.bigLogoUrl = bigLogoUrl;
    }
}

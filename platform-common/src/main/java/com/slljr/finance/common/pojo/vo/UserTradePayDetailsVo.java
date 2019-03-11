package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;

public class UserTradePayDetailsVo extends UserTradePaymentRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5765710681855599042L;
    private String bankCode;
	private String bankName;
	private String bankCardNo;

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

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
}

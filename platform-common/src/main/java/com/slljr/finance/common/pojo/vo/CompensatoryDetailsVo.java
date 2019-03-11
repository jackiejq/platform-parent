package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;

public class CompensatoryDetailsVo extends UserTradePaymentRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7251762111299736267L;
	
	private Double totalAmount;
	
	private Double totalServicecharge;

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalServicecharge() {
		return totalServicecharge;
	}

	public void setTotalServicecharge(Double totalServicecharge) {
		this.totalServicecharge = totalServicecharge;
	}
	
	

}

package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

public class UserBankCardDetailVo extends UserBankCardVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8989871580000969701L;
	
	// 还款中笔数
	private Integer numberPayments;
	// 总代偿金额
	private Double totalCompensationAmount;
	// 已还总金额
	private Double totalAmountRepaid;
	// 失败笔数
	private int failCount;
	
	public Integer getNumberPayments() {
		return numberPayments;
	}
	public void setNumberPayments(Integer numberPayments) {
		this.numberPayments = numberPayments;
	}
	public Double getTotalCompensationAmount() {
		return totalCompensationAmount;
	}
	public void setTotalCompensationAmount(Double totalCompensationAmount) {
		this.totalCompensationAmount = totalCompensationAmount;
	}
	public Double getTotalAmountRepaid() {
		return totalAmountRepaid;
	}
	public void setTotalAmountRepaid(Double totalAmountRepaid) {
		this.totalAmountRepaid = totalAmountRepaid;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
}

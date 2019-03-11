package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

public class BillStaging implements Serializable {

	/**
	 * 账单分期记录表
	 */
	private static final long serialVersionUID = 8385644976883485285L;

	//用户id
	@ApiModelProperty(value = "用户id", required = false)
	private Integer uid;	
	//订单编号
	@ApiModelProperty(value = "订单编号", required = false)
	private String orderId;
	//订单状态
	@ApiModelProperty(value = "订单状态", required = false)
	private Integer orderState;
	//账单总额
	@ApiModelProperty(value = "账单总额", required = false)
	private Double numtotal;
	//已还金额
	@ApiModelProperty(value = "已还金额", required = false)
	private Double amountPay;
	//卡内余额
	@ApiModelProperty(value = "卡内余额", required = false)
	private Double balance;
	//还款天数
	@ApiModelProperty(value = "还款天数", required = false)
	private Integer repaymentDays;
	//手续费
	@ApiModelProperty(value = "手续费", required = false)
	private Double poundage;
	//支付通道id
	@ApiModelProperty(value = "支付通道id", required = false)
	private String payPassId;
	//支付信用卡id
	@ApiModelProperty(value = "支付信用卡id", required = false)
	private String payCreditCard;
	//收款信用卡id
	@ApiModelProperty(value = "收款信用卡id", required = false)
	private String receiveCreditCard;	
	//创建时间
	@ApiParam(hidden = true)
    private Date createTime;
    //修改时间
    @ApiParam(hidden = true)
    private Date updateTime;
	    
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getOrderState() {
		return orderState;
	}
	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}
	public Double getNumtotal() {
		return numtotal;
	}
	public void setNumtotal(Double numtotal) {
		this.numtotal = numtotal;
	}

	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public int getRepaymentDays() {
		return repaymentDays;
	}
	public void setRepaymentDays(int repaymentDays) {
		this.repaymentDays = repaymentDays;
	}
	public Double getPoundage() {
		return poundage;
	}
	public void setPoundage(Double poundage) {
		this.poundage = poundage;
	}
	public String getPayPassId() {
		return payPassId;
	}
	public void setPayPassId(String payPassId) {
		this.payPassId = payPassId;
	}
	public String getPayCreditCard() {
		return payCreditCard;
	}
	public void setPayCreditCard(String payCreditCard) {
		this.payCreditCard = payCreditCard;
	}
	public String getReceiveCreditCard() {
		return receiveCreditCard;
	}
	public void setReceiveCreditCard(String receiveCreditCard) {
		this.receiveCreditCard = receiveCreditCard;
	}
	public Double getAmountPay() {
		return amountPay;
	}
	public void setAmountPay(Double amountPay) {
		this.amountPay = amountPay;
	}
	@Override
	public String toString() {
		return "BillStaging [uid=" + uid + ", orderId=" + orderId + ", orderState=" + orderState + ", numtotal="
				+ numtotal + ", amountPay=" + amountPay + ", balance=" + balance + ", repaymentDays=" + repaymentDays
				+ ", poundage=" + poundage + ", payPassId=" + payPassId + ", payCreditCard=" + payCreditCard
				+ ", receiveCreditCard=" + receiveCreditCard + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}

	
}

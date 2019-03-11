package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.model.UserBasic;

import io.swagger.annotations.ApiModelProperty;

public class UserReportTotalVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6176645477517808955L;
	//交易金额
	private Double paymentAmount;
	//交易佣金
	private Double agencyProfit;
	//有交易记录人数
	private Integer number;
	
	//交易日期
	private String date;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public Double getAgencyProfit() {
		return agencyProfit;
	}
	public void setAgencyProfit(Double agencyProfit) {
		this.agencyProfit = agencyProfit;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	
}

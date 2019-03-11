package com.slljr.finance.common.pojo.vo;

import java.util.Date;

import com.slljr.finance.common.pojo.model.UserTradeOrder;

public class UserTradeDetailsVo extends UserTradeOrder {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1380115703085061916L;
  
    private Integer payType;
    
    private Double amount;

    private Integer cardId;

    private Double serviceCharge;

    private Integer channelId;

    private Date paymentTime;

    private Integer payStatus;



	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public Double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(Double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}



   
   
}

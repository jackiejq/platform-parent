package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.UserTradeOrder;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2018/12/26.
 * @time: 14:19.
 */
public class UserTradeOrderVO extends UserTradeOrder {

    /**
     * 收款卡号
     */
    private String receiveCardNo;
    private String receiveBankName;
    /**
     * 付款卡号
     */
    private String paymentCardNo;
    private String paymentBankName;

    private String  paymentChannelName;


   //代偿总笔数
    private Integer compensatoryTotal;
    
    //代偿总金额   
    private Double amountTotal;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 隐藏的卡号信息（银行名称+支付卡后四位）
     */
    private String hidePayCardNo;

    /**
     * 隐藏的卡号信息（银行名称+收款卡后四位）
     */
    private String hideReceiveCardNo;


    public Integer getCompensatoryTotal() {
		return compensatoryTotal;
	}

	public void setCompensatoryTotal(Integer compensatoryTotal) {
		this.compensatoryTotal = compensatoryTotal;
	}

	public Double getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(Double amountTotal) {
		this.amountTotal = amountTotal;
	}

	public String getReceiveCardNo() {
        return receiveCardNo;
    }

    public void setReceiveCardNo(String receiveCardNo) {
        this.receiveCardNo = receiveCardNo;
    }

    public String getPaymentCardNo() {
        return paymentCardNo;
    }

    public void setPaymentCardNo(String paymentCardNo) {
        this.paymentCardNo = paymentCardNo;
    }

    public String getReceiveBankName() {
        return receiveBankName;
    }

    public void setReceiveBankName(String receiveBankName) {
        this.receiveBankName = receiveBankName;
    }

    public String getPaymentBankName() {
        return paymentBankName;
    }

    public void setPaymentBankName(String paymentBankName) {
        this.paymentBankName = paymentBankName;
    }

    public String getPaymentChannelName() {
        return paymentChannelName;
    }

    public void setPaymentChannelName(String paymentChannelName) {
        this.paymentChannelName = paymentChannelName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHidePayCardNo() {
        return hidePayCardNo;
    }

    public void setHidePayCardNo(String hidePayCardNo) {
        this.hidePayCardNo = hidePayCardNo;
    }

    public String getHideReceiveCardNo() {
        return hideReceiveCardNo;
    }

    public void setHideReceiveCardNo(String hideReceiveCardNo) {
        this.hideReceiveCardNo = hideReceiveCardNo;
    }
}

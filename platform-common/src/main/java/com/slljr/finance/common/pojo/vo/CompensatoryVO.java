package com.slljr.finance.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CompensatoryVO {


    /**
     * 总额
     */
    private Double amount;

    /**
     * 剩余代偿
     */
    private Double surplusAmount;

    /**
     * 手续费
     */
    private Double serviceCharge;

    /**
     * 代偿期数
     */
    private Integer number;

    /**
     * 是否显示终止代偿按钮
     */
    private Integer isDisplayStopOrder;

    /**
     * 最后还款日
     */
    @JsonFormat(pattern="MM/dd",timezone="GMT+8")
    private Date lastRepaymentDate;

    /**
     * 卡内余额
     */
    private Double cardBalance;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(Double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getIsDisplayStopOrder() {
        return isDisplayStopOrder;
    }

    public void setIsDisplayStopOrder(Integer isDisplayStopOrder) {
        this.isDisplayStopOrder = isDisplayStopOrder;
    }

    public Date getLastRepaymentDate() {
        return lastRepaymentDate;
    }

    public void setLastRepaymentDate(Date lastRepaymentDate) {
        this.lastRepaymentDate = lastRepaymentDate;
    }

    public Double getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(Double cardBalance) {
        this.cardBalance = cardBalance;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
}

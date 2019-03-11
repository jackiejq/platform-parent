package com.slljr.finance.front.utils;

import com.slljr.finance.common.pojo.model.PaymentChannel;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;

public class BillParams {
    public BillParams(UserTradeOrder order, PaymentChannel channel){
        this.order = order;
        this.channel = channel;
    }

    /**
     * 账单金额
     */
    private Double billAmount;

    /**
     * 卡内余额
     */
    private Double cardBalance;

    /**
     * 可以支付的日期集合
     */
    private List<Date> canPayDates;

    /**
     * 卡内最低余额
     */
    private Double cardBalanceMin;

    /**
     * 单笔最低金额
     */
    private Double singleAmountMin;

    /**
     * 单笔最大金额
     */
    private Double singleAmountMax;

    /**
     * 单期最大金额
     */
    private Double periodAmountMax;

    /**
     * 单日最大金额
     */
    private Double dayAmountMax;

    /**
     * 代偿订单
     */
    private UserTradeOrder order;

    /**
     * 代偿通道
     */
    private PaymentChannel channel;


    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public Double getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(Double cardBalance) {
        this.cardBalance = cardBalance;
    }

    public List<Date> getCanPayDates() {
        return canPayDates;
    }

    public void setCanPayDates(List<Date> canPayDates) {
        this.canPayDates = canPayDates;
    }

    public Double getCardBalanceMin() {
        return cardBalanceMin;
    }

    public void setCardBalanceMin(Double cardBalanceMin) {
        this.cardBalanceMin = cardBalanceMin;
    }

    public Double getSingleAmountMin() {
        return singleAmountMin;
    }

    public void setSingleAmountMin(Double singleAmountMin) {
        this.singleAmountMin = singleAmountMin;
    }

    public Double getSingleAmountMax() {
        return singleAmountMax;
    }

    public void setSingleAmountMax(Double singleAmountMax) {
        this.singleAmountMax = singleAmountMax;
    }

    public Double getPeriodAmountMax() {
        return periodAmountMax;
    }

    public void setPeriodAmountMax(Double periodAmountMax) {
        this.periodAmountMax = periodAmountMax;
    }

    public Double getDayAmountMax() {
        return dayAmountMax;
    }

    public void setDayAmountMax(Double dayAmountMax) {
        this.dayAmountMax = dayAmountMax;
    }

    public UserTradeOrder getOrder() {
        return order;
    }

    public void setOrder(UserTradeOrder order) {
        this.order = order;
    }

    public PaymentChannel getChannel() {
        return channel;
    }

    public void setChannel(PaymentChannel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

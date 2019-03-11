package com.slljr.finance.common.pojo.model;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

/**
 * 用户银行账户支付收入详情mode
 * @author LXK
 *
 */
public class UserQuickConsume {
	
	@ApiParam(hidden = true)
    private Integer id;
	
	@ApiModelProperty(value = "用户id", required = false)
    private Integer uid;
	
    @ApiModelProperty(value = "订单id", required = false)
    private String orderId;
    
    @ApiModelProperty(value = "支付金额", required = false)
    private Double payAmount;
    
    @ApiModelProperty(value = "到账金额", required = false)
    private Double getAmount;
    
    @ApiModelProperty(value = "服务费", required = false)
    private Double serviceCharge;
    
    @ApiModelProperty(value = "支付通道id", required = false)
    private Integer channelId;
    
    @ApiModelProperty(value = "支付信用卡id", required = false)
    private Integer creditCardId;
    
    @ApiModelProperty(value = "收款银行卡id", required = false)
    private Integer bankCardId;
    
    @ApiModelProperty(value = "状态[-1删除,0正常]", required = false)
    private Integer status;
    
    @ApiParam(hidden = true)
    private Date createTime;
    
    @ApiParam(hidden = true)
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Double getGetAmount() {
        return getAmount;
    }

    public void setGetAmount(Double getAmount) {
        this.getAmount = getAmount;
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

    public Integer getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(Integer creditCardId) {
        this.creditCardId = creditCardId;
    }

    public Integer getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(Integer bankCardId) {
        this.bankCardId = bankCardId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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
}
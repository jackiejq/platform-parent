package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

public class UserTradePaymentRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3960066368735630464L;

	@ApiParam(hidden = true)
	private Integer id;

	@ApiModelProperty(value = "用户id", required = false)
    private Integer uid;
	
	@ApiModelProperty(value = "订单id", required = false)
    private String orderId;
	
    @ApiModelProperty(value = "交易id'", required = false)
    private Integer tradeId;
    
    @ApiModelProperty(value = "类型[1扣款/支付,2打款/提现]", required = false)
    private Integer type;
    
    @ApiModelProperty(value = "交易金额", required = false)
    private Double amount;
    
    @ApiModelProperty(value = "卡id", required = false)
    private Integer cardId;
    
    @ApiModelProperty(value = "服务费", required = false)
    private Double serviceCharge;
    
    @ApiModelProperty(value = "通道id", required = false)
    private Integer channelId;
    
    @ApiModelProperty(value = "支付时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @ApiModelProperty(value = "其他参数,JSON格式", required = false)
    private String otherParams;

    @ApiModelProperty(value = "错误信息", required = false)
    private String errorMsg;

    @ApiModelProperty(value = "状态[-2取消,-1支付失败,0待支付,1支付待确认,2已支付]", required = false)
    private Integer status;
    
    @ApiParam(hidden = true) 
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    
    @ApiParam(hidden = true) 
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**
     * 还款重试次数
     */
    private Integer retryCount;
      
    
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

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

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(String otherParams) {
        this.otherParams = otherParams;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public String toString() {
        return "UserTradePaymentRecord{" +
                "id=" + id +
                ", uid=" + uid +
                ", orderId='" + orderId + '\'' +
                ", tradeId=" + tradeId +
                ", type=" + type +
                ", amount=" + amount +
                ", cardId=" + cardId +
                ", serviceCharge=" + serviceCharge +
                ", channelId=" + channelId +
                ", paymentTime=" + paymentTime +
                ", otherParams='" + otherParams + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", retryCount=" + retryCount +
                '}';
    }
}
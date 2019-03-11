package com.slljr.finance.common.pojo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.RepayErrorRecord")
public class RepayErrorRecord extends BasePageVO implements Serializable {
    /**
	* 
	*/
    @ApiModelProperty(value="")
    private Integer id;

    /**
	* 用户id
	*/
    @ApiModelProperty(value="用户id")
    private Integer uid;

    /**
	* 交易id
	*/
    @ApiModelProperty(value="交易id")
    private Integer tradeId;

    /**
	* 订单id,每条记录一个订单id,唯一
	*/
    @ApiModelProperty(value="订单id,每条记录一个订单id,唯一")
    private String orderId;

    /**
	* 类型[1扣款,2打款]
	*/
    @ApiModelProperty(value="类型[1扣款,2打款]")
    private Integer type;

    /**
	* 交易金额
	*/
    @ApiModelProperty(value="交易金额")
    private Double amount;

    /**
	* 卡id
	*/
    @ApiModelProperty(value="卡id")
    private Integer cardId;

    /**
	* 服务费
	*/
    @ApiModelProperty(value="服务费")
    private Double serviceCharge;

    /**
	* 通道id
	*/
    @ApiModelProperty(value="通道id")
    private Integer channelId;

    /**
	* 支付时间
	*/
    @ApiModelProperty(value="支付时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    /**
	* 其他参数,json格式存储
	*/
    @ApiModelProperty(value="其他参数,json格式存储")
    private String otherParams;

    /**
	* 错误信息
	*/
    @ApiModelProperty(value="错误信息")
    private String errorMsg;

    /**
	* 状态[-3已手动处理，-2取消,-1失败,0待支付, 1支付待确认,2已支付]
	*/
    @ApiModelProperty(value="状态[-3已手动处理，-2取消,-1失败,0待支付, 1支付待确认,2已支付]")
    private Integer status;

    /**
	* 重试次数
	*/
    @ApiModelProperty(value="重试次数")
    private Integer retryCount;

    /**
	* 引用支付记录ID
	*/
    @ApiModelProperty(value="引用支付记录ID")
    private Integer refRecordId;

    /**
	* 创建时间
	*/
    @ApiModelProperty(value="创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注异常处理信息
     */
    @ApiModelProperty(value="备注异常处理信息")
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getRefRecordId() {
        return refRecordId;
    }

    public void setRefRecordId(Integer refRecordId) {
        this.refRecordId = refRecordId;
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
package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.vo.BasePageVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 交易订单实体类
 * @author LXK
 *
 */
public class UserTradeOrder extends BasePageVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4124649640315914393L;

	@ApiParam(hidden = true) 
	private Integer id;
	
	@ApiModelProperty(value = "用户id", required = false)
    private Integer uid;

	@ApiModelProperty(value = "交易类型[1代偿,2收款]", required = false)
    private Integer type;
    
	@ApiModelProperty(value = "支付金额", required = false)
    private Double paymentAmount;
    
	@ApiModelProperty(value = "到账金额", required = false)
    private Double receiveAmount;
    
	@ApiModelProperty(value = "手续费", required = false)
    private Double serviceCharge;
    
	@ApiModelProperty(value = "通道id", required = false)
    private Integer channelId;
    
	@ApiModelProperty(value = "支付卡ID", required = false)
    private Integer paymentCardId;
    
	@ApiModelProperty(value = "收款卡ID", required = false)
    private Integer receiveCardId;
    
	@ApiModelProperty(value = "交易摘要", required = false)
    private String summary;
    
	@ApiModelProperty(value = "信用卡账单金额", required = false)
    private Double billAmount;
    
	@ApiModelProperty(value = "信用卡卡内余额", required = false)
    private Double cardBalance;
    
	@ApiModelProperty(value = "信用卡最迟支付日期", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cardLastPaymentDate;
    
	@ApiModelProperty(value = "分期交易执行日期集合", required = false)
    private String paymentDays;
    
	@ApiModelProperty(value = "状态[-3取消, -2全部失败, -1部分失败, 0订单待确认, 1进行中, 2成功]", required = false)
    private Integer status;
    
    @ApiParam(hidden = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @ApiParam(hidden = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "省份", required = false)
    private String province;
    @ApiModelProperty(value = "城市", required = false)
    private String city;

    @ApiModelProperty(value = "公司利润", required = false)
    private Double companyProfit;
    @ApiModelProperty(value = "代理利润", required = false)
    private Double agencyProfit;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(Double receiveAmount) {
        this.receiveAmount = receiveAmount;
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

    public Integer getPaymentCardId() {
        return paymentCardId;
    }

    public void setPaymentCardId(Integer paymentCardId) {
        this.paymentCardId = paymentCardId;
    }

    public Integer getReceiveCardId() {
        return receiveCardId;
    }

    public void setReceiveCardId(Integer receiveCardId) {
        this.receiveCardId = receiveCardId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

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

    public Date getCardLastPaymentDate() {
        return cardLastPaymentDate;
    }

    public void setCardLastPaymentDate(Date cardLastPaymentDate) {
        this.cardLastPaymentDate = cardLastPaymentDate;
    }

    public String getPaymentDays() {
        return paymentDays;
    }

    public void setPaymentDays(String paymentDays) {
        this.paymentDays = paymentDays;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getCompanyProfit() {
        return companyProfit;
    }

    public void setCompanyProfit(Double companyProfit) {
        this.companyProfit = companyProfit;
    }

    public Double getAgencyProfit() {
        return agencyProfit;
    }

    public void setAgencyProfit(Double agencyProfit) {
        this.agencyProfit = agencyProfit;
    }
}
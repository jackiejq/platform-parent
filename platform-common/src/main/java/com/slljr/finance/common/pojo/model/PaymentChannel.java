package com.slljr.finance.common.pojo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

public class PaymentChannel {

	@ApiModelProperty(value = "用户id", required = false)
    private Integer id;

	@ApiModelProperty(value = "通道名称", required = false)
    private String name;

	@ApiModelProperty(value = "通道代码", required = false)
    private String code;

	@ApiModelProperty(value = "平台刷卡费率/原价(百分比,eg: 0.006=0.6%", required = false)
    private Double paymentRate;

	@ApiModelProperty(value = "用户刷卡费率/售价(百分比, eg: 0.006=0.6%", required = false)
    private Double userPaymentRate;

	@ApiModelProperty(value = "提现每笔手续费/原价", required = false)
    private Double withdrawCharge;

	@ApiModelProperty(value = "提现每笔手续费/售价", required = false)
    private Double userWithdrawCharge;

	@ApiModelProperty(value = "商户号", required = false)
    private String merchantNo;

	@ApiModelProperty(value = "加密key", required = false)
    private String encryptKey;

	@ApiModelProperty(value = "接口主机地址", required = false)
    private String httpUrl;
	
	@ApiModelProperty(value = "回调接口地址", required = false)
	private String notifyUrl;
	
	@ApiModelProperty(value = "其他非公共参数,存json格式", required = false)
    private String otherParams;

	@ApiModelProperty(value = "营业开始时间(例如09:30),长度为5)", required = false)
    private String runTimeStart;

	@ApiModelProperty(value = "营业结束时间(例如21:30),长度为5", required = false)
    private String runTimeEnd;

	@ApiModelProperty(value = "类型[1代还/小额免密, 2收款/快捷支付]", required = false)
    private Integer type;

	@ApiModelProperty(value = "状态[-1维护中, 0停用, 1启用]", required = false)
    private Integer status;

	@ApiParam(hidden = true) 
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

	@ApiParam(hidden = true) 
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Double getPaymentRate() {
        return paymentRate;
    }

    public void setPaymentRate(Double paymentRate) {
        this.paymentRate = paymentRate;
    }

    public Double getUserPaymentRate() {
        return userPaymentRate;
    }

    public void setUserPaymentRate(Double userPaymentRate) {
        this.userPaymentRate = userPaymentRate;
    }

    public Double getWithdrawCharge() {
        return withdrawCharge;
    }

    public void setWithdrawCharge(Double withdrawCharge) {
        this.withdrawCharge = withdrawCharge;
    }

    public Double getUserWithdrawCharge() {
        return userWithdrawCharge;
    }

    public void setUserWithdrawCharge(Double userWithdrawCharge) {
        this.userWithdrawCharge = userWithdrawCharge;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey == null ? null : encryptKey.trim();
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl == null ? null : httpUrl.trim();
    }

    public String getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(String otherParams) {
        this.otherParams = otherParams == null ? null : otherParams.trim();
    }

  
    public String getRunTimeStart() {
		return runTimeStart;
	}

	public void setRunTimeStart(String runTimeStart) {
		this.runTimeStart = runTimeStart;
	}

	public String getRunTimeEnd() {
		return runTimeEnd;
	}

	public void setRunTimeEnd(String runTimeEnd) {
		this.runTimeEnd = runTimeEnd;
	}

	public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;

public class UserBankCard implements Serializable {

    /* */
    @ApiModelProperty(value = "id", required = false)    
    private Integer id;

    /* 用户id*/
    @ApiModelProperty(value = "用户id", required = false)
    private Integer uid;

    /* 银行id*/
    @ApiModelProperty(value = "银行id", required = false)
    private Integer bankId;

    /* 银行id*/
    @ApiModelProperty(value = "银行卡照片", required = false)
    private String imgUrl;
    
    /* 银行卡号*/
    @ApiModelProperty(value = "银行卡号", required = false)
    private String bankCardNo;

    
    /* 卡类型[1信用卡,2银行卡]*/
    @ApiModelProperty(value = "卡类型[1信用卡,2银行卡]", required = false)
    private Integer bankCardType;

    /* 预留手机号码*/
    @ApiModelProperty(value = "预留手机号码", required = false)
    private String phone;

    /* 开户行*/
    @ApiModelProperty(value = "开户行", required = false)
    private String openingBank;

    /* 开户省份*/
    @ApiModelProperty(value = "开户省份", required = false)
    private String openingBankProvince;

    /* 开户城市*/
    @ApiModelProperty(value = " 开户城市", required = false)
    private String openingBankCity;

    /* 信用卡有效期MMyy*/
    @DateTimeFormat(pattern="MMyy")
    @JsonFormat(pattern="MMyy",timezone="GMT+8")
    @ApiModelProperty(value = "信用卡有效期MMyy", required = false)
    private Date expDate;

    /* 信用卡安全码*/
    @ApiModelProperty(value = "信用卡安全码", required = false)
    private String cvvCode;

    /* 账单日MMyy*/
    @ApiModelProperty(value = "账单日dd", required = false)
    private String billDate;

    /* 最后还款日MMyy*/
    @ApiModelProperty(value = "最后还款日", required = false)
    private String lastRepaymentDate;

    /* 信用卡额度*/
    @ApiModelProperty(value = "信用卡额度", required = false)
    private Integer creditLimit;

    /* 状态[-1删除,0正常]*/
    @ApiModelProperty(value = "状态[-1删除,0正常]", required = false)
    private Integer status;

    /* */
    @ApiParam(hidden = true) 
    private Date createTime;

    /* */
    @ApiParam(hidden = true) 
    private Date updateTime;

    @ApiModelProperty(value = "银行卡加密存储", required = false)
    private String bankCardNoSign;
    
    
    private static final long serialVersionUID = 1L;
   
    public String getBankCardNoSign() {
		return bankCardNoSign;
	}

	public void setBankCardNoSign(String bankCardNoSign) {
		this.bankCardNoSign = bankCardNoSign;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public Integer getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(Integer bankCardType) {
        this.bankCardType = bankCardType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getOpeningBankProvince() {
        return openingBankProvince;
    }

    public void setOpeningBankProvince(String openingBankProvince) {
        this.openingBankProvince = openingBankProvince;
    }

    public String getOpeningBankCity() {
        return openingBankCity;
    }

    public void setOpeningBankCity(String openingBankCity) {
        this.openingBankCity = openingBankCity;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getLastRepaymentDate() {
        return lastRepaymentDate;
    }

    public void setLastRepaymentDate(String lastRepaymentDate) {
        this.lastRepaymentDate = lastRepaymentDate;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
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
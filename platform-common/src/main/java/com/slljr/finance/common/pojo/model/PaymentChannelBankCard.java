package com.slljr.finance.common.pojo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.PaymentChannelBankCard")
public class PaymentChannelBankCard implements Serializable {
    public PaymentChannelBankCard(){}
    public PaymentChannelBankCard(Integer uid, Integer bankCardId, String channelCode, String protocolCode) {
        this.uid = uid;
        this.bankCardId = bankCardId;
        this.channelCode = channelCode;
        this.protocolCode = protocolCode;
    }

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
	* 银行卡id
	*/
    @ApiModelProperty(value="银行卡id")
    private Integer bankCardId;

    /**
	* 通道代码
	*/
    @ApiModelProperty(value="通道代码")
    private String channelCode;

    /**
	* 绑卡协议代码(目前通联使用agreeid)
	*/
    @ApiModelProperty(value="绑卡协议代码(目前通联使用agreeid)")
    private String protocolCode;

    /**
	* 其他非公共参数,存json格式
	*/
    @ApiModelProperty(value="其他非公共参数,存json格式")
    private String otherParams;

    /**
	* 状态[-1禁用,0启用]
	*/
    @ApiModelProperty(value="状态[-1禁用,0启用]")
    private Integer status;

    /**
	* 
	*/
    @ApiModelProperty(value="")
    private Date createTime;

    /**
	* 
	*/
    @ApiModelProperty(value="")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

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

    public Integer getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(Integer bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getProtocolCode() {
        return protocolCode;
    }

    public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
    }

    public String getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(String otherParams) {
        this.otherParams = otherParams;
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
package com.slljr.finance.common.pojo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.PaymentChannelUser")
public class PaymentChannelUser implements Serializable {
    public PaymentChannelUser(){}
    public PaymentChannelUser(Integer uid, String channelCode, String channelUid, Integer bankCardId) {
        this.uid = uid;
        this.channelCode = channelCode;
        this.channelUid = channelUid;
        this.bankCardId = bankCardId;
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
	* 通道代码
	*/
    @ApiModelProperty(value="通道代码")
    private String channelCode;

    /**
	* 通道用户id(佳付通&通联一个用户一个id,腾付通一张卡一个id)
	*/
    @ApiModelProperty(value="通道用户id(佳付通&通联一个用户一个id,腾付通一张卡一个id)")
    private String channelUid;

    /**
	* 银行卡id
	*/
    @ApiModelProperty(value="银行卡id")
    private Integer bankCardId;

    /**
	* 状态[-1禁用, 0正常]
	*/
    @ApiModelProperty(value="状态[-1禁用, 0正常]")
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

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelUid() {
        return channelUid;
    }

    public void setChannelUid(String channelUid) {
        this.channelUid = channelUid;
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
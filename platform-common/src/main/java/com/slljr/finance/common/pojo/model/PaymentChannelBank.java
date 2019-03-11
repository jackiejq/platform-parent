package com.slljr.finance.common.pojo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.PaymentChannelBank")
public class PaymentChannelBank implements Serializable {
    /**
	* 
	*/
    @ApiModelProperty(value="")
    private Integer id;

    /**
	* 通道id
	*/
    @ApiModelProperty(value="通道id")
    private Integer channelId;

    /**
	* 通道类型[1代偿 2收款]
	*/
    @ApiModelProperty(value="通道类型[1代偿 2收款]")
    private Integer channelType;

    /**
	* 银行id
	*/
    @ApiModelProperty(value="银行id")
    private Integer bankId;

    /**
	* 单笔限额
	*/
    @ApiModelProperty(value="单笔限额")
    private Integer singleLimit;

    /**
	* 单日限额
	*/
    @ApiModelProperty(value="单日限额")
    private Integer singleDayLimit;

    /**
	* 状态[-1禁用, 0启用]
	*/
    @ApiModelProperty(value="状态[-1禁用, 0启用]")
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

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(Integer singleLimit) {
        this.singleLimit = singleLimit;
    }

    public Integer getSingleDayLimit() {
        return singleDayLimit;
    }

    public void setSingleDayLimit(Integer singleDayLimit) {
        this.singleDayLimit = singleDayLimit;
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
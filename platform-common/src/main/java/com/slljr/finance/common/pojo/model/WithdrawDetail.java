package com.slljr.finance.common.pojo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.WithdrawDetail")
public class WithdrawDetail extends BasePageVO {
    /**
	* 主键
	*/
    @ApiModelProperty(value="主键")
    private Integer id;

    /**
	* 提现金额
	*/
    @ApiModelProperty(value="提现金额")
    private Double amount;

    /**
	* 储蓄卡ID
	*/
    @ApiModelProperty(value="储蓄卡ID")
    private Integer cardId;

    /**
	* 提现用户
	*/
    @ApiModelProperty(value="提现用户")
    private Integer uid;

    /**
	* 提现状态（-1：审核不通过 0：待审核 1：审核中 2：审核通过）
	*/
    @ApiModelProperty(value="提现状态（-1：审核不通过 0：待审核 1：审核中 2：审核通过）")
    private Integer status;

    /**
	* 审核人
	*/
    @ApiModelProperty(value="审核人")
    private Integer auditUid;

    /**
	* 审核备注
	*/
    @ApiModelProperty(value="审核备注")
    private String remark;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAuditUid() {
        return auditUid;
    }

    public void setAuditUid(Integer auditUid) {
        this.auditUid = auditUid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
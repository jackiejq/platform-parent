package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

public class UserAccount implements Serializable {
    /* */
    private Integer id;

    /* 用户id*/
    private Integer uid;

    /* 现金余额*/
    private Double cashBalance;

    /* 积分余额*/
    private Double integralBalance;

    /* 代理等级id*/
    private Integer agentLevelId;

    /* 状态[-1删除,0正常]*/
    private Integer status;

    /* */
    private Date createTime;

    /* */
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

    public Double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(Double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public Double getIntegralBalance() {
        return integralBalance;
    }

    public void setIntegralBalance(Double integralBalance) {
        this.integralBalance = integralBalance;
    }

    public Integer getAgentLevelId() {
        return agentLevelId;
    }

    public void setAgentLevelId(Integer agentLevelId) {
        this.agentLevelId = agentLevelId;
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
package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

public class AgentLevel implements Serializable {
    public enum AgentLevelStatusEnum{
        NORMAL(0,"启用"), DISABLE(-1,"禁用");

        int key;
        String msg;
        AgentLevelStatusEnum(int key, String msg){
            this.key = key;
            this.msg = msg;
        }

        public int getKey() {
            return key;
        }
        public void setKey(int key) {
            this.key = key;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    /* */
    private Integer id;

    /**
	* 等级编码（APP显示用）
	*/
    private String levelCode;

    /**
	* 等级名称
	*/
    private String name;

    /**
	* 最低新增有效人数
	*/
    private Integer minNewMember;

    /**
	* 最高新增有效人数
	*/
    private Integer maxNewMember;

    /**
	* 佣金比例, 0.2=20%
	*/
    private Double commissionRate;

    /**
	* 等级类型[1每月新增有效会员, 2累计有效会员]
	*/
    private Integer type;

    /**
	* 状态[-1禁用, 0启用]
	*/
    private Integer status;

    /**
	* 
	*/
    private Date createTime;

    /**
	* 
	*/
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinNewMember() {
        return minNewMember;
    }

    public void setMinNewMember(Integer minNewMember) {
        this.minNewMember = minNewMember;
    }

    public Integer getMaxNewMember() {
        return maxNewMember;
    }

    public void setMaxNewMember(Integer maxNewMember) {
        this.maxNewMember = maxNewMember;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
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
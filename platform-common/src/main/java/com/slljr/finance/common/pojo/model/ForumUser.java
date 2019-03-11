package com.slljr.finance.common.pojo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.ForumUser")
public class ForumUser {
    /**
	* 用户id
	*/
    @ApiModelProperty(value="用户id")
    private Integer uid;

    /**
	* 用户等级
	*/
    @ApiModelProperty(value="用户等级")
    private Integer grade;

    /**
	* 积分点数
	*/
    @ApiModelProperty(value="积分点数")
    private Integer integral;

    /**
	* 用户类型[1是管理员,2,是用户,3是游客]
	*/
    @ApiModelProperty(value="用户类型[1是管理员,2,是用户,3是游客]")
    private Integer type;

    /**
	* 状态[-1禁言, 0正常]
	*/
    @ApiModelProperty(value="状态[-1禁言, 0正常]")
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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
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
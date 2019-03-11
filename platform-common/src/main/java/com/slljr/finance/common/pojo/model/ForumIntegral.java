package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 用户论坛积分实体
 * @author LXK
 *
 */
@ApiModel(value="com.slljr.finance.common.pojo.model.ForumIntegral")
public class ForumIntegral implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5958621755245983256L;

	@ApiModelProperty(value="主键")
    private Integer id;

	@ApiModelProperty(value="用户uid")
    private Integer uid;

	@ApiModelProperty(value="积分配置:0.发帖,1.回复")
    private Integer type;

	@ApiModelProperty(value="积分数量")
    private Integer integral;
    
	@ApiModelProperty(value="创建时间")
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
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

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
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
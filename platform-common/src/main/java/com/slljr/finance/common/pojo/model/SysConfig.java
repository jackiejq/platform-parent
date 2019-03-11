package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

public class SysConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3011243487855836862L;

	@ApiParam(hidden = true) 
    private Integer id;
    
	@ApiModelProperty(value = "参数key", required = false)
    private String sysKey;
    
	@ApiModelProperty(value = "参数value", required = false)
    private String sysValue;
    
	@ApiModelProperty(value = "摘要", required = false)
    private String summary;
    
	@ApiModelProperty(value = "类别", required = false)
    private Integer type;
    
	@ApiModelProperty(value = "状态[-1禁用,0启用]", required = false)
    private Integer status;
    
	@ApiModelProperty(value = "操作人用户id", required = false)
    private Integer optUid;
    
    @ApiParam(hidden = true) 
    private Date createTime;
    
    @ApiParam(hidden = true) 
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey == null ? null : sysKey.trim();
    }

    public String getSysValue() {
        return sysValue;
    }

    public void setSysValue(String sysValue) {
        this.sysValue = sysValue == null ? null : sysValue.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
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

    public Integer getOptUid() {
        return optUid;
    }

    public void setOptUid(Integer optUid) {
        this.optUid = optUid;
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
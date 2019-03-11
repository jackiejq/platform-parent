package com.slljr.finance.common.pojo.model;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

/**
 * @ApiModelProperty(value = "积分任务mode", required = false)
 * @author LXK
 *
 */
public class IntegralTask {
	@ApiParam(hidden = true)
    private Integer id;
	
	@ApiModelProperty(value = "标题", required = false)
    private String title;
	
	@ApiModelProperty(value = "奖励值", required = false)
    private Double number;
	
	@ApiModelProperty(value = "图片URL", required = false)
    private String imgUrl;
	
	@ApiModelProperty(value = "开始时间", required = false)
    private Date startTime;
	
	@ApiModelProperty(value = "结束时间", required = false)
    private Date endTime;
	
    @ApiModelProperty(value = "状态[-1删除,0正常]", required = false)
    private Integer status;
    
    @ApiParam(hidden = true)
    private Date createTime;
    
    @ApiParam(hidden = true)
    private Date updateTime;
    
    @ApiModelProperty(value = "详情", required = false)
    private String detail;

  

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }
}
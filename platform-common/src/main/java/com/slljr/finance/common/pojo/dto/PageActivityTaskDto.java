package com.slljr.finance.common.pojo.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 1、活动任务的入参
 * @author XueYi
 * 2018年12月12日 上午10:56:38
 */
public class PageActivityTaskDto implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -5370946769580532006L;

	@ApiModelProperty(value = "当前页", required = false)
	private Integer pageNum;
	
	@ApiModelProperty(value = "每页显示的条数", required = false)
	private Integer pageSize;
	
	@ApiModelProperty(value = "商品标题", required = false)
	private String title;
	
	@ApiModelProperty(value = "奖励类型[1积分,2现金]", required = false)
	private String type;
	
	@ApiModelProperty(value = "状态[-1禁用,0启用]", required = false)
	private Integer status;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "开始时间", required = false)
    private Date startTime;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "结束时间", required = false)
    private Date endTime;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getPageNum() {
		return pageNum == null ? 1 : pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		int max = 50;
		if(pageSize==null || pageSize>max) {
			pageSize=10;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}

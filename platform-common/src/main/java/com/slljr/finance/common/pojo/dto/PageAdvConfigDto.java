package com.slljr.finance.common.pojo.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 1、查询轮播图的
 * @author XueYi
 * 2018年12月7日 下午4:30:04
 */
public class PageAdvConfigDto implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 6827233494837689621L;

	@ApiModelProperty(value = "当前页", required = false)
	private Integer pageNum;
	
	@ApiModelProperty(value = "每页显示的条数", required = false)
	private Integer pageSize;
	
	@ApiModelProperty(value = "图片标题", required = false)
    private String title;
	
	@ApiModelProperty(value = "状态[-1删除,0正常]", required = false)
    private Integer status;
	
	@ApiModelProperty(value = "开始时间", required = false)
    private String startTime;

	@ApiModelProperty(value = "结束时间", required = false)
    private String endTime;
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}

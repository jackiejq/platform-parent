package com.slljr.finance.common.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
/**
 * 1、商品分页的入参
 * @author XueYi
 * 2018年12月4日 下午8:12:14
 */
public class PageGoodsDto{

	@ApiModelProperty(value = "当前页", required = false)
	private Integer pageNum;
	
	@ApiModelProperty(value = "每页显示的条数", required = false)
	private Integer pageSize;
	
	@ApiModelProperty(value = "商品标题", required = false)
    private String title;
	
	@ApiModelProperty(value = "分类id", required = false)
    private String categoryId;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

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

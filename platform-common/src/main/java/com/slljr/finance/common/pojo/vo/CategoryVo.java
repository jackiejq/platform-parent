package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

/**
 * 1、一级分类
 * @author XueYi
 * 2018年12月6日 下午8:26:45
 */
public class CategoryVo implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 3469106363176258312L;

	//商品一级分类id
	private Integer firstId;

	//商品一级分类名称
	private String firstName;

	public Integer getFirstId() {
		return firstId;
	}

	public void setFirstId(Integer firstId) {
		this.firstId = firstId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "GoodsCategoryVo [firstId=" + firstId + ", firstName=" + firstName + "]";
	}
}

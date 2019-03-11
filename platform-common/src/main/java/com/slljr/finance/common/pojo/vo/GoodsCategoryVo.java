package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.Goods;

/**
 * 1、商品列表展示
 * @author XueYi
 * 2018年12月6日 下午7:43:39
 */
public class GoodsCategoryVo extends Goods implements Serializable{
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 2849628295057950032L;
	
	//商品一级分类id
	private Integer firstId;
	
	//商品二级分类
	private Integer secondaryId;
	
	//商品一级分类名称
	private String firstName;
	
	//商品二级分类名称
	private String secondaryName;

	public Integer getFirstId() {
		return firstId;
	}

	public void setFirstId(Integer firstId) {
		this.firstId = firstId;
	}

	public Integer getSecondaryId() {
		return secondaryId;
	}

	public void setSecondaryId(Integer secondaryId) {
		this.secondaryId = secondaryId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondaryName() {
		return secondaryName;
	}

	public void setSecondaryName(String secondaryName) {
		this.secondaryName = secondaryName;
	}

	@Override
	public String toString() {
		return "GoodsCategoryVo [firstId=" + firstId + ", secondaryId=" + secondaryId + ", firstName=" + firstName
				+ ", secondaryName=" + secondaryName + "]";
	}
}

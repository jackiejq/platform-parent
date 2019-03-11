package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.GoodsCategory;

/**
 * 1、分类的实现类
 * @author XueYi
 * 2018年12月11日 上午11:22:26
 */
public class CategoryListVo extends GoodsCategory implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -6532366921654027875L;

	//商品一级分类名称
	private String firstName;

	//商品二级分类名称
	private String secondaryName;

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
}

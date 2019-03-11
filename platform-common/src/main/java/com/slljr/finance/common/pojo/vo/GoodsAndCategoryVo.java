package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.Goods;

import io.swagger.annotations.ApiModelProperty;

/**
 * 1、商品数据展示
 * @author XueYi
 * 2018年12月6日 下午2:34:51
 */
public class GoodsAndCategoryVo extends Goods implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 7038245436614034094L;

	//商品类别二级地
	@ApiModelProperty(value = "商品类别二级id", required = false)
	private Integer twoId;
	
	//商品类别二级名称
	@ApiModelProperty(value = "商品类别二级", required = false)
	private String twoName;

	public Integer getTwoId() {
		return twoId;
	}

	public void setTwoId(Integer twoId) {
		this.twoId = twoId;
	}

	public String getTwoName() {
		return twoName;
	}

	public void setTwoName(String twoName) {
		this.twoName = twoName;
	}
}

package com.slljr.finance.admin.service;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.model.AdvConfig;

/***
 * 1、轮播的业务逻辑层接口
 * @author XueYi
 * 2018年12月7日 下午3:48:39
 */
public interface IAdvConfigService {

	/**
	 * 1、添加轮播图数据
	 */
	void addAdvConfig(AdvConfig advConfig);
	
	/**
	 * 2、分页查询轮播图数据
	 * @param advConfigDto
	 * @return
	 */
	PageInfo listAdvConfig(AdvConfig advConfig, int pageNum, int pageSize);
	
	/**
	 * 3、根据id查询轮播详情
	 * @param id
	 * @return
	 */
	AdvConfig getAdvConfig(Integer id);
	
	/**
	 * 4、根据主键id删除信息
	 * @param id
	 * @return
	 */
	int deleteAdvConfig(Integer id);
	
	/**
	 * 5、修改轮播图数据
	 * @param advConfig
	 */
	void updateAdvConfig(AdvConfig advConfig);

	
}

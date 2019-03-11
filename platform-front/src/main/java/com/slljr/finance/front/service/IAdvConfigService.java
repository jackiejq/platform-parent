package com.slljr.finance.front.service;

import java.util.List;

import com.slljr.finance.common.pojo.model.AdvConfig;
/**
 * 1、查询轮播图的信息
 * @author XueYi
 * 2018年12月7日 下午6:06:04
 */
public interface IAdvConfigService {

	/**
	 * 1、查询前端显示的图片
	 * @return
	 */
	List<AdvConfig> listAdvConfig();

	/**
	 * 根据ID查询广告信息
	 *
	 * @author uncle.quentin
	 * @date   2018/12/14 10:47
	 * @param   ids
	 * @return com.slljr.finance.common.pojo.model.AdvConfig
	 * @version 1.0
	 */
	List<AdvConfig> selectAdvByIds(String ids);
}

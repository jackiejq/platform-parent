package com.slljr.finance.admin.service;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.enums.SysConfigEnum;
import com.slljr.finance.common.pojo.model.SysConfig;

/**
 * 系统参数配置服务层
 * @author LXK
 * 2018年12月10日 下午3:55:46
 */
public interface SysConfigSerivce {

	//添加
	void addSysConfig(SysConfig SysConfig);

	//删除
	void deleteSysConfig(Integer id);
	
	//修改
	void updateSysConfig(SysConfig sysConfig);
	//分页查询系统配置列表
	PageInfo<SysConfig> findSysConfigList(SysConfig sysConfig, int pageNum, int pageSize);

	SysConfig findSysConfigDetails(Integer id);

	SysConfig selectByKey(SysConfigEnum sysConfigEnum);

	SysConfig selectSysConfigByKey(SysConfigEnum sysConfigEnums);
	
}

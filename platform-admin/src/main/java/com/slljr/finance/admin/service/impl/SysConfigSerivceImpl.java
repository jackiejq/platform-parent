package com.slljr.finance.admin.service.impl;

import java.util.Date;
import java.util.List;

import com.slljr.finance.common.enums.SysConfigEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.SysConfigMapper;
import com.slljr.finance.admin.service.SysConfigSerivce;
import com.slljr.finance.common.pojo.model.SysConfig;
/**
 * 参数配置逻辑层实现类
 * @author LXK
 * 2018年12月4日 下午3:57:20
 */
@Service
public class SysConfigSerivceImpl implements SysConfigSerivce {

	@Autowired
	SysConfigMapper sysConfigMapper;
	
	//增加
	@Override
	public void addSysConfig(SysConfig sysConfig) {
		sysConfig.setStatus(0);
		sysConfig.setCreateTime(new Date());
		sysConfig.setUpdateTime(new Date());		
		sysConfigMapper.insertSelective(sysConfig);
	}

	//删除
	@Override
	public void deleteSysConfig(Integer id) {
		sysConfigMapper.deleteSysConfig(id);		
	}

	//修改
	@Override
	public void updateSysConfig(SysConfig sysConfig) {	
		sysConfig.setUpdateTime(new Date());
		sysConfigMapper.updateByPrimaryKeySelective(sysConfig);
	}

	@Override
	public PageInfo<SysConfig> findSysConfigList(SysConfig sysConfig, int pageNum, int pageSize) {
		 PageHelper.startPage(pageNum, pageSize);
	        List<SysConfig> list =  sysConfigMapper.findSysConfigList(sysConfig);
	        return new PageInfo<>(list);
	}

	@Override
	public SysConfig findSysConfigDetails(Integer id) {
		SysConfig sysConfigDetails = sysConfigMapper.selectByPrimaryKey(id);
		return sysConfigDetails;
	}

	/**
	 * 根据KEY查询
	 *
	 * @param sysConfigEnum
	 * @return com.slljr.finance.common.pojo.model.SysConfig
	 * @author uncle.quentin
	 * @date 2018/12/14 10:33
	 * @version 1.0
	 */
	@Override
	public SysConfig selectByKey(SysConfigEnum sysConfigEnum) {
		SysConfig sysConfigs = sysConfigMapper.findBySysKey(sysConfigEnum.getKey());
		return sysConfigs;
	}

	/**
	 * 获取指定配置ID
	 *
	 * @param
	 * @return java.util.List<com.slljr.finance.common.pojo.model.SysConfig>
	 * @author uncle.quentin
	 * @date 2018/12/13 21:03
	 * @version 1.0
	 */
	@Override
	public SysConfig selectSysConfigByKey(SysConfigEnum sysConfigEnums) {
		return sysConfigMapper.findBySysKey(sysConfigEnums.getKey());
	}
	
}

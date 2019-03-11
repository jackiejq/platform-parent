package com.slljr.finance.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.AdvConfigMapper;
import com.slljr.finance.admin.service.IAdvConfigService;
import com.slljr.finance.common.pojo.model.AdvConfig;
/**
 * 1、轮播图的业务逻辑层实现类
 * @author XueYi
 * 2018年12月7日 下午3:51:16
 */
@Service
public class AdvConfigServiceImpl implements IAdvConfigService {

	@Autowired
	private AdvConfigMapper advConfigMapper;
	
	/**
	 * 1、往轮播图里添加数据
	 */
	@Override
	public void addAdvConfig(AdvConfig advConfig) {
		advConfig.setStatus(0);
		advConfig.setCreateTime(new Date());
		advConfig.setUpdateTime(new Date());
		advConfigMapper.insert(advConfig);
	}

	
	/**
	 * 2、分页查询轮播图数据
	 */
	@Override
	public PageInfo<AdvConfig> listAdvConfig(AdvConfig advConfig,int pageNum,int pageSize) {
		 PageHelper.startPage(pageNum, pageSize);
	        List<AdvConfig> list =  advConfigMapper.listAdvConfig(advConfig);
	        return new PageInfo<>(list);	
	}

	/**
	 * 3、根据id查询轮播图的详情
	 */
	@Override
	public AdvConfig getAdvConfig(Integer id) {
		AdvConfig advConfig = advConfigMapper.selectByPrimaryKey(id);
		Assert.notNull(advConfig, "轮播图数据不存在");
		return advConfig;
	}
	
	/**
	 * 4、根据主键id删除轮播图信息
	 */
	@Override
	public int deleteAdvConfig(Integer id) {
		int rows = 0;
		try {
			 rows = advConfigMapper.deleteByPrimaryKey(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows>0?1:null ;
	}


	/**
	 * 5、修改轮播图信息
	 */
	@Override
	public void updateAdvConfig(AdvConfig advConfig) {
		advConfig.setUpdateTime(new Date());
		advConfigMapper.updateByPrimaryKeySelective(advConfig);
	}
}

package com.slljr.finance.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.ActivityTaskMapper;
import com.slljr.finance.admin.service.ActivityTaskSerivce;
import com.slljr.finance.common.pojo.dto.PageActivityTaskDto;
import com.slljr.finance.common.pojo.model.ActivityTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
/**
 * 参数配置逻辑层实现类
 * @author LXK
 * 2018年12月4日 下午3:57:20
 */
@Service
public class ActivityTaskSerivceImpl implements ActivityTaskSerivce {

	@Autowired
	private ActivityTaskMapper activityTaskMapper;
	
	@Override
	public void deleteActivityTask(Integer id) {
		activityTaskMapper.deleteByStatus(id);		
	}

	@Override
	public void updateActivityTask(ActivityTask activityTask) {
		activityTask.setUpdateTime(new Date());
		activityTaskMapper.updateByPrimaryKeySelective(activityTask);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int addActivityTask(ActivityTask activityTask) {		
		activityTask.setStatus(0);
		activityTask.setCreateTime(new Date());
		activityTask.setUpdateTime(new Date());
		return activityTaskMapper.insertSelective(activityTask);		
	}

	/**
	 * 根据条件分页查询任务列表
	 */
	@Override
	public PageInfo<ActivityTask> findSysConfigList(PageActivityTaskDto activityTaskDto) {
		//1、设置分页信息
		PageHelper.startPage(activityTaskDto.getPageNum(), activityTaskDto.getPageSize());
		//2、查询出活动任务信息
		List<ActivityTask> findSysConfigList = activityTaskMapper.findSysConfigList(activityTaskDto);
		return new PageInfo<>(findSysConfigList);
	}

	/**
	 * 根据id查询任务di
	 */
	@Override
	public ActivityTask getActivityTask(Integer id) {
		ActivityTask task = activityTaskMapper.selectByPrimaryKey(id);
		Assert.notNull(task, "查询任务详情失败");
		return task;
	}
}

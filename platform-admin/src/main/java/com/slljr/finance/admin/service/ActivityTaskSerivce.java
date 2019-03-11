package com.slljr.finance.admin.service;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.dto.PageActivityTaskDto;
import com.slljr.finance.common.pojo.model.ActivityTask;

/**
 * 系统参数配置服务层
 * @author LXK
 * 2018年12月10日 下午3:55:46
 */
public interface ActivityTaskSerivce {
	
	/**
	 * 1、分页查询活动任务列表
	 * @param activityTaskDto
	 * @return
	 */
	PageInfo<ActivityTask> findSysConfigList(PageActivityTaskDto activityTaskDto);

	void deleteActivityTask(Integer id);

	void updateActivityTask(ActivityTask activityTask);

	int addActivityTask(ActivityTask activityTask);
	
	/**
	 * 2、根据id查询活动任务详情
	 * @param id
	 * @return
	 */
	ActivityTask getActivityTask(Integer id);
	
}

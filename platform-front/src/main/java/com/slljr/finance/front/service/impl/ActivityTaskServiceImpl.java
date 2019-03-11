package com.slljr.finance.front.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.model.ActivityTask;
import com.slljr.finance.front.mapper.ActivityTaskMapper;
import com.slljr.finance.front.service.ActivityTaskService;
/**
   *积分任务服务层
 * @author LXK
 *
 */
@Service
public class ActivityTaskServiceImpl implements ActivityTaskService {

	@Autowired
	private ActivityTaskMapper activityTaskMapper;
	
	@Override
	public PageInfo findAll(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ActivityTask> list = activityTaskMapper.findByStatus(0);
		return new PageInfo<>(list);
	}

	@Override
	public ActivityTask findTaskDetail(int id) {		
		return activityTaskMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ActivityTask> findTaskByIds(List<Integer> ids) {
		return activityTaskMapper.selectByIds(ids);
	}

}

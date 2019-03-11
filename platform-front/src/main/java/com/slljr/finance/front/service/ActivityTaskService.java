package com.slljr.finance.front.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.model.ActivityTask;
import com.slljr.finance.common.pojo.model.IntegralTask;

/**
 * 积分任务服务接口
 * @author LXK
 *
 */

public interface ActivityTaskService {

	/**
	 * 查询任务列表
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageInfo findAll(int pageNum, int pageSize);

	/**
	 * 查询任务详情
	 * @param id
	 * @return
	 */
	ActivityTask findTaskDetail(int id);

	/**
	 * 根据Id集合查询任务详情
	 *
	 * @author uncle.quentin
	 * @date   2018/12/14 9:27
	 * @param   ids
	 * @return java.util.List<com.slljr.finance.common.pojo.model.ActivityTask>
	 * @version 1.0
	 */
	List<ActivityTask> findTaskByIds(List<Integer> ids);
}

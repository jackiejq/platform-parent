package com.slljr.finance.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.slljr.finance.admin.service.ActivityTaskSerivce;
import com.slljr.finance.common.pojo.dto.PageActivityTaskDto;
import com.slljr.finance.common.pojo.model.ActivityTask;
import com.slljr.finance.common.utils.WriteJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 积分任务Controller
 * @author LXK
 */
@RestController
@RequestMapping(value="/activityTask")
@Api(tags= {"积分任务操作的API"})
public class ActivityTaskController {

	@Autowired
	private ActivityTaskSerivce activityTaskService;
	
	/**
	   * 积分任务配置列表
	 * @return
	 */
	@RequestMapping(value="/findActivityTaskList",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "积分任务配置列表")		
	public ModelMap findActivityTaskList(PageActivityTaskDto pageActivityTaskDto){		
		  return WriteJson.successPage(activityTaskService.findSysConfigList(pageActivityTaskDto));
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/deleteActivityTask", method = RequestMethod.POST)
	@ApiOperation(value = "删除积分任务", httpMethod = "POST")
	public ModelMap deleteActivityTask(Integer id) {
		Assert.notNull(id, "删除项的id为空");		
		try {
			activityTaskService.deleteActivityTask(id);
		} catch (Exception e) {
			return WriteJson.errorWebMsg("删除失败");
		}
		return WriteJson.success();
	}
	
	/**
	 * 修改
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/updateActivityTask", method = RequestMethod.POST)
	@ApiOperation(value = "修改积分任务配置", httpMethod = "POST")
	public ModelMap updateActivityTask(ActivityTask activityTask) {
		Assert.notNull(activityTask.getId(), "删除项的id为空");		
		try {
			activityTaskService.updateActivityTask(activityTask);
		} catch (Exception e) {
			return WriteJson.errorWebMsg("修改积分任务配置失败");
		}
		return WriteJson.success();
	}
	
	/**
	 * 增加
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/addActivityTask", method = RequestMethod.POST)
	@ApiOperation(value = "增加系统配置数据", httpMethod = "POST")
	public ModelMap addActivityTask(ActivityTask activityTask) {
		try {
			Assert.notNull(activityTask, "增加数据为空");				
			activityTaskService.addActivityTask(activityTask);
			return WriteJson.success();
		} catch (Exception e) {
			e.printStackTrace();
			return WriteJson.errorWebMsg("添加失败");
		}
	}

	/**
	 * 根据任务编号查询任务详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/getActivityTask/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "根据任务编号查询任务详情", httpMethod = "GET")
	public ModelMap getActivityTask(@PathVariable("id") Integer id) {
		Assert.notNull(id, "任务编号不能为空");		
		return WriteJson.successData(activityTaskService.getActivityTask(id));
	}
}

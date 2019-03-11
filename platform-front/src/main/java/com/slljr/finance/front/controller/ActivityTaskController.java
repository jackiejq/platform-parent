package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.enums.SysConfigEnum;
import com.slljr.finance.common.pojo.model.SysConfig;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.front.config.NoLoginAnnotation;
import com.slljr.finance.front.service.SysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.model.ActivityTask;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.service.ActivityTaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务控制访问层
 * 
 * @author LanXinKui
 *
 */
@RestController
@Api(tags = { "任务页操作的API" })
@RequestMapping(value = "/integralTask")
@CrossOrigin
public class ActivityTaskController  extends BaseController{

	@Autowired
	private ActivityTaskService activityTaskService;

	/**
	 * 系统配置服务接口
	 */
	@Autowired
	private SysConfigService sysConfigService;

	/**
	 * 查询所有任务list
	 * 
	 */
	@RequestMapping(value = "/findTaskList", method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "查询所有任务list",notes = "{\"pageNum\":\"页码\",\"pageSize\":\"每页数\"}")
	@ResponseBody
	public ModelMap findTaskList(@RequestBody String param) {
		JSONObject paramObj = JsonUtil.strToJson(param);
		int pageNum = (null == paramObj.get("pageNum") ? 1 : paramObj.getInteger("pageNum"));
		int pageSize = (null == paramObj.get("pageSize") ? 10 : paramObj.getInteger("pageSize"));

		PageInfo pageInfo = activityTaskService.findAll(pageNum, pageSize);
		return WriteJson.successPage(pageInfo);
	}

	/**
	 * 任务详情查询
	 */
	@RequestMapping(value = "/findTaskDetail/{id}", method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "查询任务详情",notes = "{\"id\":\"数据ID\"}")
	@ResponseBody
	@NoLoginAnnotation
	public ModelMap findTaskDetail(@PathVariable String id) {
		Assert.notNull(id, "id不能为空");
		ActivityTask activityTask = activityTaskService.findTaskDetail(Integer.valueOf(id));
		return WriteJson.successData(activityTask);
	}

	/**
	 * 查询特定的任务信息
	 *
	 * @author uncle.quentin
	 * @date   2018/12/14 9:31
	 * @param
	 * @return org.springframework.ui.ModelMap
	 * @version 1.0
	 */
	@RequestMapping(value = "/getSpecificTask", method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "查询指定的任务信息",notes = "{\"type\":\"1:首页任务，2：任务页\"}")
	@ResponseBody
	@NoLoginAnnotation
	public ModelMap findSpecialTaskByConfig(@RequestBody String type){
		JSONObject paramObj = JsonUtil.strToJson(type);
		String typeParam = (null == paramObj.get("type") ? "" : paramObj.getString("type"));
		Assert.notNull(type, "类型不能为空");
		List<ActivityTask> activityTasks = new ArrayList<>();
		//获取系统配置的指定任务ID
		SysConfig sysConfigs;
		if (1 == Integer.valueOf(typeParam)) {
			sysConfigs = sysConfigService.selectSysConfigByKey(SysConfigEnum.APP_HOME_TASK_KEY);
		} else {
			sysConfigs = sysConfigService.selectSysConfigByKey(SysConfigEnum.APP_TASK_PAGE_KEY);
		}
		if (null != sysConfigs) {
			List<Integer> ids = new ArrayList<>();
			String[] strs = sysConfigs.getSysValue().split(",");
			for (String str : strs) {
				if (StringUtils.isNotEmpty(str)) {
					ids.add(Integer.valueOf(str));
				}
			}
			//根据配置的ID集合查询商品详细信息
			activityTasks  = activityTaskService.findTaskByIds(ids);
		}

		return WriteJson.successData(activityTasks);
	}

}

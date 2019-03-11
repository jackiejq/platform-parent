package com.slljr.finance.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.SysConfigSerivce;
import com.slljr.finance.common.pojo.model.SysConfig;
import com.slljr.finance.common.utils.WriteJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 系统参数配置Controller
 * @author LXK
 */
@RestController
@RequestMapping(value="/sysConfig")
@Api(tags= {"系统参数配置操作的API"})
public class SysConfigController {

	@Autowired
	private SysConfigSerivce sysConfigService;
	
	/**
	   * 分页系统配置列表
	 * @return
	 */
	@RequestMapping(value="/findSysConfigList",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "分页查看商品列表")		
	public ModelMap findSysConfigList(SysConfig sysConfig,@RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "10")int pageSize){		
		 PageInfo<SysConfig> pageInfo = sysConfigService.findSysConfigList(sysConfig,pageNum, pageSize);
		 return WriteJson.successPage(pageInfo);		 		
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/deleteSysConfig", method = RequestMethod.POST)
	@ApiOperation(value = "删除系统配置数据", httpMethod = "POST")
	public ModelMap deleteSysConfig(Integer id) {
		Assert.notNull(id, "删除项的id为空");		
		try {
			sysConfigService.deleteSysConfig(id);
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
	@RequestMapping(value ="/updateSysConfig", method = RequestMethod.POST)
	@ApiOperation(value = "修改系统配置数据", httpMethod = "POST")
	public ModelMap updateSysConfig(SysConfig sysConfig) {
		Assert.notNull(sysConfig.getId(), "删除项的id为空");		
		try {
			sysConfigService.updateSysConfig(sysConfig);
		} catch (Exception e) {
			return WriteJson.errorWebMsg("修改系统配置信息失败");
		}
		return WriteJson.success();
	}
	
	/**
	 * 增加
	 * @param id
	 * @return
	 */
	 @ApiImplicitParams({
         @ApiImplicitParam(name = "sysConfig", value = "sysConfig", required = false, dataType = "SysConfig", paramType = "body")
 })
	@RequestMapping(value ="/addSysConfig", method = RequestMethod.POST)
	@ApiOperation(value = "增加系统配置数据", httpMethod = "POST")
	public ModelMap addSysConfig(SysConfig sysConfig) {
		try {
			Assert.notNull(sysConfig.getSysKey(), "参数key为空");	
			Assert.notNull(sysConfig.getSysValue(),"参数value为空");
			sysConfigService.addSysConfig(sysConfig);
			return WriteJson.success();
		} catch (Exception e) {
			return WriteJson.errorWebMsg("添加失败");
		}					
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/findSysConfigDetails", method = RequestMethod.GET)
	@ApiOperation(value = "查找单条系统配置详情数据", httpMethod = "GET")
	public ModelMap findSysConfigDetails(Integer id) {
		try {
			Assert.notNull(id, "id为空");				
			SysConfig sysConfigDetails = sysConfigService.findSysConfigDetails(id);
			return WriteJson.successData(sysConfigDetails);
		} catch (Exception e) {
			e.printStackTrace();
			return WriteJson.errorWebMsg("无相关信息");
		}					
	}
	
}

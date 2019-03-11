package com.slljr.finance.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slljr.finance.admin.service.IAdvConfigService;
import com.slljr.finance.common.pojo.dto.PageAdvConfigDto;
import com.slljr.finance.common.pojo.model.AdvConfig;
import com.slljr.finance.common.utils.WriteJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 1、轮播图的Controller
 * @author XueYi
 * 2018年12月7日 下午3:57:24
 */
@RestController
@RequestMapping(value="/advConfig")
@Api(tags= {"后台管理轮播图操作的API"})
public class AdvConfigController {

	@Autowired
	private IAdvConfigService advConfigService;
	
	/**
	 * 1、添加轮播图的数据
	 * @param advConfig
	 * @return
	 */
	@RequestMapping(value ="/addAdvConfig", method = RequestMethod.POST)
	@ApiOperation(value = "添加轮播图的数据", httpMethod = "POST")
	public ModelMap addAdvConfig(AdvConfig advConfig) {
		Assert.notNull(advConfig.getTitle(), "图片标题不能为空");
		Assert.notNull(advConfig.getImgUrl(), "图片的路径不能为空");
		Assert.notNull(advConfig.getLink(), "图片添加的超链接地址不能为空");
		try {
			advConfigService.addAdvConfig(advConfig);
			return WriteJson.success();
		} catch (Exception e) {
			e.printStackTrace();
			return WriteJson.errorWebMsg("添加轮播图失败");
		}
		
	}
	
	/**
	 * 2、分页查询轮播图信息
	 * @param advConfigDto
	 * @return
	 */
	@RequestMapping(value ="/listAdvConfig", method = RequestMethod.POST)
	@ApiOperation(value = "分页查询轮播图的数据", httpMethod = "POST")
	public ModelMap listAdvConfig(AdvConfig advConfig,@RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "10")int pageSize) {
		return WriteJson.successPage(advConfigService.listAdvConfig(advConfig,pageNum, pageSize));
	}
	
	/**
	 * 3、根据id查询轮播图的详情数据
	 * @return
	 */
	@RequestMapping(value ="/getAdvConfig/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "根据id查询轮播图的详情数据", httpMethod = "GET")
	public ModelMap getAdvConfig(@ApiParam(name = "id", value = "轮播图编号", required = true) @PathVariable("id") Integer id) {
		if(null != id) {
			return WriteJson.successData(advConfigService.getAdvConfig(id));
		}
		return WriteJson.errorWebMsg("查询轮播图数据失败");
	}
	
	/**
	 * 4、删除轮播图信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/deleteAdvConfig/{id}", method = RequestMethod.POST)
	@ApiOperation(value = "根据id删除轮播图信息", httpMethod = "POST")
	public ModelMap gatAdvConfig(@ApiParam(name = "id", value = "轮播图编号", required = true) @PathVariable("id") Integer id) {
		Assert.notNull(id, "轮播图主键id不能为空");
		int falg = advConfigService.deleteAdvConfig(id);
		if(falg == 1) {
			return WriteJson.success("轮播图删除成功");
		}
		return WriteJson.errorWebMsg("轮播图删除失败");
	}
	
	/**
	 * 5、修改轮播图数据
	 * @param advConfig
	 * @return
	 */
	@RequestMapping(value ="/updateAdvConfig", method = RequestMethod.POST)
	@ApiOperation(value = "修改轮播图信息", httpMethod = "POST")
	public ModelMap updateAdvConfig(AdvConfig advConfig) {
		Assert.notNull(advConfig.getId(), "轮播图主键id不能为空");
		Assert.notNull(advConfig.getImgUrl(), "轮播图片路径不能为空");
		Assert.notNull(advConfig.getLink(), "轮播图片点击的超链接不能为空");
		Assert.notNull(advConfig.getTitle(), "轮播图的主题不能为空");
		try {
			advConfigService.updateAdvConfig(advConfig);
			return WriteJson.success("修改轮播图数据成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WriteJson.errorWebMsg("修改轮播图数据失败");
		}
	}
}

package com.slljr.finance.admin.controller;

import java.util.List;

import com.slljr.finance.common.pojo.dto.UserTradeOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.UserTradeSerivce;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.vo.UserTradePayDetailsVo;
import com.slljr.finance.common.utils.WriteJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户交易控制访问层
 * @author LXK
 * 2018年12月10日 下午4:05:42
 */
@RestController
@Api(tags={"用户交易操作的API"})
@RequestMapping(value="/userTrade")
@CrossOrigin
public class UserTradeController {

	@Autowired
	private UserTradeSerivce userTradeSerivce;

	/**
	   * 分页查看用户交易记录列表
	 * @return
	 */
	@RequestMapping(value="/findUserTradeList",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "分页查看用户交易列表")		
	public ModelMap findUserTradeList(UserTradeOrderDTO userTradeOrder){
		 PageInfo<UserTradeOrder> pageInfo = userTradeSerivce.findUserTradeList(userTradeOrder);
		 return WriteJson.successPage(pageInfo);		 		
	}
	      
	   
	@RequestMapping(value="/findUserTradeDetails",method=RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "查找用户交易记录详情信息")
	public ModelMap findUserTradeDetails(Integer id) {
		Assert.notNull(id, "id不能为空");		
		List<UserTradePayDetailsVo> userTradeOrderRecord = userTradeSerivce.findUserTradeDetails(id);
		if(userTradeOrderRecord == null) {
			return WriteJson.errorWebMsg("查询无记录");
		}
		return WriteJson.successData(userTradeOrderRecord);	
	}	
}

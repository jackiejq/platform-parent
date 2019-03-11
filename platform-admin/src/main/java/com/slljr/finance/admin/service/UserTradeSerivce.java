package com.slljr.finance.admin.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.dto.UserTradeOrderDTO;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
import com.slljr.finance.common.pojo.vo.UserTradePayDetailsVo;

/**
 * 用户交易服务层
 * @author LXK
 * 2018年12月4日 下午3:55:46
 */
public interface UserTradeSerivce {

	//查询用户交易记录
	//List<UserTradeOrder> findUserTrade(Integer uid);
	//查询用户交易记录详情
	//UserTradeDetailsVo findUserTradeDetails(Integer id);
	//分页查询用户交易记录
	PageInfo<UserTradeOrder> findUserTradeList(UserTradeOrderDTO userTradeOrder);
	//查询用户交易记录详情
	List<UserTradePayDetailsVo> findUserTradeDetails(Integer id);
	
}

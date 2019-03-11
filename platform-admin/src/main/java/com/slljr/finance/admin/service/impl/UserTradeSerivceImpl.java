package com.slljr.finance.admin.service.impl;

import java.util.List;

import com.slljr.finance.common.pojo.dto.UserTradeOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.UserTradeOrderMapper;
import com.slljr.finance.admin.mapper.UserTradePaymentRecordMapper;
import com.slljr.finance.admin.service.UserTradeSerivce;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.vo.UserTradePayDetailsVo;
/**
 * 用户交易业务逻辑层实现类
 * @author LXK
 * 2018年12月4日 下午3:57:20
 */
@Service
public class UserTradeSerivceImpl implements UserTradeSerivce {

	@Autowired
	private UserTradeOrderMapper userTradeOrderMapper;

	@Autowired
	private UserTradePaymentRecordMapper userTradePaymentRecordMapperMapper;
	//分页查询用户交易记录
	@Override
	public PageInfo<UserTradeOrder> findUserTradeList(UserTradeOrderDTO userTradeOrder) {
		return PageHelper.startPage(userTradeOrder.getPageNum(), userTradeOrder.getPageSize()).doSelectPageInfo(()-> userTradeOrderMapper.findUserTradeList(userTradeOrder));
	}

	//查询用户交易支付详情
	@Override
	public List<UserTradePayDetailsVo> findUserTradeDetails(Integer tradeId) {
		return userTradePaymentRecordMapperMapper.findByTradeId(tradeId);
	}

}

package com.slljr.finance.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.PaymentChannelMapper;
import com.slljr.finance.common.pojo.model.PaymentChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaymentChannelService {

	@Autowired
	private PaymentChannelMapper paymentChannelMapper;
	/**
	 * 分页查看支付通道列表
	 * @return
	 */
	public PageInfo<PaymentChannel> findPaymentChannelList(PaymentChannel paymentChannel, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<PaymentChannel> list =  paymentChannelMapper.findPaymentChannelList(paymentChannel);
		return new PageInfo<>(list);
	}

	public List<PaymentChannel> findByType(int channelType){
		return paymentChannelMapper.findByType(channelType);
	}

	/**
	 * 删除
	 */
	public void deletePaymentChannel(Integer id) {
		paymentChannelMapper.deleteByPaymentChannel(id);
	}


	/**
	 * 添加
	 */
	public void addPaymentChannel(PaymentChannel paymentChannel) {
		paymentChannel.setCreateTime(new Date());
		paymentChannel.setUpdateTime(new Date());
		paymentChannel.setStatus(1);
		paymentChannelMapper.insertSelective(paymentChannel);

	}

	/**
	 * 查询支付通道详情
	 */
	public PaymentChannel findPaymentChannelDetails(Integer id) {
		return paymentChannelMapper.selectByPrimaryKey(id);
	}

	public int updatePaymentChannel(PaymentChannel paymentChannel) {
		paymentChannel.setUpdateTime(new Date());
		//paymentChannel.setCreateTime(new Date())
		return paymentChannelMapper.updateByPrimaryKeySelective(paymentChannel);
	}

}

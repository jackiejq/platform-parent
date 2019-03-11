package com.slljr.finance.admin.mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.slljr.finance.common.pojo.model.PaymentChannel;

public interface PaymentChannelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PaymentChannel record);

    int insertSelective(PaymentChannel record);

    PaymentChannel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaymentChannel record);

    int updateByPrimaryKey(PaymentChannel record);

	List<PaymentChannel> findPaymentChannelList(PaymentChannel paymentChannel);

	List<PaymentChannel> findByType(@Param("type")Integer type);

	int deleteByPaymentChannel(Integer id);
	
}
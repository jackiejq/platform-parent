package com.slljr.finance.front.mapper;
import java.util.List;

import com.slljr.finance.common.pojo.model.PaymentChannelUser;
import org.apache.ibatis.annotations.Param;

public interface PaymentChannelUserMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUidAndChannelCode(@Param("uid")Integer uid,@Param("channelCode")String channelCode);





    int insert(PaymentChannelUser record);

    int insertSelective(PaymentChannelUser record);

    PaymentChannelUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaymentChannelUser record);

    int updateByPrimaryKey(PaymentChannelUser record);

    PaymentChannelUser findByUidAndChannelCode(@Param("uid")Integer uid,@Param("channelCode")String channelCode);

    List<PaymentChannelUser> findByChannelCode(@Param("channelCode")String channelCode);

	PaymentChannelUser findByUidAndChannelCodeAndBankCardId(@Param("uid")Integer uid,@Param("channelCode")String channelCode,@Param("bankCardId")Integer bankCardId);



}
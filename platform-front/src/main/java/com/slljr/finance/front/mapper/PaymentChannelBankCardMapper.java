package com.slljr.finance.front.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.slljr.finance.common.pojo.model.PaymentChannelBankCard;

public interface PaymentChannelBankCardMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUidAndChannelCode(@Param("uid")Integer uid,@Param("channelCode")String channelCode);



    int insert(PaymentChannelBankCard record);

    int insertSelective(PaymentChannelBankCard record);

    PaymentChannelBankCard selectByPrimaryKey(Integer id);

    /**
     * 查询用户银行卡是否在通道绑定
     * @param uid 用户id
     * @param bankCardId 支付卡id
     * @param channelCode 通道代码
     * @return
     */
    PaymentChannelBankCard findByUidAndBankCardIdAndChannelCode(@Param("uid")Integer uid,@Param("bankCardId")Integer bankCardId,@Param("channelCode")String channelCode);

    int updateByPrimaryKeySelective(PaymentChannelBankCard record);

    int updateByPrimaryKey(PaymentChannelBankCard record);

}
package com.slljr.finance.front.service;

import com.slljr.finance.common.enums.PaymentChannelEnum;
import com.slljr.finance.common.pojo.model.PaymentChannelUser;
import com.slljr.finance.front.mapper.PaymentChannelUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentChannelUserService {
    @Resource
    PaymentChannelUserMapper paymentChannelUserMapper;

    /**
     * 查询用户在当前通道是否已经注册
     * @param uid
     * @param cardId
     * @param channelEnum
     * @return
     */
    public PaymentChannelUser queryChannelUser(int uid, int cardId, PaymentChannelEnum channelEnum){
        if (channelEnum.getKey()== PaymentChannelEnum.TENGFUTONG.getKey()){
            return paymentChannelUserMapper.findByUidAndChannelCodeAndBankCardId(uid, channelEnum.getCode(), cardId);
        }else{
            return paymentChannelUserMapper.findByUidAndChannelCode(uid, channelEnum.getCode());
        }
    }


}

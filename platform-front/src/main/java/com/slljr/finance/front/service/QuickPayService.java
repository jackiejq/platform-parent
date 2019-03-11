package com.slljr.finance.front.service;

import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.vo.PaymentResultVO;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.front.mapper.*;
import com.slljr.finance.payment.utils.OrderNumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class QuickPayService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    OrderNumUtils orderNumUtils;
    @Resource
    UserBankCardMapper userBankCardMapper;
    @Autowired
    PaymentChannelMapper paymentChannelMapper;
    @Autowired
    PaymentChannelUserService paymentChannelUserService;
    @Resource
    PaymentChannelUserMapper paymentChannelUserMapper;
    @Resource
    PaymentChannelBankCardMapper paymentChannelBankCardMapper;
    @Resource
    UserTradePaymentRecordMapper userTradePaymentRecordMapper;
    @Resource
    UserTradeOrderMapper userTradeOrderMapper;


    public PaymentResultVO bindCard(UserBasic userBasic, Double paymentAmount, Integer payCardId, Integer recCardId){



        return null;
    }

}

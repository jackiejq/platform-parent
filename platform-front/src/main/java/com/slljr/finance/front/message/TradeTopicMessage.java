
package com.slljr.finance.front.message;

import com.alibaba.fastjson.JSON;
import com.slljr.finance.common.enums.*;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
import com.slljr.finance.common.pojo.vo.UserTradePaymentRecordVO;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.front.mapper.UserTradeOrderMapper;
import com.slljr.finance.front.mapper.UserTradePaymentRecordMapper;
import com.slljr.finance.front.runnable.FreepassPayRunnable;
import com.slljr.finance.front.runnable.QuickPayRunnable;
import com.slljr.finance.front.service.PaymentService;
import com.slljr.finance.payment.utils.PaymentResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @description: 消费者
 * @author: GUOQUN.YANG.
 * @date: 2018/3/22.
 * @time: 14:37.
 */

@Component
public class TradeTopicMessage {
    Logger log = LogManager.getLogger();
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    PaymentService paymentService;
    @Autowired
    UserTradeOrderMapper userTradeOrderMapper;
    @Autowired
    UserTradePaymentRecordMapper userTradePaymentRecordMapper;

    ExecutorService quickPayFixedThreadPool = Executors.newFixedThreadPool(20);
    ExecutorService freepassPayFixedThreadPool = Executors.newFixedThreadPool(20);

    @RabbitListener(queues = "order_queue")
    @RabbitHandler
    public void process(String message) {
        log.info("交易收到新消息:\n{}", message);

        if (StringUtils.isBlank(message)){
            log.error("消息为空!");
            return;
        }

        try {
            UserTradePaymentRecordVO record = JSON.parseObject(message, UserTradePaymentRecordVO.class);

            String recordKey = "UserTradePaymentRecord_" + record.getId();
            if (redisUtil.hasKey(recordKey)){
                log.info("该订单其他线程已在执行! \nrecord: {}", record.toString());
                return;
            }
            redisUtil.set(recordKey, recordKey, 30);

            UserTradeOrder order = userTradeOrderMapper.selectByPrimaryKey(record.getTradeId());
            if (order.getType() == PaymentChannelTypeEnum.DAIHUAN.getKey()){
                record.setDataType(1);
                freepassPayFixedThreadPool.execute(new FreepassPayRunnable(record));
            }else if(order.getType() == PaymentChannelTypeEnum.SHOUKUAN.getKey()){
                quickPayFixedThreadPool.execute(new QuickPayRunnable(record));
            }
        } catch (Exception e) {
            log.error("交易记录消费者执行异常!\nmessage:{}\nerrorMessage:{}", message, e);
        }
    }
}


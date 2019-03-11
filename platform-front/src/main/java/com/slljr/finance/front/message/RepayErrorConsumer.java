package com.slljr.finance.front.message;

import com.alibaba.fastjson.JSON;
import com.slljr.finance.common.pojo.vo.UserTradePaymentRecordVO;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.front.runnable.FreepassPayRunnable;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 异常还款数据队列消费者
 * @author: uncle.quentin.
 * @date: 2019/2/22.
 * @time: 15:00.
 */
@Component
public class RepayErrorConsumer {

    private static final Logger log = LogManager.getLogger();

    @Autowired
    RedisUtil redisUtil;

    /**
     * 线程池：核心线程：5 最大线程：10
     */
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 2000, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 异常还款数据处理
     *
     * @author uncle.quentin
     * @date   2019/2/22 15:02
     * @param   message
     * @return void
     * @version 1.0
     */
    @RabbitListener(queues = "repay_error_queue")
    @RabbitHandler
    public void process(String message) {
        log.info("异常还款消费者收到新消息:\n{}", message);

        if (StringUtils.isBlank(message)){
            log.error("RepayErrorConsumer.process消息为空!");
            return;
        }

        //转换为异常还款对象
        UserTradePaymentRecordVO record = JSON.parseObject(message, UserTradePaymentRecordVO.class);

        //判断是否被其它服务执行
        {
            String recordKey = "RepayErrorRecord_" + record.getId();
            if (redisUtil.hasKey(recordKey)){
                log.info("RepayErrorConsumer.process该订单其他线程已在执行! \nrecord: {}", record.toString());
                return;
            }
            redisUtil.set(recordKey, recordKey, 30);
        }

        //异常还款类型的数据
        record.setDataType(2);
        pool.execute(new FreepassPayRunnable(record));

    }

}

package com.slljr.finance.payment.utils;

import com.slljr.finance.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OrderNumUtils {
    @Autowired
    RedisUtil redisUtil;
    @Value("${order.num.prefix}")
    private String orderNumPrefix;
    private static final String ORDER_NUM_KEY = "ORDER_NUM_GENERATE_KEY";

    /**
     * 每毫秒生成订单号数量最大值
     */
    private static int maxSize = 10000;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");


    /**
     * 返回生成订单号(1毫秒最大生成10000个)
     * @return orderNo,23位长度
     */
    public synchronized String generate() {
        StringBuffer buffer = new StringBuffer(orderNumPrefix);

        Long num = redisUtil.incr(ORDER_NUM_KEY, 1L);
        if (num == maxSize -1) redisUtil.set(ORDER_NUM_KEY, 0L);

        long datetime = Long.parseLong(sdf.format(new Date()));
        buffer.append(datetime).append(String.valueOf((maxSize + num)).substring(1));

        return buffer.toString();
    }



}

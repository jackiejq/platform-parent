package com.slljr.finance.front.service;

import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.front.config.CommonRabbitProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 消息发送工具类
 *
 * @Author Created by uncle.quentin
 * @Date Created in 15:04 2018/4/11
 * @Version 1.0
 */
@Component
public class RabbitSender {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CommonRabbitProperties rabbitProperties;

    @Autowired
    public RabbitSender(RabbitTemplate rabbitTemplate) {
        Assert.notNull(rabbitTemplate, "rabbitTemplate is null");
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发送消息
     *
     * @param object 发送的消息
     * @Author: uncle.quentin
     * @Date: 2018/3/21 16:17
     * @version 1.0
     */
    public void sendMessage(Object object) {
        //最终发送消息
        String message = JsonUtil.getJson(object);
        this.rabbitTemplate.convertAndSend(rabbitProperties.getOrderExchange(), rabbitProperties.getOrderRoutingkey(), message);
    }

    /**
     * 发送异常还款队列数据
     *
     * @author uncle.quentin
     * @date   2019/2/22 16:36
     * @param   object
     * @return void
     * @version 1.0
     */
    public void sendRepayMessage(Object object){
        //最终发送消息
        String message = JsonUtil.getJson(object);
        this.rabbitTemplate.convertAndSend(rabbitProperties.getRepayExchange(), rabbitProperties.getRepayRoutingkey(), message);
    }
}

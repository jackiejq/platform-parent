package com.slljr.finance.front.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Rabbit配置注入
 * @author: GUOQUN.YANG.
 * @date: 2018/3/22.
 * @time: 14:37.
 */
@Configuration
public class DirectRabbitConfig {

    @Autowired
    private CommonRabbitProperties rabbitProperties;

    /**
     * 配置消息交换机
     * 针对消费者配置
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     *
     * @author  guoqun.yang
     * @date    2018/4/11 11:17
     * @return
     * @version 1.0
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(rabbitProperties.getOrderExchange(), true, false);
    }

    /**
     * 配置消息订单队列
     *
     * @author  guoqun.yang
     * @date    2018/4/11 11:17
     * @return
     * @version 1.0
     */
    @Bean
    public Queue orderQueue() {
        //队列持久
        return new Queue(rabbitProperties.getOrderQueue(), true);
    }

    /**
     * 将消息订单队列与交换机绑定
     *
     * @author  guoqun.yang
     * @date    2018/4/11 11:17
     * @return
     * @version 1.0
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(defaultExchange()).with(rabbitProperties.getOrderRoutingkey());
    }


    @Bean
    public DirectExchange repayExchange() {
        return new DirectExchange(rabbitProperties.getRepayExchange(), true, false);
    }

    /**
     * 配置消息异常还款队列
     *
     * @author  guoqun.yang
     * @date    2018/4/11 11:17
     * @return
     * @version 1.0
     */
    @Bean
    public Queue repayQueue() {
        //队列持久
        return new Queue(rabbitProperties.getRepayQueue(), true);
    }

    /**
     * 将消息异常还款队列与交换机绑定
     *
     * @author  guoqun.yang
     * @date    2018/4/11 11:17
     * @return
     * @version 1.0
     */
    @Bean
    public Binding repayBinding() {
        return BindingBuilder.bind(repayQueue()).to(repayExchange()).with(rabbitProperties.getRepayRoutingkey());
    }

}

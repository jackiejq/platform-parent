package com.slljr.finance.front.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rabbit配置属性
 * @Author Created by guoqun.yang
 * @Date Created in 12:08 2018/4/11
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "app.360jr.front")
public class CommonRabbitProperties {

    /**
     * 交换机名称
     */
    private String orderExchange;

    /**
     * 队列名
     */
    private String orderQueue;

    /**
     * routingkey
     */
    private String orderRoutingkey;

    /**
     * 异常还款队列名
     */
    private String repayQueue;

    /**
     * 异常还款交换机
     */
    private String repayExchange;

    /**
     * 异常还款Key
     */
    private String repayRoutingkey;

    public String getOrderExchange() {
        return orderExchange;
    }

    public void setOrderExchange(String orderExchange) {
        this.orderExchange = orderExchange;
    }

    public String getOrderQueue() {
        return orderQueue;
    }

    public void setOrderQueue(String orderQueue) {
        this.orderQueue = orderQueue;
    }

    public String getOrderRoutingkey() {
        return orderRoutingkey;
    }

    public void setOrderRoutingkey(String orderRoutingkey) {
        this.orderRoutingkey = orderRoutingkey;
    }

    public String getRepayQueue() {
        return repayQueue;
    }

    public void setRepayQueue(String repayQueue) {
        this.repayQueue = repayQueue;
    }

    public String getRepayExchange() {
        return repayExchange;
    }

    public void setRepayExchange(String repayExchange) {
        this.repayExchange = repayExchange;
    }

    public String getRepayRoutingkey() {
        return repayRoutingkey;
    }

    public void setRepayRoutingkey(String repayRoutingkey) {
        this.repayRoutingkey = repayRoutingkey;
    }
}

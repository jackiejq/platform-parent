
package com.slljr.finance.front.config;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * @description: 消费者
 * @author: GUOQUN.YANG.
 * @date: 2018/3/22.
 * @time: 14:37.
 */

//@Component
public class TopicReceiver1 {

    @RabbitListener(queues = "order_queue")
    @RabbitHandler
    public void process(String message) {
        System.out.println("===============================Topic Receiver1  : " + message);
    }
}


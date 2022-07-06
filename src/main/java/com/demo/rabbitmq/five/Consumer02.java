package com.demo.rabbitmq.five;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.Map;

/**
 * @auther lkx
 * @create 2022-07-06 12:29
 * @Description:
 */
public class Consumer02 {
    public static final String EXCHANGE_NAME = "topics_logs";

    public Consumer02() {
    }

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare("topics_logs", "topic");
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, "topics_logs", "*.*.orange");
        channel.queueBind(queueName, "topics_logs", "lazy.#");
        System.out.println("等待接受消息........");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "utf-8"));
            System.out.println("接受队列：" + queueName + " 绑定键: " + message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("接收失败:" + consumerTag);
        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}

package com.demo.rabbitmq.two;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @auther lkx
 * @create 2022-07-06 12:24
 * @Description:
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public Consumer() {
    }

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息者取消消费接口回调逻辑");
        };
        channel.basicConsume("hello", true, deliverCallback, cancelCallback);
    }
}

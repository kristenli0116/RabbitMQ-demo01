package com.demo.rabbitmq.five;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @auther lkx
 * @create 2022-07-05 17:07
 * @Description: 主题模式（Topics）的消费者
 */
public class Consumer02 {
    public static final String EXCHANGE_NAME = "topics_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //交换机声明
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //声明队列
        String queueName = "Q2";
        //queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.orange");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("等待接受消息........");


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "utf-8"));
            System.out.println("接受队列：" + queueName + " 绑定键: " + message.getEnvelope().getRoutingKey());
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("接收失败:" + consumerTag);
        };
        //接收消息
        //basicConsume(String queue, boolean autoAck, DeliverCallback deliverCallback, CancelCallback cancelCallback)
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}

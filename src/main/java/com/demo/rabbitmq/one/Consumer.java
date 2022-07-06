package com.demo.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @auther lkx
 * @create 2022-07-06 12:23
 * @Description:
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public Consumer() {
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.33.100");
        factory.setUsername("lilili");
        factory.setPassword("abc123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(message);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume("hello", true, deliverCallback, cancelCallback);
    }
}

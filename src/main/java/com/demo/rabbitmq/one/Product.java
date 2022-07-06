package com.demo.rabbitmq.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @auther lkx
 * @create 2022-07-06 12:22
 * @Description:
 */
public class Product {
    public static final String QUEUE_NAME = "hello";

    public Product() {
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.33.100");
        factory.setUsername("lilili");
        factory.setPassword("abc123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("hello", false, false, false, (Map) null);
        String message = "hello world";
        channel.basicPublish("", "hello", (AMQP.BasicProperties) null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println("消息发送成功");
    }
}
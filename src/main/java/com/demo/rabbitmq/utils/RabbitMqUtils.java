package com.demo.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @auther lkx
 * @create 2022-07-06 12:20
 * @Description:
 */

public class RabbitMqUtils {
    public RabbitMqUtils() {
    }

    public static Channel getChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.33.100");
        factory.setUsername("lilili");
        factory.setPassword("abc123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}

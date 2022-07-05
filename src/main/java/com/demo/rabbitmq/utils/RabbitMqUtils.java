package com.demo.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;



/**
 * @auther lkx
 * @create 2022-07-04 14:31
 * @Description:
 */
public class RabbitMqUtils {
    //得到一个连接的channel
    public static Channel getChannel() throws Exception {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP 连接RabbitMQ的队列
        factory.setHost("192.168.33.100");
        //用户名
        factory.setUsername("lilili");
        //密码
        factory.setPassword("abc123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
       Channel channel = connection.createChannel();

        return channel;
    }
}

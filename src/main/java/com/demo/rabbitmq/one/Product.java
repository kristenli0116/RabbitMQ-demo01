package com.demo.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @auther lkx
 * @create 2022-07-01 17:07
 * @Description:
 */
public class Product {

    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
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

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String message = "hello world";

        channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));

        System.out.println("消息发送成功");

    }
}

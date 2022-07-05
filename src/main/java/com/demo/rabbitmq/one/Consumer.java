package com.demo.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @auther lkx
 * @create 2022-07-01 17:26
 * @Description:
 */
public class Consumer {

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

        // 声明 接受消息
        DeliverCallback deliverCallback = (consumerTag,message) ->{
            System.out.println(message);
        };

        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("消息消费被中断");
        };

        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}

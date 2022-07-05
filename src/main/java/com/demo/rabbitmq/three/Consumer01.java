package com.demo.rabbitmq.three;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @auther lkx
 * @create 2022-07-04 15:45
 * @Description:
 */
public class Consumer01 {
    public static final String ACK_QUEUE_NAME = "ack_hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("消费者1号等待接受消息时间较短");

        //接受消息
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            //睡眠1秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到消息："+ new String(message.getBody(),"UTF-8"));
            //手动应答
            /**
             * 1. 消息的标记 tag
             * 2. 是否批量应答 false OR true
             */
        };

        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("消费者取消消费接口回调逻辑");
        };

        //设置不公平分发
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);

        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(ACK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}

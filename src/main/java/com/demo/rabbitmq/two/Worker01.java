package com.demo.rabbitmq.two;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import sun.applet.Main;

/**
 * @auther lkx
 * @create 2022-07-04 14:37
 * @Description:  工作线程01
 */
public class Worker01 {
    //队列名称
    public static final String QUEUE_NAME="hello";

    //接收消息
    public static void main(String[] args) throws Exception {
        //创建消息信道
        Channel channel = RabbitMqUtils.getChannel();

        //消息的接收
        DeliverCallback deliverCallback = (consumerTag,message) ->{
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        //消息接收被取消时，所要执行的方法
        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println(consumerTag +  "消息者取消消费接口回调逻辑");
        };

        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}

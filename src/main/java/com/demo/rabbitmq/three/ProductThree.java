package com.demo.rabbitmq.three;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @auther lkx
 * @create 2022-07-04 15:45
 * @Description:
 */
public class ProductThree {

    public static final String ACK_QUEUE_NAME = "ack_hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明队列
        boolean durable = true;
        channel.queueDeclare(ACK_QUEUE_NAME,durable,false,false,null);
        //在控制台输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //设置生产者发送消息为持久化消息（要求保存到磁盘上）保存到内存中
            channel.basicPublish("",ACK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息:" + message);
        }
    }
}

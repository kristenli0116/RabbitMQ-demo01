package com.demo.rabbitmq.two;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @auther lkx
 * @create 2022-07-04 15:04
 * @Description:
 */
public class Task01 {
    //队列名称
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        /**
         * 生成一个队列
         * 1. 队列名称
         * 2. 队列里面的消息是否持久化（磁盘） 默认情况下存储在内存中
         * 3. 该队列是否职工一个消费者进行消费  是否进行消息共享，true可以多个消费者消费
         * 4. 是否自动删除 最后一个消费者断开连接后 该队列是否自动删除 ture自动删除 false不自动闪粗
         * 5. 其他参数
         */
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            /**
             * 发送一个消息
             * 1. 发动到哪个交换机 默认
             * 2. 路由的key值是哪个 本次队列的名称
             * 3. 其他参数信息
             * 4. 发送消息的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));

        }
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String message = "hello world";

        channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));



    }
}

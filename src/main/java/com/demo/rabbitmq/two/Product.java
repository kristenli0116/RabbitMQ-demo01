package com.demo.rabbitmq.two;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

/**
 * @auther lkx
 * @create 2022-07-06 12:24
 * @Description:
 */
public class Product {
    public static final String QUEUE_NAME = "hello";

    public Product() {
    }

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        Scanner scanner = new Scanner(System.in);

        String message;
        while (scanner.hasNext()) {
            message = scanner.next();
            channel.basicPublish("", "hello", null, message.getBytes(StandardCharsets.UTF_8));
        }

        channel.queueDeclare("hello", false, false, false, null);
        message = "hello world";
        channel.basicPublish("", "hello", null, message.getBytes(StandardCharsets.UTF_8));
    }
}

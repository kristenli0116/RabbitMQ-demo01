package com.demo.rabbitmq.three;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

/**
 * @auther lkx
 * @create 2022-07-06 12:26
 * @Description:
 */
public class Product {
    public static final String ACK_QUEUE_NAME = "ack_hello";

    public Product() {
    }

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        boolean durable = true;
        channel.queueDeclare("ack_hello", durable, false, false, null);
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", "ack_hello", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息:" + message);
        }

    }
}

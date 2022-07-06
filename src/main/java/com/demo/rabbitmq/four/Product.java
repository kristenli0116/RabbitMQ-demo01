package com.demo.rabbitmq.four;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @auther lkx
 * @create 2022-07-06 12:28
 * @Description:
 */
public class Product {
    public static final int MESSAGE_COUNT = 1000;

    public Product() {
    }

    public static void main(String[] args) throws Exception {
        publishMessageAsync();
    }

    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, (Map) null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        for (int i = 0; i < 1000; ++i) {
            String message = i + "";
            channel.basicPublish("", queueName, (AMQP.BasicProperties) null, message.getBytes(StandardCharsets.UTF_8));
            boolean result = channel.waitForConfirms();
            if (result) {
                System.out.println("消息发送成功");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布1000条单独确认消息，耗时：" + (end - begin) + "ms");
    }

    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, (Map) null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        int batchNum = 100;

        for (int i = 0; i < 1000; ++i) {
            String message = i + "";
            channel.basicPublish("", queueName, (AMQP.BasicProperties) null, message.getBytes(StandardCharsets.UTF_8));
            if ((i + 1) % batchNum == 0) {
                channel.waitForConfirms();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布1000条批量确认消息，耗时：" + (end - begin) + "ms");
    }

    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, (Map) null);
        channel.confirmSelect();
        ConcurrentSkipListMap<Long, String> infoConfirmMap = new ConcurrentSkipListMap();
        long begin = System.currentTimeMillis();
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                ConcurrentNavigableMap<Long, String> headMap = infoConfirmMap.headMap(deliveryTag);
                headMap.clear();
            } else {
                infoConfirmMap.remove(deliveryTag);
            }

            System.out.println("确认的消息：" + deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = (String) infoConfirmMap.get(deliveryTag);
            System.out.println("未确认消息的编号：" + deliveryTag + "----未确认的消息" + message);
        };
        channel.addConfirmListener(ackCallback, nackCallback);

        for (int i = 0; i < 1000; ++i) {
            String message = "消息" + i;
            channel.basicPublish("", queueName, (AMQP.BasicProperties) null, message.getBytes(StandardCharsets.UTF_8));
            infoConfirmMap.put(channel.getNextPublishSeqNo(), message);
        }

        long end = System.currentTimeMillis();
        System.out.println("发布1000条批量确认消息，耗时：" + (end - begin) + "ms");
    }
}

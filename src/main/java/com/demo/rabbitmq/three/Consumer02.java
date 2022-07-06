package com.demo.rabbitmq.three;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @auther lkx
 * @create 2022-07-06 12:27
 * @Description:
 */
public class Consumer02 {
    public static final String ACK_QUEUE_NAME = "ack_hello";

    public Consumer02() {
    }

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("消费者2号等待接受消息时间较长");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }

            System.out.println("接收到消息：" + new String(message.getBody(), "UTF-8"));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者取消消费接口回调逻辑");
        };
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);
        boolean autoAck = false;
        channel.basicConsume("ack_hello", autoAck, deliverCallback, cancelCallback);
    }
}

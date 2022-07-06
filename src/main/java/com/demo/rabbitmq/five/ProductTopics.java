package com.demo.rabbitmq.five;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @auther lkx
 * @create 2022-07-06 12:29
 * @Description:
 */
public class ProductTopics {
    public static final String EXCHANGE_NAME = "topics_logs";

    public ProductTopics() {
    }

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        HashMap<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("abc.orange.rabbit", "被队列Q1，Q2接收到");
        bindingKeyMap.put("lazy.orange.123", "被队列Q1，Q2接收到");
        bindingKeyMap.put("abc.orange.123", "被队列Q1接收到");
        bindingKeyMap.put("lazy.123.rabbit", "被队列Q2接收到");
        bindingKeyMap.put("lazy.456.rabbit", "虽然满足两个绑定但只被Q2接收一次");
        bindingKeyMap.put("abc.789.xyz", "不匹配任何队列，会被丢弃");
        bindingKeyMap.put("abc.orange.xyz.rabbit", "四个单词队列不满足，会被丢弃");
        bindingKeyMap.put("lazy.orange.xyz.rabbit", "是四个单词，但匹配Q2");
        Iterator var3 = bindingKeyMap.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry<String, String> bindingKeyMapEntry = (Map.Entry<String, String>) var3.next();
            String routingKey = bindingKeyMapEntry.getKey();
            String message = bindingKeyMapEntry.getValue();
            channel.basicPublish("topics_logs", routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息： " + message);
        }

    }
}

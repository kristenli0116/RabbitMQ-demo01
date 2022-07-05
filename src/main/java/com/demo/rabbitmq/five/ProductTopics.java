package com.demo.rabbitmq.five;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther lkx
 * @create 2022-07-05 16:43
 * @Description: 主题模式（Topics）的生产者
 */
public class ProductTopics {
    public static final String EXCHANGE_NAME = "topics_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 绑定关系如下
         * Q1----->中间是orange的 带三个单词的字符串(*.orange.*)
         *
         * Q2----->最后一个是rabbit的三个单词(*.*.rabbit)
         *          第一个单词是lazy的多个单词(lazy.#)
         */
        HashMap<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("abc.orange.rabbit","被队列Q1，Q2接收到");
        bindingKeyMap.put("lazy.orange.123","被队列Q1，Q2接收到");
        bindingKeyMap.put("abc.orange.123","被队列Q1接收到");
        bindingKeyMap.put("lazy.123.rabbit","被队列Q2接收到");
        bindingKeyMap.put("lazy.456.rabbit","虽然满足两个绑定但只被Q2接收一次");
        bindingKeyMap.put("abc.789.xyz","不匹配任何队列，会被丢弃");
        bindingKeyMap.put("abc.orange.xyz.rabbit","四个单词队列不满足，会被丢弃");
        bindingKeyMap.put("lazy.orange.xyz.rabbit","是四个单词，但匹配Q2");

        for (Map.Entry<String, String> bindingKeyMapEntry : bindingKeyMap.entrySet()) {
            String routingKey = bindingKeyMapEntry.getKey();
            String message = bindingKeyMapEntry.getValue();

            //basicPublish(String exchange, String routingKey,BasicProperties props, byte[] body)
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息： " + message);
        }
    }
}

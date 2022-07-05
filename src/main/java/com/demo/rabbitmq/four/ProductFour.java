package com.demo.rabbitmq.four;

import com.demo.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @auther lkx
 * @create 2022-07-04 17:53
 * @Description:
 */
public class ProductFour {
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {

        //1. 单个确认
        //ProductFour.publishMessageIndividually();//发布1000条单独确认消息，耗时：1018ms
        //2. 批量确认
//        ProductFour.publishMessageBatch();//发布1000条批量确认消息，耗时：121ms
        //3. 异步确认
        ProductFour.publishMessageAsync();//发布1000条批量确认消息，耗时：77ms
                                        //发布1000条批量确认消息，耗时：52ms
    }

    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));

            //单个消息马上开始消息确认
            boolean result = channel.waitForConfirms();
            if (result) {
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "条单独确认消息，耗时：" + (end - begin) + "ms");
    }

    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        int batchNum = 100;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            if ((i + 1) % batchNum == 0) {
                channel.waitForConfirms();
            }
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "条批量确认消息，耗时：" + (end - begin) + "ms");
    }

    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的哈希表 适用于高并发情况下
         * 1. 键值对保存信息
         * 2. 通过键值批量删除信息
         * 3. 支持高并发
         */
        //infoConfirmMap 消息确认集合
        ConcurrentSkipListMap<Long,String> infoConfirmMap  = new ConcurrentSkipListMap<>();

        //开始时间
        long begin = System.currentTimeMillis();

        //消息确认成功的 回调函数
        /**
         * 1. deliveryTag 消息的标记
         * 2. multiple 是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            //判断是否是批量
            if (multiple){
                //2. 获得确认消息的编号
                ConcurrentNavigableMap<Long,String> headMap = infoConfirmMap.headMap(deliveryTag);
                //通过clear()全部清除
                headMap.clear();
            }else {
                //不是批量，通过remove清除
                infoConfirmMap.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        };

        //消息确认失败的 回调函数
        /**
         * 1. deliveryTag 消息的标记
         * 2. multiple 是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = infoConfirmMap.get(deliveryTag);
            System.out.println("未确认消息的编号：" + deliveryTag + "----未确认的消息" + message);
        };

        //开启消息的监听器 ，监听成功、失败的消息
        /**
         * 1. 监听成功的消息
         * 2. 监听失败的消息
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));

            //1. 将确认的消息放入 infoConfirmMap 集合中 ------》第二步 见：110行
            infoConfirmMap.put(channel.getNextPublishSeqNo(),message);
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "条批量确认消息，耗时：" + (end - begin) + "ms");
    }

}

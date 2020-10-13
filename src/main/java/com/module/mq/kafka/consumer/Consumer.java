package com.module.mq.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Consumer {
    private static Properties getConsumerProps(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.21.128:9092");
        /** group.id ??? */
        props.put("group.id", "test_3");
        props.put("session.timeout.ms", 30000);       // 如果其超时，将会可能触发rebalance并认为已经死去，重新选举Leader
        props.put("enable.auto.commit", "false");      // 关闭自动提交
        props.put("auto.offset.reset","earliest"); // 从最早的offset开始拉取，latest:从最近的offset开始消费
        /** client.id ??? */
        props.put("client.id", "consumer-2"); // 发送端id,便于统计
        props.put("max.poll.records","1000"); // 每次批量拉取条数
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    public void holdOnSync(){
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(getConsumerProps());
        List<String> topics = new ArrayList<>();
        topics.add("sun");
        consumer.subscribe(topics);
        for(;;){
            //拉取任务超时时间
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for(ConsumerRecord record : records){
                System.out.println("partition:" + record.partition());
                System.out.println("offset:" + record.offset());
                System.out.println("key:" + record.key());
                System.out.println("value:" + record.value());
            }
            //当前批次offset
            consumer.commitSync();
        }
    }
    public void holdOn(){
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(getConsumerProps());
        List<String> topics = new ArrayList<>();
        topics.add("sun");
        consumer.subscribe(topics);
        for(;;){
            //拉取任务超时时间
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for(ConsumerRecord record : records){
                System.out.println("partition:"+record.partition());
                System.out.println("offset:"+record.offset());
                System.out.println("key:"+record.key());
                System.out.println("value:"+record.value());
            }
            //当前批次offset
            consumer.commitAsync();
        }
    }
}

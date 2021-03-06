package com.module.mq.kafka.tool.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {


    private static Properties getProducerProps(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.21.128:9092");
        props.put("acks", "all"); // 发送所有ISR
        props.put("retries", 2); // 重试次数
        props.put("batch.size", 16384); // 批量发送大小
        props.put("buffer.memory", 33554432); // 缓存大小，根据本机内存大小配置
        props.put("linger.ms", 1000); // 发送频率
        props.put("client.id", "producer-syn-1"); // 发送端id,便于统计
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    public static void syncSend(){
        KafkaProducer<String, String> producer = new KafkaProducer<>(getProducerProps());
        for (int i = 0; i < 100; i++) {
            // 三个参数，topic,key：用户分配partition,value:发送的值
            ProducerRecord<String, String> record = new ProducerRecord<>("sun", "topic_" + i, "sun" + i);
            Future<RecordMetadata> metadataFuture = producer.send(record);
            RecordMetadata recordMetadata = null;
            try {
                recordMetadata = metadataFuture.get();//若无响应则阻塞
                System.out.println("发送成功！");
                System.out.println("topic:" + recordMetadata.topic());
                System.out.println("partition:" + recordMetadata.partition());
                System.out.println("offset:" + recordMetadata.offset());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("发送失败！");
                e.printStackTrace();
            }
        }
        producer.flush();
        producer.close();
    }

    public static void asyncSend(){
        KafkaProducer<String, String> producer = new KafkaProducer<>(getProducerProps());
        for (int i = 0; i < 100; i++) {
            // 三个参数，topic,key：用户分配partition,value:发送的值
            ProducerRecord<String, String> record = new ProducerRecord<>("test-syn", "topic_" + i, "test-syn-" + i);
            producer.send(record, (recordMetadata, e) -> {
                if(e != null){
                    System.out.println("发送失败！");
                    e.printStackTrace();
                }else{
                    System.out.println("发送成功！");
                    System.out.println("topic:"+recordMetadata.topic());
                    System.out.println("partition:"+recordMetadata.partition());
                    System.out.println("offset:"+recordMetadata.offset());
                }
            });
        }
        producer.flush();
        producer.close();
    }

    public static void main(String[] args) {


    }
}

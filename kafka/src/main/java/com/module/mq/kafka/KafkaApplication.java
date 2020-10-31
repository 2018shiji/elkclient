package com.module.mq.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Flink:
 * https://segmentfault.com/a/1190000018267168?utm_source=tag-newest
 * https://database.51cto.com/art/202010/628288.htm
 * https://blog.csdn.net/codragon/article/details/109288733
 */
@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }

}

package com.module.mq.kafka;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KafkaApplicationTests {
    Logger logger = LoggerFactory.getLogger(KafkaApplicationTests.class);
    @Test
    void contextLoads() {
        System.out.println("hello");
        logger.info("hello !!! my name is lizhuangjie");
    }

}

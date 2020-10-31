package com.module.mq.kafka;

import com.module.mq.kafka.product.rpcclient_log.LogExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KafkaApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testLogExtractor(){
        String logRecord = "[ctos][2020-10-22 17:48:28] [qsm+DKjPTc+ZMI4OIKzCdg==] [http-nio-8083-exec-1] [INFO]  [com.module.parser.reflect.FieldReflectForQueryParam] [msg]{label:'userID'\n" +
                " key:'userId'\n" +
                " value:''}\n" +
                " {label:'passWord'\n" +
                " key:'password'\n" +
                " value:''}\n" +
                " {label:'clientIP'\n" +
                " key:'clientIP'\n" +
                " value:''}";
        LogExtractor logExtractor = new LogExtractor(logRecord);
        logExtractor.getRpcLogPojo();
    }
}

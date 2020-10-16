package com.module.mq;

import com.module.mq.kafka.log.KFKStreamProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * https://www.cnblogs.com/balloon72/p/13177872.html
 * https://www.jianshu.com/p/acc8e86cc772
 * esHD可视化工具：https://www.cnblogs.com/Rawls/p/10079783.html
 * http://kafka.apache.org/26/documentation/streams/core-concepts
 * https://github.com/apache/kafka/blob/2.7/streams/examples/src/main/java/org/apache/kafka/streams
 */
@SpringBootApplication
public class ElkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElkApplication.class, args);

        KFKStreamProcessor.officialKafkaStream2();

    }

}

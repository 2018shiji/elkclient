package com.module.mq.kafka;

import com.module.mq.kafka.product.manual_log.LogOutput;
import com.module.mq.kafka.product.manual_log.LoggerUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
public class LogTest {
    Logger DB_SQL_LOG = LoggerUtils.Logger(LogOutput.SQL_OUTPUT);
    Logger OUTPUT_FILE_LOG = LoggerUtils.Logger(LogOutput.FILE_OUTPUT);

    @Test
    public void testLoggerOutput() {
        DB_SQL_LOG.info("DB SQL 打印进行中");
        OUTPUT_FILE_LOG.info("output file 打印进行中");
    }
}

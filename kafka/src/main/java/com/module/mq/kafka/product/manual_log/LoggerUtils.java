package com.module.mq.kafka.product.manual_log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils {
    /**
     * 打印到指定的日志文件下
     */
    public static Logger Logger(LogOutput desc) {
        return LoggerFactory.getLogger(desc.getLogFileName());
    }

}

package com.module.kafka.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils {
    public static <T> Logger Logger(Class<T> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 打印到指定的日志文件下
     */
    public static Logger Logger(LogOutput desc) {
        return LoggerFactory.getLogger(desc.getLogFileName());
    }
}

package com.module.mq.kafka.product.manual_log;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LogOutput {
    SQL_OUTPUT("sql-output"),
    FILE_OUTPUT("file-output");

    private String logFileName;

    public String getLogFileName() {
        return this.logFileName;
    }

}

package com.module.kafka.log;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LogOutput {
    DB_SQL("database-sql"),
    OUTPUT_FILE("output-file");

    private String logFileName;

    public String getLogFileName() {
        return this.logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public static LogOutput getLogOutput(String value) {
        LogOutput[] array = values();
        for(LogOutput item : array) {
            if(value.equals(item.logFileName)){
                return item;
            }
        }
        return null;
    }
}

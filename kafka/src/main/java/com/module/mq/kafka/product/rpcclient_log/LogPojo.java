package com.module.mq.kafka.product.rpcclient_log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogPojo {
    private String projectName;
    private String recordTime;
    private String dispatchID;
    private String threadName;
    private String logLevel;
    private String logClass;
    private String message;


    public LogPojo(List<String> logMarkItems, String message){
        this.projectName = logMarkItems.get(0);
        this.recordTime = logMarkItems.get(1);
        this.dispatchID = logMarkItems.get(2);
        this.threadName = logMarkItems.get(3);
        this.logLevel = logMarkItems.get(4);
        this.logClass = logMarkItems.get(5);
        this.message = message;
    }
}

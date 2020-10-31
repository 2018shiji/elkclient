package com.module.mq.kafka.product.rpcclient_log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogExtractor {
    private String logRecord;

    public LogExtractor(String logRecord){
        this.logRecord = logRecord;
    }

    public LogPojo getRpcLogPojo(){
        if(!logRecord.contains("msg"))
            return null;
        String logMark = logRecord.substring(0, logRecord.indexOf("msg")-2);
        System.out.println(logMark);

        String regex = "\\[(.*?)]";
        List<String> logMarkItems = new ArrayList();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(logMark);
        while(matcher.find()){
            logMarkItems.add(matcher.group(1));
        }

        String logMsg = logRecord.substring(logRecord.indexOf("msg")+4);
        System.out.println(logMsg);

        return new LogPojo(logMarkItems, logMsg);
    }
}

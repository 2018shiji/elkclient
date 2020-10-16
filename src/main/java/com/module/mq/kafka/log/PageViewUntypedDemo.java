package com.module.mq.kafka.log;

import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class PageViewUntypedDemo {
    public static void main(final String[] args) throws Exception {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pageview-untyped");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.21.128:9092");
        props.put(StreamsConfig.)
    }

}

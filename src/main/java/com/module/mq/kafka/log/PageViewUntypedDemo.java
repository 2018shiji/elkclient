package com.module.mq.kafka.log;

import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class PageViewUntypedDemo {
    public static void main(final String[] args) throws Exception {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pageview-untyped");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.21.128:9092");
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, );
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, )
    }

}

package com.module.mq.kafka.product.rpcclient_log;

import com.module.mq.kafka.product.manual_log.KFKStreamProcessor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class LogFilter {

    public static void groupServletMsg(){
        Properties props = KFKStreamProcessor.initKafkaStream();

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> logRecord = builder.stream("");

    }
}

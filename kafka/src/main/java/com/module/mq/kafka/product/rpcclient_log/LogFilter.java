package com.module.mq.kafka.product.rpcclient_log;

import com.module.mq.kafka.product.manual_log.KFKStreamProcessor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

@Component
public class LogFilter {

    public static void groupServletMsg(){
        Properties props = KFKStreamProcessor.initKafkaStream();

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> logRecordStream = builder.stream("topic_rpc_client");

        KStream<String, LogPojo> stringRpcLogPojoKStream = logRecordStream.flatMap(
                (KeyValueMapper<String, String, Iterable<KeyValue<String, LogPojo>>>) (key, value) -> {
                    LogPojo logPojo = new LogExtractor(value).getRpcLogPojo();
                    if(logPojo != null){
                        System.out.println(logPojo);
                        return Arrays.asList(KeyValue.pair(logPojo.getDispatchID(), logPojo));
                    } else {
                        return Arrays.asList(KeyValue.pair("no result", new LogPojo()));
                    }
                });

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
        KFKStreamProcessor.afterCare(kafkaStreams);
    }

    public static void main(String[] args){
        groupServletMsg();
    }

}

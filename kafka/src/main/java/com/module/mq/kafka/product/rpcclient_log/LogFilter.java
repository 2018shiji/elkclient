package com.module.mq.kafka.product.rpcclient_log;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.module.mq.kafka.product.manual_log.KFKStreamProcessor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
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

        KStream<String, RpcLogPojo> stringRpcLogPojoKStream = logRecordStream.flatMap(
                (KeyValueMapper<String, String, Iterable<KeyValue<String, RpcLogPojo>>>) (key, value) -> {
                    RpcLogPojo rpcLogPojo = new LogExtractor(value).getRpcLogPojo();
                    if(rpcLogPojo != null){
                        System.out.println(rpcLogPojo);
                        return Arrays.asList(KeyValue.pair(rpcLogPojo.getDispatchID(), rpcLogPojo));
                    } else {
                        return Arrays.asList(KeyValue.pair("no result", new RpcLogPojo()));
                    }
                });

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
        KFKStreamProcessor.afterCare(kafkaStreams);
    }

    public static void main(String[] args){
        groupServletMsg();
    }

}

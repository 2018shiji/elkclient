package com.module.kafka.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.Properties;

public class PageViewUntypedDemo {
    public static void main(final String[] args) throws Exception {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pageview-untyped");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.21.128:9092");
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, JsonTimestampExtractor.class);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        //从头开始消费
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final StreamsBuilder builder = new StreamsBuilder();
        final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
        final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
        final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);

        final Consumed<String, JsonNode> consumed = Consumed.with(Serdes.String(), jsonSerde);
        final KStream<String, JsonNode> views = builder.stream("streams-pageview-input", consumed);
        final KTable<String, JsonNode> users = builder.table("streams-userprofile-input", consumed);
        final KTable<String, String> userRegions = users.mapValues(record -> record.get("region").textValue());

        final KStream<JsonNode, JsonNode> regionCount = views
                .leftJoin(userRegions, (view, region) ->{
                    final ObjectNode jNode = JsonNodeFactory.instance.objectNode();
                    return (JsonNode)jNode.put("user", view.get("user").textValue())
                            .put("page", view.get("page").textValue())
                            .put("region", region == null ? "UNKNOWN" : region);
                })
                .map((user, viewRegion) -> new KeyValue<>(viewRegion.get("region").textValue(), viewRegion))
                .groupByKey(Grouped.with(Serdes.String(), jsonSerde))
                .windowedBy(TimeWindows.of(Duration.ofDays(7)).advanceBy(Duration.ofSeconds(1)))
                .count()
                .toStream()
                .map((key, value) -> {
                    final ObjectNode keyNode = JsonNodeFactory.instance.objectNode();
                    keyNode.put("window-start", key.window().start())
                            .put("region", key.key());

                    final ObjectNode valueNode = JsonNodeFactory.instance.objectNode();
                    valueNode.put("count", value);

                    return new KeyValue<>(keyNode, valueNode);
                });

        //write to the result topic
        regionCount.to("streams-pageviewstats-untyped-output", Produced.with(jsonSerde, jsonSerde));

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        //流存活时间
        Thread.sleep(5000L);
        streams.close();
    }

}

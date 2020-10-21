package com.module.mq.kafka.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.module.mq.kafka.json.pojo.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class PageViewTypedDemo {

    public static class JSONSerde<T extends JSONSerdeCompatible> implements Serializer<T>, Deserializer<T>, Serde<T> {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) { }

        @Override
        public void close() { }

        @Override
        public T deserialize(String s, byte[] bytes) {
            if(bytes == null){ return null; }

            try{
                return (T) OBJECT_MAPPER.readValue(bytes, JSONSerdeCompatible.class);
            } catch(final IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public byte[] serialize(String s, T t) {
            if(t == null){return null;}

            try{
                return OBJECT_MAPPER.writeValueAsBytes(t);
            } catch(final Exception e){
                throw new SerializationException("Error serializing JSON message", e);
            }
        }

        @Override
        public Serializer<T> serializer() {
            return this;
        }

        @Override
        public Deserializer<T> deserializer() {
            return this;
        }

    }

    public static void main(final String[] args){
        System.out.println("i am here");
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pageview-typed");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.21.128:9092");
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, JsonTimestampExtractor.class);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, JSONSerde.class);
        props.put(StreamsConfig.DEFAULT_WINDOWED_KEY_SERDE_INNER_CLASS, JSONSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JSONSerde.class);
        props.put(StreamsConfig.DEFAULT_WINDOWED_VALUE_SERDE_INNER_CLASS, JSONSerde.class);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, PageView> views = builder.stream("streams-pageview-input", Consumed.with(Serdes.String(), new JSONSerde<>()));
        final KTable<String, UserProfile> users = builder.table("streams-userprofile-input", Consumed.with(Serdes.String(), new JSONSerde<>()));
        final KStream<WPageViewByRegion, RegionCount> regionCount = views
                .leftJoin(users, (view, profile) -> {
                    System.out.println("---------------->" + view.user + "\t" + view.page +"\t" + profile.region);
                    final PageViewByRegion viewByRegion = new PageViewByRegion();
                    viewByRegion.user = view.user;
                    viewByRegion.page = view.page;

                    if(profile != null){
                        viewByRegion.region = profile.region;
                    }else{
                        viewByRegion.region = "UNKNOWN";
                    }
                    return viewByRegion;
                })
                .map((user, viewRegion) -> new KeyValue<>(viewRegion.region, viewRegion))
                .groupByKey(Grouped.with(Serdes.String(), new JSONSerde<>()))
                .windowedBy(TimeWindows.of(Duration.ofDays(7)).advanceBy(Duration.ofSeconds(1)))
                .count()
                .toStream()
                .map((key, value) -> {
                    System.out.println("------------------>" + key.window());
                    System.out.println("------------------>" + key.key());
                    final WPageViewByRegion wViewByRegion = new WPageViewByRegion();
                    wViewByRegion.windowStart = key.window().start();
                    wViewByRegion.region = key.key();

                    final RegionCount rCount = new RegionCount();
                    rCount.region = key.key();
                    rCount.count = value;
                    System.out.println(value);

                    return new KeyValue<>(wViewByRegion, rCount);
                });

        //write to the result topic
        regionCount.to("streams-pageviewstats-typed-output");

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-pipe-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);

    }
}

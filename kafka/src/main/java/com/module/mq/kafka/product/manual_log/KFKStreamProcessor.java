package com.module.mq.kafka.product.manual_log;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@Component
public class KFKStreamProcessor {

    public static Properties initKafkaStream(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "official-kfk-stream-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.21.128:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.putIfAbsent(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
        props.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return props;
    }

    public static void officialKafkaStream1() {
        Properties props = initKafkaStream();

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("streams-plaintext-input");
        KTable<String, Long> wordCounts = textLines
                .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
                .groupBy((key, word) -> word)
                .count();
        wordCounts.toStream().to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        afterCare(streams);
    }

    //processor demo
    public static void officialKafkaStream2() {

        Properties props = initKafkaStream();

        final Topology builder = new Topology();
        builder.addSource("Source", "streams-plaintext-input");
        builder.addProcessor("Process", new MyProcessorSupplier(), "Source");
        builder.addStateStore(Stores.keyValueStoreBuilder(Stores.inMemoryKeyValueStore("Counts"), Serdes.String(), Serdes.Integer()), "Process");
        builder.addSink("Sink", "streams-wordcount-output", "Process");

        KafkaStreams kafkaStreams = new KafkaStreams(builder, props);
        afterCare(kafkaStreams);
    }

    //temperature Demo
    public static void officialKafkaStream3() {
        Properties props = initKafkaStream();
        //threshold used for filtering max temperature values
        final int temperature_threshold = 20;
        //window size within which the filtering is applied
        final int temperature_window_size = 5;

        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, String> source = builder.stream("iot-temperature");
        final KStream<Windowed<String>, String> max = source
                //temperature values are sent without a key(null), so in order
                //to group and reduce them, a key is needed("temp" has been chosen)
                .selectKey((key, value) -> "temp")
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofSeconds(temperature_window_size)))
                .reduce(((value1, value2) -> {
                    if(Integer.parseInt(value1) > Integer.parseInt(value2)){
                        return value1;
                    }else{
                        return value2;
                    }
                }))
                .toStream()
                .filter((key, value) -> Integer.parseInt(value) > temperature_threshold);

        final Serde<Windowed<String>> windowedSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class);

        //need to override key serde to windowed<String> type
        max.to("iot-temperature-max", Produced.with(windowedSerde, Serdes.String()));

        afterCare(new KafkaStreams(builder.build(), props));
    }

    static class MyProcessorSupplier implements ProcessorSupplier<String, String> {
        @Override
        public Processor<String, String> get() {
            return new Processor<String, String>() {
                private ProcessorContext context;
                private KeyValueStore<String, Integer> kvStore;

                @Override
                public void init(ProcessorContext context) {
                    this.context = context;
                    this.context.schedule(Duration.ofSeconds(1), PunctuationType.STREAM_TIME, timestamp -> {
                        try(final KeyValueIterator<String, Integer> iter = kvStore.all()) {
                            System.out.println("-------" + timestamp + "--------");

                            while(iter.hasNext()) {
                                final KeyValue<String, Integer> entry = iter.next();
                                System.out.println("[" + entry.key + "," + entry.value + "]");
                                context.forward(entry.key, entry.value.toString());
                            }
                        }
                    });
                    this.kvStore = (KeyValueStore<String, Integer>) context.getStateStore("Counts");
                }

                @Override
                public void process(String key, String value) {
                    final String[] words = value.toLowerCase(Locale.getDefault()).split(" ");

                    for(final String word : words) {
                        final Integer oldValue = this.kvStore.get(word);

                        if(oldValue == null)
                            this.kvStore.put(word, 1);
                        else
                            this.kvStore.put(word, oldValue + 1);
                    }
                }

                @Override
                public void close() {

                }
            };
        }
    }

    public static void afterCare(KafkaStreams streams){
        final CountDownLatch latch = new CountDownLatch(1);

        //attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook"){
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e){
            System.exit(1);
        }

        System.exit(0);
    }

    public void processHttpRequest() {

    }

    public void processHttpResponse() {

    }

    public void processDatabaseSQL() {

    }

    public void processDispatchStack() {

    }

    public void processResultFile() {

    }
}

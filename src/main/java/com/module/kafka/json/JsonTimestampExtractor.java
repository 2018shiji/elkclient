package com.module.kafka.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.module.kafka.json.pojo.PageView;
import com.module.kafka.json.pojo.UserProfile;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class JsonTimestampExtractor implements TimestampExtractor {
    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        if(record.value() instanceof PageView){
            return ((PageView) record.value()).timestamp;
        }

        if(record.value() instanceof UserProfile){
            return ((UserProfile) record.value()).timestamp;
        }

        if(record.value() instanceof JsonNode){
            return ((JsonNode) record.value()).get("timestamp").longValue();
        }

        return 0;
//        throw new IllegalArgumentException("JsonTimestampExtractor cannot recognize the record value " + record.value());
    }
}

package com.module.mq.kafka.json.pojo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.module.mq.kafka.json.pojo.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "-t")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PageView.class, name = "pv"),
        @JsonSubTypes.Type(value = UserProfile.class, name = "up"),
        @JsonSubTypes.Type(value = PageViewByRegion.class, name = "pvbr"),
        @JsonSubTypes.Type(value = WPageViewByRegion.class, name = "wpvbr"),
        @JsonSubTypes.Type(value = RegionCount.class, name = "rc")
})
public interface JSONSerdeCompatible {

}

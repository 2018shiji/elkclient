package com.module.kafka.json.pojo;

import com.module.kafka.json.JSONSerdeCompatible;

public class RegionCount implements JSONSerdeCompatible {
    public long count;
    public String region;
}

package com.module.kafka.json.pojo;

import com.module.kafka.json.JSONSerdeCompatible;

public class WPageViewByRegion implements JSONSerdeCompatible {
    public long windowStart;
    public String region;
}

package com.module.kafka.json.pojo;

import com.module.kafka.json.JSONSerdeCompatible;

public class UserProfile implements JSONSerdeCompatible {
    public String region;
    public Long timestamp;
}

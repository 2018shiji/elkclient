package com.module.kafka.json.pojo;

import com.module.kafka.json.JSONSerdeCompatible;

public class PageView implements JSONSerdeCompatible {
    public String user;
    public String page;
    public Long timestamp;
}

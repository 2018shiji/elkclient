package com.module.kafka.json.pojo;

import com.module.kafka.json.JSONSerdeCompatible;

public class PageViewByRegion implements JSONSerdeCompatible {
    public String user;
    public String page;
    public String region;
}

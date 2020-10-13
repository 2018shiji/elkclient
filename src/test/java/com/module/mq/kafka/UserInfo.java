package com.module.mq.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private String name;
    private int age;
}

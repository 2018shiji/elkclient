package com.module.mq.kafka.product.manual_log;

import java.util.Date;

/**
 * 日志事件模型，需保持必要的扩展性，并将数据映射到数据库schema的各个字段中
 * who: 访客标识、设备指纹、登录ID
 * when: 事件发生事件、上报时间
 * where: 设备环境、网络环境、业务环境
 * what: 事件标识、事件参数
 */
public class LogEventFormat {
    private String caller;
    private Date createTime;
    private String environment;
    private String event;
}

package com.module.mq.kafka;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.indices.DeleteAliasRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

public class ESOperator {
    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;
    private static final String ES_INDEX = "index";

    @Test//创建索引
    public void createIndex() throws IOException {
        // 1. 创建索引
        CreateIndexRequest index = new CreateIndexRequest(ES_INDEX);
        // 2. 客户端执行请求, 请求后获得响应
        CreateIndexResponse response = client.indices().create(index, RequestOptions.DEFAULT);
        // 3. 打印结果
        System.out.println(response.toString());
    }

    @Test//测试索引是否存在
    public void exitIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest(ES_INDEX);
        boolean exist = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("索引是否存在： " + exist);
    }

    @Test//删除索引
    public void deleteIndex() throws IOException {
        DeleteAliasRequest request = new DeleteAliasRequest(ES_INDEX, "alias");
        AcknowledgedResponse response = client.indices().deleteAlias(request, RequestOptions.DEFAULT);
        System.out.println("索引是否删除： " + response);
    }

    @Test//创建文档
    public void createDocument() throws IOException {
        //创建对象
        UserInfo userInfo = new UserInfo("张三", 12);
        //创建请求
        IndexRequest request = new IndexRequest(ES_INDEX);
        //规则
        request.id("1").timeout(TimeValue.timeValueSeconds(1));
        //将数据放到请求中
        request.source(JSON.toJSONString(userInfo), XContentType.JSON);
        //客户端发送请求, 获取响应结果
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println( response.toString());
        System.out.println(response.status());
    }

    @Test//判断文档是否存在
    public void exitDocument() throws IOException {
        GetRequest request = new GetRequest(ES_INDEX, "1");
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none");

        boolean exist = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exist);
    }

    @Test//获取文档信息
    public void getDocument() throws IOException {
        GetRequest request = new GetRequest(ES_INDEX, "1");

    }
}

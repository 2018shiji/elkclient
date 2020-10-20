package com.module.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.indices.DeleteAliasRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Component
public class ESOperator {
    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;
    private static final String ES_INDEX = "index";

    //创建索引 == 创建数据库
    public void createIndex() throws IOException {
        // 1. 创建索引
        CreateIndexRequest index = new CreateIndexRequest(ES_INDEX);
        // 2. 客户端执行请求, 请求后获得响应
        CreateIndexResponse response = client.indices().create(index, RequestOptions.DEFAULT);
        // 3. 打印结果
        System.out.println("创建索引的结果： " + response.toString());
    }

    //测试索引是否存在 == 测试数据库是否存在
    public void exitIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest(ES_INDEX);
        boolean exist = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("索引是否存在： " + exist);
    }

    //删除索引 == 删除数据库
    public void deleteIndex() throws IOException {
        DeleteAliasRequest request = new DeleteAliasRequest(ES_INDEX, "alias");
        AcknowledgedResponse response = client.indices().deleteAlias(request, RequestOptions.DEFAULT);
        System.out.println("索引是否删除： " + response);
    }

    //创建文档 == 创建一行数据
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
        System.out.println("创建文档的结果： " + response.toString());
        System.out.println(response.status());
    }

    //判断文档是否存在 == 判断行数据是否存在
    public void exitDocument() throws IOException {
        GetRequest request = new GetRequest(ES_INDEX, "1");
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none");

        boolean exist = client.exists(request, RequestOptions.DEFAULT);
        System.out.println("文档是否存在： " + exist);
    }

    //获取文档信息 == 获取行数据
    public void getDocument() throws IOException {
        GetRequest request = new GetRequest(ES_INDEX, "1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println("获取到的结果： " + response.getSourceAsString());
    }

    //更新文档 == 更新行数据
    public void updateDocument() throws IOException {
        UserInfo userInfo = new UserInfo("李四", 12);

        UpdateRequest request = new UpdateRequest(ES_INDEX, "1");
        request.timeout("1s");

        request.doc(JSON.toJSONString(userInfo), XContentType.JSON);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println("更新的结果： " + response.status());
    }

    //删除文档 == 删除行数据
    public void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest(ES_INDEX, "1");
        request.timeout("1s");

        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println("删除的结果： " + response.status());
    }

    //批量添加 == 批量添加行数据
    public void bulkDocument() throws IOException {
        BulkRequest request = new BulkRequest();
        request.timeout("10s");

        ArrayList<UserInfo> userInfos = new ArrayList();
        userInfos.add(new UserInfo("李四",1));
        userInfos.add(new UserInfo("李四",2));
        userInfos.add(new UserInfo("李四",3));
        userInfos.add(new UserInfo("李四",4));
        userInfos.add(new UserInfo("李四",5));
        userInfos.add(new UserInfo("李四",6));
        userInfos.add(new UserInfo("李四",7));

        //进行批处理请求
        for(int i = 0; i < userInfos.size(); i++){
            request.add(
                    new IndexRequest(ES_INDEX)
                    .id("" + (i+1))
                    .source(JSON.toJSONString(userInfos.get(i)), XContentType.JSON)
            );
        }

        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println("批量插入的结果： " + response.hasFailures());
    }

    //查询 == 查询行数据
    public void searchDocument() throws IOException {
        SearchRequest request = new SearchRequest(ES_INDEX);
        //构建搜索条件
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        //查询条件使用QueryBuilders工具来实现
        //QueryBuilders.termQuery 精准查询
        //QueryBuilders.matchAllQuery() 匹配全部
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("name", "李四");
        searchBuilder.query(matchQuery);
        searchBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        request.source(searchBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        System.out.println("查询出的结果： " + JSON.toJSONString(response.getHits()));
    }
}

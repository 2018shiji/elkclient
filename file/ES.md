| MySQL                   | Elastic Search        |
| ----------------------- | --------------------- |
| Database                | Index                 |
| Table                   | Type                  |
| Row                     | Document              |
| Column                  | Field                 |
| Schema                  | Mapping               |
| Index                   | Everything is indexed |
| SQL                     | Query DSL             |
| SELECT * FROM table ... | GET http://...        |
| UPDATE table SET ...    | PUT http://...        |

ES: 分布式实时文件存储 + 实时分析的分布式搜索引擎，没有事务概念，无法恢复被删除的数据

```
docker run -d --name es -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.6.2
docker exec -it es /bin/bash
cd config
vi elasticsearch.yml
# 加入跨域配置
http.cors.enabled:true
http.cors.allow-origin:"*"
    
esHD可视化工具：https://www.cnblogs.com/Rawls/p/10079783.html
```

Elk安装教程：

https://segmentfault.com/a/1190000019872317?utm_source=tag-newest

https://www.cnblogs.com/xiejava/p/12505253.html

```
logstash安装史：
sudo apt install ruby
gem -v
检查并修改镜像源:
gem sources -l
gem sources --add https://gems.ruby-china.com/ --remove https://rubygems.org/
查看修改结果：cat ~/.gemrc
开启gem server: gem server

docker pull logstash:7.6.2
docker run --name logstash -d -p 5044:5044 -p 9600:9600 logstash:7.6.2
......
需要对logstash-sample.conf中的host进行修改: 192.168.21.128:9200
curl ✔ （curl http://localhost:9092）
logstash查看已安装的插件：
docker exec -it logstash /bin/bash
cd bin
./logstash-plugin list
```

```
物联网：https://github.com/phodal/awesome-iot#ai
码头自动化：
https://www.cnki.net/KCMS/detail/detail.aspx?filename=GKKJ202007002&dbname=cjfdtotal&dbcode=CJFD&v=MDI5OTVoVzc3QUlpYkFaTEc0SE5ITXFJOUZab1I2RGc4L3poWVU3enNPVDNpUXJSY3pGckNVUjdxZVorZHFGeS8=
港口智能照明：
https://www.cnki.net/KCMS/detail/detail.aspx?filename=GKKJ202009002&dbname=cjfdtotal&dbcode=CJFD&v=MDQ4MjdVN3pzT1QzaVFyUmN6RnJDVVI3cWVaK2RxRnl6aFdyckpJaWJBWkxHNEhOSE1wbzlGWm9SNkRnOC96aFk=
日志埋点：
https://blog.csdn.net/jackwang1780/article/details/107643221?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~all~sobaiduend~default-2-107643221.nonecase&utm_term=kafka%E6%97%A5%E5%BF%97%E5%9F%8B%E7%82%B9%20logstash&spm=1000.2123.3001.4430
https://blog.csdn.net/jessicaiu/article/details/82348115
kafka stream:
https://github.com/apache/kafka/blob/2.7/streams/examples/src/main/java/org/apache/kafka/streams/examples/wordcount/WordCountProcessorDemo.java
```

```
netstat -nptl
cd /usr
ls
docker-compose up
topic_rpc_client
test_topic_5_3
```


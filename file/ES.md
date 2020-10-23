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

```
MySQL中建立多张表，用于记录各工程打印的日志，另外建立一些数据库模型，用于模拟分布式事务，进而选取合适的解决方案。
https://csdnnews.blog.csdn.net/article/details/108934857
https://blog.csdn.net/qq_36963950/article/details/108905277
https://mp.weixin.qq.com/s?__biz=MjM5MjAwODM4MA%3D%3D&chksm=bea7d41489d05d0219d5a12b56e970473e4e61241403e5f0e53f80a0bfe790149ec599c65da4&idx=3&mid=2650777031&scene=21&sn=bf7483c61af814601023fac1f1f0972d#wechat_redirect
MySQL中spring quartz定时任务持久化落地。
服务调用之间的失败响应，重试机制等，服务的自动部署（Jenkins），多线程编程（无状态）
```

```
cd /usr
ls
docker-compose up(启动zookeeper和Kafka)
```

Jenkins：

https://www.jenkins.io/zh/doc/pipeline/tour/getting-started/

SQL语句练习：

https://www.jianshu.com/p/476b52ee4f1b

https://www.cnblogs.com/Hackerman/p/10934413.html

```sql
1.查询“01”课程比“02”课程成绩高的学生的信息及课程分数，因为需要全部的学生信息，则需要在sc表中得到符合条件的sid后与student表进行join
select * from Student RIGHT JOIN(
	select t1.SId, class1, class2 from
		(select SID, score as class1 from sc where sc.CID = '01') as t1,
    	(select SID, score as class2 from sc where sc.CID = '02') as t2
    where t1.SID = t2.SID AND t1.class1 > t2.class2
)r
on Student.SID = r.SId;

select * from (
    select t1.SId, class1, class2 from
    	(select SId, score as class1 FROM sc WHERE sc.CId = '01') as t1,
    	(select SId, score as class2 FROM sc WHERE sc.CId = '02') as t2
    where t1.SId = t2.SId and t1.class1 > t2.class2
)r
LEFT JOIN Student
on Studer.SId = r.SId;
```

```sql
2.查询同时存在“01”课程和“02”课程的情况
select * from
	(select * from sc where sc.CId = "01") as t1,
	(select * from sc where sc.CId = "02") as t2
where t1.SId = t2.SId;
```

```sql
3.查询所有存在的“01”课程以及可能存在的“02”课程（“02”课程不存在时显示为null）
select * from
(select * from sc where sc.CId = "01") as t1
left join
(select * from sc where sc.CId = "02") as t2
on t1.SId = t2.SId;
```

```sql
4.查询不存在“01”课程但存在“02”课程的情况
select * from sc
where sc.SId not in (
	select SId from sc where sc.CId = '01'
)
AND sc.CId = '02';
```

```sql
5.查询平均成绩大于等于60分的同学的学生编号和学生姓名和平均成绩
这里只用根据学生ID把成绩分组，对分组中的score求平均值，最后在选取
```


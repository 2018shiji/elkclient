package com.module.kafka;

import com.module.kafka.log.KFKStreamProcessor;
import com.module.es.ESOperator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ElkApplicationTests {
    Logger logger = LoggerFactory.getLogger(ElkApplicationTests.class);

    @Test
    void contextLoads() {
        System.out.println("hello");
        logger.info("hello !!! my name is lizhuangjie");
    }

    @Autowired
    ESOperator esOperator;

    @Test
    void testCreateIndex() throws IOException {
        esOperator.createIndex();
    }

    @Test
    void testExitIndex() throws IOException {
        esOperator.exitIndex();
    }

    @Test
    void testDeleteIndex() throws IOException {
        esOperator.deleteIndex();
    }

    @Test
    void testCreateDocument() throws IOException {
        esOperator.createDocument();
    }

    @Test
    void testExitDocument() throws IOException {
        esOperator.exitDocument();
    }

    @Test
    void testGetDocument() throws IOException {
        esOperator.getDocument();
    }

    @Test
    void testUpdateDocument() throws IOException {
        esOperator.updateDocument();
    }

    @Test
    void testDeleteDocument() throws IOException {
        esOperator.deleteDocument();
    }

    @Test
    void testBulkDocument() throws IOException {
        esOperator.bulkDocument();
    }

    @Test
    void testSearchDocument() throws IOException {
        esOperator.searchDocument();
    }

    @Autowired
    KFKStreamProcessor stream;
    @Test
    void testOfficialKafkaStream(){
        stream.officialKafkaStream1();
    }
}

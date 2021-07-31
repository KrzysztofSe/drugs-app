package com.krzysztofse.drugs.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.krzysztofse.drugs.drugs.repository.model.DrugDocument;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class IntegrationTestBase {

    @Autowired
    public WireMockServer wireMockServer;

    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    public ObjectMapper objectMapper;

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
        mongoTemplate.dropCollection(DrugDocument.class);
    }
}

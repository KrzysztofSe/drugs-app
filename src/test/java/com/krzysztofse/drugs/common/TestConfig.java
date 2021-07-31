package com.krzysztofse.drugs.common;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.krzysztofse.drugs.fda.gateway.FdaGateway;
import com.krzysztofse.drugs.fda.gateway.FdaQueryBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TestConfig {

    @Bean
    public WireMockServer wireMockServer() {
        final WireMockServer server = new WireMockServer(new WireMockConfiguration().dynamicPort());
        server.start();
        return server;
    }

    @Bean
    @Primary
    public FdaGateway fdaGateway(
            final RestTemplate restTemplate,
            final FdaQueryBuilder fdaQueryBuilder,
            final WireMockServer wireMockServer) {
        return new FdaGateway(restTemplate, fdaQueryBuilder,
                String.format("http://localhost:%s", wireMockServer.port()));
    }
}

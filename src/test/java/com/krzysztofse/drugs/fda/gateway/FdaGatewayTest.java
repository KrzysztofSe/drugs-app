package com.krzysztofse.drugs.fda.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.krzysztofse.drugs.fda.FdaFixture;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResult;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResultList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class FdaGatewayTest {

    private final static WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final FdaGateway fdaGateway = new FdaGateway(
            new RestTemplateBuilder().build(),
            new FdaQueryBuilder(),
            String.format("http://localhost:%s", wireMockServer.port()));

    private final Fixture fixture = new Fixture();

    @BeforeAll
    public static void beforeAll() {
        wireMockServer.start();
    }

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    @AfterAll
    public static void afterAll() {
        wireMockServer.shutdown();
    }

    @Test
    public void shouldGetDataByApplicationNumber() throws Exception {
        wireMockServer.stubFor(get(urlPathEqualTo(fixture.fdaRootUrl))
                .withQueryParam("search", equalTo(fixture.applicationNumberSearch))
                .withQueryParam("limit", equalTo(fixture.applicationNumberLimit))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(objectMapper.writeValueAsString(fixture.resultList))));

        Optional<FdaDrugResult> result = fdaGateway.getDrugDataByApplicationNumber(fixture.applicationNumber);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(fixture.result);

        wireMockServer.verify(getRequestedFor(urlPathEqualTo(fixture.fdaRootUrl))
                .withQueryParam("search", equalTo(fixture.applicationNumberSearch))
                .withQueryParam("limit", equalTo(fixture.applicationNumberLimit)));
    }

    @Test
    public void shouldReturnEmptyIfNotFound() {
        wireMockServer.stubFor(get(urlPathEqualTo(fixture.fdaRootUrl))
                .withQueryParam("search", equalTo(fixture.applicationNumberSearch))
                .withQueryParam("limit", equalTo(fixture.applicationNumberLimit))
                .willReturn(aResponse()
                        .withStatus(404)));

        Optional<FdaDrugResult> result = fdaGateway.getDrugDataByApplicationNumber(fixture.applicationNumber);

        assertThat(result.isPresent()).isFalse();

        wireMockServer.verify(getRequestedFor(urlPathEqualTo(fixture.fdaRootUrl))
                .withQueryParam("search", equalTo(fixture.applicationNumberSearch))
                .withQueryParam("limit", equalTo(fixture.applicationNumberLimit)));
    }

    @Test
    public void shouldGetDataList() throws Exception {
        wireMockServer.stubFor(get(urlPathEqualTo(fixture.fdaRootUrl))
                .withQueryParam("search", equalTo(fixture.requestSearch))
                .withQueryParam("skip", equalTo(fixture.request.getPaging().getSkip().toString()))
                .withQueryParam("limit", equalTo(fixture.request.getPaging().getPageSize().toString()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(objectMapper.writeValueAsString(fixture.searchResultList))));

        Optional<FdaDrugResultList> result = fdaGateway.getDrugData(fixture.request);

        assertThat(result.isPresent()).isTrue();

        FdaDrugResultList list = result.get();

        assertThat(list.getResults()).hasSize(2);
        assertThat(list.getMeta().getResults().getTotal()).isEqualTo(2);

        assertThat(list.getResults().get(0)).usingRecursiveComparison().isEqualTo(fixture.result);
        assertThat(list.getResults().get(1)).usingRecursiveComparison().isEqualTo(fixture.result2);

        wireMockServer.verify(getRequestedFor(urlPathEqualTo(fixture.fdaRootUrl))
                .withQueryParam("search", equalTo(fixture.requestSearch))
                .withQueryParam("skip", equalTo(fixture.request.getPaging().getSkip().toString()))
                .withQueryParam("limit", equalTo(fixture.request.getPaging().getPageSize().toString())));
    }

    public static class Fixture extends FdaFixture {
        final String applicationNumber = "123";
        final String applicationNumber2 = "456";
        final String applicationNumberLimit = "1";
        final String applicationNumberSearch = "(application_number:\"123\")";
        final FdaDrugSearchRequest request = buildRequest(List.of("manufacturer"), null, 0, 2);
        final String requestSearch = "(openfda.manufacturer_name:\"manufacturer\")";
        final FdaDrugResult result = buildFdaDrug(applicationNumber);
        final FdaDrugResult result2 = buildFdaDrug(applicationNumber2);
        final FdaDrugResultList resultList = buildFdaDrugResultList(List.of(result));
        final FdaDrugResultList searchResultList = buildFdaDrugResultList(List.of(result, result2));

    }


}
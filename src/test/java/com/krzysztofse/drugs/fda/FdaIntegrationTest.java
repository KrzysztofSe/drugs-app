package com.krzysztofse.drugs.fda;

import com.krzysztofse.drugs.common.IntegrationTest;
import com.krzysztofse.drugs.common.IntegrationTestBase;
import com.krzysztofse.drugs.common.ResponsePage;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugResponse;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResult;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResultList;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class FdaIntegrationTest extends IntegrationTestBase {

    private final Fixture fixture = new Fixture();

    @Test
    public void shouldReturnDrugsFromFda() throws Exception {
        wireMockServer.stubFor(get(urlPathEqualTo(fixture.fdaRootUrl))
                .withQueryParam("search", equalTo(fixture.requestSearch))
                .withQueryParam("skip", equalTo(fixture.request.getPaging().getSkip().toString()))
                .withQueryParam("limit", equalTo(fixture.request.getPaging().getPageSize().toString()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(objectMapper.writeValueAsString(fixture.searchResultList))));

        ResponseEntity<ResponsePage<FdaDrugResponse>> response = restTemplate.exchange(
                fixture.endpointUrl,
                HttpMethod.POST,
                new HttpEntity<>(fixture.request),
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSize()).isEqualTo(2);

        List<FdaDrugResponse> responseList = response.getBody().toList();
        assertThat(responseList.get(0)).usingRecursiveComparison().isEqualTo(fixture.response1);
        assertThat(responseList.get(1)).usingRecursiveComparison().isEqualTo(fixture.response2);
    }

    public static class Fixture extends FdaFixture {
        final String applicationNumber = "123";
        final String applicationNumber2 = "456";
        final FdaDrugSearchRequest request = buildRequest(manufacturerName, null, 0, 2);
        final String requestSearch = "(openfda.manufacturer_name:\"man1\")";
        final FdaDrugResult result = buildFdaDrug(applicationNumber);
        final FdaDrugResult result2 = buildFdaDrug(applicationNumber2);
        final FdaDrugResultList searchResultList = buildFdaDrugResultList(List.of(result, result2));
        final FdaDrugResponse response1 = buildFdaDrugResponse(applicationNumber);
        final FdaDrugResponse response2 = buildFdaDrugResponse(applicationNumber2);
    }
}
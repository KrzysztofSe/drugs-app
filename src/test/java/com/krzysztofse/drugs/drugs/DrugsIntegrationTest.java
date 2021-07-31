package com.krzysztofse.drugs.drugs;

import com.krzysztofse.drugs.common.IntegrationTest;
import com.krzysztofse.drugs.common.IntegrationTestBase;
import com.krzysztofse.drugs.common.request.PageResponse;
import com.krzysztofse.drugs.drugs.controller.model.DrugListRequest;
import com.krzysztofse.drugs.drugs.controller.model.DrugResponse;
import com.krzysztofse.drugs.drugs.controller.model.DrugSaveRequest;
import com.krzysztofse.drugs.fda.FdaFixture;
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
public class DrugsIntegrationTest extends IntegrationTestBase {

    private final Fixture fixture = new Fixture();

    @Test
    public void shouldSave() {
        restTemplate.put(fixture.drugsUrl, fixture.saveRequest1);

        ResponseEntity<DrugResponse> response = restTemplate.getForEntity(fixture.drugsPath, DrugResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .extracting("applicationNumber", "manufacturerName", "substanceName", "productNumbers")
                .containsExactly(fixture.applicationNumber1, fixture.manufacturerName, fixture.substanceName, fixture.productNumbers);
    }

    @Test
    public void shouldSaveFromFda() throws Exception {
        wireMockServer.stubFor(get(urlPathEqualTo(fixture.fdaRootUrl))
                .withQueryParam("search", equalTo(fixture.applicationNumberSearch))
                .withQueryParam("limit", equalTo(fixture.applicationNumberLimit))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(objectMapper.writeValueAsString(fixture.resultList))));

        restTemplate.put(fixture.drugsPath, fixture.saveRequest1);

        ResponseEntity<DrugResponse> response = restTemplate.getForEntity(fixture.drugsPath, DrugResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .extracting("applicationNumber", "manufacturerName", "substanceName", "productNumbers")
                .containsExactly(fixture.applicationNumber1, fixture.manufacturerName, fixture.substanceName, fixture.productNumbers);
    }

    @Test
    public void shouldList() {
        restTemplate.put(fixture.drugsUrl, fixture.saveRequest1);
        restTemplate.put(fixture.drugsUrl, fixture.saveRequest2);

        ResponseEntity<PageResponse<DrugResponse>> result = restTemplate.exchange(
                fixture.drugsListUrl,
                HttpMethod.POST,
                new HttpEntity<>(fixture.listRequest),
                new ParameterizedTypeReference<>() {});

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getTotal()).isEqualTo(2);
        List<DrugResponse> responseList = result.getBody().getContent();
        assertThat(responseList.get(0))
                .extracting("applicationNumber").isEqualTo(fixture.applicationNumber1);
        assertThat(responseList.get(1))
                .extracting("applicationNumber").isEqualTo(fixture.applicationNumber2);
    }

    public static class Fixture extends DrugsFixture {
        public final String applicationNumber1 = "3436";
        public final String applicationNumber2 = "3456";
        public final String drugsPath = String.format("%s/%s", drugsUrl, applicationNumber1);
        public final String applicationNumberLimit = "1";
        public final String applicationNumberSearch = "(application_number:\"3436\")";
        public final DrugSaveRequest saveRequest1 = buildDrugSaveRequest(applicationNumber1);
        public final DrugSaveRequest saveRequest2 = buildDrugSaveRequest(applicationNumber2);
        public final DrugListRequest listRequest = buildDrugListRequest(0, 2);
        public final FdaDrugResult result1 = new FdaFixture().buildFdaDrug(applicationNumber1);
        public final FdaDrugResultList resultList = new FdaFixture().buildFdaDrugResultList(List.of(result1));
    }
}

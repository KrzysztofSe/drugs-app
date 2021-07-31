package com.krzysztofse.drugs.fda.service;

import com.krzysztofse.drugs.fda.FdaFixture;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugResponse;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResult;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FdaMapperTest {

    private final FdaMapper cut = new FdaMapper();

    private final Fixture fixture = new Fixture();

    @Test
    public void shouldMapResponse() {
        final FdaDrugResponse result = cut.mapResponse(fixture.fdaDrugDto);
        assertThat(result)
                .extracting(fixture.fields)
                .containsExactly(fixture.values);
    }

    @Test
    public void shouldMapDto() {
        final FdaDrugDto result = cut.mapDto(fixture.fdaDrugResult);
        assertThat(result)
                .extracting(fixture.fields)
                .containsExactly(fixture.values);
    }

    public static class Fixture extends FdaFixture {
        public final String applicationNumber = "456";
        public final String[] fields = {"applicationNumber", "manufacturerName", "brandName", "substanceName", "productNumbers"};
        public final Object[] values = {applicationNumber, manufacturerName, brandName, substanceName, productNumbers};
        public final FdaDrugDto fdaDrugDto = buildFdaDrugDto(applicationNumber);
        public final FdaDrugResult fdaDrugResult = buildFdaDrug(applicationNumber);
    }

}
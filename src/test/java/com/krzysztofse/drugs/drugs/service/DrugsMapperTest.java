package com.krzysztofse.drugs.drugs.service;

import com.krzysztofse.drugs.drugs.DrugsFixture;
import com.krzysztofse.drugs.drugs.controller.model.DrugResponse;
import com.krzysztofse.drugs.drugs.controller.model.DrugSaveRequest;
import com.krzysztofse.drugs.drugs.repository.model.DrugDocument;
import com.krzysztofse.drugs.drugs.service.model.DrugDto;
import com.krzysztofse.drugs.fda.FdaFixture;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DrugsMapperTest {

    private final DrugsMapper cut = new DrugsMapper();

    private final Fixture fixture = new Fixture();

    @Test
    public void shouldMapResponse() {
        final DrugResponse result = cut.mapResponse(fixture.drugDto);
        assertThat(result)
                .extracting(fixture.responseFields)
                .containsExactly(fixture.values);
    }

    @Test
    public void shouldMapDocumentFromDto() {
        final DrugDocument result = cut.mapDocument(fixture.fdaDrugDto);
        assertThat(result)
                .extracting(fixture.fields)
                .containsExactly(fixture.values);
    }

    @Test
    public void shouldMapDocumentFromSaveRequest() {
        final DrugDocument result = cut.mapDocument(fixture.drugSaveRequest);
        assertThat(result)
                .extracting(fixture.fields)
                .containsExactly(fixture.values);
    }

    @Test
    public void shouldMapDto() {
        final DrugDto result = cut.mapDto(fixture.drugDocument);
        assertThat(result)
                .extracting(fixture.fields)
                .containsExactly(fixture.values);
    }


    public static class Fixture extends DrugsFixture {
        public final String applicationNumber = "235";

        private final String[] fields = { "id", "manufacturerName", "substanceName", "productNumbers" };
        private final String[] responseFields = { "applicationNumber", "manufacturerName", "substanceName", "productNumbers" };
        private final Object[] values = { applicationNumber, manufacturerName, substanceName, productNumbers };

        public final DrugDto drugDto = buildDrugDto(applicationNumber);
        public final DrugDocument drugDocument = buildDrugDocument(applicationNumber);
        public final FdaDrugDto fdaDrugDto = new FdaFixture().buildFdaDrugDto(applicationNumber);
        public final DrugSaveRequest drugSaveRequest = buildDrugSaveRequest(applicationNumber);
    }
}
package com.krzysztofse.drugs.fda.gateway;

import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FdaQueryBuilderTest {

    private final FdaQueryBuilder cut = new FdaQueryBuilder();

    @Test
    public void shouldBuildSearchQuery() {
        final List<String> manufacturerName = List.of("manufacturer 1", "different manufacturer");
        final List<String> brandName = List.of("some brand");
        final FdaDrugSearchRequest request = new FdaDrugSearchRequest(manufacturerName, brandName, null);

        final String expected = "(openfda.manufacturer_name:\"manufacturer+1\"+AND+\"different+manufacturer\")+AND+" +
                "(openfda.brand_name:\"some+brand\")";

        final String result = cut.buildSearchQuery(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldBuildSearchQueryWithoutBrandNameIfNull() {
        final List<String> manufacturerName = List.of("manufacturer");
        final FdaDrugSearchRequest request = new FdaDrugSearchRequest(manufacturerName, null, null);

        final String expected = "(openfda.manufacturer_name:\"manufacturer\")";

        final String result = cut.buildSearchQuery(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldBuildSearchQueryWithoutBrandNameIfEmpty() {
        final List<String> manufacturerName = List.of("manufacturer");
        final List<String> brandName = Collections.emptyList();
        final FdaDrugSearchRequest request = new FdaDrugSearchRequest(manufacturerName, brandName, null);

        final String expected = "(openfda.manufacturer_name:\"manufacturer\")";

        final String result = cut.buildSearchQuery(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldBuildSearchQueryForApplicationNumber() {
        final String appNum = "ABC123";
        final String expected = String.format("(application_number:\"%s\")", appNum);

        final String result = cut.buildSearchQueryForApplicationNumber(appNum);

        assertThat(result).isEqualTo(expected);
    }
}
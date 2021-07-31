package com.krzysztofse.drugs.fda.gateway;

import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class FdaQueryBuilder {

    private static final String MANUFACTURER_NAME_FIELD = "openfda.manufacturer_name";
    private static final String BRAND_NAME_FIELD = "openfda.brand_name";
    private static final String APPLICATION_NUMBER_FIELD = "application_number";
    private static final String AND_OPERATOR = "+AND+";

    public String buildSearchQuery(final FdaDrugSearchRequest request) {
        return new Builder()
                .append(MANUFACTURER_NAME_FIELD, request.getManufacturerName())
                .append(BRAND_NAME_FIELD, request.getBrandName())
                .build();
    }

    public String buildSearchQueryForApplicationNumber(final String applicationNumber) {
        return new Builder()
                .append(APPLICATION_NUMBER_FIELD, List.of(applicationNumber))
                .build();
    }

    private static class Builder {
        private final List<String> result = new ArrayList<>();

        public Builder append(final String field, final List<String> terms) {
            if (!isNull(terms) && !terms.isEmpty()) {
                result.add(buildSingleQuery(field, terms));
            }
            return this;
        }

        public String build() {
            return String.join(AND_OPERATOR, result);
        }

        private String buildSingleQuery(final String field, final List<String> terms) {
            return "(".concat(field)
                    .concat(":")
                    .concat(buildSearchTerm(terms))
                    .concat(")");
        }

        private String buildSearchTerm(final List<String> terms) {
            return terms.stream()
                    .map(String::trim)
                    .map(this::plus)
                    .map(this::quoted)
                    .collect(Collectors.joining(AND_OPERATOR));
        }

        private String plus(final String string) {
            return string.replace(' ', '+');
        }

        private String quoted(final String string) {
            return "\"".concat(string.trim()).concat("\"");
        }
    }
}

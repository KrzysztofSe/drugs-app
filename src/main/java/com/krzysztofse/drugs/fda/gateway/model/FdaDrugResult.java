package com.krzysztofse.drugs.fda.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FdaDrugResult {

    private final String applicationNumber;
    private final OpenFda openFda;
    private final List<FdaProduct> products;

    @JsonCreator
    public FdaDrugResult(
            final @JsonProperty("application_number") String applicationNumber,
            final @JsonProperty("openfda") OpenFda openFda,
            final @JsonProperty("products") List<FdaProduct> products) {
        this.applicationNumber = applicationNumber;
        this.openFda = openFda;
        this.products = products;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public OpenFda getOpenFda() {
        return openFda;
    }

    public List<FdaProduct> getProducts() {
        return products;
    }
}

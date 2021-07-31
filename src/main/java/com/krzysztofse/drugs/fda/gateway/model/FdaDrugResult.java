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
            @JsonProperty("application_number") final String applicationNumber,
            @JsonProperty("openfda") final OpenFda openFda,
            @JsonProperty("products") final List<FdaProduct> products) {
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

package com.krzysztofse.drugs.fda.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FdaProduct {

    private final String productNumber;

    @JsonCreator
    public FdaProduct(@JsonProperty("product_number") final String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductNumber() {
        return productNumber;
    }
}

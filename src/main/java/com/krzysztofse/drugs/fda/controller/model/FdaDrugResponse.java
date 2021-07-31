package com.krzysztofse.drugs.fda.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FdaDrugResponse {

    private final String applicationNumber;
    private final List<String> manufacturerName;
    private final List<String> brandName;
    private final List<String> substanceName;
    private final List<String> productNumbers;

    @JsonCreator
    private FdaDrugResponse(
            @JsonProperty("applicationNumber") final String applicationNumber,
            @JsonProperty("manufacturerName") final List<String> manufacturerName,
            @JsonProperty("brandName") final List<String> brandName,
            @JsonProperty("substanceName") final List<String> substanceName,
            @JsonProperty("productNumbers") final List<String> productNumbers) {
        this.applicationNumber = applicationNumber;
        this.manufacturerName = manufacturerName;
        this.brandName = brandName;
        this.substanceName = substanceName;
        this.productNumbers = productNumbers;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public List<String> getManufacturerName() {
        return manufacturerName;
    }

    public List<String> getBrandName() {
        return brandName;
    }

    public List<String> getSubstanceName() {
        return substanceName;
    }

    public List<String> getProductNumbers() {
        return productNumbers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String applicationNumber;
        private List<String> manufacturerName;
        private List<String> brandName;
        private List<String> substanceName;
        private List<String> productNumbers;

        public Builder withApplicationNumber(String value) {
            this.applicationNumber = value;
            return this;
        }

        public Builder withManufacturerName(List<String> value) {
            this.manufacturerName = value;
            return this;
        }

        public Builder withBrandName(List<String> value) {
            this.brandName = value;
            return this;
        }

        public Builder withSubstanceName(List<String> value) {
            this.substanceName = value;
            return this;
        }

        public Builder withProductNumbers(List<String> value) {
            this.productNumbers = value;
            return this;
        }

        public FdaDrugResponse build() {
            return new FdaDrugResponse(
                    this.applicationNumber,
                    this.manufacturerName,
                    this.brandName,
                    this.substanceName,
                    this.productNumbers);
        }
    }
}

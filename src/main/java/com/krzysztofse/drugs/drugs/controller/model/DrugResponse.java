package com.krzysztofse.drugs.drugs.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DrugResponse {

    private final String applicationNumber;
    private final List<String> manufacturerName;
    private final List<String> substanceName;
    private final List<String> productNumbers;

    @JsonCreator
    public DrugResponse(
            @JsonProperty("applicationNumber") final String applicationNumber,
            @JsonProperty("manufacturerName") final List<String> manufacturerName,
            @JsonProperty("substanceName") final List<String> substanceName,
            @JsonProperty("productNumbers") final List<String> productNumbers) {
        this.applicationNumber = applicationNumber;
        this.manufacturerName = manufacturerName;
        this.substanceName = substanceName;
        this.productNumbers = productNumbers;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public List<String> getManufacturerName() {
        return manufacturerName;
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
        private List<String> substanceName;
        private List<String> productNumbers;

        public Builder withApplicationNumber(String applicationNumber) {
            this.applicationNumber = applicationNumber;
            return this;
        }

        public Builder withManufacturerName(List<String> manufacturerName) {
            this.manufacturerName = manufacturerName;
            return this;
        }

        public Builder withSubstanceName(List<String> substanceName) {
            this.substanceName = substanceName;
            return this;
        }

        public Builder withProductNumbers(List<String> productNumbers) {
            this.productNumbers = productNumbers;
            return this;
        }

        public DrugResponse build() {
            return new DrugResponse(
                    this.applicationNumber,
                    this.manufacturerName,
                    this.substanceName,
                    this.productNumbers);
        }
    }
}

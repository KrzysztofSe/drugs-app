package com.krzysztofse.drugs.fda.service.model;

import java.util.List;

public class FdaDrugDto {

    private final String applicationNumber;
    private final List<String> manufacturerName;
    private final List<String> brandName;
    private final List<String> substanceName;
    private final List<String> productNumbers;

    private FdaDrugDto(
            final String applicationNumber,
            final List<String> manufacturerName,
            final List<String> brandName,
            final List<String> substanceName,
            final List<String> productNumbers) {
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

        public FdaDrugDto build() {
            return new FdaDrugDto(
                    this.applicationNumber,
                    this.manufacturerName,
                    this.brandName,
                    this.substanceName,
                    this.productNumbers);
        }
    }
}

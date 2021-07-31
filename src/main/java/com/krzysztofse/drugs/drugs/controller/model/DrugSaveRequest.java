package com.krzysztofse.drugs.drugs.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krzysztofse.drugs.common.constraints.NotNullOrBlankListValuesConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugSaveRequest {

    @NotNull
    private final String applicationNumber;

    @NotEmpty
    @NotNullOrBlankListValuesConstraint
    private final List<String> manufacturerName;

    @NotEmpty
    @NotNullOrBlankListValuesConstraint
    private final List<String> substanceName;

    @NotEmpty
    @NotNullOrBlankListValuesConstraint
    private final List<String> productNumbers;

    @JsonCreator
    public DrugSaveRequest(
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
}

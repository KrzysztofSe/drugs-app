package com.krzysztofse.drugs.fda.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFda {

    private final List<String> brandName;
    private final List<String> manufacturerName;
    private final List<String> substanceName;

    @JsonCreator
    public OpenFda(
            final @JsonProperty("brand_name") List<String> brandName,
            final @JsonProperty("manufacturer_name") List<String> manufacturerName,
            final @JsonProperty("substance_name") List<String> substanceName) {
        this.brandName = brandName;
        this.manufacturerName = manufacturerName;
        this.substanceName = substanceName;
    }

    public List<String> getBrandName() {
        return brandName;
    }

    public List<String> getManufacturerName() {
        return manufacturerName;
    }

    public List<String> getSubstanceName() {
        return substanceName;
    }
}

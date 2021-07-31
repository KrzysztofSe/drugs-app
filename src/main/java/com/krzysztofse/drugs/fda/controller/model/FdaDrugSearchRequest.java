package com.krzysztofse.drugs.fda.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krzysztofse.drugs.common.constraints.FdaPagingConstraint;
import com.krzysztofse.drugs.common.constraints.NotNullOrBlankListValuesConstraint;
import com.krzysztofse.drugs.common.request.Paging;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static java.util.Objects.isNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FdaDrugSearchRequest {

    @NotNull
    @Size(min=1, max=100)
    @NotNullOrBlankListValuesConstraint
    private final List<String> manufacturerName;

    @Size(min=1, max=100)
    @NotNullOrBlankListValuesConstraint
    private final List<String> brandName;

    @Valid
    @FdaPagingConstraint
    private final Paging paging;

    @JsonCreator
    public FdaDrugSearchRequest(
            final @JsonProperty("manufacturerName") List<String> manufacturerName,
            final @JsonProperty("brandName") List<String> brandName,
            final @JsonProperty("paging") Paging paging) {
        this.manufacturerName = manufacturerName;
        this.brandName = brandName;
        this.paging = isNull(paging) ? Paging.getDefault() : paging;
    }

    public List<String> getManufacturerName() {
        return manufacturerName;
    }

    public List<String> getBrandName() {
        return brandName;
    }

    public Paging getPaging() {
        return paging;
    }
}

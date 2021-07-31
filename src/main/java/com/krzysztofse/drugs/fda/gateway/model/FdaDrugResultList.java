package com.krzysztofse.drugs.fda.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FdaDrugResultList {

    private final ResultMeta meta;
    private final List<FdaDrugResult> results;

    @JsonCreator
    public FdaDrugResultList(
            @JsonProperty("meta") final ResultMeta meta,
            @JsonProperty("results") final List<FdaDrugResult> results) {
        this.meta = meta;
        this.results = results;
    }

    public ResultMeta getMeta() {
        return meta;
    }

    public List<FdaDrugResult> getResults() {
        return results;
    }
}

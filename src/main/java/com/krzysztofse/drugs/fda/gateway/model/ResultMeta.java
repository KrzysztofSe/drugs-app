package com.krzysztofse.drugs.fda.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultMeta {

    private final ResultPagingInfo results;

    @JsonCreator
    public ResultMeta(@JsonProperty("results") final ResultPagingInfo results) {
        this.results = results;
    }

    public ResultPagingInfo getResults() {
        return results;
    }
}

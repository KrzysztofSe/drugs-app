package com.krzysztofse.drugs.fda.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultPagingInfo {

    private final Integer skip;
    private final Integer limit;
    private final Integer total;

    @JsonCreator
    public ResultPagingInfo(
            @JsonProperty("skip") final Integer skip,
            @JsonProperty("limit") final Integer limit,
            @JsonProperty("total") final Integer total) {
        this.skip = skip;
        this.limit = limit;
        this.total = total;
    }

    public Integer getSkip() {
        return skip;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getTotal() {
        return total;
    }
}

package com.krzysztofse.drugs.drugs.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krzysztofse.drugs.common.request.Paging;

import javax.validation.Valid;

public class DrugListRequest {

    @Valid
    private final Paging paging;

    @JsonCreator
    public DrugListRequest(@JsonProperty("paging") final Paging paging) {
        this.paging = paging;
    }

    public Paging getPaging() {
        return paging;
    }
}

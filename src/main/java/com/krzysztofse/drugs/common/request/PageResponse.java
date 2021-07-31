package com.krzysztofse.drugs.common.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {

    public final Long total;
    public final List<T> content;

    @JsonCreator
    public PageResponse(
            @JsonProperty("total") final Long total,
            @JsonProperty("content") final List<T> content) {
        this.total = total;
        this.content = content;
    }

    public Long getTotal() {
        return total;
    }

    public List<T> getContent() {
        return content;
    }

    public static <T> PageResponse<T> from(final Page<T> page) {
        return new PageResponse<>(page.getTotalElements(), page.toList());
    }
}

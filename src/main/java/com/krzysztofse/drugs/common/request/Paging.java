package com.krzysztofse.drugs.common.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

import static java.util.Objects.isNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Paging {

    private static final Integer DEFAULT_PAGE_VALUE = 0;
    private static final Integer DEFAULT_PAGE_SIZE_VALUE = 1;

    @Range(min=0)
    private final Integer page;

    @Range(min=1, max=100)
    private final Integer pageSize;

    @JsonCreator
    public Paging(
            final @JsonProperty("page") Integer page,
            final @JsonProperty("pageSize") Integer pageSize) {
        this.page = isNull(page) ? DEFAULT_PAGE_VALUE : page;
        this.pageSize = isNull(pageSize) ? DEFAULT_PAGE_SIZE_VALUE : pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    @JsonIgnore
    public Integer getSkip() {
        return page * pageSize;
    }

    public static Paging getDefault() {
        return new Paging(DEFAULT_PAGE_VALUE, DEFAULT_PAGE_SIZE_VALUE);
    }

    public Pageable toPageable() {
        return PageRequest.of(page, pageSize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paging paging = (Paging) o;
        return Objects.equals(page, paging.page) && Objects.equals(pageSize, paging.pageSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, pageSize);
    }
}

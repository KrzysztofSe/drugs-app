package com.krzysztofse.drugs.drugs.repository.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document
public class DrugDocument {

    @Id
    private final String id;
    private final List<String> manufacturerName;
    private final List<String> substanceName;
    private final List<String> productNumbers;

    @JsonCreator
    public DrugDocument(
            final String id,
            final List<String> manufacturerName,
            final List<String> substanceName,
            final List<String> productNumbers) {
        this.id = id;
        this.manufacturerName = manufacturerName;
        this.substanceName = substanceName;
        this.productNumbers = productNumbers;
    }

    public String getId() {
        return id;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private List<String> manufacturerName;
        private List<String> substanceName;
        private List<String> productNumbers;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withManufacturerName(List<String> manufacturerName) {
            this.manufacturerName = manufacturerName;
            return this;
        }

        public Builder withSubstanceName(List<String> substanceName) {
            this.substanceName = substanceName;
            return this;
        }

        public Builder withProductNumbers(List<String> productNumbers) {
            this.productNumbers = productNumbers;
            return this;
        }

        public DrugDocument build() {
            Objects.requireNonNull(this.id, "Id must not be null");
            return new DrugDocument(
                    this.id,
                    this.manufacturerName,
                    this.substanceName,
                    this.productNumbers);
        }
    }
}

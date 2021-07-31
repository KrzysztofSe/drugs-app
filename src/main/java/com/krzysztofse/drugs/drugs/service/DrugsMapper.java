package com.krzysztofse.drugs.drugs.service;

import com.krzysztofse.drugs.drugs.controller.model.DrugResponse;
import com.krzysztofse.drugs.drugs.controller.model.DrugSaveRequest;
import com.krzysztofse.drugs.drugs.repository.model.DrugDocument;
import com.krzysztofse.drugs.drugs.service.model.DrugDto;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;
import org.springframework.stereotype.Component;

@Component
public class DrugsMapper {

    public DrugResponse mapResponse(final DrugDto dto) {
        return DrugResponse.builder()
                .withApplicationNumber(dto.getId())
                .withManufacturerName(dto.getManufacturerName())
                .withSubstanceName(dto.getSubstanceName())
                .withProductNumbers(dto.getProductNumbers())
                .build();
    }

    public DrugDocument mapDocument(final FdaDrugDto item) {
        return DrugDocument.builder()
                .withId(item.getApplicationNumber())
                .withManufacturerName(item.getManufacturerName())
                .withSubstanceName(item.getSubstanceName())
                .withProductNumbers(item.getProductNumbers())
                .build();
    }

    public DrugDocument mapDocument(final DrugSaveRequest request) {
        return DrugDocument.builder()
                .withId(request.getApplicationNumber())
                .withManufacturerName(request.getManufacturerName())
                .withSubstanceName(request.getSubstanceName())
                .withProductNumbers(request.getProductNumbers())
                .build();
    }

    public DrugDto mapDto(final DrugDocument document) {
        return DrugDto.builder()
                .withId(document.getId())
                .withManufacturerName(document.getManufacturerName())
                .withSubstanceName(document.getSubstanceName())
                .withProductNumbers(document.getProductNumbers())
                .build();
    }
}

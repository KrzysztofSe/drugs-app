package com.krzysztofse.drugs.fda.service;

import com.krzysztofse.drugs.fda.controller.model.FdaDrugResponse;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResult;
import com.krzysztofse.drugs.fda.gateway.model.FdaProduct;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FdaMapper {

    public FdaDrugResponse mapResponse(final FdaDrugDto dto) {
        return FdaDrugResponse.builder()
                .withApplicationNumber(dto.getApplicationNumber())
                .withManufacturerName(dto.getManufacturerName())
                .withBrandName(dto.getBrandName())
                .withSubstanceName(dto.getSubstanceName())
                .withProductNumbers(dto.getProductNumbers())
                .build();
    }

    public FdaDrugDto mapDto(final FdaDrugResult item) {
        return FdaDrugDto.builder()
                .withApplicationNumber(item.getApplicationNumber())
                .withManufacturerName(item.getOpenFda().getManufacturerName())
                .withBrandName(item.getOpenFda().getBrandName())
                .withSubstanceName(item.getOpenFda().getSubstanceName())
                .withProductNumbers(mapProductNumbers(item))
                .build();
    }

    private List<String> mapProductNumbers(final FdaDrugResult result) {
        return result.getProducts().stream()
                .map(FdaProduct::getProductNumber)
                .collect(Collectors.toList());
    }

}

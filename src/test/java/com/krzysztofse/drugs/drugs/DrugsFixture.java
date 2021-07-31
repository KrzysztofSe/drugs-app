package com.krzysztofse.drugs.drugs;

import com.krzysztofse.drugs.common.CommonFixture;
import com.krzysztofse.drugs.common.request.Paging;
import com.krzysztofse.drugs.drugs.controller.model.DrugListRequest;
import com.krzysztofse.drugs.drugs.controller.model.DrugSaveRequest;
import com.krzysztofse.drugs.drugs.repository.model.DrugDocument;
import com.krzysztofse.drugs.drugs.service.model.DrugDto;

import java.util.List;

public class DrugsFixture extends CommonFixture {

    public final String drugsUrl = "/drugs";
    public final String drugsListUrl = drugsUrl + "/list";
    public final String drugsApplicationNumberUrl = drugsUrl + "/{applicationNumber}";

    public DrugSaveRequest buildDrugSaveRequest(final String applicationNumber) {
        return buildDrugSaveRequest(
                applicationNumber,
                manufacturerName,
                substanceName,
                productNumbers);
    }

    public DrugSaveRequest buildDrugSaveRequest(final String applicationNumber,
            final List<String> manufacturerName,
            final List<String> substanceName,
            final List<String> productNumbers) {

        return new DrugSaveRequest(
                applicationNumber,
                manufacturerName,
                substanceName,
                productNumbers);
    }

    public DrugListRequest buildDrugListRequest(final Integer page, final Integer pageSize) {
        return new DrugListRequest(new Paging(page, pageSize));
    }

    public DrugDto buildDrugDto(final String applicationNumber) {
        return DrugDto.builder()
                .withId(applicationNumber)
                .withManufacturerName(manufacturerName)
                .withSubstanceName(substanceName)
                .withProductNumbers(productNumbers)
                .build();
    }

    public DrugDocument buildDrugDocument(final String applicationNumber) {
        return DrugDocument.builder()
                .withId(applicationNumber)
                .withManufacturerName(manufacturerName)
                .withSubstanceName(substanceName)
                .withProductNumbers(productNumbers)
                .build();
    }
}

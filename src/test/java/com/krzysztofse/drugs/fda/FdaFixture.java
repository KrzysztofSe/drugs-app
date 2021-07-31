package com.krzysztofse.drugs.fda;

import com.krzysztofse.drugs.common.CommonFixture;
import com.krzysztofse.drugs.common.request.Paging;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugResponse;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResult;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResultList;
import com.krzysztofse.drugs.fda.gateway.model.FdaProduct;
import com.krzysztofse.drugs.fda.gateway.model.OpenFda;
import com.krzysztofse.drugs.fda.gateway.model.ResultMeta;
import com.krzysztofse.drugs.fda.gateway.model.ResultPagingInfo;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;

import java.util.List;
import java.util.stream.Collectors;

public class FdaFixture extends CommonFixture {

    public final String endpointUrl = "/fda/drugs/search";

    public FdaDrugResultList buildFdaDrugResultList(final List<FdaDrugResult> results) {
        return new FdaDrugResultList(
                new ResultMeta(new ResultPagingInfo(0, results.size(), results.size())),
                results);
    }


    public FdaDrugResult buildFdaDrug(final String applicationNumber) {
        return new FdaDrugResult(
                applicationNumber,
                new OpenFda(brandName, manufacturerName, substanceName),
                productNumbers.stream().map(FdaProduct::new).collect(Collectors.toList()));
    }

    public FdaDrugDto buildFdaDrugDto(final String applicationNumber) {
        return FdaDrugDto.builder()
                .withApplicationNumber(applicationNumber)
                .withManufacturerName(manufacturerName)
                .withBrandName(brandName)
                .withSubstanceName(substanceName)
                .withProductNumbers(productNumbers)
                .build();
    }

    public FdaDrugResponse buildFdaDrugResponse(final String applicationNumber) {
        return FdaDrugResponse.builder()
                .withApplicationNumber(applicationNumber)
                .withManufacturerName(manufacturerName)
                .withBrandName(brandName)
                .withSubstanceName(substanceName)
                .withProductNumbers(productNumbers)
                .build();
    }

    public FdaDrugSearchRequest buildRequest(
            final List<String> manufacturerName,
            final List<String> brandName,
            final Integer page,
            final Integer pageSize) {

        return new FdaDrugSearchRequest(
                manufacturerName,
                brandName,
                new Paging(page, pageSize));
    }
}

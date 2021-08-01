package com.krzysztofse.drugs.fda.service;

import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import com.krzysztofse.drugs.fda.gateway.FdaGateway;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResult;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FdaService {

    private final FdaGateway fdaGateway;
    private final FdaMapper fdaMapper;

    public FdaService(
            final FdaGateway fdaGateway,
            final FdaMapper fdaMapper) {
        this.fdaGateway = fdaGateway;
        this.fdaMapper = fdaMapper;
    }

    public Page<FdaDrugDto> getDrugData(final FdaDrugSearchRequest request) {
        Pageable pageable = request.getPaging().toPageable();
        return fdaGateway.getDrugData(request)
                .map(resultList -> mapPage(resultList.getResults(), pageable, resultList.getMeta().getResults().getTotal()))
                .orElse(mapPage(Collections.emptyList(), pageable, 0));
    }

    public Optional<FdaDrugDto> getDrugDataByApplicationNumber(final String applicationNumber) {
        return fdaGateway.getDrugDataByApplicationNumber(applicationNumber)
                .map(fdaMapper::mapDto);
    }

    private Page<FdaDrugDto> mapPage(final List<FdaDrugResult> list, Pageable pageable, long total) {
        return new PageImpl<>(
                list.stream().map(fdaMapper::mapDto).collect(Collectors.toList()),
                pageable,
                total);
    }
}

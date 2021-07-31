package com.krzysztofse.drugs.drugs.service;

import com.krzysztofse.drugs.common.exception.ApplicationException;
import com.krzysztofse.drugs.drugs.controller.model.DrugListRequest;
import com.krzysztofse.drugs.drugs.controller.model.DrugSaveRequest;
import com.krzysztofse.drugs.drugs.repository.DrugsRepository;
import com.krzysztofse.drugs.drugs.repository.model.DrugDocument;
import com.krzysztofse.drugs.drugs.service.model.DrugDto;
import com.krzysztofse.drugs.fda.service.FdaService;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class DrugsService {

    private final FdaService fdaService;
    private final DrugsMapper drugsMapper;
    private final DrugsRepository drugsRepository;

    public DrugsService(
            final FdaService fdaService,
            final DrugsMapper drugsMapper,
            final DrugsRepository drugsRepository) {
        this.fdaService = fdaService;
        this.drugsMapper = drugsMapper;
        this.drugsRepository = drugsRepository;
    }

    public void saveDrugDataFromFdaByApplicationNumber(final String applicationNumber) {
        final FdaDrugDto item = fdaService.getDrugDataByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ApplicationException.NotFoundException(
                        String.format("No drug application found for application number '%s'", applicationNumber)));

        final DrugDocument document = drugsMapper.mapDocument(item);
        drugsRepository.save(document);
    }

    public void saveDrugData(final DrugSaveRequest request) {
        final DrugDocument document = drugsMapper.mapDocument(request);
        drugsRepository.save(document);
    }

    public DrugDto getDrug(final String applicationNumber) {
        return drugsRepository.findById(applicationNumber)
                .map(drugsMapper::mapDto)
                .orElseThrow(() -> new ApplicationException.NotFoundException(
                        String.format("No drug application found for application number '%s'", applicationNumber)));
    }

    public Page<DrugDto> getDrugs(final DrugListRequest request) {
        return drugsRepository.findAll(request.getPaging().toPageable())
                .map(drugsMapper::mapDto);
    }
}

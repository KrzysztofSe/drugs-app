package com.krzysztofse.drugs.drugs.controller;

import com.krzysztofse.drugs.drugs.controller.model.DrugListRequest;
import com.krzysztofse.drugs.drugs.controller.model.DrugResponse;
import com.krzysztofse.drugs.drugs.controller.model.DrugSaveRequest;
import com.krzysztofse.drugs.drugs.service.DrugsMapper;
import com.krzysztofse.drugs.drugs.service.DrugsService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/drugs", produces = MediaType.APPLICATION_JSON_VALUE)
public class DrugsController {

    private final DrugsService drugsService;
    private final DrugsMapper drugsMapper;

    public DrugsController(
            final DrugsService drugsService,
            final DrugsMapper drugsMapper) {
        this.drugsService = drugsService;
        this.drugsMapper = drugsMapper;
    }

    @PutMapping(path = "/{applicationNumber}")
    public ResponseEntity<Void> saveFromFda(final @PathVariable String applicationNumber) {
        drugsService.saveDrugDataFromFdaByApplicationNumber(applicationNumber);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> save(final @Valid @RequestBody DrugSaveRequest request) {
        drugsService.saveDrugData(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{applicationNumber}")
    public DrugResponse get(final @PathVariable String applicationNumber) {
        return drugsMapper.mapResponse(drugsService.getDrug(applicationNumber));
    }

    @PostMapping(path = "/list")
    public Page<DrugResponse> list(@Valid @RequestBody final DrugListRequest request) {
        return drugsService.getDrugs(request)
                .map(drugsMapper::mapResponse);
    }
}

package com.krzysztofse.drugs.drugs.controller;

import com.krzysztofse.drugs.drugs.controller.model.DrugListRequest;
import com.krzysztofse.drugs.drugs.controller.model.DrugResponse;
import com.krzysztofse.drugs.drugs.controller.model.DrugSaveRequest;
import com.krzysztofse.drugs.drugs.service.DrugsMapper;
import com.krzysztofse.drugs.drugs.service.DrugsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Drugs", description = "Provides a way to create, update and read drug data to/from a persistent storage")
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

    @Operation(summary = "Saves a drug application record with specified application number directly from openFDA",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Record saved successfully"),
                    @ApiResponse(responseCode = "404", description = "Record not found in the openFDA database")})
    @PutMapping(path = "/{applicationNumber}")
    public ResponseEntity<Void> saveFromFda(final @PathVariable String applicationNumber) {
        drugsService.saveDrugDataFromFdaByApplicationNumber(applicationNumber);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Saves a specified drug application record",
            responses = { @ApiResponse(responseCode = "200", description = "Record saved successfully") })
    @PutMapping
    public ResponseEntity<Void> save(final @Valid @RequestBody DrugSaveRequest request) {
        drugsService.saveDrugData(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Retrieves single drug application record for a given application number",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Record retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Record not found in the local database")})
    @GetMapping(path = "/{applicationNumber}")
    public DrugResponse get(final @PathVariable String applicationNumber) {
        return drugsMapper.mapResponse(drugsService.getDrug(applicationNumber));
    }

    @Operation(summary = "Lists saved drug application records. Supports pagination",
            responses = { @ApiResponse(responseCode = "200", description = "Records retrieved successfully") })
    @PostMapping(path = "/list")
    public Page<DrugResponse> list(@Valid @RequestBody final DrugListRequest request) {
        return drugsService.getDrugs(request)
                .map(drugsMapper::mapResponse);
    }
}

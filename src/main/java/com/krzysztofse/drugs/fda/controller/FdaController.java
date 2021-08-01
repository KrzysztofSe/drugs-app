package com.krzysztofse.drugs.fda.controller;

import com.krzysztofse.drugs.common.request.PageResponse;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugResponse;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import com.krzysztofse.drugs.fda.service.FdaMapper;
import com.krzysztofse.drugs.fda.service.FdaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "FDA", description = "Provides a way to retrieve drug data from openFDA API")
@RestController
@RequestMapping(path = "/fda", produces = MediaType.APPLICATION_JSON_VALUE)
public class FdaController {

    private final FdaService fdaService;
    private final FdaMapper fdaMapper;

    public FdaController(
            final FdaService fdaService,
            final FdaMapper fdaMapper) {
        this.fdaService = fdaService;
        this.fdaMapper = fdaMapper;
    }

    @Operation(summary = "Retrieves drug records from openFDA API. Supports basic filtering and pagination",
            description = "Supports an array of manufacturers and brands. " +
                    "All given search terms will be joined with a logical 'AND'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Results retrieved successfully"),
                    @ApiResponse(responseCode = "503", description = "FDA service unreachable") })
    @PostMapping(path = "/drugs/search")
    public PageResponse<FdaDrugResponse> search(final @Valid @RequestBody FdaDrugSearchRequest request) {
        return PageResponse.from(fdaService.getDrugData(request)
                .map(fdaMapper::mapResponse));
    }
}

package com.krzysztofse.drugs.fda.controller;

import com.krzysztofse.drugs.fda.controller.model.FdaDrugResponse;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import com.krzysztofse.drugs.fda.service.FdaMapper;
import com.krzysztofse.drugs.fda.service.FdaService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

// TODO documentation

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

    @PostMapping(path = "/drugs/search")
    public Page<FdaDrugResponse> search(final @Valid @RequestBody FdaDrugSearchRequest request) {
        return fdaService.getDrugData(request)
                .map(fdaMapper::mapResponse);
    }
}

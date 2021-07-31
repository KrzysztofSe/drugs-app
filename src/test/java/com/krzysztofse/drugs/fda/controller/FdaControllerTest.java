package com.krzysztofse.drugs.fda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krzysztofse.drugs.fda.FdaFixture;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugResponse;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import com.krzysztofse.drugs.fda.service.FdaMapper;
import com.krzysztofse.drugs.fda.service.FdaService;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FdaController.class)
class FdaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FdaService fdaService;

    @MockBean
    private FdaMapper fdaMapper;

    private final Fixture fixture = new Fixture();

    @Test
    public void shouldNotAllowNullManufacturerNameList() throws Exception {
        final FdaDrugSearchRequest request = fixture.buildRequest(null, null, 0, 1);

        mockMvc.perform(post(fixture.endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(fdaService, fdaMapper);
    }

    @Test
    public void shouldNotAllowEmptyManufacturerNameList() throws Exception {
        final FdaDrugSearchRequest request = fixture.buildRequest(emptyList(), null, 0, 1);

        mockMvc.perform(post(fixture.endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(fdaService, fdaMapper);
    }

    @Test
    public void shouldNotAllowListsWithNullOrBlankValues() throws Exception {
        final FdaDrugSearchRequest request = fixture.buildRequest(newArrayList(" "), newArrayList(null, "value"), 0, 1);

        mockMvc.perform(post(fixture.endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(fdaService, fdaMapper);
    }

    @Test
    public void shouldNotAllowPagingOutsideOfBounds() throws Exception {
        // pageSize < 0
        mockMvc.perform(post(fixture.endpointUrl).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(fixture.buildPagingRequest(0, -1))))
                .andExpect(status().isBadRequest());

        // pageSize > 100
        mockMvc.perform(post(fixture.endpointUrl).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(fixture.buildPagingRequest(0, 101))))
                .andExpect(status().isBadRequest());

        // page < 0
        mockMvc.perform(post(fixture.endpointUrl).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(fixture.buildPagingRequest(-1, 1))))
                .andExpect(status().isBadRequest());

        // page and pageSize over 26k
        mockMvc.perform(post(fixture.endpointUrl).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(fixture.buildPagingRequest(260, 100))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnDrugsSearchResults() throws Exception {
        final FdaDrugSearchRequest request = fixture.buildRequest(newArrayList("value"), null, 0, 1);
        final Page<FdaDrugDto> dtos = new PageImpl<>(List.of(fixture.dto));

        when(fdaService.getDrugData(refEq(request))).thenReturn(dtos);
        when(fdaMapper.mapResponse(fixture.dto)).thenReturn(fixture.response);

        mockMvc.perform(post(fixture.endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].applicationNumber", equalTo(fixture.applicationNumber)));

        verify(fdaService).getDrugData(refEq(request));
        verify(fdaMapper).mapResponse(fixture.dto);
        verifyNoMoreInteractions(fdaService, fdaMapper);
    }

    public static class Fixture extends FdaFixture {
        private final String applicationNumber = "123";
        public final FdaDrugDto dto = buildFdaDrugDto(applicationNumber);
        public final FdaDrugResponse response = buildFdaDrugResponse(applicationNumber);

        public FdaDrugSearchRequest buildPagingRequest(final Integer page, final Integer pageSize) {
            return buildRequest(List.of("manufacture"), null, page, pageSize);
        }
    }
}
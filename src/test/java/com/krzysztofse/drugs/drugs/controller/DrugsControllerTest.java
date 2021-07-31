package com.krzysztofse.drugs.drugs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krzysztofse.drugs.drugs.DrugsFixture;
import com.krzysztofse.drugs.drugs.controller.model.DrugListRequest;
import com.krzysztofse.drugs.drugs.controller.model.DrugResponse;
import com.krzysztofse.drugs.drugs.controller.model.DrugSaveRequest;
import com.krzysztofse.drugs.drugs.service.DrugsMapper;
import com.krzysztofse.drugs.drugs.service.DrugsService;
import com.krzysztofse.drugs.drugs.service.model.DrugDto;
import org.assertj.core.util.Lists;
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

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DrugsController.class)
class DrugsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DrugsService drugsService;

    @MockBean
    private DrugsMapper drugsMapper;

    private final Fixture fixture = new Fixture();

    @Test
    public void shouldSaveFromFda() throws Exception {
        mockMvc.perform(put(fixture.drugsApplicationNumberUrl, fixture.applicationNumber))
                .andExpect(status().isOk());

        verify(drugsService).saveDrugDataFromFdaByApplicationNumber(fixture.applicationNumber);
        verifyNoMoreInteractions(drugsService, drugsMapper);
    }

    @Test
    public void shouldNotAllowNullValuesInSaveRequest() throws Exception {
        final DrugSaveRequest requestWithNulls = fixture.buildDrugSaveRequest(null, null,
                null, null);

        mockMvc.perform(put(fixture.drugsUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(requestWithNulls)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(drugsService, drugsMapper);
    }

    @Test
    public void shouldNotAllowEmptyListsInSaveRequest() throws Exception {
        final DrugSaveRequest requestWithEmptyLists = fixture.buildDrugSaveRequest(fixture.applicationNumber, List.of(),
                List.of(), List.of());

        mockMvc.perform(put(fixture.drugsUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(requestWithEmptyLists)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(drugsService, drugsMapper);
    }

    @Test
    public void shouldNotAllowNullsOrBlanksInListsInSaveRequest() throws Exception {
        final DrugSaveRequest requestWithNullsAndBlanks = fixture.buildDrugSaveRequest(fixture.applicationNumber,
                List.of("  "), List.of(""), Lists.newArrayList("ddd", null));

        mockMvc.perform(put(fixture.drugsUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(requestWithNullsAndBlanks)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(drugsService, drugsMapper);
    }

    @Test
    public void shouldSave() throws Exception {
        final DrugSaveRequest validRequest = fixture.buildDrugSaveRequest(fixture.applicationNumber);

        mockMvc.perform(put(fixture.drugsUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(validRequest)))
                .andExpect(status().isOk());

        verify(drugsService).saveDrugData(refEq(validRequest));
        verifyNoMoreInteractions(drugsService, drugsMapper);
    }

    @Test
    public void shouldGetByApplicationNumber() throws Exception {
        when(drugsService.getDrug(fixture.applicationNumber)).thenReturn(fixture.drugDto);
        when(drugsMapper.mapResponse(fixture.drugDto)).thenReturn(fixture.drugResponse);

        mockMvc.perform(get(fixture.drugsApplicationNumberUrl, fixture.applicationNumber))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(fixture.drugResponse)));

        verify(drugsService).getDrug(fixture.applicationNumber);
        verify(drugsMapper).mapResponse(fixture.drugDto);
        verifyNoMoreInteractions(drugsService, drugsMapper);
    }

    @Test
    public void shouldNotAllowPagingOutsideOfBounds() throws Exception {
        // pageSize < 0
        mockMvc.perform(post(fixture.drugsListUrl).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(fixture.buildDrugListRequest(0, -1))))
                .andExpect(status().isBadRequest());

        // pageSize > 100
        mockMvc.perform(post(fixture.drugsListUrl).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(fixture.buildDrugListRequest(0, 101))))
                .andExpect(status().isBadRequest());

        // page < 0
        mockMvc.perform(post(fixture.drugsListUrl).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(fixture.buildDrugListRequest(-1, 1))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetDrugsList() throws Exception {
        final DrugListRequest request = fixture.buildDrugListRequest(0, 2);
        final Page<DrugDto> drugs = new PageImpl<>(List.of(fixture.drugDto, fixture.drugDto2));

        when(drugsService.getDrugs(refEq(request))).thenReturn(drugs);
        when(drugsMapper.mapResponse(fixture.drugDto)).thenReturn(fixture.drugResponse);
        when(drugsMapper.mapResponse(fixture.drugDto2)).thenReturn(fixture.drugResponse2);

        mockMvc.perform(post(fixture.drugsListUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new PageImpl<>(List.of(fixture.drugResponse, fixture.drugResponse2)))));

        verify(drugsService).getDrugs(refEq(request));
        verify(drugsMapper).mapResponse(fixture.drugDto);
        verify(drugsMapper).mapResponse(fixture.drugDto2);
        verifyNoMoreInteractions(drugsService, drugsMapper);
    }

    private static class Fixture extends DrugsFixture {
        public final String applicationNumber = "appNum";
        public final String applicationNumber2 = "appNum2";
        public final DrugDto drugDto = DrugDto.builder().withId(applicationNumber).build();
        public final DrugDto drugDto2 = DrugDto.builder().withId(applicationNumber2).build();
        public final DrugResponse drugResponse = DrugResponse.builder().withApplicationNumber(applicationNumber).build();
        public final DrugResponse drugResponse2 = DrugResponse.builder().withApplicationNumber(applicationNumber2).build();
    }

}
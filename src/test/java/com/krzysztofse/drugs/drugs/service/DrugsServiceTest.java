package com.krzysztofse.drugs.drugs.service;

import com.krzysztofse.drugs.common.exception.ApplicationException;
import com.krzysztofse.drugs.drugs.DrugsFixture;
import com.krzysztofse.drugs.drugs.controller.model.DrugListRequest;
import com.krzysztofse.drugs.drugs.controller.model.DrugSaveRequest;
import com.krzysztofse.drugs.drugs.repository.DrugsRepository;
import com.krzysztofse.drugs.drugs.repository.model.DrugDocument;
import com.krzysztofse.drugs.drugs.service.model.DrugDto;
import com.krzysztofse.drugs.fda.FdaFixture;
import com.krzysztofse.drugs.fda.service.FdaService;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DrugsServiceTest {

    @Mock
    private FdaService fdaService;

    @Mock
    private DrugsRepository drugsRepository;

    @Mock
    private DrugsMapper drugsMapper;

    @InjectMocks
    private DrugsService cut;

    private final Fixture fixture = new Fixture();

    @Test
    public void shouldSaveDrugFromFda() {
        when(fdaService.getDrugDataByApplicationNumber(fixture.applicationNumber))
                .thenReturn(Optional.of(fixture.fdaDrugDto));
        when(drugsMapper.mapDocument(fixture.fdaDrugDto)).thenReturn(fixture.document);

        cut.saveDrugDataFromFdaByApplicationNumber(fixture.applicationNumber);

        verify(fdaService).getDrugDataByApplicationNumber(fixture.applicationNumber);
        verify(drugsMapper).mapDocument(fixture.fdaDrugDto);
        verify(drugsRepository).save(fixture.document);
        verifyNoMoreInteractions(fdaService, drugsMapper, drugsRepository);
    }

    @Test
    public void shouldThrowExceptionWhenNoDrugFoundInFda() {
        ApplicationException.NotFoundException ex = assertThrows(
                ApplicationException.NotFoundException.class,
                () -> cut.saveDrugDataFromFdaByApplicationNumber(fixture.applicationNumber));

        assertThat(ex.getMessage()).isEqualTo(fixture.expectedExceptionMessage);

        verify(fdaService).getDrugDataByApplicationNumber(fixture.applicationNumber);
        verifyNoMoreInteractions(fdaService, drugsMapper, drugsRepository);
    }

    @Test
    public void shouldSaveGivenDrug() {
        when(drugsMapper.mapDocument(fixture.saveRequest)).thenReturn(fixture.document);

        cut.saveDrugData(fixture.saveRequest);

        verify(drugsMapper).mapDocument(fixture.saveRequest);
        verify(drugsRepository).save(fixture.document);
        verifyNoMoreInteractions(fdaService, drugsMapper, drugsRepository);
    }

    @Test
    public void shouldGetDrug() {
        when(drugsRepository.findById(fixture.applicationNumber)).thenReturn(Optional.of(fixture.document));
        when(drugsMapper.mapDto(fixture.document)).thenReturn(fixture.drugDto);

        DrugDto result = cut.getDrug(fixture.applicationNumber);

        assertThat(result).isEqualTo(fixture.drugDto);

        verify(drugsRepository).findById(fixture.applicationNumber);
        verify(drugsMapper).mapDto(fixture.document);
        verifyNoMoreInteractions(fdaService, drugsMapper, drugsRepository);
    }

    @Test
    public void shouldThrowExceptionWhenNoDrugFoundInRepo() {
        ApplicationException.NotFoundException ex = assertThrows(
                ApplicationException.NotFoundException.class,
                () -> cut.getDrug(fixture.applicationNumber));

        assertThat(ex.getMessage()).isEqualTo(fixture.expectedExceptionMessage);

        verify(drugsRepository).findById(fixture.applicationNumber);
        verifyNoMoreInteractions(fdaService, drugsMapper, drugsRepository);
    }

    @Test
    public void shouldGetDrugList() {
        final Pageable pageable = fixture.listRequest.getPaging().toPageable();
        when(drugsRepository.findAll(eq(pageable))).thenReturn(new PageImpl<>(List.of(fixture.document)));
        when(drugsMapper.mapDto(fixture.document)).thenReturn(fixture.drugDto);

        Page<DrugDto> result = cut.getDrugs(fixture.listRequest);

        assertThat(result).containsExactly(fixture.drugDto);

        verify(drugsRepository).findAll(eq(pageable));
        verify(drugsMapper).mapDto(fixture.document);
        verifyNoMoreInteractions(fdaService, drugsMapper, drugsRepository);
    }

    public static class Fixture extends DrugsFixture {
        public final FdaFixture fdaFixture = new FdaFixture();
        public final String applicationNumber = "123";
        public final FdaDrugDto fdaDrugDto = fdaFixture.buildFdaDrugDto(applicationNumber);
        public final DrugDto drugDto = DrugDto.builder()
                .withId(applicationNumber)
                .build();
        public final DrugDocument document = DrugDocument.builder()
                .withId(applicationNumber)
                .build();
        public final DrugSaveRequest saveRequest = buildDrugSaveRequest(applicationNumber);
        public final DrugListRequest listRequest = buildDrugListRequest(0, 1);
        public final String expectedExceptionMessage = "No drug application found for application number '123'";
    }

}
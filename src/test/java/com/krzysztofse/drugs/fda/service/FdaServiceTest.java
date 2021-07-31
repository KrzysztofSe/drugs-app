package com.krzysztofse.drugs.fda.service;

import com.krzysztofse.drugs.fda.FdaFixture;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import com.krzysztofse.drugs.fda.gateway.FdaGateway;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResult;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResultList;
import com.krzysztofse.drugs.fda.service.model.FdaDrugDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FdaServiceTest {

    @Mock
    private FdaGateway fdaGateway;

    @Mock
    private FdaMapper fdaMapper;

    @InjectMocks
    private FdaService cut;

    private final Fixture fixture = new Fixture();

    @Test
    public void shouldGetDrugDataByAppNumber() {
        final FdaDrugResult fdaDrug = fixture.buildFdaDrug(fixture.appNumber1);
        final FdaDrugDto fdaDrugDto = fixture.buildFdaDrugDto(fixture.appNumber1);

        when(fdaGateway.getDrugDataByApplicationNumber(fixture.appNumber1)).thenReturn(Optional.of(fdaDrug));
        when(fdaMapper.mapDto(fdaDrug)).thenReturn(fdaDrugDto);

        final Optional<FdaDrugDto> result = cut.getDrugDataByApplicationNumber(fixture.appNumber1);

        assertThat(result.get()).isEqualTo(fdaDrugDto);

        verify(fdaGateway).getDrugDataByApplicationNumber(fixture.appNumber1);
        verify(fdaMapper).mapDto(fdaDrug);
        verifyNoMoreInteractions(fdaGateway, fdaMapper);
    }

    @Test
    public void shouldGetDrugDataList() {
        final FdaDrugResult fdaDrug1 = fixture.buildFdaDrug(fixture.appNumber1);
        final FdaDrugResult fdaDrug2 = fixture.buildFdaDrug(fixture.appNumber2);
        final FdaDrugDto fdaDrugDto1 = fixture.buildFdaDrugDto(fixture.appNumber1);
        final FdaDrugDto fdaDrugDto2 = fixture.buildFdaDrugDto(fixture.appNumber2);
        final FdaDrugResultList resultList = fixture.buildFdaDrugResultList(List.of(fdaDrug1, fdaDrug2));

        when(fdaGateway.getDrugData(fixture.request)).thenReturn(Optional.of(resultList));
        when(fdaMapper.mapDto(fdaDrug1)).thenReturn(fdaDrugDto1);
        when(fdaMapper.mapDto(fdaDrug2)).thenReturn(fdaDrugDto2);

        final Page<FdaDrugDto> result = cut.getDrugData(fixture.request);

        assertThat(result.get().collect(Collectors.toList())).containsExactlyInAnyOrder(fdaDrugDto1, fdaDrugDto2);

        verify(fdaGateway).getDrugData(fixture.request);
        verify(fdaMapper).mapDto(fdaDrug1);
        verify(fdaMapper).mapDto(fdaDrug2);
        verifyNoMoreInteractions(fdaGateway, fdaMapper);
    }

    private static class Fixture extends FdaFixture {
        public final String appNumber1 = "appNumber";
        public final String appNumber2 = "appNumber2";
        public final FdaDrugSearchRequest request = buildRequest(null, null, null, null);

    }
}
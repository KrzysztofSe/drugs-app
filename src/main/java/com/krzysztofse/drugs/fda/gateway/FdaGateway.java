package com.krzysztofse.drugs.fda.gateway;

import com.krzysztofse.drugs.common.exception.ApplicationException;
import com.krzysztofse.drugs.fda.controller.model.FdaDrugSearchRequest;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResult;
import com.krzysztofse.drugs.fda.gateway.model.FdaDrugResultList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Component
public class FdaGateway {

    private static final Logger logger = LoggerFactory.getLogger(FdaGateway.class);

    private final RestTemplate restTemplate;
    private final FdaQueryBuilder fdaQueryBuilder;
    private final String fdaUrl;

    private static final String SEARCH_PARAM = "search";
    private static final String SKIP_PARAM = "skip";
    private static final String LIMIT_PARAM = "limit";

    private static final String FDA_UNREACHABLE_MESSAGE = "FDA service is unreachable at this time.";

    public FdaGateway(
            final RestTemplate restTemplate,
            final FdaQueryBuilder fdaQueryBuilder,
            final @Value("${fda.url}") String fdaUrl) {
        this.restTemplate = restTemplate;
        this.fdaQueryBuilder = fdaQueryBuilder;
        this.fdaUrl = fdaUrl;
    }

    public Optional<FdaDrugResultList> getDrugData(final FdaDrugSearchRequest request) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(fdaUrl)
                .queryParam(SEARCH_PARAM, fdaQueryBuilder.buildSearchQuery(request))
                .queryParam(SKIP_PARAM, request.getPaging().getSkip())
                .queryParam(LIMIT_PARAM, request.getPaging().getPageSize())
                .build().encode().toUri();

        try {
            return performRequest(uri);
        } catch (HttpClientErrorException e) {
            return handleClientException(e);
        } catch (HttpServerErrorException e) {
            return handleServerException(e);
        }
    }

    public Optional<FdaDrugResult> getDrugDataByApplicationNumber(final String applicationNumber) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(fdaUrl)
                .queryParam(SEARCH_PARAM, fdaQueryBuilder.buildSearchQueryForApplicationNumber(applicationNumber))
                .queryParam(LIMIT_PARAM, 1)
                .build().encode().toUri();

        try {
            return performRequest(uri)
                    .filter(body -> !body.getResults().isEmpty())
                    .map(body -> body.getResults().get(0));
        } catch (HttpClientErrorException e) {
            return handleClientException(e);
        } catch (HttpServerErrorException e) {
            return handleServerException(e);
        }
    }

    private Optional<FdaDrugResultList> performRequest(final URI uri) {
        logger.debug("Sending FDA request: {}", uri);
        return Optional.ofNullable(restTemplate.getForEntity(uri, FdaDrugResultList.class).getBody());
    }

    private <T> Optional<T> handleClientException(final HttpClientErrorException e) {
        switch (e.getStatusCode()) {
            case NOT_FOUND:
                return Optional.empty();
            default:
                throw e;
        }
    }

    private <T> Optional<T> handleServerException(final HttpServerErrorException e) {
        switch (e.getStatusCode()) {
            case BAD_GATEWAY:
            case GATEWAY_TIMEOUT:
            case SERVICE_UNAVAILABLE:
                logger.error("FDA service unreachable due to {}", e.getStatusCode());
                throw new ApplicationException.FdaUnreachableException(FDA_UNREACHABLE_MESSAGE);
            default:
                throw e;
        }
    }
}

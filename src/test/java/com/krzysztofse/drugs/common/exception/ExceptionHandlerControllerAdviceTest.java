package com.krzysztofse.drugs.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionHandlerControllerAdviceTest {

    private final ExceptionHandlerControllerAdvice cut = new ExceptionHandlerControllerAdvice();

    @Test
    public void shouldReturnMessageFromNotFoundException() {
        final String message = "custom message";
        final ErrorResponse result = cut.handleNotFound(new RuntimeException(message));
        assertThat(result.getMessage()).isEqualTo(message);
    }

    @Test
    public void shouldReturnDefaultMessageFromNotFoundException() {
        final ErrorResponse result = cut.handleNotFound(new RuntimeException());
        assertThat(result.getMessage()).isEqualTo("Not found");
    }

    @Test
    public void shouldReturnMessageFromFdaUnreachableException() {
        final String message = "custom message";
        final ErrorResponse result = cut.handleFdaUnreachable(new RuntimeException(message));
        assertThat(result.getMessage()).isEqualTo(message);
    }

    @Test
    public void shouldReturnDefaultMessageFromFdaUnreachableException() {
        final ErrorResponse result = cut.handleFdaUnreachable(new RuntimeException());
        assertThat(result.getMessage()).isEqualTo("FDA unreachable");
    }

}
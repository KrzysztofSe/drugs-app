package com.krzysztofse.drugs.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionHandlerControllerAdviceTest {

    private final ExceptionHandlerControllerAdvice cut = new ExceptionHandlerControllerAdvice();

    @Test
    public void shouldReturnMessageFromException() {
        final String message = "custom message";
        final ErrorResponse result = cut.handleNotFound(new RuntimeException(message));
        assertThat(result.getMessage()).isEqualTo(message);
    }

    @Test
    public void shouldReturnDefaultMessage() {
        final ErrorResponse result = cut.handleNotFound(new RuntimeException());
        assertThat(result.getMessage()).isEqualTo("Not found");
    }

}
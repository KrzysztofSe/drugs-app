package com.krzysztofse.drugs.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler({ ApplicationException.NotFoundException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final RuntimeException e) {
        return new ErrorResponse(Optional.ofNullable(e.getMessage())
                .orElse("Not found"));
    }

    @ExceptionHandler({ ApplicationException.FdaUnreachableException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleFdaUnreachable(final RuntimeException e) {
        return new ErrorResponse(Optional.ofNullable(e.getMessage())
                .orElse("FDA unreachable"));
    }
}

package com.krzysztofse.drugs.common.exception;

public class ApplicationException extends RuntimeException {

    public ApplicationException(final String message) {
        super(message);
    }

    public static class NotFoundException extends ApplicationException {
        public NotFoundException(final String message) {
            super(message);
        }
    }

}

package com.microservice.authentication.exception;

public class RecordAlreadyExistsException extends Exception{
    public RecordAlreadyExistsException() {
        super();
    }

    public RecordAlreadyExistsException(String message) {
        super(message);
    }

    public RecordAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected RecordAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

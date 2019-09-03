package com.example.fieldforce.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FfaException extends RuntimeException {
    private String errorCode, errorMessage;
    private int status;
    private Object data;

    public FfaException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode.name();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public FfaException(ErrorCode errorCode, String... fields) {
        this.errorMessage = (String.format(errorCode.getErrorMessage(), fields));
        this.errorCode = errorCode.name();
    }

    public FfaException(ErrorCode errorCode, int status, Object data, String... fields) {
        this.status = status;
        this.data = data;
        this.errorMessage = (String.format(errorCode.getErrorMessage(), fields));
        this.errorCode = errorCode.name();
    }

    public FfaException(ErrorCode errorCode, int status, String... fields) {
        this.status = status;
        this.errorMessage = (String.format(errorCode.getErrorMessage(), fields));
        this.errorCode = errorCode.name();
    }

    public FfaException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public FfaException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

}
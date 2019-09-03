package com.example.fieldforce.exception;

import com.example.fieldforce.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = {"com.example.fieldforce"})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(FfaException.class)
    public @ResponseBody
    ApiResponse handleException(FfaException ex) {
        ApiResponse apiOutput = new ApiResponse();
        apiOutput.setSuccess(false);
        String errorCode = ex.getErrorCode();
        if (errorCode != null) {
            apiOutput.setErrorCode(ex.getErrorCode());
            apiOutput.setErrorMessage(ex.getErrorMessage());
        }
        log.error("Exception occured", ex);
        return apiOutput;
    }


}

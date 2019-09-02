package com.example.fieldforce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T>  {
    private boolean success=true;
    private String errorCode;
    private String errorMessage;
    private T data;

    public ApiResponse(T data){
        this.data=data;
    }
}

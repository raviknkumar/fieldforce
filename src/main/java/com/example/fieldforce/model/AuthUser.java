package com.example.fieldforce.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthUser {
    private String name;
    private Integer id;
}

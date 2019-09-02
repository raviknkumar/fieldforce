package com.example.fieldforce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDto {

    private Integer id;
    private String name;
    private String street;
    private String type1;
    private String type2;
    private String addressLine1;
    private String addressLine2;
    private String phoneNumber;

}

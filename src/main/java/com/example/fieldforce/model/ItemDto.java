package com.example.fieldforce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Integer id;
    private String itemName;
    private Integer brandId;
    private Double price;
    private Long availableCount;
}

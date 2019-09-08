package com.example.fieldforce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrderDetailDto {
    private Integer id;
    private Integer saleOrderId;
    private Integer itemId;
    private String itemName;
    private Double salePrice;
    private Double taxPrice;
    private Double purchasePrice;
    private Integer pieces;
    private Integer boxes;
    private Double discount;
}
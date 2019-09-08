package com.example.fieldforce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrderDto {
    private Integer id;
    private String shopName;
    private Integer shopId;
    private String orderDate;
    private Double totalPrice;
    private Double totalTax;
    private Double delieveryCharge;
}

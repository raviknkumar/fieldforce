package com.example.fieldforce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrderRequestDto {

    private SaleOrderDto saleOrder;
    private List<SaleOrderDetailDto> saleOrderDetails;

    public static SaleOrderRequestDto convertSOAndSOD(SaleOrderDto saleOrderDto1, List<SaleOrderDetailDto> saleOrderDetails1){
        return SaleOrderRequestDto.builder()
                .saleOrder(saleOrderDto1)
                .saleOrderDetails(saleOrderDetails1)
                .build();
    }

}

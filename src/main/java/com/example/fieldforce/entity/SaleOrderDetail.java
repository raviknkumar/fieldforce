package com.example.fieldforce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "sale_order_detail")
public class SaleOrderDetail extends BaseEntity{
    @Column(name = "sale_order_id")
    private Integer saleOrderId;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column(name = "tax_price")
    private Double taxPrice;

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(name = "pieces")
    private Integer pieces;

    @Column(name = "boxes")
    private Integer boxes;

    @Column(name = "discount")
    private Double discount;
}
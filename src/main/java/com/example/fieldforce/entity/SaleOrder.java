package com.example.fieldforce.entity;

import java.util.Date;


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
@Entity(name = "sale_order")
public class SaleOrder extends BaseEntity {

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_id")
    private Integer shopId;

    @Column(name = "order_date")
    private String orderDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "total_tax")
    private Double totalTax;

    @Column(name="delievery_charge")
    private Double delieveryCharge;
}

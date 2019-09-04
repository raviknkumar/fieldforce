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
@Entity(name = "item")
public class Item extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "price")
    private Double price;

    @Column(name = "inventory")
    private Integer inventory;
}

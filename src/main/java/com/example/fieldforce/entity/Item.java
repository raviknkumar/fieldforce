package com.example.fieldforce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "item")
@Table(uniqueConstraints={@UniqueConstraint(name = "item_name_unique", columnNames={"name"})})
public class Item extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "box_price")
    private Double boxPrice;

    @Column(name = "piece_price")
    private Double piecePrice;

    @Column(name = "inventory")
    private Integer inventory;

    @Column(name = "tax_percent")
    private Double taxPercent;
}

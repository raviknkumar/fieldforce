package com.example.fieldforce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "shop")
@Table(indexes = { @Index(name = "street_index", columnList = "street") },
        uniqueConstraints = {@UniqueConstraint(name = "shop_name_unique", columnNames = "name")})
public class Shop extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "street")
    private String street;

    @Column(name = "type1")
    private String type1;

    @Column(name = "type2")
    private String type2;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "phone_number")
    private String phoneNumber;

}

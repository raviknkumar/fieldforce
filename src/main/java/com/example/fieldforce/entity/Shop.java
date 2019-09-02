package com.example.fieldforce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "shop")
@Table(indexes = { @Index(name = "street_index", columnList = "id,street") })
public class Shop extends BaseEntity {

    @Column(name = "name")
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

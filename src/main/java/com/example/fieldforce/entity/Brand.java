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
@Entity(name = "brand")
public class Brand extends BaseEntity{
    @Column(name = "name")
    private String name;
}

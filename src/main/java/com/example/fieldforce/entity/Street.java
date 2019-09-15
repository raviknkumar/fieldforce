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
@Entity(name = "street")
@Table(uniqueConstraints={@UniqueConstraint(name = "street_name_unique", columnNames={"name"})})
public class Street extends BaseEntity {

    @Column(name = "name")
    private String name;

}

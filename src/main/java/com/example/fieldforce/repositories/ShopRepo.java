package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepo extends JpaRepository<Shop, Integer> {
    List<Shop> getAllByStreet(String streetName);
}

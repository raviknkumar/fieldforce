package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepo extends JpaRepository<Shop, Integer> {
}

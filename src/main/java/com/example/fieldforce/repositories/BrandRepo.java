package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepo extends JpaRepository<Brand, Integer> {
}

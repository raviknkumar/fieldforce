package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.Street;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreetRepo extends JpaRepository<Street, Integer> {
}

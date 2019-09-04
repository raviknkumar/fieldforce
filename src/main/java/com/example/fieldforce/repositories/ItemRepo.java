package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepo extends JpaRepository<Item, Integer> {
}

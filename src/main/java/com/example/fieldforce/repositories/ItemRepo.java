package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Integer> {
    List<Item> findAllByIdIn(List<Integer> ids);
}

package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleOrderRepo extends JpaRepository<SaleOrder, Integer> {
    Optional<SaleOrder> findByShopIdAndOrderDate(Integer shopId, String orderDate);
    List<SaleOrder> findAllByOrderDate(String orderDate);
    List<SaleOrder> deleteAllByOrderDate(String orderDate);
}

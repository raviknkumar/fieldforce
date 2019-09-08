package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.SaleOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleOrderDetailRepo extends JpaRepository<SaleOrderDetail, Integer> {
    List<SaleOrderDetail> findAllBySaleOrderId(Integer saleOrderId);
}

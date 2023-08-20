package com.danis.repository;

import com.danis.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long>,  FilterOrderRepository {
}

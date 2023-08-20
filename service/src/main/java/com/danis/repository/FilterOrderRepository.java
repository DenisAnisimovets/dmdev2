package com.danis.repository;

import com.danis.entity.Orders;

import java.util.List;

public interface FilterOrderRepository {
    List<Orders> findByUserId(Long userId);
}

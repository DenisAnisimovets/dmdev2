package com.danis.dao;

import com.danis.entity.GoodInOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodInOrderRepository extends JpaRepository<GoodInOrder, Long> {
}

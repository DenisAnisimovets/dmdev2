package com.danis.dao;

import com.danis.entity.GoodInOrder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class GoodInOrderRepository extends RepositoryBase<Long, GoodInOrder> {

    public GoodInOrderRepository(EntityManager entityManager) {
        super(GoodInOrder.class, entityManager);
    }

}

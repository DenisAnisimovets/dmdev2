package com.danis.dao;

import com.danis.entity.GoodInOrder;

import javax.persistence.EntityManager;

public class GoodInOrderRepository extends RepositoryBase<Long, GoodInOrder> {

    public GoodInOrderRepository(EntityManager entityManager) {
        super(GoodInOrder.class, entityManager);
    }

}

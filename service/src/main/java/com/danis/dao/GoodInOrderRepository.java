package com.danis.dao;

import com.danis.entity.GoodInOrder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class GoodInOrderRepository extends RepositoryBase<Long, GoodInOrder> {

    public GoodInOrderRepository(EntityManager entityManager) {
        super(GoodInOrder.class, entityManager);
    }

}

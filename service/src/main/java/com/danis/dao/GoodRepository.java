package com.danis.dao;

import com.danis.entity.Good;

import javax.persistence.EntityManager;

public class GoodRepository extends RepositoryBase<Long, Good> {
    public GoodRepository(EntityManager entityManager) {
        super(Good.class, entityManager);
    }
}

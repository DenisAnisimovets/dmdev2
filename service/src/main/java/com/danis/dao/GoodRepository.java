package com.danis.dao;

import com.danis.entity.Good;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class GoodRepository extends RepositoryBase<Long, Good> {

    public GoodRepository(EntityManager entityManager) {
        super(Good.class, entityManager);
    }

}

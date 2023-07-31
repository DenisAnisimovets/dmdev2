package com.danis.dao;

import com.danis.entity.Good;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class GoodRepository extends RepositoryBase<Long, Good> {

    public GoodRepository(EntityManager entityManager) {
        super(Good.class, entityManager);
    }

}

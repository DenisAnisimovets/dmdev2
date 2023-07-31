package com.danis.dao;

import com.danis.entity.GoodInBucket;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class GoodInBucketRepository extends RepositoryBase<Long, GoodInBucket> {

    public GoodInBucketRepository(EntityManager entityManager) {
        super(GoodInBucket.class, entityManager);
    }

}

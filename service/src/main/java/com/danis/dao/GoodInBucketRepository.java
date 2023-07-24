package com.danis.dao;

import com.danis.entity.GoodInBucket;

import javax.persistence.EntityManager;

public class GoodInBucketRepository extends RepositoryBase<Long, GoodInBucket> {
    public GoodInBucketRepository(EntityManager entityManager) {
        super(GoodInBucket.class, entityManager);
    }
}

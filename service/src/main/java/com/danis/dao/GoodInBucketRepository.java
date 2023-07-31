package com.danis.dao;

import com.danis.entity.GoodInBucket;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class GoodInBucketRepository extends RepositoryBase<Long, GoodInBucket> {

    public GoodInBucketRepository(EntityManager entityManager) {
        super(GoodInBucket.class, entityManager);
    }

}

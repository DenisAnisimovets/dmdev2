package com.danis.dao;

import com.danis.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRepository extends RepositoryBase<Long, User> {
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}

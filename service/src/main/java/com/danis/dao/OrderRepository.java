package com.danis.dao;

import com.danis.entity.Order;
import com.danis.entity.Order_;
import com.danis.entity.User;
import com.danis.entity.User_;

import javax.persistence.EntityManager;
import java.util.List;

public class OrderRepository extends RepositoryBase<Long, Order> {
    EntityManager entityManager;
    Class<Order> clazz;

    public OrderRepository(EntityManager entityManager) {
        super(Order.class, entityManager);
        this.entityManager = entityManager;
        clazz = Order.class;
    }

    public List<Order> findByUserId(Long userId) {
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(clazz);
        var orders = criteria.from(Order.class);
        var users = orders.join(Order_.USER);
        criteria.where(cb.equal(users.get(User_.ID), userId));
        return entityManager.createQuery(criteria)
                .getResultList();
    }
}

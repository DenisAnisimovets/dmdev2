package com.danis.dao;

import com.danis.dto.OrderFilter;
import com.danis.entity.Orders;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

import static com.danis.entity.QOrders.orders;

@Component
public class OrderRepository extends RepositoryBase<Long, Orders> {

    public OrderRepository(EntityManager entityManager) {
        super(Orders.class, entityManager);
    }

    public List<Orders> findByUserId(Long userId) {
//        var cb = getEntityManager().getCriteriaBuilder();
//        var criteria = cb.createQuery(getClazz());
//        var orders = criteria.from(Orders.class);
//        var users = orders.join(Orders_.USER);
//        criteria.where(cb.equal(users.get(User_.ID), userId));
//        return getEntityManager().createQuery(criteria)
//                .getResultList();
//    }

        OrderFilter orderFilter = OrderFilter.builder()
                .userIds(List.of(userId)).
                build();

        var predicate = QPredicate.builder()
                .add(orderFilter.getUserIds(), orders.user.id::in)
                .buildAnd();

        return new JPAQuery<Orders>(getEntityManager())
                .select(orders)
                .from(orders)
                .where(predicate)
                .fetch();

    }
}

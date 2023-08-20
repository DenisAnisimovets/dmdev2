package com.danis.repository;

import com.danis.dto.OrderFilter;
import com.danis.entity.Orders;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

import static com.danis.entity.QOrders.orders;

@RequiredArgsConstructor
public class FilterOrderRepositoryImpl implements FilterOrderRepository {

    private final EntityManager entityManager;

    @Override
    public List<Orders> findByUserId(Long userId) {

        OrderFilter orderFilter = OrderFilter.builder()
                .userIds(List.of(userId)).
                build();

        var predicate = QPredicate.builder()
                .add(orderFilter.getUserIds(), orders.user.id::in)
                .buildAnd();

        return new JPAQuery<Orders>(entityManager)
                .select(orders)
                .from(orders)
                .where(predicate)
                .fetch();

    }
}

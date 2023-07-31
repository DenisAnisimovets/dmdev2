package com.danis.util;

import com.danis.entity.Good;
import com.danis.entity.GoodInBucket;
import com.danis.entity.GoodInOrder;
import com.danis.entity.Orders;
import com.danis.entity.Role;
import com.danis.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class EntityTestUtil {
    public Good createGood(String goodName) {
        return Good.builder()
                .goodName(goodName)
                .quantity(100)
                .price(100)
                .build();
    }

    public User createUser(String userName) {
        return User.builder()
                .username(userName)
                .firstname("Ivan")
                .lastname("Ivanov")
                .role(Role.ADMIN)
                .birth_date(LocalDate.of(2001, 1, 1))
                .build();
    }

    public Orders createOrder(User user) {
        return Orders.builder()
                .creation_date(LocalDateTime.of(2023, 1, 1, 15, 0, 0))
                .user(user)
                .sum(100L)
                .build();
    }

    public GoodInOrder createGoodInOrder(Good good, Orders orders) {
        return GoodInOrder
                .builder()
                .good(good)
                .orders(orders)
                .price(good.getPrice())
                .quantity(100)
                .build();
    }

    public GoodInBucket createGoodInBucket(User user, Good good) {
        return GoodInBucket
                .builder()
                .user(user)
                .good(good)
                .price(good.getPrice())
                .quantity(100)
                .creation_date(LocalDateTime.of(2023, 1, 1, 15,0,0))
                .build();
    }
}

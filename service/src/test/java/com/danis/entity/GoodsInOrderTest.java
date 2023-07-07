package com.danis.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.HibernateUtil;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GoodsInOrderTest {
    private static SessionFactory sessionFactory = null;
    private User user = null;
    private Order order = null;
    private Good good = null;
    private GoodsInOrder goodsInOrder = null;

    @BeforeAll
    static void beforeTests() {
        try {
            sessionFactory = HibernateUtil.buildSessionFactory();
        } finally {
        }
    }

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("User999999")
                .build();

        order = Order.builder()
                .creation_date(LocalDateTime.of(2023, 1, 1, 15, 0, 0))
                .userId(user.getId())
                .sum(100L)
                .build();

        good = Good.
                builder()
                .goodName("Good1")
                .price(100)
                .quantity(120)
                .build();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.save(order);
            session.save(good);
            session.getTransaction().commit();
        }
    }


    @AfterEach
    void tearDown() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(order);
            session.delete(user);
            session.delete(good);
            session.getTransaction().commit();
        }
    }

    @Test
    void insertGoodInOrder() {
        try (Session session = sessionFactory.openSession()) {
            goodsInOrder = GoodsInOrder
                    .builder()
                    .goodId(good.getId())
                    .orderId(order.getId())
                    .price(good.getPrice())
                    .quantity(1)
                    .build();
            session.beginTransaction();

            session.save(goodsInOrder);

            session.getTransaction().commit();
            Assertions.assertNotNull(goodsInOrder.getId());

        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.delete(goodsInOrder);

            session.getTransaction().commit();
        }
    }

    @Test
    void readGoodInOrder() {

        GoodsInOrder expectedGoodInOrder = GoodsInOrder
                .builder()
                .goodId(good.getId())
                .orderId(order.getId())
                .price(good.getPrice())
                .quantity(1)
                .build();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(expectedGoodInOrder);
            session.getTransaction().commit();
        }

        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            GoodsInOrder actualGoodsInOrder = session1.get(GoodsInOrder.class, expectedGoodInOrder.getId());
            assertThat(expectedGoodInOrder.equals(actualGoodsInOrder));
        }

        try (Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();
            session2.delete(expectedGoodInOrder);

            session2.getTransaction().commit();
        }
    }

    @AfterAll
    static void afterTests() {
        try {
            sessionFactory.close();
        } finally {
        }
    }
}
package com.danis.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.HibernateUtil;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    private static SessionFactory sessionFactory = null;

    @BeforeAll
    static void beforeTests() {
        try {
            sessionFactory = HibernateUtil.buildSessionFactory();
        } finally {
        }
    }

    @Test
    void insertOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Order order = Order.builder()
                    .creation_date(LocalDateTime.of(2023, 1, 1, 15, 0, 0))
                    .userId(1L)
                    .sum(100L)
                    .build();
            session.save(order);
            session.getTransaction().commit();
            Assertions.assertNotNull(order.getId());
        }
    }

    @Test
    void readOrder() {
        User user = User.builder()
                .username("User3")
                .build();

        try (Session session0 = sessionFactory.openSession()) {
            session0.beginTransaction();
            session0.save(user);
            session0.getTransaction().commit();
        }
        Order expectedOrder = Order.builder()
                .creation_date(LocalDateTime.of(2023, 1, 1, 15, 0, 0))
                .userId(user.getId())
                .sum(100L)
                .build();

        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            session1.save(expectedOrder);
            session1.getTransaction().commit();
        }

        try (Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();
            Order actualOrder = session2.get(Order.class, expectedOrder.getId());
            assertThat(expectedOrder.equals(actualOrder));
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
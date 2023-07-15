package com.danis.entity;

import com.danis.util.EntityTestUtil;
import com.danis.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderTest {
    private static SessionFactory sessionFactory;

    @BeforeAll
    static void beforeTests() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @Test
    void insertOrder() {
        try (Session session = sessionFactory.openSession()) {
            User user = EntityTestUtil.createUser("UserForOrder");
            session.beginTransaction();
            session.save(user);
            Order order = EntityTestUtil.createOrder(user);
            session.save(order);
            assertNotNull(order.getId());
            session.getTransaction().rollback();
        }
    }

    @Test
    void readOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser("UserForOrder");
            session.save(user);

            Order expectedOrder = EntityTestUtil.createOrder(user);

            session.save(expectedOrder);
            session.clear();
            Order actualOrder = session.get(Order.class, expectedOrder.getId());
            assertThat(actualOrder).isEqualTo(expectedOrder);
            session.getTransaction().rollback();
        }
    }

    @Test
    void updateOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser("UserForOrder");
            session.save(user);

            Order expectedOrder = EntityTestUtil.createOrder(user);

            session.save(expectedOrder);
            Long expectedSum = Long.valueOf(100000);
            expectedOrder.setSum(expectedSum);
            session.flush();
            session.clear();

            Order actualOrder = session.get(Order.class, expectedOrder.getId());
            assertThat(actualOrder.getSum()).isEqualTo(expectedSum);
            session.getTransaction().rollback();
        }
    }

    @Test
    void deleteOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser("UserForOrder");
            session.save(user);

            Order order = EntityTestUtil.createOrder(user);

            session.save(order);

            session.delete(order);
            session.flush();
            session.clear();
            Order actualOrder = session.get(Order.class, order.getId());
            assertNull(actualOrder);
            session.getTransaction().rollback();
        }
    }

    @Test
    void addGoodInOrderByCascade() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser("UserForOrder");
            session.save(user);
            Order expectedOrder = EntityTestUtil.createOrder(user);
            session.save(expectedOrder);
            Good good = EntityTestUtil.createGood("First good");
            session.save(good);

            GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, expectedOrder);
            expectedOrder.getGoodsInOrder().add(goodInOrder);
            session.flush();

            assertNotNull(goodInOrder.getId());
            session.getTransaction().rollback();
        }
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
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

class GoodInOrderTest {
    private static SessionFactory sessionFactory;

    @BeforeAll
    static void beforeTests() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @Test
    void insertGoodInOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser("UserForGoodInOrder");
            Order order = EntityTestUtil.createOrder(user);
            Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
            session.save(user);
            session.save(order);
            session.save(good);
            GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, order);

            session.save(goodInOrder);

            assertNotNull(goodInOrder.getId());

            session.getTransaction().rollback();
        }
    }

    @Test
    void readGoodInOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser("UserForGoodInOrder");
            Order order = EntityTestUtil.createOrder(user);
            Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
            session.save(user);
            session.save(order);
            session.save(good);

            GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, order);
            session.save(expectedGoodInOrder);
            session.clear();

            GoodInOrder actualGoodInOrder = session.get(GoodInOrder.class, expectedGoodInOrder.getId());
            assertThat(actualGoodInOrder).isEqualTo(actualGoodInOrder);

            session.getTransaction().rollback();
        }
    }

    @Test
    void updateGoodInOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser("UserForGoodInOrder");
            Order order = EntityTestUtil.createOrder(user);
            Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
            session.save(user);
            session.save(order);
            session.save(good);

            GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, order);
            session.save(expectedGoodInOrder);
            Integer expectedQuantity = Integer.valueOf(1000000);
            expectedGoodInOrder.setQuantity(expectedQuantity);
            session.flush();
            session.clear();

            GoodInOrder actualGoodInOrder = session.get(GoodInOrder.class, expectedGoodInOrder.getId());
            assertThat(actualGoodInOrder.getQuantity()).isEqualTo(expectedQuantity);
            session.getTransaction().rollback();
        }
    }

    @Test
    void deleteGoodInOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser("UserForGoodInOrder");
            Order order = EntityTestUtil.createOrder(user);
            Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
            session.save(user);
            session.save(order);
            session.save(good);

            GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, order);
            session.save(expectedGoodInOrder);
            session.delete(expectedGoodInOrder);
            session.flush();
            session.clear();

            GoodInOrder actualGoodInOrder = session.get(GoodInOrder.class, expectedGoodInOrder.getId());
            assertNull(actualGoodInOrder);
            session.getTransaction().rollback();
        }
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
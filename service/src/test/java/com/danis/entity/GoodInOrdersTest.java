package com.danis.entity;

import com.danis.util.EntityTestUtil;
import com.danis.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GoodInOrdersTest {
    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    static void beforeTests() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void insertGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Orders orders = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        session.save(user);
        session.save(orders);
        session.save(good);
        GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, orders);

        session.save(goodInOrder);

        assertNotNull(goodInOrder.getId());
    }

    @Test
    void readGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Orders orders = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        session.save(user);
        session.save(orders);
        session.save(good);

        GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, orders);
        session.save(expectedGoodInOrder);
        session.clear();

        GoodInOrder actualGoodInOrder = session.get(GoodInOrder.class, expectedGoodInOrder.getId());
        assertThat(actualGoodInOrder).isEqualTo(actualGoodInOrder);
    }

    @Test
    void updateGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Orders orders = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        session.save(user);
        session.save(orders);
        session.save(good);

        GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, orders);
        session.save(expectedGoodInOrder);
        Integer expectedQuantity = Integer.valueOf(1000000);
        expectedGoodInOrder.setQuantity(expectedQuantity);
        session.flush();
        session.clear();

        GoodInOrder actualGoodInOrder = session.get(GoodInOrder.class, expectedGoodInOrder.getId());
        assertThat(actualGoodInOrder.getQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    void deleteGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Orders orders = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        session.save(user);
        session.save(orders);
        session.save(good);

        GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, orders);
        session.save(expectedGoodInOrder);
        session.delete(expectedGoodInOrder);
        session.flush();
        session.clear();

        GoodInOrder actualGoodInOrder = session.get(GoodInOrder.class, expectedGoodInOrder.getId());
        assertNull(actualGoodInOrder);
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
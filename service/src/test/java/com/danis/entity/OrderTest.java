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

class OrderTest {
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
    void insertOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);
        Order order = EntityTestUtil.createOrder(user);
        session.save(order);
        assertNotNull(order.getId());
    }

    @Test
    void readOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);

        Order expectedOrder = EntityTestUtil.createOrder(user);

        session.save(expectedOrder);
        session.clear();
        Order actualOrder = session.get(Order.class, expectedOrder.getId());
        assertThat(actualOrder).isEqualTo(expectedOrder);
    }

    @Test
    void updateOrder() {
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
    }

    @Test
    void deleteOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);

        Order order = EntityTestUtil.createOrder(user);

        session.save(order);

        session.delete(order);
        session.flush();
        session.clear();
        Order actualOrder = session.get(Order.class, order.getId());
        assertNull(actualOrder);
    }

    @Test
    void addGoodInOrderByCascade() {
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
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
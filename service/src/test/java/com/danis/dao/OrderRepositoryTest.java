package com.danis.dao;

import com.danis.entity.Good;
import com.danis.entity.GoodInOrder;
import com.danis.entity.Order;
import com.danis.entity.User;
import com.danis.util.EntityTestUtil;
import com.danis.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderRepositoryTest {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;
    private static OrderRepository orderRepository;
    private static UserRepository userRepository;
    private static GoodRepository goodRepository;

    @BeforeAll
    static void beforeAll() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        entityManager = HibernateTestUtil.buildSessionProxy(sessionFactory);
        orderRepository = new OrderRepository(entityManager);
        userRepository = new UserRepository(entityManager);
        goodRepository = new GoodRepository(entityManager);
    }

    @BeforeEach
    void setUp() {
        orderRepository.getEntityManager().getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        orderRepository.getEntityManager().getTransaction().rollback();
    }

    @Test
    void createOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Order order = EntityTestUtil.createOrder(user);

        orderRepository.save(order);

        assertNotNull(order.getId());
    }

    @Test
    void readOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Order expectedOrder = EntityTestUtil.createOrder(user);
        orderRepository.save(expectedOrder);
        entityManager.clear();

        Optional<Order> actualOrder = orderRepository.findById(expectedOrder.getId());

        assertTrue(actualOrder.isPresent());
        assertThat(actualOrder.get()).isEqualTo(expectedOrder);
    }

    @Test
    void updateOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Order expectedOrder = EntityTestUtil.createOrder(user);
        orderRepository.save(expectedOrder);
        Long expectedSum = Long.valueOf(100000);

        expectedOrder.setSum(expectedSum);
        entityManager.flush();
        entityManager.clear();

        Optional<Order> actualOrder = orderRepository.findById(expectedOrder.getId());
        assertTrue(actualOrder.isPresent());
        assertThat(actualOrder.get().getSum()).isEqualTo(expectedSum);
    }

    @Test
    void deleteOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Order order = EntityTestUtil.createOrder(user);
        orderRepository.save(order);

        orderRepository.delete(order.getId());
        entityManager.flush();
        entityManager.clear();

        Optional<Order> actualOrder = orderRepository.findById(order.getId());
        assertTrue(actualOrder.isEmpty());
    }

    @Test
    void addGoodInOrderByCascade() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Order expectedOrder = EntityTestUtil.createOrder(user);
        orderRepository.save(expectedOrder);
        Good good = EntityTestUtil.createGood("First good");
        goodRepository.save(good);
        GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, expectedOrder);

        expectedOrder.getGoodsInOrder().add(goodInOrder);
        entityManager.flush();

        assertNotNull(goodInOrder.getId());
    }

    @Test
    void findAll() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Order order1 = EntityTestUtil.createOrder(user);
        Order order2 = EntityTestUtil.createOrder(user);
        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> allOrders = orderRepository.findAll();

        assertThat(allOrders).hasSize(2);
        assertTrue(allOrders.contains(order1));
        assertTrue(allOrders.contains(order2));
    }

    @Test
    void findByUserId() {
        User user1 = EntityTestUtil.createUser("UserForOrder1");
        User user2 = EntityTestUtil.createUser("UserForOrder2");
        userRepository.save(user1);
        userRepository.save(user2);
        Order order1 = EntityTestUtil.createOrder(user1);
        Order order2 = EntityTestUtil.createOrder(user2);
        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> byUserId = orderRepository.findByUserId(user1.getId());

        assertThat(byUserId).hasSize(1);
        assertTrue(byUserId.contains(order1));
        assertFalse(byUserId.contains(order2));
    }
}
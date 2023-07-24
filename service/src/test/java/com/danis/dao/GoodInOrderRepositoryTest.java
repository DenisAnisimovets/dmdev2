package com.danis.dao;

import com.danis.entity.Good;
import com.danis.entity.GoodInBucket;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoodInOrderRepositoryTest {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;
    private static UserRepository userRepository;
    private static GoodRepository goodRepository;
    private static GoodInOrderRepository goodInOrderRepository;
    private static OrderRepository orderRepository;

    @BeforeAll
    static void beforeAll() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        entityManager = HibernateTestUtil.buildSessionProxy(sessionFactory);
        userRepository = new UserRepository(entityManager);
        goodRepository = new GoodRepository(entityManager);
        orderRepository = new OrderRepository(entityManager);
        goodInOrderRepository = new GoodInOrderRepository(entityManager);
    }

    @BeforeEach
    void setUp() {
        goodInOrderRepository.getEntityManager().getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        goodInOrderRepository.getEntityManager().getTransaction().rollback();
    }

    @Test
    void createGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Order order = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        userRepository.save(user);
        orderRepository.save(order);
        goodRepository.save(good);
        GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, order);

        goodInOrderRepository.save(goodInOrder);

        assertNotNull(goodInOrder.getId());
    }

    @Test
    void readGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Order order = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        userRepository.save(user);
        orderRepository.save(order);
        goodRepository.save(good);

        GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, order);
        goodInOrderRepository.save(expectedGoodInOrder);
        entityManager.clear();

        Optional<GoodInOrder> actualGoodInOrder = goodInOrderRepository.findById(expectedGoodInOrder.getId());
        assertThat(actualGoodInOrder).isNotEmpty();
        assertThat(actualGoodInOrder.get()).isEqualTo(expectedGoodInOrder);
    }

    @Test
    void updateGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Order order = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        userRepository.save(user);
        orderRepository.save(order);
        goodRepository.save(good);

        GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, order);
        goodInOrderRepository.save(expectedGoodInOrder);
        Integer expectedQuantity = Integer.valueOf(1000000);
        expectedGoodInOrder.setQuantity(expectedQuantity);
        entityManager.flush();
        entityManager.clear();

        Optional<GoodInOrder> actualGoodInOrder = goodInOrderRepository.findById(expectedGoodInOrder.getId());
        assertThat(actualGoodInOrder).isNotEmpty();
        assertThat(actualGoodInOrder.get().getQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    void deleteGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Order order = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        userRepository.save(user);
        orderRepository.save(order);
        goodRepository.save(good);

        GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, order);
        goodInOrderRepository.save(expectedGoodInOrder);
        goodInOrderRepository.delete(expectedGoodInOrder.getId());
        entityManager.flush();
        entityManager.clear();

        Optional<GoodInOrder> actualGoodInOrder = goodInOrderRepository.findById(expectedGoodInOrder.getId());
        assertThat(actualGoodInOrder).isEmpty();
    }

    @Test
    void findAll() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Good good1 = EntityTestUtil.createGood("GoodForGoodInOrder1");
        Good good2 = EntityTestUtil.createGood("GoodForGoodInOrder2");
        Order order1 = EntityTestUtil.createOrder(user);
        Order order2 = EntityTestUtil.createOrder(user);
        userRepository.save(user);
        orderRepository.save(order1);
        orderRepository.save(order2);
        goodRepository.save(good1);
        goodRepository.save(good2);
        GoodInOrder goodInOrder1 = EntityTestUtil.createGoodInOrder(good1, order1);
        GoodInOrder goodInOrder2 = EntityTestUtil.createGoodInOrder(good2, order2);
        goodInOrderRepository.save(goodInOrder1);
        goodInOrderRepository.save(goodInOrder2);

        List<GoodInOrder> allGoodsInOrder = goodInOrderRepository.findAll();

        assertThat(allGoodsInOrder).hasSize(2);
        assertTrue(allGoodsInOrder.contains(goodInOrder1));
        assertTrue(allGoodsInOrder.contains(goodInOrder2));
    }

}
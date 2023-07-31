package com.danis.dao;

import com.danis.entity.Good;
import com.danis.entity.GoodInOrder;
import com.danis.entity.Orders;
import com.danis.entity.User;
import com.danis.util.EntityTestUtil;
import com.danis.util.TestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersRepositoryTest extends TestBase {

    private static OrderRepository orderRepository;
    private static UserRepository userRepository;
    private static GoodRepository goodRepository;

    @BeforeAll
    static void beforeAllTest() {
        orderRepository = context.getBean(OrderRepository.class);
        userRepository = context.getBean(UserRepository.class);
        goodRepository = context.getBean(GoodRepository.class);
    }

    @Test
    void createOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Orders orders = EntityTestUtil.createOrder(user);

        orderRepository.save(orders);

        assertNotNull(orders.getId());
    }

    @Test
    void readOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Orders expectedOrders = EntityTestUtil.createOrder(user);
        orderRepository.save(expectedOrders);
        entityManager.clear();

        Optional<Orders> actualOrder = orderRepository.findById(expectedOrders.getId());

        assertTrue(actualOrder.isPresent());
        assertThat(actualOrder.get()).isEqualTo(expectedOrders);
    }

    @Test
    void updateOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Orders expectedOrders = EntityTestUtil.createOrder(user);
        orderRepository.save(expectedOrders);
        Long expectedSum = Long.valueOf(100000);

        expectedOrders.setSum(expectedSum);
        entityManager.flush();
        entityManager.clear();

        Optional<Orders> actualOrder = orderRepository.findById(expectedOrders.getId());
        assertTrue(actualOrder.isPresent());
        assertThat(actualOrder.get().getSum()).isEqualTo(expectedSum);
    }

    @Test
    void deleteOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Orders orders = EntityTestUtil.createOrder(user);
        orderRepository.save(orders);

        orderRepository.delete(orders);
        entityManager.flush();
        entityManager.clear();

        Optional<Orders> actualOrder = orderRepository.findById(orders.getId());
        assertTrue(actualOrder.isEmpty());
    }

    @Test
    void addGoodInOrderByCascade() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Orders expectedOrders = EntityTestUtil.createOrder(user);
        orderRepository.save(expectedOrders);
        Good good = EntityTestUtil.createGood("First good");
        goodRepository.save(good);
        GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, expectedOrders);

        expectedOrders.getGoodsInOrder().add(goodInOrder);
        entityManager.flush();

        assertNotNull(goodInOrder.getId());
    }

    @Test
    void findAll() {
        User user = EntityTestUtil.createUser("UserForOrder");
        userRepository.save(user);
        Orders orders1 = EntityTestUtil.createOrder(user);
        Orders orders2 = EntityTestUtil.createOrder(user);
        orderRepository.save(orders1);
        orderRepository.save(orders2);

        List<Orders> allOrders = orderRepository.findAll();

        assertThat(allOrders).hasSize(2);
        assertTrue(allOrders.contains(orders1));
        assertTrue(allOrders.contains(orders2));
    }

    @Test
    void findByUserId() {
        User user1 = EntityTestUtil.createUser("UserForOrder1");
        User user2 = EntityTestUtil.createUser("UserForOrder2");
        userRepository.save(user1);
        userRepository.save(user2);
        Orders orders1 = EntityTestUtil.createOrder(user1);
        Orders orders2 = EntityTestUtil.createOrder(user2);
        orderRepository.save(orders1);
        orderRepository.save(orders2);

        List<Orders> byUserId = orderRepository.findByUserId(user1.getId());

        assertThat(byUserId).hasSize(1);
        assertTrue(byUserId.contains(orders1));
        assertFalse(byUserId.contains(orders2));
    }
}
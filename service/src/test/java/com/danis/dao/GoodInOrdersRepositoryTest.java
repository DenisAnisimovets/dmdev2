package com.danis.dao;

import com.danis.entity.Good;
import com.danis.entity.GoodInOrder;
import com.danis.entity.Orders;
import com.danis.entity.User;
import com.danis.util.EntityTestUtil;
import com.danis.util.TestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class GoodInOrdersRepositoryTest extends TestBase {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final GoodInOrderRepository goodInOrderRepository;
    private final OrderRepository orderRepository;

    @Test
    void createGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Orders orders = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        userRepository.save(user);
        orderRepository.save(orders);
        goodRepository.save(good);
        GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, orders);

        goodInOrderRepository.save(goodInOrder);

        assertNotNull(goodInOrder.getId());
    }

    @Test
    void readGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Orders orders = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        userRepository.save(user);
        orderRepository.save(orders);
        goodRepository.save(good);

        GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, orders);
        goodInOrderRepository.save(expectedGoodInOrder);
        entityManager.clear();

        Optional<GoodInOrder> actualGoodInOrder = goodInOrderRepository.findById(expectedGoodInOrder.getId());
        assertThat(actualGoodInOrder).isNotEmpty();
        assertThat(actualGoodInOrder.get()).isEqualTo(expectedGoodInOrder);
    }

    @Test
    void updateGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInOrder");
        Orders orders = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        userRepository.save(user);
        orderRepository.save(orders);
        goodRepository.save(good);

        GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, orders);
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
        Orders orders = EntityTestUtil.createOrder(user);
        Good good = EntityTestUtil.createGood("GoodForGoodInOrder");
        userRepository.save(user);
        orderRepository.save(orders);
        goodRepository.save(good);

        GoodInOrder expectedGoodInOrder = EntityTestUtil.createGoodInOrder(good, orders);
        goodInOrderRepository.save(expectedGoodInOrder);
        goodInOrderRepository.delete(expectedGoodInOrder);
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
        Orders orders1 = EntityTestUtil.createOrder(user);
        Orders orders2 = EntityTestUtil.createOrder(user);
        userRepository.save(user);
        orderRepository.save(orders1);
        orderRepository.save(orders2);
        goodRepository.save(good1);
        goodRepository.save(good2);
        GoodInOrder goodInOrder1 = EntityTestUtil.createGoodInOrder(good1, orders1);
        GoodInOrder goodInOrder2 = EntityTestUtil.createGoodInOrder(good2, orders2);
        goodInOrderRepository.save(goodInOrder1);
        goodInOrderRepository.save(goodInOrder2);

        List<GoodInOrder> allGoodsInOrder = goodInOrderRepository.findAll();

        assertThat(allGoodsInOrder).hasSize(2);
        assertTrue(allGoodsInOrder.contains(goodInOrder1));
        assertTrue(allGoodsInOrder.contains(goodInOrder2));
    }

}
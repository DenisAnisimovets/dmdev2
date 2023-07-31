package com.danis.entity;

import com.danis.dao.QPredicate;
import com.danis.dto.OrderFilter;
import com.danis.util.EntityTestUtil;
import com.danis.util.HibernateTestUtil;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.danis.entity.QOrders.orders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrdersTest {
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
        Orders orders = EntityTestUtil.createOrder(user);
        session.save(orders);
        assertNotNull(orders.getId());
    }

    @Test
    void readOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);

        Orders expectedOrders = EntityTestUtil.createOrder(user);

        session.save(expectedOrders);
        session.clear();
        Orders actualOrders = session.get(Orders.class, expectedOrders.getId());
        assertThat(actualOrders).isEqualTo(expectedOrders);
    }

    @Test
    void updateOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);

        Orders expectedOrders = EntityTestUtil.createOrder(user);

        session.save(expectedOrders);
        Long expectedSum = Long.valueOf(100000);
        expectedOrders.setSum(expectedSum);
        session.flush();
        session.clear();

        Orders actualOrders = session.get(Orders.class, expectedOrders.getId());
        assertThat(actualOrders.getSum()).isEqualTo(expectedSum);
    }

    @Test
    void deleteOrder() {
        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);

        Orders orders = EntityTestUtil.createOrder(user);

        session.save(orders);

        session.delete(orders);
        session.flush();
        session.clear();
        Orders actualOrders = session.get(Orders.class, orders.getId());
        assertNull(actualOrders);
    }

    @Test
    void addGoodInOrderByCascade() {
        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);
        Orders expectedOrders = EntityTestUtil.createOrder(user);
        session.save(expectedOrders);
        Good good = EntityTestUtil.createGood("First good");
        session.save(good);

        GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, expectedOrders);
        expectedOrders.getGoodsInOrder().add(goodInOrder);
        session.flush();

        assertNotNull(goodInOrder.getId());
    }

    @Test
    void readOrderByEntityGraph() {

        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);
        Orders expectedOrders = EntityTestUtil.createOrder(user);
        session.save(expectedOrders);
        Good good1 = EntityTestUtil.createGood("First good");
        session.save(good1);
        Good good2 = EntityTestUtil.createGood("Second good");
        session.save(good2);
        GoodInOrder goodInOrder1 = EntityTestUtil.createGoodInOrder(good1, expectedOrders);
        GoodInOrder goodInOrder2 = EntityTestUtil.createGoodInOrder(good2, expectedOrders);
        expectedOrders.getGoodsInOrder().add(goodInOrder1);
        expectedOrders.getGoodsInOrder().add(goodInOrder2);
        session.flush();
        session.clear();

        var orderGraph = session.createEntityGraph(Orders.class);
        orderGraph.addAttributeNodes("user", "goodsInOrder");
        var goodInOrderSubGraph = orderGraph.addSubgraph("goodsInOrder", GoodInOrder.class);
        goodInOrderSubGraph.addAttributeNodes("good", "quantity", "price", "creation_date");

        Map<String, Object> properties = Map.of(
                GraphSemantic.FETCH.getJpaHintName(), orderGraph
        );
        var actualOrder = session.find(Orders.class, expectedOrders.getId(), properties);
        assertThat(actualOrder).isEqualTo(expectedOrders);
        System.out.println(actualOrder.getUser().getUsername());
        System.out.println(actualOrder.getGoodsInOrder().size());
        var orders = session.createQuery(
                        "select o from Orders o", Orders.class)
                .setHint(GraphSemantic.LOAD.getJpaHintName(), orderGraph)
                .list();
//        orders.forEach(it -> System.out.println(it.getUser().getUsername()));
//        orders.forEach(it -> System.out.println(it.getGoodsInOrder().size()));
    }

    @Test
    void readOrderByEntityGraphWithoutGoods() {

        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);
        Orders expectedOrders = EntityTestUtil.createOrder(user);
        session.save(expectedOrders);
        Good good1 = EntityTestUtil.createGood("First good");
        session.save(good1);
        Good good2 = EntityTestUtil.createGood("Second good");
        session.save(good2);
        GoodInOrder goodInOrder1 = EntityTestUtil.createGoodInOrder(good1, expectedOrders);
        GoodInOrder goodInOrder2 = EntityTestUtil.createGoodInOrder(good2, expectedOrders);
        expectedOrders.getGoodsInOrder().add(goodInOrder1);
        expectedOrders.getGoodsInOrder().add(goodInOrder2);
        session.flush();
        session.clear();

        var orderGraph = session.createEntityGraph(Orders.class);
        orderGraph.addAttributeNodes("user");
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(), orderGraph
        );
        var actualOrder = session.find(Orders.class, expectedOrders.getId(), properties);
        assertThat(actualOrder).isEqualTo(expectedOrders);
//        System.out.println(actualOrder.getUser().getUsername());
//        System.out.println(actualOrder.getGoodsInOrder().size());

        var orders = session.createQuery(
                        "select o from Orders o", Orders.class)
                .setHint(GraphSemantic.LOAD.getJpaHintName(), orderGraph)
                .list();
//        orders.forEach(it -> System.out.println(it.getUser().getUsername()));
//        orders.forEach(it -> System.out.println(it.getGoodsInOrder().size()));
    }

    @Test
    void readOrdersByQueryDSL() {
        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);
        Orders expectedOrders = EntityTestUtil.createOrder(user);
        session.save(expectedOrders);
        Good good = EntityTestUtil.createGood("First good");
        session.save(good);
        GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, expectedOrders);
        expectedOrders.getGoodsInOrder().add(goodInOrder);
        session.flush();
        session.clear();

        List<Long> idList = new ArrayList<>();
        idList.add(expectedOrders.getId());

        OrderFilter orderFilter = OrderFilter.builder()
                .minSum(99L)
                .ids(idList).
                build();

        var predicate = QPredicate.builder()
                .add(orderFilter.getMinSum(), orders.sum::gt)
                .add(orderFilter.getMaxSum(), orders.sum::lt)
                .add(orderFilter.getIds(), orders.id::in)
                .add(orderFilter.getUserIds(), orders.user.id::in)
                .buildAnd();

        var orderList = new JPAQuery<Orders>(session)
                .select(orders)
                .from(orders)
                .join(orders.goodsInOrder, QGoodInOrder.goodInOrder)
                .where(predicate)
                .fetch();

        assertThat(orderList.size()).isEqualTo(1);
        assertThat(orderList.get(0)).isEqualTo(expectedOrders);
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
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

import static com.danis.entity.QOrder.order;
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

    @Test
    void readOrderByEntityGraph() {

        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);
        Order expectedOrder = EntityTestUtil.createOrder(user);
        session.save(expectedOrder);
        Good good1 = EntityTestUtil.createGood("First good");
        session.save(good1);
        Good good2 = EntityTestUtil.createGood("Second good");
        session.save(good2);
        GoodInOrder goodInOrder1 = EntityTestUtil.createGoodInOrder(good1, expectedOrder);
        GoodInOrder goodInOrder2 = EntityTestUtil.createGoodInOrder(good2, expectedOrder);
        expectedOrder.getGoodsInOrder().add(goodInOrder1);
        expectedOrder.getGoodsInOrder().add(goodInOrder2);
        session.flush();
        session.clear();

        var orderGraph = session.createEntityGraph(Order.class);
        orderGraph.addAttributeNodes("user", "goodsInOrder");
        var goodInOrderSubGraph = orderGraph.addSubgraph("goodsInOrder", GoodInOrder.class);
        goodInOrderSubGraph.addAttributeNodes("good", "quantity", "price", "creation_date");

        Map<String, Object> properties = Map.of(
                GraphSemantic.FETCH.getJpaHintName(), orderGraph
        );
        var actualOrder = session.find(Order.class, expectedOrder.getId(), properties);
        assertThat(actualOrder).isEqualTo(expectedOrder);
        System.out.println(actualOrder.getUser().getUsername());
        System.out.println(actualOrder.getGoodsInOrder().size());
        var orders = session.createQuery(
                        "select o from Order o", Order.class)
                .setHint(GraphSemantic.LOAD.getJpaHintName(), orderGraph)
                .list();
//        orders.forEach(it -> System.out.println(it.getUser().getUsername()));
//        orders.forEach(it -> System.out.println(it.getGoodsInOrder().size()));
    }

    @Test
    void readOrderByEntityGraphWithoutGoods() {

        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);
        Order expectedOrder = EntityTestUtil.createOrder(user);
        session.save(expectedOrder);
        Good good1 = EntityTestUtil.createGood("First good");
        session.save(good1);
        Good good2 = EntityTestUtil.createGood("Second good");
        session.save(good2);
        GoodInOrder goodInOrder1 = EntityTestUtil.createGoodInOrder(good1, expectedOrder);
        GoodInOrder goodInOrder2 = EntityTestUtil.createGoodInOrder(good2, expectedOrder);
        expectedOrder.getGoodsInOrder().add(goodInOrder1);
        expectedOrder.getGoodsInOrder().add(goodInOrder2);
        session.flush();
        session.clear();

        var orderGraph = session.createEntityGraph(Order.class);
        orderGraph.addAttributeNodes("user");
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(), orderGraph
        );
        var actualOrder = session.find(Order.class, expectedOrder.getId(), properties);
        assertThat(actualOrder).isEqualTo(expectedOrder);
//        System.out.println(actualOrder.getUser().getUsername());
//        System.out.println(actualOrder.getGoodsInOrder().size());

        var orders = session.createQuery(
                        "select o from Order o", Order.class)
                .setHint(GraphSemantic.LOAD.getJpaHintName(), orderGraph)
                .list();
//        orders.forEach(it -> System.out.println(it.getUser().getUsername()));
//        orders.forEach(it -> System.out.println(it.getGoodsInOrder().size()));
    }

    @Test
    void readOrdersByQueryDSL() {
        User user = EntityTestUtil.createUser("UserForOrder");
        session.save(user);
        Order expectedOrder = EntityTestUtil.createOrder(user);
        session.save(expectedOrder);
        Good good = EntityTestUtil.createGood("First good");
        session.save(good);
        GoodInOrder goodInOrder = EntityTestUtil.createGoodInOrder(good, expectedOrder);
        expectedOrder.getGoodsInOrder().add(goodInOrder);
        session.flush();
        session.clear();

        List<Long> idList = new ArrayList<>();
        idList.add(expectedOrder.getId());

        OrderFilter orderFilter = OrderFilter.builder()
                .minSum(99L)
                .ids(idList).
                build();

        var predicate = QPredicate.builder()
                .add(orderFilter.getMinSum(), order.sum::gt)
                .add(orderFilter.getMaxSum(), order.sum::lt)
                .add(orderFilter.getIds(), order.id::in)
                .add(orderFilter.getUserIds(), order.user.id::in)
                .buildAnd();

        var orderList = new JPAQuery<Order>(session)
                .select(order)
                .from(order)
                .join(order.goodsInOrder, QGoodInOrder.goodInOrder)
                .where(predicate)
                .fetch();

        assertThat(orderList.size()).isEqualTo(1);
        assertThat(orderList.get(0)).isEqualTo(expectedOrder);
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
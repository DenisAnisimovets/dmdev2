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

class GoodInBucketTest {
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
            User user = EntityTestUtil.createUser("UserForGoodInBucket");
            Good good = EntityTestUtil.createGood("GoodForGoodInOBucket");
            session.save(user);
            session.save(good);

            GoodInBucket goodInBucket = EntityTestUtil.createGoodInBucket(user, good);
            session.save(goodInBucket);

            assertNotNull(goodInBucket.getId());
    }

    @Test
    void readGoodInOrder() {
            User user = EntityTestUtil.createUser("UserForGoodInBucket");
            Good good = EntityTestUtil.createGood("GoodForGoodInBucket");
            session.save(user);
            session.save(good);

            GoodInBucket expectedGoodInBucket = EntityTestUtil.createGoodInBucket(user, good);
            session.save(expectedGoodInBucket);
            session.clear();

            GoodInBucket actualGoodInBucket = session.get(GoodInBucket.class, expectedGoodInBucket.getId());
            assertThat(actualGoodInBucket).isEqualTo(expectedGoodInBucket);
    }

    @Test
    void updateGoodInOrder() {
            User user = EntityTestUtil.createUser("UserForGoodInBucket");
            Good good = EntityTestUtil.createGood("GoodForGoodInBucket");
            session.save(user);
            session.save(good);

            GoodInBucket expectedGoodInBucket = EntityTestUtil.createGoodInBucket(user, good);
            session.save(expectedGoodInBucket);
            Integer expectedQuantity = Integer.valueOf(1000000);
            expectedGoodInBucket.setQuantity(expectedQuantity);
            session.flush();
            session.clear();

            GoodInBucket actualGoodInBucket = session.get(GoodInBucket.class, expectedGoodInBucket.getId());
            assertThat(actualGoodInBucket.getQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    void deleteGoodInOrder() {
            User user = EntityTestUtil.createUser("UserForGoodInBucket");
            Good good = EntityTestUtil.createGood("GoodForGoodInBucket");
            session.save(user);
            session.save(good);

            GoodInBucket expectedGoodInBucket = EntityTestUtil.createGoodInBucket(user, good);
            session.save(expectedGoodInBucket);
            session.delete(expectedGoodInBucket);
            session.flush();
            session.clear();

            GoodInBucket actualGoodInBucket = session.get(GoodInBucket.class, expectedGoodInBucket.getId());
            assertNull(actualGoodInBucket);
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
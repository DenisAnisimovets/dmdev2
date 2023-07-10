package com.danis.entity;

import com.danis.util.EntityTestUtil;
import com.danis.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GoodInBucketTest {
    private static SessionFactory sessionFactory = null;

    @BeforeAll
    static void beforeTests() {
        try {
            sessionFactory = HibernateTestUtil.buildSessionFactory();
        } finally {
        }
    }

    @Test
    void insertGoodInOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser( "UserForGoodInBucket");
            Good good = EntityTestUtil.createGood("GoodForGoodInOBucket");
            session.save(user);
            session.save(good);

            GoodInBucket goodInBucket = EntityTestUtil.createGoodInBucket(user, good);
            session.save(goodInBucket);

            Assertions.assertNotNull(goodInBucket.getId());
            session.getTransaction().rollback();
        }
    }

    @Test
    void readGoodInOrder() {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser( "UserForGoodInBucket");
            Good good = EntityTestUtil.createGood("GoodForGoodInBucket");
            session.save(user);
            session.save(good);

            GoodInBucket expectedGoodInBucket = EntityTestUtil.createGoodInBucket(user, good);
            session.save(expectedGoodInBucket);
            session.clear();

            GoodInBucket actualGoodInBucket = session.get(GoodInBucket.class, expectedGoodInBucket.getId());
            assertThat(expectedGoodInBucket.equals(actualGoodInBucket));
            session.getTransaction().rollback();
        }
    }

    @Test
    void updateGoodInOrder() {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser( "UserForGoodInBucket");
            Good good = EntityTestUtil.createGood("GoodForGoodInBucket");
            session.save(user);
            session.save(good);

            GoodInBucket expectedGoodInBucket = EntityTestUtil.createGoodInBucket(user, good);
            session.save(expectedGoodInBucket);
            Integer newQuantity = Integer.valueOf(1000000);
            expectedGoodInBucket.setQuantity(newQuantity);
            session.flush();
            session.clear();

            GoodInBucket actualGoodInBucket = session.get(GoodInBucket.class, expectedGoodInBucket.getId());
            assertThat(actualGoodInBucket.getQuantity().equals(newQuantity));
            session.getTransaction().rollback();
        }
    }

    @Test
    void deleteGoodInOrder() {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser( "UserForGoodInBucket");
            Good good = EntityTestUtil.createGood("GoodForGoodInBucket");
            session.save(user);
            session.save(good);

            GoodInBucket expectedGoodInBucket = EntityTestUtil.createGoodInBucket(user, good);
            session.save(expectedGoodInBucket);
            session.delete(expectedGoodInBucket);
            session.flush();
            session.clear();

            GoodInBucket actualGoodInBucket = session.get(GoodInBucket.class, expectedGoodInBucket.getId());
            Assertions.assertNull(actualGoodInBucket);
            session.getTransaction().rollback();
        }
    }

    @AfterAll
    static void afterTests() {
        try {
            sessionFactory.close();
        } finally {
        }
    }
}
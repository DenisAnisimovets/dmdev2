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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GoodTest {
    private static SessionFactory sessionFactory = null;

    @BeforeAll
    static void beforeTests() {
        try {
            sessionFactory = HibernateTestUtil.buildSessionFactory();
        } finally {
        }
    }

    @Test
    void InsertGood() {
        try (Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            Good good = EntityTestUtil.createGood("Good for test");
            session.save(good);
            assertNotNull(good.getId());
            session.getTransaction().rollback();
        }
    }

    @Test
    void readGood() {
        Good expectedGood = EntityTestUtil.createGood("Good for test");

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(expectedGood);
            session.clear();
            Good actualGood = session.get(Good.class, expectedGood.getId());
            assertThat(expectedGood.equals(actualGood));
            session.getTransaction().rollback();
        }
    }

    @Test
    void updateGood() {
        try (Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            Good good = EntityTestUtil.createGood("Good for test");
            session.save(good);
            ;

            good.setGoodName("Good after test");
            session.flush();
            session.clear();

            Good actualGood = session.get(Good.class, good.getId());
            assertEquals("Good after test", actualGood.getGoodName());
            session.getTransaction().rollback();
        }
    }

    @Test
    void deleteGood() {
        try (Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            Good actualGood = EntityTestUtil.createGood("Good for test");
            session.save(actualGood);


            session.delete(actualGood);
            session.flush();
            session.clear();

            Good expectedGood = session.get(Good.class, actualGood.getId());
            Assertions.assertNull(expectedGood);
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
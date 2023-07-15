package com.danis.entity;

import com.danis.util.EntityTestUtil;
import com.danis.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GoodTest {
    private static SessionFactory sessionFactory;

    @BeforeAll
    static void beforeTests() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
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
            assertThat(actualGood).isEqualTo(expectedGood);
            session.getTransaction().rollback();
        }
    }

    @Test
    void updateGood() {
        try (Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            Good expectedGood = EntityTestUtil.createGood("Good for test");
            session.save(expectedGood);

            expectedGood.setGoodName("Good after test");
            session.flush();
            session.clear();

            Good actualGood = session.get(Good.class, expectedGood.getId());
            assertEquals("Good after test", actualGood.getGoodName());
            session.getTransaction().rollback();
        }
    }

    @Test
    void deleteGood() {
        try (Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            Good expectedGood = EntityTestUtil.createGood("Good for test");
            session.save(expectedGood);


            session.delete(expectedGood);
            session.flush();
            session.clear();

            Good actualGood = session.get(Good.class, expectedGood.getId());
            assertNull(actualGood);
            session.getTransaction().rollback();
        }
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
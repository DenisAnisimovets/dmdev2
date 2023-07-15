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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GoodTest {
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
    void InsertGood() {
        Good good = EntityTestUtil.createGood("Good for test");
        session.save(good);
        assertNotNull(good.getId());
    }

    @Test
    void readGood() {
        Good expectedGood = EntityTestUtil.createGood("Good for test");

        session.save(expectedGood);
        session.clear();
        Good actualGood = session.get(Good.class, expectedGood.getId());
        assertThat(actualGood).isEqualTo(expectedGood);
    }

    @Test
    void updateGood() {
        Good expectedGood = EntityTestUtil.createGood("Good for test");
        session.save(expectedGood);

        expectedGood.setGoodName("Good after test");
        session.flush();
        session.clear();

        Good actualGood = session.get(Good.class, expectedGood.getId());
        assertEquals("Good after test", actualGood.getGoodName());
    }

    @Test
    void deleteGood() {
        Good expectedGood = EntityTestUtil.createGood("Good for test");
        session.save(expectedGood);


        session.delete(expectedGood);
        session.flush();
        session.clear();

        Good actualGood = session.get(Good.class, expectedGood.getId());
        assertNull(actualGood);
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
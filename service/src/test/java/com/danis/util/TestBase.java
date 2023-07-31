package com.danis.util;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;

public class TestBase {
    public static SessionFactory sessionFactory;
    public static EntityManager entityManager;

    @BeforeAll
    static void beforeAll() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        entityManager = HibernateTestUtil.buildSessionProxy(sessionFactory);
    }

    @BeforeEach
    void setUp() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        entityManager.getTransaction().rollback();
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
        sessionFactory.close();
    }
}

package com.danis.util;

import com.danis.config.ApplicationTestConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;

public class TestBase {
    protected static EntityManager entityManager;
    protected static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void beforeAll() {
        context = new AnnotationConfigApplicationContext(ApplicationTestConfiguration.class);
        entityManager = context.getBean(EntityManager.class);
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
        context.close();
    }
}

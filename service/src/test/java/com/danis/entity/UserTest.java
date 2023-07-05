package com.danis.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.HibernateUtil;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static SessionFactory sessionFactory = null;

    @BeforeAll
    static void beforeTests() {
        try {
            sessionFactory = HibernateUtil.buildSessionFactory();
        } finally {
        }
    }

    @Test
    void InsertUser() {
        try ( Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            User user = User.builder()
                    .username("User1")
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .role(Role.ADMIN)
                    .birth_date(LocalDate.of(2001, 1, 1))
                    .build();
            session.save(user);
            session.getTransaction().commit();
            assertTrue(user.getId() != null);
        }
    }

    @Test
    void readUser() {
        User expectedUser = User.builder()
                .username("User2")
                .firstname("Ivan")
                .lastname("Ivanov")
                .role(Role.ADMIN)
                .birth_date(LocalDate.of(2001, 1, 1))
                .build();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(expectedUser);
            session.getTransaction().commit();

        }
        ;
        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            User actualUser = session1.get(User.class, expectedUser.getId());
            assertThat(expectedUser.equals(actualUser));
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
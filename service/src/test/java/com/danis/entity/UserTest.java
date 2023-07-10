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

class UserTest {
    private static SessionFactory sessionFactory = null;

    @BeforeAll
    static void beforeTests() {
        try {
            sessionFactory = HibernateTestUtil.buildSessionFactory();
        } finally {
        }
    }

    @Test
    void InsertUser() {
        try (Session session = sessionFactory.openSession();) {
            User user = EntityTestUtil.createUser("User for test");
            session.beginTransaction();

            session.save(user);
            assertNotNull(user.getId());
            session.getTransaction().rollback();
        }
    }

    @Test
    void readUser() {
        User expectedUser = EntityTestUtil.createUser("User for test");

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(expectedUser);
            session.clear();
            User actualUser = session.get(User.class, expectedUser.getId());
            assertThat(expectedUser.equals(actualUser));
            session.getTransaction().rollback();
        }
    }

    @Test
    void updateUser() {
        try (Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            User user = EntityTestUtil.createUser("User for test");
            session.save(user);

            user.setUsername("User after test");
            session.flush();
            session.clear();

            User actualUser = session.get(User.class, user.getId());
            assertEquals("User after test", actualUser.getUsername());
            session.getTransaction().rollback();
        }
    }

    @Test
    void deleteUser() {
        try (Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            User actualUser = EntityTestUtil.createUser("User for test");
            session.save(actualUser);


            session.delete(actualUser);
            session.flush();
            session.clear();

            User expectedUser = session.get(User.class, actualUser.getId());
            Assertions.assertNull(expectedUser);
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
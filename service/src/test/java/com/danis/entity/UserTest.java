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

class UserTest {
    private static SessionFactory sessionFactory;
    private Session session;

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

    @BeforeAll
    static void beforeTests() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @Test
    void InsertUser() {
        User user = EntityTestUtil.createUser("User for test");

        session.save(user);

        assertNotNull(user.getId());
    }

    @Test
    void readUser() {
        User expectedUser = EntityTestUtil.createUser("User for test");

        session.save(expectedUser);
        session.clear();
        User actualUser = session.get(User.class, expectedUser.getId());
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void updateUser() {
        User user = EntityTestUtil.createUser("User for test");
        session.save(user);

        user.setUsername("User after test");
        session.flush();
        session.clear();

        User actualUser = session.get(User.class, user.getId());
        assertEquals("User after test", actualUser.getUsername());
    }

    @Test
    void deleteUser() {
        User expectedUser = EntityTestUtil.createUser("User for test");
        session.save(expectedUser);

        session.delete(expectedUser);
        session.flush();
        session.clear();

        User actualUser = session.get(User.class, expectedUser.getId());
        assertNull(actualUser);
    }

    @AfterAll
    static void afterTests() {
        sessionFactory.close();
    }
}
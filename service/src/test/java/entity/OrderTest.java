package entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.HibernateUtil;

import java.time.LocalDate;

class OrderTest {
    @Test
    void InsertOrder() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Order order = Order.builder()
                    .creation_date(LocalDate.of(2023, 1, 1))
                    .user_Id(1L)
                    .value(100L)
                    .build();
            session.save(order);
            session.getTransaction().commit();
            Assertions.assertTrue(order.getId() != null);
        }
    }

    @Test
    void readOrder() {

        User user = User.builder()
                .username("User3")
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session0 = sessionFactory.openSession()) {
                session0.beginTransaction();
                session0.save(user);
                session0.getTransaction().commit();
            }
            Order order = Order.builder()
                    .creation_date(LocalDate.of(2023, 1, 1))
                    .user_Id(user.getId())
                    .value(100L)
                    .build();

            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();
                session1.save(order);
                session1.getTransaction().commit();
            }

            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();
                Order order1 = session2.get(Order.class, order.getId());
                assert order.equals(order1);
            }
        }
    }
}
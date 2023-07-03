package entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.HibernateUtil;

import java.time.LocalDate;

class UsersTest {

    @Test
    void InsertUser() {
        try( SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
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
            Assertions.assertTrue(user.getId()!=null);
        }
    }

    @Test
    void readUser() {
        User user = User.builder()
                .username("User2")
                .firstname("Ivan")
                .lastname("Ivanov")
                .role(Role.ADMIN)
                .birth_date(LocalDate.of(2001, 1, 1))
                .build();

        try( SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try( Session session = sessionFactory.openSession()) {
                 session.beginTransaction();
                 session.save(user);
                 session.getTransaction().commit();

            };
           try(Session session1 = sessionFactory.openSession()) {
               session1.beginTransaction();
               User user1 = session1.get(User.class, user.getId());
               assert user.equals(user1);
           }
        }
    }
}
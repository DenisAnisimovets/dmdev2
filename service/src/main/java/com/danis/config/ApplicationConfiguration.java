package com.danis.config;

import com.danis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;

@ComponentScan(basePackages = "com.danis")
@Configuration
public class ApplicationConfiguration {

    @Bean
    EntityManager entityManager(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }

    @Bean
    SessionFactory sessionFactory() {
        return HibernateUtil.buildSessionFactory();
    }
}

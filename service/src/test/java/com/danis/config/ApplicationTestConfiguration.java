package com.danis.config;

import com.danis.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ApplicationConfiguration.class)
@Configuration
public class ApplicationTestConfiguration {
    @Bean
    SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }
}

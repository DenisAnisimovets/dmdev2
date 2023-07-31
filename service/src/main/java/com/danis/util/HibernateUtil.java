package com.danis.util;

import com.danis.entity.GoodInBucket;
import com.danis.entity.Good;
import com.danis.entity.GoodInOrder;
import com.danis.entity.Orders;
import com.danis.entity.User;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Orders.class);
        configuration.addAnnotatedClass(Good.class);
        configuration.addAnnotatedClass(GoodInOrder.class);
        configuration.addAnnotatedClass(GoodInBucket.class);
        return configuration;
    }
}

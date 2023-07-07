package util;

import com.danis.entity.Bucket;
import com.danis.entity.Good;
import com.danis.entity.GoodsInOrder;
import com.danis.entity.Order;
import com.danis.entity.User;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(Good.class);
        configuration.addAnnotatedClass(GoodsInOrder.class);
        configuration.addAnnotatedClass(Bucket.class);
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}

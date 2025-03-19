package com.projects.tennisscoreboard.Utils;

import com.projects.tennisscoreboard.entity.Match;
import com.projects.tennisscoreboard.entity.Player;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Match.class);
            configuration.addAnnotatedClass(Player.class);
            configuration.configure();
            return configuration.buildSessionFactory();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}

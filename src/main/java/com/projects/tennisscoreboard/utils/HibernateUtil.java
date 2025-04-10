package com.projects.tennisscoreboard.utils;

import com.projects.tennisscoreboard.entity.Match;
import com.projects.tennisscoreboard.entity.Player;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
@UtilityClass
public class HibernateUtil {

    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration()
                    .addAnnotatedClass(Match.class)
                    .addAnnotatedClass(Player.class);
            return configuration.buildSessionFactory();
        } catch (Throwable e) {
            log.error("SessionFactory initialization error");
            throw new ExceptionInInitializerError(e);
        }
    }
}

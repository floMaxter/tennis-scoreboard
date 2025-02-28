package com.projects.tennisscoreboard.Utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;

@UtilityClass
public class HibernateUtil {

    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    public static Transaction getTransaction() {
        var session = sessionFactory.getCurrentSession();
        var transaction = HibernateUtil.getSessionFactory().getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction = session.beginTransaction();
        }
        return transaction;
    }

    public static void transactionRollback(Transaction transaction) {
        if (transaction.getStatus() == TransactionStatus.ACTIVE
            || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
            transaction.rollback();
        }
    }

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            return configuration.buildSessionFactory();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}

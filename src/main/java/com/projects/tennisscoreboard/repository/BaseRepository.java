package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.exception.AlreadyExistsException;
import com.projects.tennisscoreboard.exception.DatabaseException;
import com.projects.tennisscoreboard.utils.HibernateUtil;
import com.projects.tennisscoreboard.utils.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

@Slf4j
public abstract class BaseRepository<K extends Serializable, E> implements Repository<K, E> {

    private final Class<E> entityClass;
    protected final SessionFactory sessionFactory;


    public BaseRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public E save(E entity) {
        Transaction transaction = null;
        try (var session = sessionFactory.openSession()) {
            log.info("Saving entity {}", entity);

            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();

            log.info("Entity {} saved", entity);
            return entity;
        } catch (RuntimeException e) {
            log.error("Error saving the entity {}", entity);
            rollbackTransaction(transaction);

            if (e.getMessage().contains("UNIQUE")) {
                log.error("Such an entity {} already exists in the DB", entity);
                throw new AlreadyExistsException("Such an entity already exists");
            }
            throw new DatabaseException("Database error.");
        }
    }

    private void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            try {
                log.info("Rollback transaction");
                transaction.rollback();
            } catch (Exception e) {
                log.error("Failed to rollback transaction");
            }
        }
    }

    @Override
    public List<E> findAll(Integer page) {
        var offset = (page - 1) * PaginationUtil.RECORDS_PER_PAGE;

        try {
            log.info("Search for all entities of type {}", entityClass);
            var session = sessionFactory.getCurrentSession();
            var criteria = session.getCriteriaBuilder().createQuery(entityClass);
            criteria.from(entityClass);

            return session.createQuery(criteria)
                    .setFirstResult(offset)
                    .setMaxResults(PaginationUtil.RECORDS_PER_PAGE)
                    .getResultList();
        } catch (RuntimeException e) {
            log.error("Error when searching for entities of type {}", entityClass);
            throw new DatabaseException("Database error.");
        }
    }
}

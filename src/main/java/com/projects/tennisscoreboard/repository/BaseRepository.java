package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


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
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return entity;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(K id) {
        Transaction transaction = null;
        try (var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            var entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            } else {
                throw new RuntimeException("Entity not found");
            }

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(E entity) {
        Transaction transaction = null;
        try (var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<E> findById(K id) {
        try (var session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(entityClass, id));
        }
    }

    @Override
    public List<E> findAll() {
        try (var session = sessionFactory.openSession()) {
            var criteria = session.getCriteriaBuilder().createQuery(entityClass);
            criteria.from(entityClass);
            return session.createQuery(criteria)
                    .getResultList();
        }
    }
}

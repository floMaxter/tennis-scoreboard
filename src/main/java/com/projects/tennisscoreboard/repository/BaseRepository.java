package com.projects.tennisscoreboard.repository;

import com.projects.tennisscoreboard.Utils.HibernateUtil;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


public abstract class BaseRepository<K extends Serializable, E> implements Repository<K, E> {

    private final Class<E> clazz;
    protected final SessionFactory sessionFactory;

    public BaseRepository(Class<E> clazz) {
        this.clazz = clazz;
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public E save(E entity) {
        sessionFactory.getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        var session = sessionFactory.getCurrentSession();
        session.remove(id);
        session.flush();
    }

    @Override
    public void update(E entity) {
        sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        var session = sessionFactory.getCurrentSession();
        var criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria)
                .getResultList() ;
    }
}
